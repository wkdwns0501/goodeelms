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

function autoHyphen(target) {
	  target.value = target.value
	    .replace(/[^0-9]/g, '') // 숫자가 아닌 문자 제거
	    .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3") // 그룹화
	    .replace(/(\-{1,2})$/g, ""); // 마지막에 남는 하이픈 제거
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
		      <div class="col-md-3">
		        <div class="card h-100">
		          <div class="card-header">
		            <strong>학생 등록</strong>
		          </div>
		          <div class="card-body">
		            <form action="<c:url value='/admin/addStudent/register'/>" method="post" id="studentForm" 
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
					  
					  <!-- 2열: 전공 -->
					  <div class="row mb-2">
					    <div class="col-md-12">
					      <label class="form-label">학과</label>
							<select name="majorId" class="form-select form-select-sm" required>
						      <option value="" selected disabled>-- 학과 선택 --</option>
							      <c:forEach var="m" items="${majorList}">
							        <option value="${m.majorId}">${m.majorName}</option>
							      </c:forEach>
						    </select>
					    </div>
					  </div>
					  
					  <!-- 3열: 주민등록번호 -->
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
					
					  <!-- 4열: 학번, phone -->
					  <div class="row mb-3">				
					    <div class="col-md-6">
					      <label class="form-label">학번</label>
					      <input type="text" name="studentNo"
					             class="form-control form-control-sm"
					             required>
					    </div>
					    <div class="col-md-6">
						  <label class="form-label">핸드폰 번호</label>
						  <input type="text" name="studentPhone"
						         class="form-control form-control-sm"
						         placeholder="010-1234-5678"
						         maxlength="13" 
						         pattern="010-[0-9]{3,4}-[0-9]{4}" 
						         title="010-0000-0000 형식으로 입력해주세요."
						         oninput="autoHyphen(this)"
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
		      <div class="col-md-9">
		        <div class="card">
		          <div class="card-header">
		            <strong>학생 목록</strong>
		          </div>
		          <div class="card-body p-0">
		           <form action="<c:url value='/admin/addStudent/search'/>" method="get"
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
					  <table class="table table-sm table-hover mb-0" style="table-layout: fixed; width: 100%;">
					    <thead class="table-light sticky-top">
					      <tr>
					        <th style="width: 10%;">학번</th>
					        <th style="width: 5%;">이름</th>
					        <th style="width: 12%;">주민번호</th>
					        <th style="width: 24%;">전공</th>
					        <th style="width: 12%;">핸드폰</th>
					        <th style="width: 6%;">성별</th>
					        <th style="width: 28%;">주소</th>
					        <th style="width: 5%;">상태</th>
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
					          <td class="text-truncate" style="max-width:150px;" title="${s.studentAddress}">
								  ${s.studentAddress}
							  </td>
					          <td>${s.studentStatus}</td>
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