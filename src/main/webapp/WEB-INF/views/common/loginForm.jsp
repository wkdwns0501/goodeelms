<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/layout.css">
</head>
<body>
	<%@ include file="/header.jsp"%>
	<%@ include file="/sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid">
			<div class="page-shell">
				<div class="row justify-content-center">
					<div class="col-md-6 col-lg-4">
						<div class="card shadow-sm border-0 mt-5">
							<div class="card-body p-4">
								<h5 class="card-title mb-4 text-center fw-bold ">로그인</h5>

								<c:if test="${not empty errorMessage}">
									<div class="alert alert-danger alert-dismissible fade show"
										role="alert">
										<i class="bi bi-exclamation-triangle"></i> ${errorMessage}
										<button type="button" class="btn-close"
											data-bs-dismiss="alert" aria-label="Close"></button>
									</div>
									<c:remove var="errorMessage" scope="session" />
								</c:if>
								<form action="${pageContext.request.contextPath}/common/login" method="post">
									<div class="mb-3">
										<label for="login_id" class="form-label text-muted small" id="label_id">아이디</label> 
										<input type="text" class="form-control" id="login_id" name="login_id"
											placeholder="아이디를 입력하세요" required>
									</div>

									<div class="mb-3">
										<label for="login_password" class="form-label text-muted small">비밀번호</label> 
										<input type="password" class="form-control" id="login_password" name="login_password" 
											placeholder="비밀번호를 입력하세요" required>
									</div>

									<div class="d-grid gap-2 mt-4">
										<button type="submit" class="btn btn-success fw-bold text-white">로그인</button>
									</div>
								</form>
								
								<div class="mt-4 pt-3 border-top">
								   <div class="text-center mt-2">
						            <a href="${pageContext.request.contextPath}/common/resetPassword" class="text-decoration-none small text-muted">
						                <i class="bi bi-question-circle me-1"></i> 비밀번호 초기화
						            </a>
						        </div>
								</div>
								
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>

	<%@ include file="/footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>