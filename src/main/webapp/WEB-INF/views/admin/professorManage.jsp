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
	<%-- 폼 내부 value 값 저장 --%>
    const currentStatus = form.elements['currentStatus'].value;
    const newProfessorStatus = form.elements['newProfessorStatus'].value;
    const professorName = form.elements['professorName'].value;
    const majorName = form.elements['majorName'].value;
    
    <%-- 기존 상태에 두고 저장 누를 시 return --%>
    if(currentStatus === newProfessorStatus) {
        alert('기존 상태로는 변경할 수 없습니다.');
        return false;
    }
    
    <%-- 다를 시 confirm 창 진행 --%>
    return confirm(
    		professorName + " (" + majorName + ") : " +
    	    currentStatus + " -> " +
    	    newProfessorStatus + "\n" +
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
        <div>
		 <h5 class="mb-1">계정관리</h5>
	    </div>

        <!-- 검색 영역 -->
        <form action="<c:url value='/professorManage/search'/>" method="get" class="row g-2 mb-3">
          <div class="col-md-3">
            <label class="form-label mb-1">이름</label>
            <input type="text" name="professorName" class="form-control form-control-sm" value="${param.professorName}">
          </div>

          <div class="col-md-3">
            <label class="form-label mb-1">학과</label>
            <input type="text" name="majorName" class="form-control form-control-sm" value="${param.majorName}">
          </div>

          <div class="col-md-3">
            <label class="form-label mb-1">이메일</label>
            <input type="text" name="professorEmail" class="form-control form-control-sm" value="${param.professorEmail}">
          </div>

          <div class="col-md-3 d-flex align-items-end" style="width: 100px">
            <button type="submit" class="btn btn-sm btn-primary w-100">조회</button>
          </div>
        </form>

        <!-- 학생 테이블 출력 영역 -->
        <div class="card">
          <div class="card-body p-0">
            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
              <table class="table table-sm table-hover mb-0">
                <thead class="table-light sticky-top">
                  <tr>
                    <th style="width:10%;">이름</th>
                    <th style="width:20%;">학과</th>
                    <th style="width:30%;">이메일</th>
                    <th style="width:20%;">재직 상태</th>
                    <th style="width:20%;">관리</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="s" items="${professorList}">
                    <tr>
                      <td>${s.professorName}</td>
                      <td>${s.majorName}</td>
                      <td>${s.professorEmail}</td>
                      <td>${s.professorStatus}</td>
                      <td>
                        <form action="<c:url value='/professorManage/updateStatus'/>" method="post" class="d-flex gap-1 align-items-center">
                          <select name="newProfessorStatus" class="form-select form-select-sm flex-grow-1">
                            <option value="재직" ${s.professorStatus == '재직' ? 'selected' : ''}>재직</option>
                            <option value="휴직" ${s.professorStatus == '휴직' ? 'selected' : ''}>휴직</option>
                            <option value="계약" ${s.professorStatus == '계약' ? 'selected' : ''}>계약</option>
                            <option value="위촉" ${s.professorStatus == '위촉' ? 'selected' : ''}>위촉</option>
                          </select>
                         
                          <button type="submit" class="btn btn-sm btn-outline-primary flex-shrink-0" style="width: 100px"
                           onclick="return checkForm(this.form)">저장</button>
                                                     
                          <input type="hidden" name="professorId" value="${s.professorId}">
                          <input type="hidden" name="professorEmail" value="${s.professorEmail}">
                          <input type="hidden" name="currentStatus" value="${s.professorStatus}">
                          <input type="hidden" name="majorName" value="${s.majorName}">
                          <input type="hidden" name="professorName" value="${s.professorName}">
                          <!-- 현재 검색 정보 hidden으로 같이 넘기기 -->
                          <input type="hidden" name="searchName" value="${param.professorName}">
      					  <input type="hidden" name="searchMajor" value="${param.majorName}">
        				  <input type="hidden" name="searchEmail" value="${param.professorEmail}">     				  
        				  <%--
                          <input type="hidden" name="studentId" value="${s.studentId}">
                          <input type="hidden" name="studentName" value="${s.studentName}">
                          <input type="hidden" name="studentNo" value="${s.studentNo}">
                          <input type="hidden" name="majorName" value="${s.majorName}">
                          <input type="hidden" name="currentStatus" value="${s.studentStatus}"> --%>
                        </form>
                      </td>
                    </tr>
                  </c:forEach>

                  <c:if test="${empty professorList}">
                    <tr>
                      <td colspan="5" class="text-center text-muted py-3">
                        조회된 교수가 없습니다.
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
