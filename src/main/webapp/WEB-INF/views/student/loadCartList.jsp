<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card shadow-sm">
  <div class="card-header d-flex align-items-center justify-content-between">
    <span class="fw-semibold">장바구니</span>
    <span class="text-muted small">총 <strong>${cartCount}</strong>과목</span>
  </div>

  <div class="list-group list-group-flush">
    <!-- 예시: cartItems -->
    <c:forEach var="item" items="${cartItems}">
      <div class="list-group-item">
        <div class="d-flex justify-content-between gap-2">
          <div>
            <div class="fw-semibold">${item.title}</div>
            <div class="text-muted small">
              ${item.professor} · ${item.credit}학점
            </div>
          </div>

          <form method="post" action="<c:url value='/enroll/cart/remove'/>" class="m-0">
            <input type="hidden" name="lectureId" value="${item.id}">
            <button type="submit" class="btn btn-sm btn-outline-danger">삭제</button>
          </form>
        </div>
      </div>
    </c:forEach>

    <c:if test="${empty cartItems}">
      <div class="list-group-item text-center text-muted py-4">
        장바구니가 비어있습니다.
      </div>
    </c:if>
  </div>

  
</div>