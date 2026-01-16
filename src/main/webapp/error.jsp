<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error Page</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />

<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

</head>
<body>
	<%@ include file="header.jsp"%>
	<%@ include file="sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid">
			<div class="page-shell">
				<p class="text-muted mb-3">
				<div class="mb-4 text-danger">
					<i class="bi bi-exclamation-octagon-fill display-1"></i>
				</div>

				<h2 class="h3 fw-bold text-dark">요청을 처리할 수 없습니다</h2>
				<p class="text-muted small mb-4">서비스 이용에 불편을 드려 죄송합니다. 아래의 오류
					내용을 확인해 주세요.</p>

				<div
					class="p-3 mb-4 bg-light rounded border-start border-danger border-4 text-start">
					<div class="d-flex align-items-center mb-1">
						<i class="bi bi-info-circle me-2 text-secondary small"></i> <span
							class="text-uppercase fw-bold text-secondary small"
							style="letter-spacing: 1px;">Error Details</span>
					</div>
					<div class="text-dark fw-medium text-break">
						<c:choose>
							<c:when test="${not empty sessionScope.errorMessage}">
								<c:out value="${sessionScope.errorMessage}" />
							</c:when>
							<c:otherwise>
                  예기치 못한 시스템 오류가 발생했습니다.
               </c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="d-flex gap-2"></div>
			</div>
		</div>
	</main>

	<c:remove var="errorMessage" scope="session" />

	<%@ include file="footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>