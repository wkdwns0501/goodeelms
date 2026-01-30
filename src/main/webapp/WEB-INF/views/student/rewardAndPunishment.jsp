<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학사경고/우등이력 - GoodeeLMS</title>

<!-- Bootstrap 5 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

</head>
<body>
	<%@ include file="/header.jsp"%>
	<%@ include file="/sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid">
			<div class="page-shell">

				<c:if test="${not empty param.error && param.error == 'noDTO'}">
					<script type="text/javascript">
						alert("로그인 후 이용 가능합니다.");
					</script>
				</c:if>

				<c:if test="${not empty msg}">
					<div class="alert alert-success alert-dismissible fade show mb-4"
						role="alert">
						${msg}
						<button type="button" class="btn-close" data-bs-dismiss="alert"
							aria-label="Close"></button>
					</div>
				</c:if>

				<div class="mb-5 text-center">
					<h4 class="fw-bold mb-2">
						<i class="bi bi-credit-card-2-front me-2 text-primary"></i>학사경고 / 장학이력
					</h4>
					<p class="text-muted mb-3">학사경고와 장학이력을 조회합니다.</p>
				</div>

				<section class="mb-4">
					<div class="card border-0 shadow-sm">
						<div class="card-header bg-white py-3 border-bottom">
							<h6 class="mb-0 fw-bold">
								<i class="bi bi-gift me-2 text-danger"></i>학사 경고 상세
							</h6>
						</div>
						<div class="table-responsive">
							<table class="table table-hover align-middle mb-0">
								<thead class="table-light">
									<tr class="text-center">
										<th style="width: 30%;">해당학기</th>
										<th style="width: 30%;">평균 점수</th>
									</tr>
								</thead>
								
								<tbody>
									<c:forEach var="probagationDTO" items="${probagation}">
										<tr class="text-center">
											<td>${probagationDTO.lectureYear}-${probagationDTO.lectureSemester}</td> 
											<td>${probagationDTO.score}</td>
										</tr>
									</c:forEach>
								
									<c:if test="${empty probagation}">
										<tr>
											<td colspan="3" class="py-5 text-center text-muted small">학사경고
												내역이 존재하지 않습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</section>

				<section class="mb-4">
					<div class="card border-0 shadow-sm">
						<div class="card-header bg-white py-3 border-bottom">
							<h6 class="mb-0 fw-bold">
								<i class="bi bi-gift me-2 text-danger"></i>장학금 수혜 내역
							</h6>
						</div>
						<div class="table-responsive">
							<table class="table table-hover align-middle mb-0">
								<thead class="table-light">
									<tr class="text-center">
										<th style="width: 30%;">지급 학기</th>
										<th style="width: 40%;">지급 항목</th>
										<th class="text-end pe-5">지급 금액</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="scholarshipDTO" items="${scholarship}">
										<tr class="text-center">
											<td>${scholarshipDTO.formattedSemester}</td>
											<td class="text-muted small">교내 장학금(성적우수)</td>
											<td class="text-end pe-5 fw-bold text-success">+ <fmt:formatNumber
													value="${scholarshipDTO.scholarshipAmount}" type="number" />원
											</td>
										</tr>
									</c:forEach>
									<c:if test="${empty scholarship}">
										<tr>
											<td colspan="3" class="py-5 text-center text-muted small">장학수혜
												내역이 존재하지 않습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</section>

			</div>
		</div>
	</main>

	<%@ include file="/footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>