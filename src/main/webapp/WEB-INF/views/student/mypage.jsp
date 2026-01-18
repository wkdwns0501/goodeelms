<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>

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
				<h5 class="mb-4">
					<i class="bi bi-person-badge"></i> 마이페이지 (학생 정보)
				</h5>

				<div class="card shadow-sm">
					<div class="card-header bg-white">
						<h6 class="card-title mb-0">기본 인적 사항</h6>
					</div>
					<div class="card-body">
						<div class="table-responsive">
							<table class="table table-bordered align-middle">
								<colgroup>
									<col style="width: 20%; background-color: #f8f9fa;">
									<col style="width: 30%;">
									<col style="width: 20%; background-color: #f8f9fa;">
									<col style="width: 30%;">
								</colgroup>
								<tbody>
									<tr>
										<th class="text-center">학번</th>
										<td>${sessionScope.studentDTO.studentNo}</td>
										<th class="text-center">성명</th>
										<td>${sessionScope.studentDTO.studentName}</td>
									</tr>
									<tr>
										<th class="text-center">주민번호</th>
										<td>${sessionScope.studentDTO.studentIdentityNumber}</td>
										<th class="text-center">성별</th>
										<td>${sessionScope.studentDTO.studentGender}</td>
									</tr>
									<tr>
										<th class="text-center">연락처</th>
										<td>${sessionScope.studentDTO.studentPhone}</td>
										<th class="text-center">이메일</th>
										<td>${sessionScope.studentDTO.studentEmail}</td>
									</tr>
									<tr>
										<th class="text-center">학적 상태</th>
										<td>${sessionScope.studentDTO.studentStatus} </td>
										<th class="text-center">학생 계좌</th>
										<td>${sessionScope.studentDTO.studentBank}</td>
									</tr>
									<tr>
										<th class="text-center">주소</th>
										<td colspan="3">${sessionScope.studentDTO.studentAddress}</td>
									</tr>
								</tbody>
							</table>
						</div>

						<div class="d-flex justify-content-end gap-2 mt-3">
							<button type="button" class="btn btn-sm btn-outline-primary">정보수정 </button>
						</div>
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