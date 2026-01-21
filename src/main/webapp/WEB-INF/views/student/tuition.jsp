<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
						<i class="bi bi-credit-card-2-front me-2 text-primary"></i>등록금 납부
						및 장학 현황
					</h4>
					<p class="text-muted mb-3">당해 학기 납부 내역과 장학 수혜 정보를 한눈에 확인하세요.</p>
					<span
						class="badge ${tuition.paymentStatus == '납부완료' ? 'bg-success' : 'bg-warning text-dark'} p-2 px-4 fs-6 shadow-sm">
						현재 상태: ${tuition.paymentStatus} </span>
				</div>

				<section class="mb-5">
					<div class="card border-0 shadow-sm mb-3">
						<div
							class="card-body p-4 d-flex justify-content-between align-items-center">
							<span class="text-muted fw-bold"><i
								class="bi bi-dash-square me-2"></i>총 등록금</span>
							<h3 class="fw-bold text-dark mb-0">4,500,000원</h3>
						</div>
					</div>

					<div class="card border-0 shadow-sm bg-light mb-3">
						<div
							class="card-body p-4 d-flex justify-content-between align-items-center">
							<span class="text-muted fw-bold"><i
								class="bi bi-check-circle-fill me-2 text-primary"></i>납부한 금액</span>
							<h3 class="fw-bold text-primary mb-0">
								<fmt:formatNumber value="${tuition.paymentAmount}" type="number" />
								원
							</h3>
						</div>
					</div>

					<div
						class="card border-0 shadow-sm border-start border-danger border-5 mb-3">
						<div
							class="card-body p-4 d-flex justify-content-between align-items-center">
							<span class="text-danger fw-bold"><i
								class="bi bi-exclamation-triangle-fill me-2"></i>미납 잔액</span>
							<h3 class="fw-bold text-danger mb-0">
								<fmt:formatNumber value="${4500000 - tuition.paymentAmount}"
									type="number" />
								원
							</h3>
						</div>
					</div>
				</section>


				<section class="mb-5">
					<div
						class="card border-0 shadow-sm border-top border-primary border-5">
						<div class="card-body p-4">
							<h6 class="fw-bold mb-3">
								<i class="bi bi-wallet2 me-2"></i>등록금 납부하기
							</h6>

							<c:choose>
								<c:when test="${tuition.paymentStatus != '납부완료'}">
									<form
										action="${pageContext.request.contextPath}/student/tuition/pay"
										method="post" class="row g-3">
										<div class="col-md-8">
											<div class="input-group">
												<span class="input-group-text bg-white">₩</span> <input
													type="number" name="payment" id="paymentInput"
													class="form-control" placeholder="납부할 금액을 입력하세요" min="1000"
													max="${4500000 - tuition.paymentAmount}" required
													oninput="formatDisplay(this)">
											</div>
											<div id="amountDisplay"
												class="mt-1 small fw-bold text-primary"
												style="height: 20px;"></div>
											<small class="text-muted mt-1 d-block"> 최대 납부 가능 금액:
												<fmt:formatNumber value="${4500000 - tuition.paymentAmount}"
													type="number" />원 <a href="javascript:void(0);"
												onclick="document.getElementById('paymentInput').value=${4500000 - tuition.paymentAmount}; formatDisplay(document.getElementById('paymentInput'));"
												class="ms-2 badge bg-secondary text-decoration-none">전액
													입력</a>
											</small>
										</div>
										<div class="col-md-4">
											<button type="submit" class="btn btn-primary w-100 fw-bold">
												<i class="bi bi-credit-card me-1"></i> 납입하기
											</button>
										</div>
									</form>
								</c:when>
								<c:otherwise>
									<div class="text-center py-2">
										<p class="text-success mb-0 fw-bold">
											<i class="bi bi-check-all me-1"></i>이번 학기 등록금을 모두 납부하셨습니다.
										</p>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</section>

				<section class="mb-5">
					<div class="card border-0 shadow-sm">
						<div class="card-header bg-white py-3 border-bottom">
							<h6 class="mb-0 fw-bold">
								<i class="bi bi-file-earmark-text me-2 text-secondary"></i>납부 상세
								정보
							</h6>
						</div>
						<div class="table-responsive">
							<table class="table align-middle mb-0">
								<tbody>
									<tr>
										<th class="ps-4 text-muted bg-light" style="width: 250px;">최종
											납부 일시</th>
										<td class="ps-4 fw-bold text-dark"><c:choose>
												<c:when test="${not empty tuition.paymentDate}">${tuition.formattedPaymentDate}</c:when>
												<c:otherwise>
													<span class="text-muted fw-normal">납부 기록 없음</span>
												</c:otherwise>
											</c:choose></td>
									</tr>
									<tr>
										<th class="ps-4 text-muted bg-light">납부 방식</th>
										<td class="ps-4">가상계좌 이체 <small
											class="text-secondary ms-2">(구디은행 ${account})</small>
										</td>
									</tr>
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
											<td>${scholarshipDTO.scholarshipSemester}</td>
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

	<script>
		function formatDisplay(input) {
			const display = document.getElementById('amountDisplay');
			const value = input.value;

			if (value) {
				// 숫자를 3자리마다 콤마 찍기
				const formatted = Number(value).toLocaleString('ko-KR');
				display.innerText = "확인 금액: " + formatted + " 원";
			} else {
				display.innerText = "";
			}
		}
	</script>

</body>
</html>