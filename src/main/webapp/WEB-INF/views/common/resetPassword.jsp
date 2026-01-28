<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GoodeeLMS</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />
</head>
<body>
	<%@ include file="/header.jsp"%>
	<%@ include file="/sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid py-5">
			<div class="row justify-content-center">
				<div class="col-md-10 col-lg-8">

					<div class="page-title mb-4 text-center">
						<h4 class="fw-bold text-primary">비밀번호 초기화</h4>
						<p class="text-muted">비밀번호 재설정을 위해 인증을 완료 해주세요.</p>
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
							<form method="post" id="signupForm" action="${pageContext.request.contextPath}/common/resetPassword">
								<input type="hidden" name="studentId" value="<c:out value="${studentId}"/>"> 
								<input type="hidden" name="studentNo" id="studentNoHidden" />
								<input type="hidden" name="studentName" id="studentNamelHidden" /> 

								<div class="p-4 bg-light rounded-3 mb-4 border">
									<h6 class="fw-bold mb-3 text-secondary">개인정보 확인</h6>
									<div class="row g-3">
										<div class="col-md-6">
											<label class="form-label small">학번</label> 
											<input name="studentNo" id="studentNo" class="form-control" placeholder="학번을 입력하세요">
										</div>
										
										<div class="col-md-6">
											<label class="form-label small">주민번호</label>
											 <input name="studentName" id="studentName" class="form-control">
										</div>
									</div>
								</div>

									<button type="submit" class="btn btn-primary btn-lg fw-bold">정보수정</button>
								</form>
							</div>
						</div>
						
					</div>
				</div>
			</div>
	</main>

	<%@ include file="/footer.jsp"%>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>