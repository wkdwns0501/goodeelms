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

				<div class="mb-5">
					<h5 class="fw-bold mb-3">
						<i class="bi bi-clock-history me-2 text-secondary"></i>강의목록
					</h5>
					<div class="table-responsive">
						<table
							class="table table-bordered table-hover align-middle mb-0 text-center">
							
							<thead>
								<tr>
			            <th>강의명</th>
			            <th>전공</th>
			            <th>이수구분</th>
			            <th>학점</th>
			            <th>교수명</th>
			            <th>강의동</th>
			            <th>호수</th>
			            <th>분반</th>
		        		</tr>
							</thead>
							
							<tbody>
								<c:forEach var="entry" items="${lectures}">
									<c:set var="lectureDTO" value="${entry.value}" /> 
									<c:choose>				
										<c:when test="${not empty lectureDTO}">					
											<tr>
												<td>${lectureDTO.lectureName}</td>
												<td>${lectureDTO.majorName}</td>
												<td>${lectureDTO.lectureType}</td>
												<td>${lectureDTO.lectureCredit}</td>
												<td>${lectureDTO.professorName}</td>
												<td>${lectureDTO.buildingName}</td>
												<td>${lectureDTO.lectureRoom}</td>
												<td>${lectureDTO.lectureSection}</td>
										 </tr>
                    </c:when>
										<c:otherwise>
											<tr><td rowspan="">강의에 대한 정보가 없습니다.</td></tr>
										</c:otherwise>
								 </c:choose>
								</c:forEach>
								
								<c:if test="${empty lectures}">
									<tr>
										<td colspan="8" class="text-center text-muted">수강중인 강의 목록이 없습니다.</td>
									</tr>
								</c:if>
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