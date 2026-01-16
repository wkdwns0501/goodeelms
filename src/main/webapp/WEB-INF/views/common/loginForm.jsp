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
								<h5 class="card-title mb-4 text-center fw-bold">로그인</h5>

								<c:choose>
									<c:when test="${errorMessage == 'data_required'}">
										<div class="alert alert-danger py-2 small text-center"
											role="alert">아이디와 비밀번호를 입력해주세요.</div>
									</c:when>
										<c:when test="${errorMessage == 'login_fail'}">
										<div class="alert alert-danger py-2 small text-center"
											role="alert">아이디와 비밀번호가 일치하지 않습니다.</div>
									</c:when>
									<c:when test="${errorMessage == 'unchecked_userType'}">
										<div class="alert alert-warning py-2 small text-center"
											role="alert">로그인 유형을 선택해주세요.</div>
									</c:when>
								</c:choose>

								<form action="${pageContext.request.contextPath}/login"
									method="post">
									<div class="mb-4 text-center">
										<div class="btn-group w-100" role="group">
											<input type="radio" class="btn-check" name="userType"
												id="type_student" value="STUDENT" checked> <label
												class="btn btn-outline-primary" for="type_student">학생</label>

											<input type="radio" class="btn-check" name="userType"
												id="type_professor" value="PROFESSOR"> <label
												class="btn btn-outline-primary" for="type_professor">교수</label>
										</div>
									</div>

									<div class="mb-3">
										<label for="id_field" class="form-label text-muted small"
											id="label_id">아이디</label> <input type="text"
											class="form-control" id="id_field" name="student_no"
											placeholder="아이디를 입력하세요" required>
									</div>

									<div class="mb-3">
										<label for="student_password"
											class="form-label text-muted small">비밀번호</label> <input
											type="password" class="form-control" id="pass_field"
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

	<%@ include file="/footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

	<script>
		document.addEventListener('DOMContentLoaded', function() {
			const studentRadio = document.getElementById('type_student');
			const professorRadio = document.getElementById('type_professor');
			const idField = document.getElementById('id_field');
			const passField = document.getElementById('pass_field'); 
			const labelId = document.getElementById('label_id');

			function loginForm() {
				if (studentRadio.checked) {
					idField.name = "student_no";
					passField.name = "student_password";
					idField.placeholder = "학번을 입력하세요";
				} else {
					idField.name = "professor_email";
					passField.name = "professor_password";
					idField.placeholder = "아이디를 입력하세요";
				}
			}

			studentRadio.addEventListener('change', loginForm);
			professorRadio.addEventListener('change', loginForm);

			loginForm();
		});
	</script>
</body>
</html>