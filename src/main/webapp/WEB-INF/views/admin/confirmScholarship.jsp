<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장학 관리 - GoodeeLMS</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  /* 1번 페이지 기준 공통 스타일 */
  .badge-soft-lime {
    background-color: #d1f7c4;
    color: #1f4d2b;
    border: 1px solid #b7efaa;
  }

  /* 테이블 헤더 스타일 */
  .grade-table th {
    background-color: #f8f9fa !important;
    font-weight: 600;
    color: #495057;
    padding-top: 10px !important;
    padding-bottom: 10px !important;
    font-size: 0.9rem;
  }

  /* 행 간격 조절 (콤팩트) */
  .table td {
    padding-top: 8px !important;
    padding-bottom: 8px !important;
    vertical-align: middle;
  }

  /* 카드 및 탭 스타일 */
  .card {
    border: none;
    box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
    border-radius: 8px;
  }
  
  .nav-tabs .nav-link {
    color: #6c757d;
    font-weight: 500;
  }
  
  .nav-tabs .nav-link.active {
    color: #198754; /* 1번 스타일의 그린 포인트 */
    font-weight: 700;
    border-bottom: 3px solid #198754;
  }

  .semester-info {
    background-color: #f8f9fa;
    border-radius: 6px;
    padding: 10px 15px;
    font-weight: 600;
    color: #495057;
    display: inline-block;
  }

  /* 스위치 스타일 커스텀 */
  .form-check-input:checked {
    background-color: #198754;
    border-color: #198754;
  }
</style>

<script>
  // ... (기존 generateSemesterOptions 및 스크립트 로직 동일) ...
  // 중복 선언 방지를 위해 window.onload 하나로 합치는 것이 좋습니다.
  
  function generateSemesterOptions() {
    const select = document.getElementById("semesterSelect");
    if(!select) return; // select가 있는 경우만 실행
    const startYear = 2020;
    const now = new Date();
    const currentYear = now.getFullYear();
    const currentMonth = now.getMonth() + 1;
    let semesters = [];
    for (let year = startYear; year <= currentYear; year++) {
      if (year < currentYear || (year === currentYear && currentMonth >= 8)) {
        semesters.push({value: year + "_1", text: year + "년 1학기" });
      }
      const isPastYear = year < (currentYear - 1);
      const isLastYearAndReady = (year === currentYear - 1 && currentMonth >= 2);
      if (isPastYear || isLastYearAndReady) {
        semesters.push({ value: year + "_2", text: year + "년 2학기" });
      }
    }
    semesters.sort((a, b) => b.value.localeCompare(a.value));
    semesters.forEach(s => {
      const option = document.createElement("option");
      option.value = s.value;
      option.textContent = s.text;
      if ("${currentSemester}" === s.value) option.selected = true;
      select.appendChild(option);
    });
  }

  document.addEventListener("DOMContentLoaded", function() {
      // 1. 옵션 생성
      generateSemesterOptions();

      // 2. 전체 선택 로직
      const selectAll = document.getElementById("selectAll");
      const switches = document.querySelectorAll(".custom-switch");
      if (selectAll) {
          selectAll.addEventListener("change", function() {
              switches.forEach(sw => {
                  if (!sw.disabled) sw.checked = selectAll.checked;
              });
          });
      }

      // 3. 탭 파라미터 확인
      const urlParams = new URLSearchParams(window.location.search);
      const tab = urlParams.get('tab');
      if (tab === 'manage') {
          const manageTabBtn = document.querySelector('button[data-bs-target="#tab-manage"]');
          if (manageTabBtn) {
              new bootstrap.Tab(manageTabBtn).show();
          }
      }
  });
  
  function validateConfirmForm(form) {
	    // disabled가 아닌 체크박스 중 체크된 것만 찾기
	    const checkedBoxes = form.querySelectorAll('input[name="checkConfirmed"]:checked:not(:disabled)');
	    
	    if (checkedBoxes.length === 0) {
	        alert("새롭게 확정할 학생이 없습니다.\n이미 모두 확정되었거나 선택된 학생이 없습니다.");
	        return false; // 제출 중단
	    }
	    
	    return confirm(checkedBoxes.length + "명의 학생을 장학생으로 확정하시겠습니까?");
	}
</script>
</head>

<body>
<%@ include file="/header.jsp" %>
<%@ include file="/sideNavbar.jsp" %>

<main class="content">
  <div class="container-fluid">
    
    <div class="d-flex align-items-center justify-content-between mb-4">
      <div>
        <h4 class="mb-0"><b>장학 관리</b></h4>
        <small class="text-muted">학기별 성적 우수 학생을 장학생으로 확정하고 관리합니다.</small>
      </div>
      <div class="semester-info shadow-sm">
        <i class="bi bi-calendar-check"></i> 조회 학기 : ${nowYear}년 ${nowSemester}학기
      </div>
    </div>

    <ul class="nav nav-tabs mb-4" id="scholarshipTab" role="tablist">
      <li class="nav-item">
        <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#tab-register" type="button">신규 등록/조회</button>
      </li>
      <li class="nav-item">
        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#tab-manage" type="button">변경/관리</button>
      </li>
    </ul>

    <div class="tab-content">
      <div class="tab-pane fade show active" id="tab-register" role="tabpanel">
        <div class="card shadow-sm border-0">
          <form action="<c:url value='/admin/confirmScholarship/confirm'/>" method="post">
            <input type="hidden" name="yearSemester" value="${currentSemester}">   
            <div class="table-responsive" style="max-height: 550px; overflow-y: auto;">
              <table class="table table-hover align-middle mb-0 text-center grade-table">
                <thead class="sticky-top">
                  <tr>
                    <th style="width: 15%;">학번</th>
                    <th style="width: 15%;">이름</th>
                    <th style="width: 25%;">학과</th>
                    <th style="width: 20%;">평점 (4.5)</th>
                    <th style="width: 25%;">장학생 확정</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="s" items="${scholarshipList}">
                    <tr>
                      <td><span class="badge rounded-pill badge-soft-lime px-2">${s.studentNo}</span></td>
                      <td class="fw-semibold">${s.studentName}</td>
                      <td class="text-muted">${s.majorName}</td>
                      <td><b class="text-primary">${s.gpa}</b></td>
                      <td>
                        <div class="form-check form-switch d-inline-block">
                          <input class="form-check-input custom-switch" type="checkbox" name="checkConfirmed" 
                                 value="${s.studentId}" id="switch_${s.studentId}"
                                 ${s.isConfirmed == 'Y' ? 'checked disabled' : ''}>
                        </div>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>

            <div class="card-footer d-flex justify-content-between align-items-center bg-light border-top-0 p-3">
                <div class="form-check form-switch ms-2">
                  <input class="form-check-input" type="checkbox" id="selectAll">
                  <label class="form-check-label small fw-bold" for="selectAll">전체 선택</label>
                </div>
                <button type="submit" class="btn btn-success px-4" onclick="return validateConfirmForm(this.form)">
                  장학생 확정 저장
                </button>
            </div>  
          </form>
        </div>
      </div>

      <div class="tab-pane fade" id="tab-manage" role="tabpanel">
        <div class="card shadow-sm border-0">
          <div class="table-responsive" style="max-height: 550px; overflow-y: auto;">
            <table class="table table-hover align-middle mb-0 text-center grade-table">
              <thead class="sticky-top">
                <tr>
                  <th style="width: 15%;">학번</th>
                  <th style="width: 15%;">이름</th>
                  <th style="width: 25%;">학과</th>
                  <th style="width: 15%;">평점 (4.5)</th>
                  <th style="width: 15%;">상태</th>
                  <th style="width: 15%;">관리</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="s" items="${scholarshipList}">
                  <tr>
                    <td><span class="badge rounded-pill badge-soft-lime px-2">${s.studentNo}</span></td>
                    <td class="fw-semibold">${s.studentName}</td>
                    <td class="text-muted">${s.majorName}</td>
                    <td><b>${s.gpa}</b></td>
                    <td>
                      <c:choose>
                        <c:when test="${s.isConfirmed == 'Y'}">
                          <span class="badge bg-success-subtle text-success border border-success-subtle px-3">장학생</span>
                        </c:when>
                        <c:otherwise>
                          <span class="badge bg-light text-muted border px-3">일반</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <form action="<c:url value='/admin/confirmScholarship/manage'/>" method="post">
                        <input type="hidden" name="studentId" value="${s.studentId}">
                        <input type="hidden" name="yearSemester" value="${currentSemester}">
                        <c:choose>
                          <c:when test="${s.isConfirmed == 'Y'}">
                            <input type="hidden" name="action" value="delete">
                            <button type="submit" class="btn btn-outline-danger btn-sm px-3" onclick="return confirm('장학 취소를 진행하시겠습니까?')">취소</button>
                          </c:when>
                          <c:otherwise>
                            <input type="hidden" name="action" value="insert">
                            <button type="submit" class="btn btn-outline-success btn-sm px-3" onclick="return confirm('장학생으로 등록하시겠습니까?')">등록</button>
                          </c:otherwise>
                        </c:choose>
                      </form>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div> 
        </div>
      </div>
    </div>
  </div>
</main>

<%@ include file="/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>