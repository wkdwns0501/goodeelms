<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학생 관리 - GoodeeLMS</title>

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
    font-size: 0.85rem;
  }

  /* 행 간격 조절 (콤팩트) */
  .table td {
    padding-top: 8px !important;
    padding-bottom: 8px !important;
    vertical-align: middle;
    font-size: 0.85rem;
  }

  /* 카드 및 폼 스타일 */
  .card {
    border: none;
    box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
    border-radius: 8px;
  }
  
  .card-header {
    background-color: #ffffff;
    border-bottom: 1px solid #f0f0f0;
    padding: 1rem;
  }

  .form-label {
    font-weight: 600;
    font-size: 0.8rem;
    color: #555;
    margin-bottom: 0.4rem;
  }

  .invalid-feedback {
    font-size: 0.75rem !important;
    color: #dc3545 !important;
    font-weight: 500;
    margin-top: 0.2rem;
  }

  /* 검색 영역 전용 카드 */
  .search-section {
    background-color: #fcfcfc;
    border-bottom: 1px solid #eee;
    padding: 15px;
  }
</style>
</head>
<body>
  <%@ include file="/header.jsp" %>
  <%@ include file="/sideNavbar.jsp" %>

  <main class="content">
    <div class="container-fluid">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <div>
          <h4 class="mb-0"><b>학생 계정 관리</b></h4>
          <small class="text-muted">신규 학생을 등록하거나 기존 학생 명단을 조회할 수 있습니다.</small>
        </div>
      </div>

      <div class="row g-4">
        <div class="col-md-4">
          <div class="card h-100">
            <div class="card-header">
              <h6 class="mb-0 text-primary"><b><i class="bi bi-person-plus"></i> 학생 정보 등록</b></h6>
            </div>
            <div class="card-body p-4">
              <form action="<c:url value='/admin/addStudent/register'/>" method="post" id="studentForm" 
                    onsubmit="return validateStudentForm()" novalidate>

                <div class="row mb-3">
                  <div class="col-md-6">
                    <label class="form-label">이름</label>
                    <input type="text" name="studentName" class="form-control form-control-sm" oninput="filterSpecialChars(this)">
                    <div class="invalid-feedback">이름을 입력해주세요.</div>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">성별</label>
                    <select name="studentGender" class="form-select form-select-sm">
                      <option value="">선택</option>
                      <option value="남">남</option>
                      <option value="여">여</option>
                    </select>
                    <div class="invalid-feedback">성별을 선택해주세요.</div>
                  </div>
                </div>

                <div class="mb-3">
                  <label class="form-label">학과</label>
                  <select name="majorId" class="form-select form-select-sm">
                    <option value="" selected disabled>-- 학과 선택 --</option>
                    <c:forEach var="m" items="${majorList}">
                      <option value="${m.majorId}"><c:out value="${m.majorName}"/></option>
                    </c:forEach>
                  </select>
                  <div class="invalid-feedback">학과를 선택해주세요.</div>
                </div>

                <div class="row mb-3">
                  <div class="col-md-6">
                    <label class="form-label">주민번호 앞자리</label>
                    <input type="text" name="identityFront" class="form-control form-control-sm" maxlength="6" pattern="[0-9]{6}">
                    <div class="invalid-feedback">앞 6자리를 입력해주세요.</div>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">주민번호 뒷자리</label>
                    <input type="text" name="identityBack" class="form-control form-control-sm" maxlength="7" pattern="[0-9]{7}">
                    <div class="invalid-feedback">필수 입력란입니다.</div>
                  </div>
                </div>

                <div class="row mb-4">				
                  <div class="col-md-6">
                    <label class="form-label">학번</label>
                    <input type="text" name="studentNo" class="form-control form-control-sm">
                    <div class="invalid-feedback">학번을 입력해주세요.</div>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">핸드폰 번호</label>
                    <input type="text" name="studentPhone" class="form-control form-control-sm" placeholder="010-0000-0000" maxlength="13" oninput="autoHyphen(this)">
                    <div class="invalid-feedback">연락처를 입력해주세요.</div>
                  </div>
                </div>

                <div class="d-grid mt-2">
                  <button type="submit" class="btn btn-primary">학생 등록하기</button>
                </div>					
              </form>
            </div>
          </div>
        </div>

        <div class="col-md-8">
          <div class="card h-100">
            <div class="search-section rounded-top">
              <form action="<c:url value='/admin/addStudent/search'/>" method="get" class="row g-2">
                <div class="col-md-3">
                  <input type="text" name="studentName" class="form-control form-control-sm" placeholder="이름 검색" value="<c:out value="${param.studentName}"/>" oninput="filterNameAndMajor(this)">
                </div>
                <div class="col-md-3">
                  <input type="text" name="majorName" class="form-control form-control-sm" placeholder="학과 검색" value="<c:out value="${param.majorName}"/>" oninput="filterNameAndMajor(this)">
                </div>
                <div class="col-md-3">
                  <input type="text" name="studentNo" class="form-control form-control-sm" placeholder="학번 검색" value="<c:out value="${param.studentNo}"/>" oninput="filterOnlyNumber(this)">
                </div>
                <div class="col-md-3 d-grid">
                  <button class="btn btn-sm btn-success">조회하기</button>
                </div>
              </form>
            </div>

            <div class="card-body p-0">
              <div class="table-responsive" style="max-height: 550px;">
                <table class="table table-hover align-middle mb-0 text-center grade-table">
                  <thead class="sticky-top">
                    <tr>
                      <th style="width: 120px;">학번</th>
                      <th style="width: 60px;">이름</th>
                      <th style="width: 140px;">주민번호</th>
                      <th style="width: 190px;">전공</th>
                      <th style="width: 130px;">핸드폰</th>
                      <th style="width: 60px;">성별</th>
                      <th style="width: 90px;">상태</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="s" items="${studentList}">
                      <tr>
                        <td><span class="badge rounded-pill badge-soft-lime px-2"><c:out value="${s.studentNo}"/></span></td>
                        <td class="fw-semibold"><c:out value="${s.studentName}"/></td>
                        <td class="text-muted"><c:out value="${s.studentIdentityNumber}"/></td>
                        <td class="text-start ps-3"><c:out value="${s.majorName}"/></td>
                        <td><c:out value="${s.studentPhone}"/></td>
                        <td><c:out value="${s.studentGender}"/></td>
                        <td><span class="badge border text-dark bg-light px-2"><c:out value="${s.studentStatus}"/></span></td>
                      </tr>
                    </c:forEach>
                    <c:if test="${empty studentList}">
                      <tr>
                        <td colspan="7" class="text-center text-muted py-5">조회된 학생이 없습니다.</td>
                      </tr>
                    </c:if>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </main>

  <%@ include file="/footer.jsp" %>
  <script src="<c:url value='/resources/js/studentRegister.js'/>"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script type="text/javascript" src="/resources/js/loadLectures.js"></script>
</body>
</html>