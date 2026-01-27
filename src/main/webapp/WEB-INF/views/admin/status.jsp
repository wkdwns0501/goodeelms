<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학사관리 - GoodeeLMS</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  /* 1번 페이지 기준 커스텀 스타일 */
  .badge-soft-lime {
    background-color: #d1f7c4;
    color: #1f4d2b;
    border: 1px solid #b7efaa;
  }

  .grade-table th {
    background-color: #f8f9fa !important;
    font-weight: 600;
    color: #495057;
  }

  /* 1번 페이지 특유의 여백 유지 */
  .table td {
    padding-top: 8px !important;
    padding-bottom: 8px !important;
    vertical-align: middle;
  }

  .search-card {
    background-color: #ffffff;
    border: 1px solid #dee2e6;
    border-radius: 8px;
  }
  /* 2. 저장 버튼 가로 정렬 보장 */
  .btn-save-custom {
    white-space: nowrap; /* 글자가 밑으로 꺾이지 않게 */
    min-width: 60px;     /* 최소 너비 확보 */
  }
  
  .form-label {
    font-weight: 500;
    font-size: 0.85rem;
    color: #495057;
  }

  /* 테이블 헤더 고정 및 스타일 */
  .table-responsive {
    border-radius: 0 0 8px 8px;
  }
  
  /* 테이블 헤더 폰트 살짝 작게 */
  .grade-table th {
    padding-top: 10px !important;
    padding-bottom: 10px !important;
    font-size: 0.9rem;
  }
</style>

<script type="text/javascript">
function checkForm(form) {
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

function filterNameAndMajor(obj) {
    const regExp = /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]/g;
    if (regExp.test(obj.value)) {
        obj.value = obj.value.replace(regExp, '');
    }
}

function filterOnlyNumber(obj) {
    const regExp = /[^0-9]/g;
    if (regExp.test(obj.value)) {
        obj.value = obj.value.replace(regExp, '');
    }
}
</script>
</head>
<body>
  <%@ include file="/header.jsp" %>
  <%@ include file="/sideNavbar.jsp" %>

  <main class="content">
    <div class="container-fluid">
      <div class="page-shell">
        
        <div class="d-flex align-items-center justify-content-between mb-4">
          <div>
            <h4 class="mb-0"><b>학사 관리</b></h4>
            <small class="text-muted">학생들의 학적 상태(재학, 휴학 등)를 조회하고 변경할 수 있습니다.</small>
          </div>
        </div>

        <div class="card search-card shadow-sm mb-4">
          <div class="card-body p-4">
            <form action="<c:url value='/admin/studentStatus/search'/>" method="get" class="row g-3">
              <div class="col-md-3">
                <label class="form-label">이름</label>
                <input type="text" name="studentName" class="form-control" placeholder="이름 입력"
                       value="<c:out value="${param.studentName}"/>" oninput="filterNameAndMajor(this)">
              </div>

              <div class="col-md-3">
                <label class="form-label">학과</label>
                <input type="text" name="majorName" class="form-control" placeholder="학과 입력"
                       value="<c:out value="${param.majorName}"/>" oninput="filterNameAndMajor(this)">
              </div>

              <div class="col-md-3">
                <label class="form-label">학번</label>
                <input type="text" name="studentNo" class="form-control" placeholder="학번 입력"
                       value="<c:out value="${param.studentNo}"/>" oninput="filterOnlyNumber(this)">
              </div>

              <div class="col-md-3 d-flex align-items-end">
                <button type="submit" class="btn btn-success w-100">조회하기</button>
              </div>
            </form>
          </div>
        </div>

        <div class="card shadow-sm border-0">
          <div class="table-responsive" style="max-height: 500px; overflow-y: auto;">
            <table class="table table-hover align-middle mb-0 text-center grade-table">
              <thead class="sticky-top">
                <tr>
                  <th style="width:12%;">학번</th>
                  <th style="width:13%;">이름</th>
                  <th style="width:20%;">학과</th>
                  <th style="width:15%;">현재 학적</th>
                  <th style="width:40%;">상태 변경 및 사유</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="s" items="${studentList}">
                  <tr>
                    <td>
                      <span class="badge rounded-pill badge-soft-lime px-3">
                        <c:out value="${s.studentNo}"/>
                      </span>
                    </td>
                    <td class="fw-semibold"><c:out value="${s.studentName}"/></td>
                    <td class="text-muted"><c:out value="${s.majorName}"/></td>
                    <td>
                      <span class="badge border text-dark bg-light px-3"><c:out value="${s.studentStatus}"/></span>
                    </td>
                    <td>
                      <form action="<c:url value='/admin/studentStatus/updateStatus'/>" method="post" class="d-flex gap-2 px-2 align-items-center">
                        <select name="newStudentStatus" class="form-select form-select-sm" style="width:85px;">
                          <option value="재학" ${s.studentStatus == '재학' ? 'selected' : ''}>재학</option>
                          <option value="휴학" ${s.studentStatus == '휴학' ? 'selected' : ''}>휴학</option>
                          <option value="졸업" ${s.studentStatus == '졸업' ? 'selected' : ''}>졸업</option>
                          <option value="퇴학" ${s.studentStatus == '퇴학' ? 'selected' : ''}>퇴학</option>
                          <option value="자퇴" ${s.studentStatus == '자퇴' ? 'selected' : ''}>자퇴</option>
                          <option value="재적" ${s.studentStatus == '재적' ? 'selected' : ''}>재적</option>
                        </select>
                        <input type="text" name="statusReason" class="form-control form-control-sm" 
						         placeholder="변경 사유 입력" oninput="filterNameAndMajor(this)">
						 
					    <button type="submit" class="btn btn-sm btn-outline-success px-3 btn-save-custom"
						          onclick="return checkForm(this.form)">저장</button>
                         
                        <input type="hidden" name="searchName" value="<c:out value="${param.studentName}"/>">
                        <input type="hidden" name="searchMajor" value="<c:out value="${param.majorName}"/>">
                        <input type="hidden" name="searchNo" value="<c:out value="${param.studentNo}"/>">     				  
                        <input type="hidden" name="studentId" value="<c:out value="${s.studentId}"/>">
                        <input type="hidden" name="studentName" value="<c:out value="${s.studentName}"/>">
                        <input type="hidden" name="studentNo" value="<c:out value="${s.studentNo}"/>">
                        <input type="hidden" name="majorName" value="<c:out value="${s.majorName}"/>">
                        <input type="hidden" name="currentStatus" value="<c:out value="${s.studentStatus}"/>">
                      </form>
                    </td>
                  </tr>
                </c:forEach>

                <c:if test="${empty studentList}">
                  <tr>
                    <td colspan="5" class="text-center text-muted py-5">
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
  </main>

  <%@ include file="/footer.jsp" %>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>