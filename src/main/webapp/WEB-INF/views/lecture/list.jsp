<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의 목록</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  /* 비율 기반 레이아웃: 내용 길어도 한 칸이 과하게 커지지 않게 */
  .lecture-table {
    table-layout: fixed;
    width: 100%;
  }

  /* 긴 텍스트 ... 처리 */
  .lecture-table .truncate {
    display: block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
</style>

</head>
<body>
  <%@ include file="/header.jsp" %>
  <%@ include file="/sideNavbar.jsp" %>
  
  <main class="content">
	  <div class="container-fluid">
	    <div class="page-shell">
	       <!-- 상단 타이틀 + 버튼 -->
        <div class="d-flex align-items-center justify-content-between mb-3">
          <div>
            <h4 class="mb-0">강의 목록</h4>
            <small class="text-muted">내 학과(교수 소속) 강의 리스트</small>
          </div>

          <c:if test="${not empty sessionScope.professor_id}">
            <a class="btn btn-success btn-sm" href="<c:url value='/lecture/add'/>">+ 강의 등록</a>
          </c:if>
        </div>

        <!-- 검색창 -->
        <div class="card shadow-sm border-0 mb-3">
          <div class="card-body">
            <form method="get" action="<c:url value='/lecture/list'/>" class="row g-2 align-items-center">
              <div class="col-md-8">
                <input type="text" name="keyword" class="form-control"
                       placeholder="강의명 또는 교수명 또는 건물명 검색"
                       value="<c:out value='${keyword}'/>">
              </div>
              <div class="col-md-4 d-flex gap-2">
                <button type="submit" class="btn btn-primary w-100">검색</button>
                <a class="btn btn-outline-secondary w-100" href="<c:url value='/lecture/list'/>">초기화</a>
              </div>
            </form>
            <div class="form-text mt-2">* 검색은 강의명/교수명/건물명 기준이며, 페이지당 5개씩 표시됩니다.</div>
          </div>
        </div>

        <!-- 테이블 -->
        <div class="table table-hover align-middle mb-0 text-center lecture-table">
				  <div class="card-body">
				    <div class="table-responsive">
				      <table class="table table-hover align-middle mb-0">
				        <thead class="table-light">
				          <tr>
				            <th style="width:110px;">강의코드</th>
				            <th>강의명</th>
				            <th style="width:120px;">교수</th>
				            <th style="width:90px;">유형</th>
				            <th style="width:90px;">학점</th>
				            <th style="width:120px;">학기</th>
				            <th style="width:70px;">분반</th>
				            <th style="width:140px;">강의실</th>
				            <th style="width:100px;">정원</th>
				          </tr>
				        </thead>
				
				        <tbody>
				          <c:choose>
				            <c:when test="${empty lectures}">
				              <tr>
				                <td colspan="8" class="text-center text-muted py-5">
				                  조회 결과가 없습니다.
				                </td>
				              </tr>
				            </c:when>
				
				            <c:otherwise>
				              <c:forEach var="lec" items="${lectures}">
				                <tr>
				                  <td class="fw-semibold">${lec.lectureCodeDisplay}</td>
				
													<td class="text-start">
													  <c:url var="detailUrl" value="/lecture/detail">
													    <c:param name="lectureId" value="${lec.lectureId}"/>
													  </c:url>
													  <a class="fw-semibold text-decoration-none truncate" href="${detailUrl}">
													    ${lec.lectureName}
													  </a>
													</td>
				                  <td>${lec.professorName}</td>
				                  <td>${lec.lectureType}</td>
				                  <td>${lec.lectureCredit}</td>
				                  <td>${lec.lectureYear} - ${lec.lectureSemester}</td>
				                  <td>${lec.lectureSection}</td>
				                  <td class="text-center">
													  <c:out value="${lec.buildingName}" /> 
													  <c:out value="${lec.lectureRoom}" />호
													</td>
				                  <td>${lec.lectureCurrentPeople} / ${lec.lectureCapacity}</td>
				                </tr>
				              </c:forEach>
				            </c:otherwise>
				          </c:choose>
				        </tbody>
				      </table>
				    </div>
				  </div>
				</div>

        <!-- 페이징 -->
        <c:if test="${totalPage > 1}">
          <nav class="mt-3">
            <ul class="pagination justify-content-center">
              <!-- 이전 -->
              <li class="page-item ${page <= 1 ? 'disabled' : ''}">
                <a class="page-link"
                   href="<c:url value='/lecture/list'><c:param name='page' value='${page-1}'/><c:if test='${not empty keyword}'><c:param name='keyword' value='${keyword}'/></c:if></c:url>">
                  이전
                </a>
              </li>
              <!-- 페이지 번호 -->
              <c:forEach var="p" begin="1" end="${totalPage}">
                <li class="page-item ${p == page ? 'active' : ''}">
                  <a class="page-link"
                     href="<c:url value='/lecture/list'><c:param name='page' value='${p}'/><c:if test='${not empty keyword}'><c:param name='keyword' value='${keyword}'/></c:if></c:url>">
                    ${p}
                  </a>
                </li>
              </c:forEach>
              <!-- 다음 -->
              <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
                <a class="page-link"
                   href="<c:url value='/lecture/list'><c:param name='page' value='${page+1}'/><c:if test='${not empty keyword}'><c:param name='keyword' value='${keyword}'/></c:if></c:url>">
                  다음
                </a>
              </li>
            </ul>
          </nav>
        </c:if>
	    </div>
	  </div>
	</main>
  
  <%@ include file="/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>