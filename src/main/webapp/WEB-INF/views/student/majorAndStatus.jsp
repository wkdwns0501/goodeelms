<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학적/전공 - GoodeeLMS</title>

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

				<div
					class="d-flex justify-content-between align-items-center mb-5 pb-3 border-bottom">
					<div>
						<h3 class="fw-bold mb-1 text-dark">
							<i class="bi bi-person-badge me-2 text-primary"></i>학적 및 전공 변동 이력
						</h3>
						<p class="text-muted mb-0">현재 전공 정보와 과거 학적 변동 사항을 확인하실 수 있습니다.</p>
					</div>
				</div>

				<div class="mb-5">
					<h5 class="fw-bold mb-3">
						<i class="bi bi-info-circle me-2 text-primary"></i>현재 소속 정보
					</h5>
					<div class="table-responsive">
						<table class="table table-bordered align-middle mb-0">
							<tbody>
								<tr>
									<th class="table-light ps-4" style="width: 200px;">전공코드</th>
									<td class="ps-4 fw-bold text-dark">${majorList[0]}</td>
								</tr>
								<tr>
									<th class="table-light ps-4">전공명</th>
									<td class="ps-4 fw-bold">${majorList[1]}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="mb-5">
					<h5 class="fw-bold mb-3">
						<i class="bi bi-clock-history me-2 text-secondary"></i>학적 상태 변경 내역
					</h5>
					<div class="table-responsive">
						<table
							class="table table-bordered table-hover align-middle mb-0 text-center">
							<thead class="table-light">
								<tr>
									<th style="width: 25%;">학적상태</th>
									<th style="width: 35%;">변경사유</th>
									<th style="width: 30%;">변경일시</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="statusDTO" items="${statusList}">
									<tr>
										<td><span>${statusDTO.statusType}</span></td>
										<td class="text-center">${statusDTO.statusReason}</td>
										<td class="text-muted"><c:choose>
												<c:when test="${not empty statusDTO.statusAt}">
                                                    ${statusDTO.formattedStatusAt}
                                                </c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose></td>
									</tr>
								</c:forEach>
								<c:if test="${empty statusList}">
									<tr>
										<td colspan="3" class="py-5 text-center text-muted">변경
											이력이 없습니다.</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>

				<c:if test="${not empty majorHistory}">
					<div class="mb-4">
						<h5 class="fw-bold mb-3">
							<i class="bi bi-arrow-left-right me-2"></i>전공 변경(전과) 기록
						</h5>
						<div class="table-responsive border rounded">
							<table class="table table-bordered align-middle mb-0 text-center">
								<thead class="table-light">
									<tr>
										<th style="width: 35%;">전과 이전 학과</th>
										<th style="width: 35%;">전과 이후 학과</th>
										<th style="width: 30%;">전과일시</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="py-3"><span class="fw-bold text-muted">${majorHistory.fromMajorName}</span>
											<i class="bi bi-arrow-right ms-2 text-secondary"></i></td>

										<td class="py-3 bg-light-primary"><span
											class="fw-bold text-primary">${majorHistory.toMajorName}</span>
										</td>

										<td class="text-muted small"><c:choose>
												<c:when test="${not empty majorHistory.changedAt}">
                        ${majorHistory.formattedChangedMajorDate}
                    </c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</c:if>

			</div>
		</div>
	</main>

	<%@ include file="/footer.jsp"%>

	<c:if test="${not empty param.error}">
		<c:choose>
			<c:when test="${param.error == 'noDTO'}">
				<script type="text/javascript">
					alert("로그인 후 이용 가능합니다.");
				</script>
			</c:when>
		</c:choose>
	</c:if>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>