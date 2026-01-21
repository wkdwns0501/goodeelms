<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card shadow-sm">
  <div class="card-header d-flex align-items-center justify-content-between">
    <span class="fw-semibold">장바구니</span>
    <span class="text-muted small">총 <strong>${totalCount}</strong>강의</span>
  </div>

  <div class="list-group list-group-flush" id="cartEl" data-user="${sessionScope.student_id}">
  	<c:set var="totalCartCredit" value = "0"/>
    <!-- 예시: cartItems -->
    <c:forEach var="lec" items="${lectureList}">
      <div class="list-group-item">
        <div class="d-flex justify-content-between gap-2">
          <div>
            <div class="fw-semibold">${lec.lectureName}</div>
            <div class="text-muted small">
              ${lec.majorName} · ${lec.professorName} · ${lec.lectureCredit}학점
              <c:set var="totalCartCredit" value="${totalCartCredit + lec.lectureCredit}"/>
            </div>
          </div>

          <div class="d-flex justify-content-between gap-3">
	          <c:if test="${lec.preEnrollmentStatus eq 're_apply'}">
		          <div class="text-center justify-content-center">
		          	<p class="fs-6 fw-bold">(${lec.lectureCurrentPeople} / ${lec.lectureCapacity})</p>
		          </div>
	          </c:if>
	          <button type="button" class="btn btn-sm ${lec.preEnrollmentStatus eq 'completed' ? 'btn-success' : 'btn-info fw-bold fs-6'} delete-cart" 
						        ${lec.preEnrollmentStatus eq 'completed' ? 'disabled' : ''}>
					    <c:choose>
				        <c:when test="${lec.preEnrollmentStatus eq 'completed'}">수강 신청 완료</c:when>
				        <c:when test="${lec.preEnrollmentStatus eq 're_apply'}">수강 신청하기</c:when>
					    </c:choose>
						</button>
          </div>
        </div>
      </div>
    </c:forEach>
    <div class="card-body">
	    <div class="d-flex justify-content-between mb-2">
	      <span class="text-muted">합계 학점</span>
	      <span class="fw-semibold" id="creditInfo" data-total="${totalCartCredit}" data-limit="${limitCartCredit}">
	      ${totalCartCredit} / ${limitCartCredit} 학점
	      </span>
	    </div>
	
	  </div>
    <c:if test="${empty lectureList}">
      <div class="list-group-item text-center text-muted py-4">
        장바구니가 비어있습니다.
      </div>
    </c:if>
  </div>

  
</div>