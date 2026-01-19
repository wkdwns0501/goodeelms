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

							<form
								action="${pageContext.request.contextPath}/professor/signup"
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
									<div class="form-text text-danger">※ 입력하신 이메일은 로그인 아이디로
										사용되니 정확히 입력해 주세요.</div>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">비밀번호</label> <input
										type="password" class="form-control" name="professor_password"
										placeholder="비밀번호를 입력하세요" required>
								</div>

								<div class="mb-3">
									<label class="form-label small fw-bold">전공</label> <select
										class="form-select" name="major_id" required>
										<option value="" selected disabled>전공을 선택하세요</option>
										<option value="1">컴퓨터공학과</option>
										<option value="2">소프트웨어학과</option>
										<option value="3">정보통신공학과</option>
										<option value="4">인공지능학과</option>
										<option value="5">전기공학과</option>
										<option value="6">전자공학과</option>
										<option value="7">기계공학과</option>
										<option value="8">신소재공학과</option>
										<option value="9">화학공학과</option>
										<option value="10">건축학과</option>
										<option value="11">경영학과</option>
										<option value="12">경제학과</option>
										<option value="13">회계학과</option>
										<option value="14">국제통상학과</option>
										<option value="15">행정학과</option>
										<option value="16">법학과</option>
										<option value="17">정치외교학과</option>
										<option value="18">심리학과</option>
										<option value="19">사회복지학과</option>
										<option value="20">국어국문학과</option>
										<option value="21">영어영문학과</option>
										<option value="22">사학과</option>
										<option value="23">철학과</option>
										<option value="24">미디어커뮤니케이션학과</option>
										<option value="25">시각디자인학과</option>
										<option value="26">산업디자인학과</option>
										<option value="27">음악학과</option>
										<option value="28">체육학과</option>
										<option value="29">간호학과</option>
										<option value="30">의생명공학과</option>
									</select>
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