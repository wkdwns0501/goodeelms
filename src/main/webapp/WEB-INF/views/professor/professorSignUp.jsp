<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>교수 회원가입</title>
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
							<p class="text-muted small mb-4">교수 시스템 이용을 위해 정보를 입력해 주세요.</p>

							<form action="${pageContext.request.contextPath}/signup"
								method="post">
								<c:if test="${not empty errorMessage}">
									<div class="alert alert-danger py-2 small text-center"
										role="alert">${errorMessage}</div>
								</c:if>

								<div class="mb-3">
									<label class="form-label small fw-bold">성함</label> <input
										class="form-control" name="professor_name" type="text"
										placeholder="성함을 입력하세요" required>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">이메일 주소</label> <input
										type="email" class="form-control" name="professor_email"
										placeholder="example@email.com" required>
										<div class="form-text text-danger">※ 입력하신 이메일은 로그인 아이디로 사용되니 정확히 입력해 주세요.</div>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">비밀번호</label> <input
										type="password" class="form-control" name="professor_password"
										placeholder="비밀번호를 입력하세요" required>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">재직상태</label> <input
										class="form-control" name="professor_status"
										placeholder="재직상태 입력하세요" required>
								</div>


								<div class="d-grid gap-2 mt-4">
									<button type="submit" class="btn btn-primary">회원가입</button>
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
</body>
</html>