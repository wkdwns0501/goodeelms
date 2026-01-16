<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>초기 정보 등록</title>
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
							<h5 class="card-title mb-2 fw-bold text-primary">초기 정보 등록</h5>
							<p class="text-muted small mb-4">최초 로그인입니다. 새로운 비밀번호와 추가 정보를
								입력해 주세요.</p>

							<form action="${pageContext.request.contextPath}/student/signup"
								method="post">

								<c:if test="${errorMessage == 'samePassword'}">
									<div class="alert alert-danger py-2 small text-center"
										role="alert">기존 비밀번호와 동일합니다.</div>
								</c:if>

								<c:if test="${errorMessage == 'already_used'}">
									<div class="alert alert-danger py-2 small text-center"
										role="alert">이미 전화번호 혹은 이메일을 사용되고 있습니다.</div>
								</c:if>

								<div class="mb-3">
									<label class="form-label small fw-bold">아이디(학번)</label> <input
										class="form-control bg-light" name="student_no" type="text"
										value="${sessionScope.student_no}" readonly>
								</div>

								<div class="row">
									<div class="col-md-6 mb-3">
										<label class="form-label small fw-bold">기존 비밀번호</label> <input
											type="password" class="form-control"
											name="origin_student_password" placeholder="기존 비밀번호 입력"
											required>
									</div>
									<div class="col-md-6 mb-3">
										<label class="form-label small fw-bold">새로운 비밀번호</label> <input
											type="password" class="form-control"
											name="new_student_password" placeholder="새 비밀번호 입력" required>
									</div>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">이메일</label> <input
										type="email" class="form-control" name="student_email"
										placeholder="example@email.com" required>
								</div>

								<div class="row">
									<div class="col-md-6 mb-3">
										<label class="form-label small fw-bold">전화번호</label> <input
											type="text" class="form-control" name="student_phone"
											placeholder="010-0000-0000" required>
									</div>
									<div class="col-md-6 mb-3">
										<label class="form-label small fw-bold">은행명 계좌번호</label> <input
											type="text" class="form-control" name="student_bank"
											placeholder="XX은행 000-..." required>
									</div>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">주소</label> <input
										type="text" class="form-control" name="student_address"
										placeholder="주소를 입력하세요">
								</div>

								<div class="d-grid gap-2 mt-4">
									<button type="submit" class="btn btn-success">정보 수정</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>

	<%@ include file="/footer.jsp"%>
</body>
</html>