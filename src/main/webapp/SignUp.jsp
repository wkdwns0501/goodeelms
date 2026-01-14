<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GoodeeLMS</title>

<!-- Bootstrap 5 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

</head>
<body>
	<%@ include file="header.jsp"%>
	<%@ include file="sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid py-4">
			<div class="page-shell">
				<div class="row justify-content-center">
					<div class="col-lg-8">
						<div class="card shadow-sm border-0">
							<div class="card-header bg-white py-3 border-bottom">
								<h5 class="mb-0 fw-bold">회원가입</h5>
								<small class="text-muted">학사 정보 등록을 위해 아래 정보를 정확히
									입력해주세요.</small>
							</div>
							<div class="card-body p-4">
								<form action="PrcoessSingUp" method="post">
									<div class="row g-3 mb-4">
										<div class="col-md-12">
											<label class="form-label fw-semibold">학번 (ID)</label> <input
												type="text" name="student_no" class="form-control"
												placeholder="학번 8자리를 입력하세요" required>
										</div>
										<div class="col-md-6">
											<label class="form-label fw-semibold">비밀번호</label> <input
												type="password" name="student_password" class="form-control"
												placeholder="비밀번호 설정" required>
										</div>
										<div class="col-md-6">
											<label class="form-label fw-semibold">비밀번호 확인</label> <input
												type="password" name="student_password_confirm"
												class="form-control" placeholder="비밀번호 재입력" required>
										</div>
									</div>

									<hr class="my-4">

									<div class="row g-3">
										<div class="col-md-4">
											<label class="form-label fw-semibold">이름</label> <input
												type="text" name="student_name" class="form-control"
												required>
										</div>
										<div class="col-md-4">
											<label class="form-label fw-semibold">연락처</label> <input
												type="text" name="student_phone" class="form-control"
												placeholder="010-0000-0000">
										</div>
										<div class="col-md-4">
											<label class="form-label fw-semibold">성별</label> <select
												name="student_gender" class="form-select">
												<option value="">성별을 선택</option>
												<option value="M">남성</option>
												<option value="F">여성</option>
											</select>
										</div>
										<div class="col-md-6">
											<label class="form-label fw-semibold">주민등록번호</label> <input
												type="text" name="student_identity_number"
												class="form-control" placeholder="'-' 제외 입력">
										</div>
										<div class="col-md-6">
											<label class="form-label fw-semibold">이메일</label> <input
												type="email" name="student_email" class="form-control"
												placeholder="example@university.ac.kr">
										</div>
										<div class="col-12">
											<label class="form-label fw-semibold">주소</label> <input
												type="text" name="student_address" class="form-control"
												placeholder="전체 주소를 입력하세요">
										</div>
									</div>

									<hr class="my-4">
										<div class="col-md-6">
											<label class="form-label fw-semibold">환불 계좌 은행</label> <input
												type="text" name="student_bank" class="form-control"
												placeholder="은행명 및 계좌번호">
										</div>
									</div>

									<div class="d-grid gap-2">
										<button type="submit" class="btn btn-primary btn-lg">회원가입
											완료</button>
										<button type="button" class="btn btn-light"
											onclick="history.back();">취소</button>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>

	<%@ include file="footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>