<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GoodeeLMS</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

</head>
<body>
  <%@ include file="/header.jsp" %>
  <%@ include file="/sideNavbar.jsp" %>
  
  <main class="content">
  <div class="container-fluid">
    <div class="page-shell">
      <h5 class="mb-2">학사관리</h5>
			<!-- 검색 영역 -->
<form action="/student/search" method="get" class="mb-3">
  <div class="row align-items-end g-2">

    <div class="col-md-3">
      <label class="form-label mb-1">이름</label>
      <input type="text" name="studentName" class="form-control form-control-sm">
    </div>

    <div class="col-md-3">
      <label class="form-label mb-1">학과</label>
       <input type="text" name="majorName" class="form-control form-control-sm">
    </div>

    <div class="col-md-3">
      <label class="form-label mb-1">학번</label>
      <input type="text" name="studentNo" class="form-control form-control-sm">
    </div>

    <div class="col-md-3">
      <button type="submit" class="btn btn-sm btn-primary w-30">
        조회
      </button>
    </div>
	<div class="card">
  <div class="card-body p-0">

    <div style="max-height: 400px; overflow-y: auto;">
      <table class="table table-sm table-hover mb-0">
        <thead class="table-light sticky-top">
          <tr>
            <th>학번</th>
            <th>이름</th>
            <th>학과</th>
            <th>학적 상태</th>
            <th>관리</th>
          </tr>
        </thead>

        <tbody>
          <c:forEach var="s" items="${studentList}">
            <tr>
              <td>${s.studentNo}</td>
              <td>${s.studentName}</td>
              <td>${s.majorName}</td>
              <td>${s.studentStatus}</td>
              <td>
                <form action="/student/updateStatus" method="post" class="d-flex gap-1 align-items-center" style=""width: 150px;">				  
				    <select name="studentStatus" class="form-select form-select-sm">
				      <option value="재학"  ${s.studentStatus == '재학' ? 'selected' : ''}>재학</option>
				      <option value="휴학"  ${s.studentStatus == '휴학' ? 'selected' : ''}>휴학</option>
				      <option value="졸업"  ${s.studentStatus == '졸업' ? 'selected' : ''}>졸업</option>
				      <option value="퇴학"  ${s.studentStatus == '퇴학' ? 'selected' : ''}>퇴학</option>
				    </select>
				  <input type="hidden" name="studentNo" value="${s.studentNo}">
				  <button type="submit" class="btn btn-sm btn-outline-primary">저장</button>
				</form>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty studentList}">
            <tr>
              <td colspan="5" class="text-center text-muted py-3">
                조회된 학생이 없습니다.
              </td>
            </tr>
          </c:if>
        </tbody>
      </table>
    </div>

  </div>
</div>
  </div>
</form>

    </div>
  </div>
</main>
  
  <%@ include file="/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>