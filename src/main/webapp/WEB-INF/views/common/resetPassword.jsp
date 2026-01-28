<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 재설정 - GoodeeLMS</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />
<style>
.card {
	border-radius: 15px;
}
/* 초록색 테마에 맞춘 포커스 효과 */
.form-control:focus {
	border-color: #198754;
	box-shadow: 0 0 0 0.25rem rgba(25, 135, 84, 0.15);
}
/* 라디오 버튼 선택 시 초록색 테마 적용 */
.btn-outline-success {
	border-color: #198754;
	color: #198754;
}

.btn-check:checked+.btn-outline-success {
	background-color: #198754;
	border-color: #198754;
}
</style>
</head>
<body>
	<%@ include file="/header.jsp"%>
	<%@ include file="/sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid py-5">
			<div class="row justify-content-center">
				<div class="col-md-10 col-lg-8">
					<div class="page-title mb-4 text-center">
						<h4 class="fw-bold text-success">비밀번호 재설정</h4>
						<p class="text-muted">본인 확인을 위해 정보를 입력해 주세요.</p>
					</div>

					<div id="message-area">
						<c:if test="${not empty msg}">
							<div
								class="alert alert-info alert-dismissible fade show shadow-sm"
								role="alert">
								<i class="bi bi-info-circle me-2"></i>
								<c:out value="${msg}" />
								<button type="button" class="btn-close" data-bs-dismiss="alert"
									aria-label="Close"></button>
							</div>
						</c:if>
					</div>

					<div class="card shadow border-0">
						<div class="card-body p-4 p-md-5">
							<form method="post" id="resetForm"
								action="${pageContext.request.contextPath}/common/resetPassword">

								<div class="text-center mb-4">
									<div class="btn-group w-100" role="group">
										<input type="radio" class="btn-check" name="userRole"
											id="roleStudent" value="STUDENT" checked
											onclick="toggleRole('STUDENT')"> <label
											class="btn btn-outline-success py-2 fw-bold"
											for="roleStudent">학생</label> <input type="radio"
											class="btn-check" name="userRole" id="roleProfessor"
											value="PROFESSOR" onclick="toggleRole('PROFESSOR')">
										<label class="btn btn-outline-success py-2 fw-bold"
											for="roleProfessor">교수</label>
									</div>
								</div>

								<div class="p-4 bg-light rounded-3 mb-4 border">
									<div class="row g-4">
										<div class="col-md-12">
											<label id="idLabel"
												class="form-label small fw-bold text-secondary">학번
												</label> <input type="text" id="mainIdInput"
												class="form-control form-control-lg" placeholder="9자리 학번 입력"
												required> <input type="hidden" name="userId"
												id="hiddenUserId"> <input type="hidden"
												name="professorEmail" id="hiddenProfessorEmail">
										</div>

										<div id="professorPwArea" class="col-md-12"
											style="display: none;">
											<label class="form-label small fw-bold text-secondary">새
												비밀번호 (6자 이상, 영문+숫자 조합)</label> <input type="password"
												name="newPassword" id="newPassword"
												class="form-control form-control-lg"
												placeholder="변경할 비밀번호를 입력하세요">
										</div>

										<div id="studentRrnArea" class="col-md-12">
											<label class="form-label small fw-bold text-secondary">주민등록번호</label>
											<div class="d-flex align-items-center">
												<input type="text" id="rrn-front"
													class="form-control text-center form-control-lg"
													maxlength="6" oninput="this.value=this.value.replace(/[^0-9]/g,''); if(this.value.length==6) document.getElementById('rrn-back').focus();">
												<span class="mx-3 fw-bold">-</span> <input type="password"
													id="rrn-back"
													class="form-control text-center form-control-lg"
													maxlength="7" oninput="this.value=this.value.replace(/[^0-9]/g,'');">
											</div>
											<input type="hidden" name="studentIdentityNum"
												id="studentIdentityNum">
											<div class="form-text mt-2 text-muted small">
												<i class="bi bi-patch-exclamation me-1"></i> 초기 비밀번호로 변경됩니다.
											</div>
										</div>
									</div>
								</div>

								<div class="d-grid">
									<button type="button" onclick="handleResetSubmit()"
										class="btn btn-success btn-lg fw-bold shadow-sm text-white">
										<i></i>비밀번호 재설정
									</button>
								</div>
							</form>
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
		let currentRole = 'STUDENT';

		function toggleRole(role) {
			currentRole = role;
			const label = document.getElementById('idLabel');
			const input = document.getElementById('mainIdInput');
			const pwArea = document.getElementById('professorPwArea');
			const rrnArea = document.getElementById('studentRrnArea');

			if (role === 'STUDENT') {
				label.innerText = "학번";
				input.placeholder = "9자리 숫자";
				input.value = "";
				pwArea.style.display = 'none';
				rrnArea.style.display = 'block';
			} else {
				label.innerText = "교수 이메일";
				input.placeholder = "example@goodee.ac.kr";
				input.value = "";
				pwArea.style.display = 'block';
				rrnArea.style.display = 'none';
			}
		}

		function handleResetSubmit() {
			const idVal = document.getElementById('mainIdInput').value;
			const form = document.getElementById('resetForm');

			if (currentRole === 'STUDENT') {
				const front = document.getElementById('rrn-front').value;
				const back = document.getElementById('rrn-back').value;

				if (!/^\d{9}$/.test(idVal)) {
					alert("학번은 숫자 9자리여야 합니다.");
					return;
				}
				if (front.length !== 6 || back.length !== 7) {
					alert("주민번호를 정확히 입력해 주세요.");
					return;
				}

				document.getElementById('studentIdentityNum').value = front
						+ "-" + back;
				document.getElementById('hiddenUserId').value = idVal;
				document.getElementById('hiddenProfessorEmail').disabled = true;
				document.getElementById('studentIdentityNum').disabled = false;
			} else {
				const newPw = document.getElementById('newPassword').value;
				const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;

				if (!idVal.includes('@')) {
					alert("유효한 이메일을 입력하세요.");
					return;
				}
				if (!pwRegex.test(newPw)) {
					alert("비밀번호는 6자 이상, 영문/숫자 조합이어야 합니다.");
					return;
				}

				document.getElementById('hiddenProfessorEmail').value = idVal;
				document.getElementById('hiddenUserId').disabled = true;
				document.getElementById('studentIdentityNum').disabled = true;
			}
			form.submit();
		}
	</script>
</body>
</html>