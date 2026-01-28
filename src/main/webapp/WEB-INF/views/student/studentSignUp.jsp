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
	<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	%>

	<%@ include file="/header.jsp"%>

	<div class="nav-disabled">
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

					<div id="error-message-area">
						<c:if test="${not empty errorMessage}">
							<div class="alert alert-danger alert-dismissible fade show"
								role="alert">
								<i class="bi bi-exclamation-triangle"></i>
								<c:out value="${errorMessage}" />
								<button type="button" class="btn-close" data-bs-dismiss="alert"
									aria-label="Close"></button>
							</div>
							<c:remove var="errorMessage" scope="session" />
						</c:if>
					</div>

					<div class="card">
						<div class="card-body p-4 p-md-5">
							<form method="post" id="signupForm"
								action="${pageContext.request.contextPath}/student/signup">
								<input type="hidden" name="student_id"
									value="<c:out value="${studentId}"/>"> <input
									type="hidden" name="student_phone" id="studentPhoneHidden" />
								<input type="hidden" name="student_email"
									id="studentEmailHidden" /> <input type="hidden"
									name="student_bank" id="studentBankHidden" />

								<div class="p-4 bg-light rounded-3 mb-4 border">
									<h6 class="fw-bold mb-3 text-secondary">비밀번호 변경</h6>
									<div class="row g-3">
										<div class="col-md-12">
											<label class="form-label small">현재 비밀번호</label> <input
												type="password" name="origin_student_password" id="originPw"
												class="form-control">
										</div>
										<div class="col-md-6">
											<label class="form-label small">새 비밀번호</label> <input
												type="password" name="new_student_password" id="newPw"
												class="form-control" placeholder="영문 소문자와 숫자를 포함한 6자 이상">
										</div>
										<div class="col-md-6">
											<label class="form-label small">새 비밀번호 확인</label> <input
												type="password" id="confirmNewPw" class="form-control">
										</div>
									</div>
								</div>

								<div class="row mb-4">
									<div class="col-md-6">
										<label class="form-label">연락처</label>
										<div class="d-flex align-items-center gap-2">
											<select class="form-select" style="max-width: 100px;"
												id="phone1">
												<c:forTokens items="010,011,016,017,018,019" delims=","
													var="p">
													<option value="${p}" ${phone1 == p ? 'selected' : ''}>${p}</option>
												</c:forTokens>
											</select> <span>-</span> <input type="text" class="form-control"
												id="phone2" maxlength="4" placeholder="1234"
												value="<c:out value='${phone2}'/>"> <span>-</span> <input
												type="text" class="form-control" id="phone3" maxlength="4"
												placeholder="5678" value="<c:out value='${phone3}'/>">
										</div>
									</div>
									<div class="col-md-6">
										<label class="form-label">이메일</label>
										<div class="d-flex align-items-center gap-2">
											<input type="text" class="form-control" id="emailId"
												placeholder="이메일" value="<c:out value='${emailId}'/>">
											<span class="text-muted">@</span> <select class="form-select"
												id="emailDomain">
												<option value="naver.com"
													${emailDomain == 'naver.com' ? 'selected' : ''}>naver.com</option>
												<option value="daum.net"
													${emailDomain == 'daum.net' ? 'selected' : ''}>daum.net</option>
												<option value="google.com"
													${emailDomain == 'gmail.com' ? 'selected' : ''}>gmail.com</option>
												<option value="goodee.ac.kr"
													${emailDomain == 'goodee.ac.kr' ? 'selected' : ''}>goodee.ac.kr</option>
											</select>
										</div>
									</div>
								</div>

								<div class="row mb-4">
									<div class="col-md-4">
										<label class="form-label">은행</label> <select id="bankName"
											class="form-select">
											<option value="국민은행" ${bankName == '국민은행' ? 'selected' : ''}>국민은행</option>
											<option value="농협은행" ${bankName == '농협은행' ? 'selected' : ''}>농협은행</option>
											<option value="우리은행" ${bankName == '우리은행' ? 'selected' : ''}>우리은행</option>
											<option value="카카오뱅크"
												${bankName == '카카오뱅크' ? 'selected' : ''}>카카오뱅크</option>
										</select>
									</div>
									<div class="col-md-8">
										<label class="form-label">계좌번호</label> <input type="text"
											id="bankAccount" class="form-control"
											placeholder="숫자3자리-숫자7자리 형식"
											value="<c:out value='${bankAccount}'/>">
									</div>
								</div>

								<div class="mb-5">
									<label class="form-label">주소</label> <input type="text"
										name="student_address" id="studentAddress"
										placeholder="5자리 이상의 주소를 입력" class="form-control"
										value="<c:out value='${studentDTO.studentAddress}'/>">
								</div>

								<div class="d-grid">
									<button type="submit" class="btn btn-primary btn-lg fw-bold">정보수정</button>
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
	<script
		src="${pageContext.request.contextPath}/resources/js/studentSignup.js"></script>
	<script>
		window.onpageshow = function(event) {
			const isBackNavigation = event.persisted
					|| (window.performance && window.performance
							.getEntriesByType("navigation")[0].type === 'back_forward');

			if (isBackNavigation) {
				location
						.replace("${pageContext.request.contextPath}/common/logout");
			}
		};
	</script>
</body>
</html>