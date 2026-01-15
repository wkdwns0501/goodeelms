<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card shadow-sm">
	<div class="card-header d-flex align-items-center justify-content-between">
	  <span class="fw-semibold">강의 리스트</span>
	  <span class="text-muted small">총 <strong>${totalCount}</strong>건</span>
	</div>
	
	<div class="table-responsive">
	  <table class="table table-hover align-middle mb-0">
	    <thead class="table-light">
	      <tr>
	        <th style="width:90px;">구분</th>
	        <th>과목명</th>
	        <th style="width:120px;">교수</th>
	        <th style="width:90px;" class="text-center">학점</th>
	        <th style="width:140px;" class="text-center">정원(신청)</th>
	        <th style="width:120px;" class="text-center">담기</th>
	      </tr>
	    </thead>
	    <tbody>
	      <!-- 예시: lectures 리스트를 서버에서 넘긴다고 가정 -->
	      <c:forEach var="lec" items="${lectureList}">
	        <tr>
	          <td>
	            <span class="badge text-bg-secondary">${lec.lectureCode}</span>
	          </td>
	          <td>
	            <div class="fw-semibold">${lec.lectureName}</div>
	            <div class="text-muted small">
	              ${lec.lectureDescription} · ${lec.lectureRoom} · ${lec.lectureType}
	            </div>
	          </td>
	          <td>${lec.professorName}</td>
	          <td class="text-center">${lec.lectureCredit}</td>
	          <td class="text-center">
	            <span class="fw-semibold">${lec.lectureCapacity}</span>
	            <span class="text-muted">(${lec.lectureCurrentPeople})</span>
	          </td>
	          <td class="text-center">
            	<!-- 장바구니 담기: POST 권장 -->
	            <button type="submit" class="btn btn-sm btn-outline-success add-cart" 
	            	data-lec="${lec.lectureId}" data-stu ="${sessionScope.student_id}">
	              장바구니
	            </button>
	          </td>
	        </tr>
	      </c:forEach>
	
	      <!-- 비었을 때 -->
	      <c:if test="${empty lectureList}">
	        <tr>
	          <td colspan="6" class="text-center text-muted py-4">
	            표시할 강의가 없습니다.
	          </td>
	        </tr>
	      </c:if>
	    </tbody>
	  </table>
	</div>
	
		<!-- 페이징 자리 (원하면 추가) -->
		<div class="card-body border-top small text-muted text-center">
		  <c:if test="${pageNums > 1}">
        <nav class="mt-3">
          <ul class="pagination justify-content-center">
            <!-- 이전 -->
            <li class="page-item ${viewPage <= 1 ? 'disabled' : ''}">
              <a class="page-link lectureCart-list"
                 href="<c:url value=''>
                 <c:param name='viewPage' value='${viewPage-1}'/>
                 <c:if test='${not empty search_word}'><c:param name='search_word' value='${search_word}'/></c:if>
                 </c:url>">
                이전
              </a>
            </li>
            <!-- 페이지 번호 -->
            <c:forEach var="p" begin="1" end="${pageNums}">
              <li class="page-item ${p == page ? 'active' : ''} ">
                <a class="page-link lectureCart-list"
                   href="<c:url value=''>
                   <c:param name='viewPage' value='${p}'/>
                   <c:if test='${not empty search_word}'><c:param name='search_word' value='${search_word}'/></c:if>
                   </c:url>">
                  ${p}
                </a>
              </li>
            </c:forEach>
            <!-- 다음 -->
            <li class="page-item ${viewPage >= pageNums ? 'disabled' : ''}">
              <a class="page-link lectureCart-list"
                 href="<c:url value=''>
                 <c:param name='viewPage' value='${viewPage+1}'/>
                 <c:if test='${not empty search_word}'><c:param name='keyword' value='${search_word}'/></c:if>
                 </c:url>">
                다음
              </a>
            </li>
          </ul>
        </nav>
      </c:if>
		</div>
	</div>
