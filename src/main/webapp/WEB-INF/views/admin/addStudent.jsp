<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학생 등록</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<script>
function validateStudentForm() {
  const name = document.querySelector('[name="studentName"]').value;
  const studentNo = document.querySelector('[name="studentNo"]').value;

  // 이름에 숫자 포함 여부
  if (/\d/.test(name)) {
    alert('이름에는 숫자를 입력할 수 없습니다.');
    return false;
  }

  // 학번 뒷자리에 문자 포함 여부
  if (!/^\d+$/.test(studentNo)) {
    alert('학번은 숫자만 입력 가능합니다.');
    return false;
  }

  return true;
}
</script>
</head>
<body>
	<%@ include file="/header.jsp" %>
 	<%@ include file="/sideNavbar.jsp" %>
		  	<main class="content">
		  <div class="container-fluid">
		    <div class="row g-3">
		
		      <!-- 좌측: 학생 등록 -->
		      <div class="col-md-4">
		        <div class="card h-100">
		          <div class="card-header">
		            <strong>학생 등록</strong>
		          </div>
		          <div class="card-body">
		            <form action="<c:url value='/addStudent/register'/>" method="post" id="studentForm" 
		            onsubmit="return validateStudentForm()">

					  <!-- 1열: 이름 / 성별 -->
					  <div class="row mb-2">
					    <div class="col-md-6">
					      <label class="form-label">이름</label>
					      <input type="text" name="studentName"
					             class="form-control form-control-sm" required>
					    </div>
					
					    <div class="col-md-6">
					      <label class="form-label">성별</label>
					      <select name="studentGender"
					              class="form-select form-select-sm" required>
					        <option value="">선택</option>
					        <option value="남">남</option>
					        <option value="여">여</option>
					      </select>
					    </div>
					  </div>
					
					  <!-- 2열: 주민등록번호 -->
					  <div class="row mb-2">
					    <div class="col-md-6">
					      <label class="form-label">주민등록번호 앞자리</label>
					      <input type="text" name="identityFront"
					             class="form-control form-control-sm"
					             maxlength="6" pattern="[0-9]{6}" required>
					    </div>
					    <div class="col-md-6">
					      <label class="form-label">주민등록번호 뒷자리</label>
					      <input type="text" name="identityBack" 
					             class="form-control form-control-sm"
					             maxlength="7" pattern="[0-9]{7}" required>
					    </div>
					  </div>
					
					  <!-- 4열: 학번 -->
					  <div class="row mb-3">				
					    <div class="col-md-6">
					      <label class="form-label">학번</label>
					      <input type="text" name="studentNo"
					             class="form-control form-control-sm"
					             required>
					    </div>
					    <div class="col-md-6">
						  <label class="form-label">Phone(-제외)</label>
						  <input type="text" name="studentPhone"
						         class="form-control form-control-sm"
						         placeholder="01012345678"
						         maxlength="11" 
						         pattern="01[0][0-9]{7,8}" 
						         title="숫자만 입력 가능하며, 010으로 시작하는 10~11자리여야 합니다."
						         required>
						</div>
					  </div>
					
					
					  <div class="d-grid">
					    <button type="submit" class="btn btn-sm btn-primary">
					      등록
					    </button>
					  </div>					
					</form>
		          </div>
		        </div>
		      </div>
		
		      <!-- 우측: 학생 조회 -->
		      <div class="col-md-8">
		        <div class="card">
		          <div class="card-header">
		            <strong>학생 목록</strong>
		          </div>
		          <div class="card-body p-0">
		           <form action="<c:url value='/addStudent/search'/>" method="get"
					      class="row g-2 mb-2">
					
					  <div class="col-md-4">
					    <input type="text" name="studentName"
					           class="form-control form-control-sm"
					           placeholder="이름"
					           value="${param.studentName}">
					  </div>
					
					  <div class="col-md-4">
					    <input type="text" name="majorName"
					           class="form-control form-control-sm"
					           placeholder="학과"
					           value="${param.majorName}">
					  </div>
					
					  <div class="col-md-3">
					    <input type="text" name="studentNo"
					           class="form-control form-control-sm"
					           placeholder="학번"
					           value="${param.studentNo}">
					  </div>
					
					  <div class="col-md-1 d-grid">
					    <button class="btn btn-sm btn-secondary">조회</button>
					  </div>
					</form>
					<div class="table-responsive" style="max-height: 500px;">
					  <table class="table table-sm table-hover mb-0">
					    <thead class="table-light sticky-top">
					      <tr>
					        <th>학번</th>
					        <th>이름</th>
					        <th>주민번호</th>
					        <th>전공</th>
					        <th>핸드폰</th>
					        <th>성별</th>
					        <th>주소</th>
					        <th>상태</th>
					        <th>계좌</th>
					      </tr>
					    </thead>
					
					    <tbody>
					      <c:forEach var="s" items="${studentList}">
					        <tr>
					          <td>${s.studentNo}</td>
					          <td>${s.studentName}</td>
					          <td>${s.studentIdentityNumber}</td>
					          <td>${s.majorName}</td>
					          <td>${s.studentPhone}</td>
					          <td>${s.studentGender}</td>
					          <td class="text-truncate" style="max-width:150px;">
					            ${s.studentAddress}
					          </td>
					          <td>${s.studentStatus}</td>
					          <td>${s.studentBank}</td>
					        </tr>
					      </c:forEach>
					
					      <c:if test="${empty studentList}">
					        <tr>
					          <td colspan="9" class="text-center text-muted py-3">
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
		
		    </div>
		  </div>
		</main>
	<%@ include file="/footer.jsp" %>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script type="text/javascript" src="/resources/js/loadLectures.js"></script>
</body>
</html>