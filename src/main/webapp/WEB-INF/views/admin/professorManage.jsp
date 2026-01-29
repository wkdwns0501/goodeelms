<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계정 관리 - GoodeeLMS</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  /* 1번 페이지 기준 공통 스타일 */
  .badge-soft-lime {
    background-color: #d1f7c4;
    color: #1f4d2b;
    border: 1px solid #b7efaa;
  }

  .grade-table th {
    background-color: #f8f9fa !important;
    font-weight: 600;
    color: #495057;
    padding-top: 10px !important;
    padding-bottom: 10px !important;
    font-size: 0.9rem;
  }

  /* 행 두께 콤팩트 조절 */
  .table td {
    padding-top: 6px !important;
    padding-bottom: 6px !important;
    vertical-align: middle;
  }

  .search-card {
    background-color: #ffffff;
    border: 1px solid #dee2e6;
    border-radius: 8px;
  }

  .form-label {
    font-weight: 500;
    font-size: 0.85rem;
    color: #495057;
  }

  /* 저장 버튼 스타일 */
  .btn-save-custom {
    white-space: nowrap;
    min-width: 60px;
  }

  .email-text {
    color: #0d6efd;
    font-size: 0.9rem;
  }
  
  /* select 박스 너비를 조금 더 여유 있게 (85px -> 100px) */
  .status-select {
    width: 100px !important;
  }

  /* 저장 버튼 너비를 고정해서 안정감 부여 */
  .btn-save-fixed {
    width: 70px;
    white-space: nowrap;
  }
  
  /* 폼 내부 요소들이 중앙에 모이도록 정렬 */
  .manage-form {
    display: flex;
    gap: 10px;
    align-items: center;
    justify-content: center; /* 왼쪽 쏠림 방지: 중앙 정렬 */
    width: 100%;
  }
</style>

<script type="text/javascript">
function checkForm(form) {
    const currentStatus = form.elements['currentStatus'].value;
    const newProfessorStatus = form.elements['newProfessorStatus'].value;
    const professorName = form.elements['professorName'].value;
    const majorName = form.elements['majorName'].value;
    
    if(currentStatus === newProfessorStatus) {
        alert('기존 상태로는 변경할 수 없습니다.');
        return false;
    }
    
    return confirm(
        professorName + " (" + majorName + ") : " +
        currentStatus + " -> " +
        newProfessorStatus + "\n" +
        "저장하시겠습니까?"
    );
}

function filterNameAndMajor(obj) {
    const regExp = /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]/g;
    if (regExp.test(obj.value)) {
        obj.value = obj.value.replace(regExp, '');
    }
}

function filterEmail(obj) {
    const regExp = /[^a-zA-Z0-9@._-]/g;
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
            <h4 class="mb-0"><b>교수 계정 관리</b></h4>
            <small class="text-muted">교수진의 재직 상태 및 개인 정보를 조회하고 관리합니다.</small>
          </div>
        </div>

        <div class="card search-card shadow-sm mb-4">
          <div class="card-body p-4">
            <form action="<c:url value='/admin/professorManage/search'/>" method="get" class="row g-3">
              <div class="col-md-3">
                <label class="form-label">이름</label>
                <input type="text" name="professorName" class="form-control" placeholder="이름 입력"
                       value="<c:out value="${param.professorName}"/>" oninput="filterNameAndMajor(this)">
              </div>

              <div class="col-md-3">
                <label class="form-label">학과</label>
                <input type="text" name="majorName" class="form-control" placeholder="학과 입력"
                       value="<c:out value="${param.majorName}"/>" oninput="filterNameAndMajor(this)">
              </div>

              <div class="col-md-3">
                <label class="form-label">이메일</label>
                <input type="text" name="professorEmail" class="form-control" placeholder="이메일 입력"
                       value="<c:out value="${param.professorEmail}"/>" oninput="filterEmail(this)">
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
                  <th style="width:12%;">이름</th>
                  <th style="width:18%;">학과</th>
                  <th style="width:30%;">이메일</th>
                  <th style="width:15%;">현재 상태</th>
                  <th style="width:25%;">상태 변경 관리</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="s" items="${professorList}">
                  <tr>
                    <td class="fw-semibold"><c:out value="${s.professorName}" /></td>
                    <td class="text-muted"><c:out value="${s.majorName}" /></td>
                    <td class="email-text"><c:out value="${s.professorEmail}" /></td>
                    <td>
                      <span class="badge border text-dark bg-light px-3"><c:out value="${s.professorStatus}" /></span>
                    </td>
                    <td>
					<form action="<c:url value='/admin/professorManage/updateStatus'/>" method="post" class="manage-form">
					    <select name="newProfessorStatus" class="form-select form-select-sm status-select">
                          <option value="재직" ${s.professorStatus == '재직' ? 'selected' : ''}>재직</option>
                          <option value="휴직" ${s.professorStatus == '휴직' ? 'selected' : ''}>휴직</option>
                          <option value="계약" ${s.professorStatus == '계약' ? 'selected' : ''}>계약</option>
                          <option value="위촉" ${s.professorStatus == '위촉' ? 'selected' : ''}>위촉</option>
                          <option value="퇴직" ${s.professorStatus == '퇴직' ? 'selected' : ''}>퇴직</option>
                        </select>
                       
                        <button type="submit" class="btn btn-sm btn-outline-success btn-save-fixed"
            					onclick="return checkForm(this.form)">저장</button>
                                                     
                        <input type="hidden" name="professorId" value ="<c:out value="${s.professorId}"/>">
                        <input type="hidden" name="professorEmail" value ="<c:out value="${s.professorEmail}"/>">
                        <input type="hidden" name="currentStatus" value ="<c:out value="${s.professorStatus}"/>">
                        <input type="hidden" name="majorName" value ="<c:out value="${s.majorName}" />">
                        <input type="hidden" name="professorName" value ="<c:out value="${s.professorName}" />">
                        <input type="hidden" name="searchName" value ="<c:out value="${param.professorName}"/>">
                        <input type="hidden" name="searchMajor" value ="<c:out value="${param.majorName}" />">
                        <input type="hidden" name="searchEmail" value ="<c:out value="${param.professorEmail}"/>">     				  
                      </form>
                    </td>
                  </tr>
                </c:forEach>

                <c:if test="${empty professorList}">
                  <tr>
                    <td colspan="5" class="text-center text-muted py-5">
                      조회된 교수가 없습니다.
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