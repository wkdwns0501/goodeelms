<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학적정보 조회</title>

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
				<h5 class="mb-2">학적사항, 전공변경 이력을 조회합니다.</h5>
				<p class="text-muted mb-3">
					<c:if test="${not empty msg}">
						<div class="alert alert-success alert-dismissible fade show"
							role="alert">${msg}</div>
					</c:if>
				</p>


				<table class="table table-bordered align-middle mb-4">
					<tbody>
						<tr>
							<th class="table-light" style="width: 20%;">전공코드</th>
							<td>${majorList[0]}</td>
						</tr>
						<tr>
							<th class="table-light">전공명</th>
							<td>${majorList[1]}</td>
						</tr>
					</tbody>
				</table>

				<table class="table table-bordered align-middle mb-4">
					<thead>
						<tr class="table-secondary">
							<th>학적상태</th>
							<th>변경사유</th>
							<th>변경일시</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="statusDTO" items="${statusList}">
							<tr>
								<td>${statusDTO.statusType}</td>
								<td>${statusDTO.statusReason}</td>
								<td class="fw">
									<c:choose>
										<c:when test="${not empty statusDTO.statusAt}">
													${statusDTO.formattedStatusAt}</c:when>
										<c:otherwise>-</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty statusList}">
							<tr>
								<td colspan="3" class="text-center">변경 이력이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>

				<c:if test="${not empty majorHistory}">
					<table class="table table-bordered align-middle mb-4">
						<thead>
							<tr class="table-secondary">
								<th>전과 이전 학과</th>
								<th>전과 이후 학과</th>
								<th>전과일시</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>${majorHistory.fromMajorName}</td>
								<td>${majorHistory.toMajorName}</td>
								<td class="fw">
									<c:choose>
										<c:when test="${not empty majorHistory.changedAt}">
													${majorHistory.formattedChangedMajorDate}</c:when>
										<c:otherwise>-</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
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