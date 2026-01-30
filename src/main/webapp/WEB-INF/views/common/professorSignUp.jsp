<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 - GoodeeLMS</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />
</head>
<body>
	<%@ include file="/header.jsp"%>
	<%@ include file="/sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid">
			<div class="row justify-content-center">
				<div class="col-md-8 col-lg-6">
					<div class="card shadow-sm border-0 mt-5">
						<div class="card-body p-4">
							<h5 class="card-title mb-2 fw-bold text-primary">교수 회원가입</h5>
							<p class="text-muted small mb-4">시스템 이용을 위해 정보를 입력해 주세요.</p>

							<form action="${pageContext.request.contextPath}/common/signup"
								method="post">
								<c:if test="${not empty errorMessage}">
									<div
										class="alert alert-danger alert-dismissible fade show shadow-sm d-flex justify-content-center align-items-center"
										role="alert"">
										<div class="d-flex align-items-center">
											<i class="bi bi-exclamation-triangle me-2"></i> <span>${errorMessage}</span>
										</div>
										<button type="button" class="btn-close"
											data-bs-dismiss="alert" aria-label="Close"></button>
									</div>
								</c:if>

								<div class="mb-3">
									<label class="form-label small fw-bold">성함</label> <input
										class="form-control" id="professor_name" name="professor_name"
										type="text" placeholder="성함을 입력하세요"
										value="<c:out value='${professorDTO.professorName}'/>"
										title="성함에는 숫자나 특수문자를 입력할 수 없습니다."
										oninput="validateName(this)">
									<div id="nameError" class="form-text text-danger"
										style="display: none;">숫자와 특수문자는 입력할 수 없습니다.</div>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">이메일 주소</label> <input
										type="email" class="form-control" name="professor_email"
										placeholder="example@email.com"
										value="<c:out value='${professorDTO.professorEmail}'/>">
									<div class="form-text text-danger">※ 입력하신 이메일은 로그인 아이디로
										사용되니 정확히 입력해 주세요.</div>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">비밀번호</label> <input
										type="password" class="form-control" name="professor_password"
										placeholder="비밀번호를 입력하세요">
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">전공</label> <select
										class="form-select" onfocus="this.size=10;"
										onblur="this.size=1;" onchange="this.size=1; this.blur();"
										name="major_id">

										<option value=""
											${empty professorDTO or professorDTO.majorId == 0 ? 'selected' : ''}
											disabled>전공을 선택하세요</option>

										<c:forEach var="major" items="${majorList}">
											<option value="${major.majorId}"
												${professorDTO.majorId == major.majorId ? 'selected' : ''}>
												${major.majorName}</option>
										</c:forEach>
									</select>
								</div>

								<div class="d-grid gap-2 mt-4">
									<button type="submit" class="btn btn-success">회원가입</button>
									<a href="${pageContext.request.contextPath}/main.jsp"
										class="btn btn-outline-secondary btn-sm text-center">취소</a>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>

	<%@ include file="/footer.jsp"%>

	<script>
		const name = document.getElementById('professor_name').value.trim();
		const nameRegex = /^[a-zA-Z가-힣\s]+$/;

		function validateName(input) {
			const regex = /[^a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ\s]/g;
			const errorMsg = document.getElementById('nameError');

			if (regex.test(input.value)) {
				input.value = input.value.replace(regex, '');
				errorMsg.style.display = 'block';
			} else {
				errorMsg.style.display = 'none';
			}
		}

		function validateForm() {
			const name = document.getElementById('professor_name').value.trim();
			const email = document.getElementsByName('professor_email')[0].value
					.trim();
			const pwd = document.getElementsByName('professor_password')[0].value;
			const major = document.getElementsByName('major_id')[0].value;

			if (!name || !email || !pwd || !major) {
				alert("모든 필수 정보를 입력해 주세요.");
				return false;
			}
			return true;
		}
	</script>

</body>
</html>