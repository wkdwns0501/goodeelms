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

<script type="text/javascript">
function checkForm(form) {
    // form.elements['name'] 방식을 사용하면 해당 폼 객체 내부의 요소만 정확히 집어냅니다.
    const currentStatus = form.elements['currentStatus'].value;
    const newStudentStatus = form.elements['newStudentStatus'].value;
    const studentName = form.elements['studentName'].value;
    const studentNo = form.elements['studentNo'].value;
    
    if(currentStatus === newStudentStatus) {
        alert('기존 상태로는 변경할 수 없습니다.');
        return false;
    }
    
    return confirm(
    	    studentName + " (" + studentNo + ") : " +
    	    currentStatus + " -> " +
    	     newStudentStatus + "\n" +
    	    "저장하시겠습니까?"
    	);
}
</script>
</head>
<body>
  <%@ include file="/header.jsp" %>
  <%@ include file="/sideNavbar.jsp" %>

  <main class="content">
    <div class="container-fluid">
      <div class="page-shell">
        <h5 class="mb-2">학사관리</h5>

        <!-- 검색 영역 -->
        <form action="/student/search" method="get" class="row g-2 mb-3">
          <div class="col-md-3">
            <label class="form-label mb-1">이름</label>
            <input type="text" name="studentName" class="form-control form-control-sm" value="${param.studentName}">
          </div>

          <div class="col-md-3">
            <label class="form-label mb-1">학과</label>
            <input type="text" name="majorName" class="form-control form-control-sm" value="${param.majorName}">
          </div>

          <div class="col-md-3">
            <label class="form-label mb-1">학번</label>
            <input type="text" name="studentNo" class="form-control form-control-sm" value="${param.studentNo}">
          </div>

          <div class="col-md-3 d-flex align-items-end" style="width: 100px">
            <button type="submit" class="btn btn-sm btn-primary w-100">조회</button>
          </div>
        </form>

        <!-- 학생 테이블 -->
        <div class="card">
          <div class="card-body p-0">
            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
              <table class="table table-sm table-hover mb-0">
                <thead class="table-light sticky-top">
                  <tr>
                    <th style="width:10%;">학번</th>
                    <th style="width:20%;">이름</th>
                    <th style="width:30%;">학과</th>
                    <th style="width:20%;">학적 상태</th>
                    <th style="width:20%;">관리(사유)</th>
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
                        <form action="/student/updateStatus" method="post" class="d-flex gap-1 align-items-center">
                          <select name="newStudentStatus" class="form-select form-select-sm flex-grow-1">
                            <option value="재학" ${s.studentStatus == '재학' ? 'selected' : ''}>재학</option>
                            <option value="휴학" ${s.studentStatus == '휴학' ? 'selected' : ''}>휴학</option>
                            <option value="졸업" ${s.studentStatus == '졸업' ? 'selected' : ''}>졸업</option>
                            <option value="퇴학" ${s.studentStatus == '퇴학' ? 'selected' : ''}>퇴학</option>
                          </select>
                          <input type="text" name="statusReason" class="form-control form-control-sm">
                         
                          <button type="submit" class="btn btn-sm btn-outline-primary flex-shrink-0" style="width: 100px"
                           onclick="return checkForm(this.form)">저장</button>
                           
                          <input type="hidden" name="searchName" value="${param.studentName}">
      					  <input type="hidden" name="searchMajor" value="${param.majorName}">
        				  <input type="hidden" name="searchNo" value="${param.studentNo}">
        				  
                          <input type="hidden" name="studentId" value="${s.studentId}">
                          <input type="hidden" name="studentName" value="${s.studentName}">
                          <input type="hidden" name="studentNo" value="${s.studentNo}">
                          <input type="hidden" name="majorName" value="${s.majorName}">
                          <input type="hidden" name="currentStatus" value="${s.studentStatus}">
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
        <!-- /학생 테이블 -->

      </div>
    </div>
  </main>

  <%@ include file="/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
