<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>God</title>

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

				<c:if test="${not empty param.error}">
					<c:choose>
						<c:when test="${param.error == 'noDTO'}">
							<script type="text/javascript">
								alert("로그인 후 이용 가능합니다.");
							</script>
						</c:when>
					</c:choose>
				</c:if>

				<div class="d-flex justify-content-between align-items-center mb-4">
					<p class="text-muted mb-3">
						<c:if test="${not empty msg}">
							<div class="alert alert-success alert-dismissible fade show"
								role="alert">${msg}</div>
						</c:if>
					</p>

					<div>
						<h5 class="mb-1 fw-bold">등록금 납부 현황</h5>
						<p class="text-muted small mb-0">등록금 납부 현황과 장학 정보입니다.</p>
					</div>
					<span>현재 납부 상태: ${tuition.paymentStatus} </span>
				</div>

				<div class="row g-3 mb-4">
					<div class="col-md-4">
						<div class="card border-0 shadow-sm bg-light">
							<div class="card-body text-center p-4">
								<div class="text-muted small mb-1">총 등록금</div>
								<h3 class="fw-bold text-dark">4,500,000원</h3>
							</div>
						</div>
					</div>

					<div class="col-md-4">
						<div class="card border-0 shadow-sm bg-light">
							<div class="card-body text-center p-4">
								<div class="text-muted small mb-1">납부한 금액</div>
								<h3 class="fw-bold text-dark">
									<fmt:formatNumber value="${tuition.paymentAmount}"
										type="number" />
									원
								</h3>
							</div>
						</div>
					</div>

					<div class="col-md-4">
						<div class="card border-0 shadow-sm bg-white border">
							<div class="card-body text-center p-4">
								<div class="text-muted small mb-1">납부 완료까지 남은 금액</div>
								<h3 class="fw-bold text-danger">
									<fmt:formatNumber value="${4500000- tuition.paymentAmount}"
										type="number" />
									원
								</h3>
							</div>
						</div>
					</div>
				</div>

					<div class="card border-0 shadow-sm">
					<div class="card-header bg-white py-3">
						<h6 class="mb-0 fw-bold">
							<i class="bi bi-list-check me-2"></i>납부 상세 정보
						</h6>
					</div>

					<div class="table-responsive">
						<table class="table table-hover align-middle mb-0">
							<thead class="table-light">
								<tr>
									<th class="ps-4">항목</th>
									<th>내용</th>
									<th>비고</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="ps-4 text-muted">최종 납부 일시</td>
									<td class="fw"><c:choose>
											<c:when test="${not empty tuition.paymentDate}">
											${tuition.formattedPaymentDate}
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose></td>
									<td>-</td>
								</tr>
								<tr>
									<td class="ps-4 text-muted">납부 방식</td>
									<td>가상계좌 이체</td>
									<td>구디은행 123-456-7890123</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				
				<div class="card border-0 shadow-sm">
					<div class="card-header bg-white py-3">
						<h6 class="mb-0 fw-bold">
							<i class="bi bi-list-check me-2"></i>장학 정보
						</h6>
					</div>

					<div class="table-responsive">
						<table class="table table-hover align-middle mb-0">
							<thead class="table-light">
								<tr>
									<th>지급 학기</th>
									<th>지급 금액</th>
								</tr>
							</thead>
							
							<tbody>
								<c:forEach var="scholarshipDTO" items="${scholarship}">
									<tr>
										<td>${scholarshipDTO.scholarshipSemester}</td>
										<td>${scholarshipDTO.scholarshipAmount}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				
				
				
			</div>
		</div>
	</main>

	<%@ include file="/footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>