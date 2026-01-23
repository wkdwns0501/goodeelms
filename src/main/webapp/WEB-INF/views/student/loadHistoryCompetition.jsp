<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card shadow-sm h-100 d-flex flex-column" style="min-height: 0;">
  <div class="card-header d-flex align-items-center justify-content-between">
    <span class="fw-semibold">수강신청 현황</span>
    <span class="text-muted small">총 <strong>${totalCount}</strong>강의</span>
  </div>

  <div class="list-group list-group-flush flex-grow-1" id="cartEl" style="overflow-y: auto; min-height: 0;" 
  			data-user="${sessionScope.student_id}">
  	<c:set var="totalCartCredit" value = "0"/>
    <!-- 예시: cartItems -->
    <c:forEach var="lec" items="${lectureList}">
      <div class="list-group-item">
        <div class="d-flex justify-content-between gap-2">
          <div>
            <div class="fw-semibold">${lec.lectureName}</div>
            <div class="text-muted small">
              ${lec.majorName} · ${lec.professorName} · ${lec.lectureCredit}학점
              <%-- <c:if test="${lec.preEnrollmentStatus eq 'completed'}"> --%>
	              <c:set var="totalCartCredit" value="${totalCartCredit + lec.lectureCredit}"/>
              <%-- </c:if> --%>
            </div>
          </div>

          <div class="d-flex justify-content-between gap-3">
	          <c:if test="${lec.preEnrollmentStatus eq 're_apply'}">
		          <div class="text-center justify-content-center">
		          	<p class="fs-6 fw-bold">(${lec.lectureCurrentPeople} / ${lec.lectureCapacity})</p>
		          </div>
	          </c:if>
	          
					    <c:choose>
				        <c:when test="${lec.preEnrollmentStatus eq 'completed'}">
				        	<p class="btn btn-sm btn-success fw-bold fs-6">수강 신청 완료</p>
				        </c:when>
				        <c:when test="${lec.preEnrollmentStatus eq 're_apply'}">
					        <button type="button" class="btn btn-sm btn-info fw-bold fs-6 add-on-cart" data-target ="${lec.lectureId}" data-credit="${lec.lectureCredit}">
					        수강 신청하기
									</button>
				        </c:when>
					    </c:choose>
          </div>
        </div>
      </div>
    </c:forEach>
    <c:if test="${empty lectureList}">
      <div class="list-group-item text-center text-muted py-4">
        수강신청 된 강의가 없습니다.
      </div>
    </c:if>
  </div>
  <div class="card-body border-top flex-shrink-0">
   <div class="d-flex justify-content-between mb-0">
     <span class="text-muted">합계 학점</span>
     <span class="fw-semibold" id="creditInfo" data-total="${totalCartCredit}" data-limit="${limitCartCredit}">
     ${totalCartCredit} / ${limitCartCredit} 학점
     </span>
   </div>
  </div>
  
</div>