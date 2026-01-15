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
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />
</head>
<body>
	<%@ include file="header.jsp"%>
	<%@ include file="sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid">
			<div class="page-shell">
				<div class="row justify-content-center">
					<div class="col-md-6 col-lg-4">
						<div class="card shadow-sm border-0 mt-5">
							<div class="card-body p-4">
								<h5 class="card-title mb-4 text-center fw-bold">로그인</h5>

								<c:if test="${param.error == '1'}">
									<div class="alert alert-danger py-2 small text-center"
										role="alert">아이디와 비밀번호가 일치하지 않습니다.</div>
								</c:if>

								<form action="ProcessLogin" method="post">
									<div class="mb-3">
										<label for="student_no" class="form-label text-muted small">아이디</label>
										<input type="text" class="form-control" id="student_no"
											name="student_no" placeholder="아이디를 입력하세요" required>
									</div>

									<div class="mb-3">
										<label for="student_password"
											class="form-label text-muted small">비밀번호</label> <input
											type="password" class="form-control" id="student_password"
											name="student_password" placeholder="비밀번호를 입력하세요" required>
									</div>

									<div class="d-grid gap-2 mt-4">
										<button type="submit" class="btn btn-primary">로그인</button>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>
	//
	<%@ include file="footer.jsp"%>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>