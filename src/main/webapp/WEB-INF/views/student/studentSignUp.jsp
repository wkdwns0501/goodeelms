<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GoodeeLMS</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />
<style>
.nav-disabled {
	pointer-events: none; /* 클릭 차단 */
	user-select: none;
}

.card {
	border-radius: 15px;
	border: none;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.form-label {
	font-weight: 600;
	color: #495057;
}
</style>
</head>
<body>
	<div class="nav-disabled">
		<%@ include file="/header.jsp"%>
		<%@ include file="/sideNavbar.jsp"%>
	</div>

	<main class="content">
		<div class="container-fluid py-5">
			<div class="row justify-content-center">
				<div class="col-md-10 col-lg-8">

					<div class="page-title mb-4 text-center">
						<h4 class="fw-bold text-primary">초기정보 수정</h4>
						<p class="text-muted">학사 시스템 이용을 위해 초기 정보를 설정해주세요.</p>
					</div>

					<div class="card">
						<div class="card-body p-4 p-md-5">
							<form method="post" id="signupForm"
								action="<c:url value='/student/signup'/>" novalidate>

								<div class="p-4 bg-light rounded-3 mb-4 border">
									<h6 class="fw-bold mb-3 text-secondary">비밀번호 변경</h6>
									<div class="row g-3">
										<div class="col-md-12">
											<label class="form-label small">현재 비밀번호</label> <input
												type="password" name="origin_student_password" id="originPw"
												class="form-control" placeholder="현재 비밀번호를 입력하세요" required>
										</div>
										<div class="col-md-6">
											<label class="form-label small">새 비밀번호</label> <input
												type="password" name="new_student_password" id="newPw"
												class="form-control" placeholder="6자 이상 영문/숫자 조합" required>
										</div>
										<div class="col-md-6">
											<label class="form-label small">새 비밀번호 확인</label> <input
												type="password" id="confirmNewPw" class="form-control"
												placeholder="비밀번호 확인" required>
										</div>
									</div>
								</div>

								<div class="row mb-4">
									<div class="col-md-6">
										<label class="form-label">연락처</label> <input type="hidden"
											name="student_phone" id="studentPhoneHidden" />
										<div class="d-flex align-items-center gap-2">
											<select class="form-select" style="max-width: 100px;"
												id="phone1">
												<option value="010">010</option>
												<option value="011">011</option>
												<option value="016">016</option>
												<option value="017">017</option>
												<option value="018">018</option>
												<option value="019">019</option>
											</select> <span>-</span> <input type="text" class="form-control"
												id="phone2" maxlength="4" placeholder="1234"> <span>-</span>
											<input type="text" class="form-control" id="phone3"
												maxlength="4" placeholder="5678">
										</div>
									</div>

									<div class="col-md-6">
										<label class="form-label">이메일</label> <input type="hidden"
											name="student_email" id="studentEmailHidden" />
										<div class="d-flex align-items-center gap-2">
											<input type="text" class="form-control" id="emailId"
												placeholder="아이디"> <span class="text-muted">@</span>
											<select class="form-select" id="emailDomain">
												<option value="naver.com">naver.com</option>
												<option value="google.com">google.com</option>
												<option value="daum.net">daum.net</option>
												<option value="hanmail.net">hanmail.net</option>
												<option value="goodee.ac.kr">goodee.ac.kr</option>
											</select>
										</div>
									</div>
								</div>

								<div class="row mb-4">
									<div class="col-md-4">
										<label class="form-label">은행</label> <select
											name="student_bank" class="form-select">
											<option value="국민은행">국민은행</option>
											<option value="농협은행">농협은행</option>
											<option value="신한은행">신한은행</option>
											<option value="우리은행">우리은행</option>
											<option value="카카오뱅크">카카오뱅크</option>
										</select>
									</div>
									<div class="col-md-8">
										<label class="form-label">계좌번호</label> <input type="text"
											name="account_number" id="accountNumber" class="form-control"
											placeholder="숫자와 '-'만 입력 가능">
									</div>
								</div>

								<div class="mb-5">
									<label class="form-label">주소</label> <input type="text"
										name="student_address" id="studentAddress"
										class="form-control" placeholder="상세 주소까지 입력해주세요">
								</div>

								<div class="d-grid">
									<button type="submit" class="btn btn-primary btn-lg fw-bold">정보 수정</button>
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
			document.getElementById('signupForm').onsubmit = function(e) {
			const originPw = document.getElementById('originPw').value;
			const newPw = document.getElementById('newPw').value;
			const confirmNewPw = document.getElementById('confirmNewPw').value;
			const p2 = document.getElementById('phone2').value;
			const p3 = document.getElementById('phone3').value;
			const eid = document.getElementById('emailId').value;
			const edom = document.getElementById('emailDomain').value;
			const addr = document.getElementById('studentAddress').value;

			// 1. 비밀번호 체크
			if (!originPw || !newPw) {
				alert("비밀번호를 입력해주세요.");
				return false;
			}
			if (newPw !== confirmNewPw) {
				alert("새 비밀번호 확인이 일치하지 않습니다.");
				return false;
			}
			if (newPw === originPw) {
				alert("새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다.");
				return false;
			}

			// 2. 연락처 조합 (010-1234-5678)
			if (!/^\d{3,4}$/.test(p2) || !/^\d{4}$/.test(p3)) {
				alert("연락처를 올바르게 입력해주세요.");
				return false;
			}
			document.getElementById('studentPhoneHidden').value = document
					.getElementById('phone1').value
					+ "-" + p2 + "-" + p3;

			// 3. 이메일 조합 (id@domain.com)
			if (!eid) {
				alert("이메일 아이디를 입력해주세요.");
				return false;
			}
			document.getElementById('studentEmailHidden').value = eid + "@"
					+ edom;

			// 4. 주소 체크
			if (addr.trim().length < 5) {
				alert("주소를 정확히 입력해주세요.");
				return false;
			}

			return true;
		};
	</script>
</body>
</html>