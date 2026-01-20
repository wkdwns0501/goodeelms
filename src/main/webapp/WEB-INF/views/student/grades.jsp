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

</head>
<body>
	<%@ include file="/header.jsp"%>
	<%@ include file="/sideNavbar.jsp"%>

	<main class="content">
		<div class="container-fluid">
			<div class="page-shell">
				<h5 class="mb-2">강의 내역 조회</h5>
				<p class="text-muted mb-3">
				<table class="table table-hover align-middle">
					<thead class="table-dark">
						<tr>
							<th>연도</th>
							<th>학기</th>
							<th>강의코드</th>
							<th>강의명</th>
							<th>성적</th>
							<th>재수강 여부</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="entry" items="${grade}">
						    <c:set var="lecture" value="${entry.value}" /> 
						    <tr>
						        <td>${lecture.lectureYear}</td>
						        <td>${lecture.lectureSemester}학기</td>
						        <td>${lecture.lectureCode}</td>
						        <td>${lecture.lectureName}</td>
						        
						        <td class="fw-bold text-primary">
						            <c:choose>
						                <c:when test="${lecture.score == 4.5}"><span class="text-success">4.5 (A+)</span></c:when>
						                <c:when test="${lecture.score == 4.0}"><span class="text-success">4.0 (A)</span></c:when>
						                <c:when test="${lecture.score == 3.5}"><span class="text-success">3.5 (B+)</span></c:when>
						                <c:when test="${lecture.score == 3.0}"><span class="text-success">3.0 (B)</span></c:when>
						                <c:when test="${lecture.score == 2.5}"><span class="text-success">2.5 (C+)</span></c:when>
						                <c:when test="${lecture.score == 2.0}"><span class="text-success">2.0 (C)</span></c:when>
						                <c:otherwise><span class="text-danger"> ${lecture.score} (F) </span></c:otherwise>
						            </c:choose>
						        </td>
						
						        <td>
						            <c:choose>
						                <c:when test="${lecture.score <= 3.0}">
						                    <span class="badge bg-info">재수강 가능</span>
						                </c:when>
						                <c:otherwise>
						                    <span></span>
						                </c:otherwise>
						            </c:choose>
						        </td>
						    </tr>
						</c:forEach>
						<c:if test="${empty grade}">
							<tr>
								<td colspan="5" class="text-center">수강 이력이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
				</p>


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