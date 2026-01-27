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

<style>
.lecture-table {
	table-layout: fixed;
	width: 100%;
}

.lecture-table .truncate {
	display: block;
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
}

.td-room {
	max-width: clamp(9rem, 14vw, 14rem);
}

/* 강의 코드 */
.badge-soft-lime {
	background-color: #d1f7c4; /* 연한 연두 */
	color: #1f4d2b; /* 글자 진초록 */
	border: 1px solid #b7efaa; /* 살짝 테두리 */
}

/* 전공 */
.badge-major {
	background-color: #eef2f6; /* 연한 블루그레이 */
	color: #2f4f6f;
	border: 1px solid #cfd8e3;
	font-weight: 500;
}

/* 교양 */
.badge-general {
	background-color: #f7f5f2; /* 연한 베이지 */
	color: #6b5e4f;
	border: 1px solid #e0dbd3;
	font-weight: 500;
}

.subline {
	font-size: 0.78rem;
}
</style>

</head>
<body>
	<%@ include file="/header.jsp"%>
	<%@ include file="/sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid">
			<div class="page-shell">

				<div class="mb-5">
					<h5 class="fw-bold mb-3">
						<i class="bi bi-clock-history me-2 text-secondary"></i>강의목록
					</h5>
					<div class="table-responsive">
						<table class="table table-hover align-middle mb-0 text-center lecture-table">
							<thead class="table-light">
								<tr>
									<th style="width: 10%;">강의코드</th>
									<th style="width: 20%;">강의명</th>
									<th style="width: 15%;">교수명</th>
									<th style="width: 10%;">유형</th>
									<th style="width: 10%;">학점</th>
									<th style="width: 10%;">분반</th>
									<th style="width: 10%;">강의실</th>
								</tr>
							</thead>
							
							<tbody>
								<c:choose>
									<c:when test="${not empty lectures}">
										<c:forEach var="entry" items="${lectures}">
											<c:set var="lectureDTO" value="${entry.value}" />
											<tr>
												<td><span class="badge badge-soft-lime rounded-pill">${lectureDTO.lectureCode}</span></td>
												<td class="text-center fw-bold text-dark">${lectureDTO.lectureName}</td>
												<td><strong>${lectureDTO.professorName}</strong></td>
												<td><span	class="badge rounded-pill ${lectureDTO.lectureType eq '전공' ? 'badge-major' : 'badge-general'}">
														<c:out value="${lectureDTO.lectureType}" /></span>
												</td>
												<td>${lectureDTO.lectureCredit}</td>
												<td>${lectureDTO.lectureSection}</td>
												<td>${lectureDTO.buildingName} ${lectureDTO.lectureRoom}호</td>
											</tr>
										</c:forEach>
									</c:when>

									<c:otherwise>
										<tr>
											<td colspan="7" class="text-center py-5 text-muted"><i class="bi bi-exclamation-circle d-block mb-2 fs-2"></i> 현재
												수강 중인 강의가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>

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