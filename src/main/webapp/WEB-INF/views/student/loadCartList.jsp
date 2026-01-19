<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card shadow-sm">
  <div class="card-header d-flex align-items-center justify-content-between">
    <span class="fw-semibold">장바구니</span>
    <span class="text-muted small">총 <strong>${totalCount}</strong>강의</span>
  </div>

  <div class="list-group list-group-flush">
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

          <button type="button" class="btn btn-sm btn-outline-danger delete-cart" data-target="${lec.lectureId}" data-user="${sessionScope.student_id}">삭제</button>
        </div>
      </div>
    </c:forEach>
    <div class="card-body border-top">
	    <div class="d-flex justify-content-between mb-2">
	      <span class="text-muted">합계 학점</span>
	      <span class="fw-semibold" id="creditInfo" data-total="${totalCartCredit}" data-limit="${limitCartCredit}">
	      ${totalCartCredit} / ${limitCartCredit} 학점
	      </span>
	    </div>
	
	    <div class="d-grid gap-2">
	      <form method="post" action="<c:url value='/enroll/cart/clear'/>" class="m-0">
	        <button type="submit" class="btn btn-outline-secondary">장바구니 비우기</button>
	      </form>
	    </div>
	  </div>
    <c:if test="${empty lectureList}">
      <div class="list-group-item text-center text-muted py-4">
        장바구니가 비어있습니다.
      </div>
    </c:if>
  </div>

  
</div>