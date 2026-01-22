<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장학 관리</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<script>
function generateSemesterOptions() {
	  // 사용자가 선택한 년도+학기
	  const select = document.getElementById("semesterSelect");
	  // 몇년도부터 조회할지 
	  const startYear = 2020;
	  
	  // 현재 년,월 변수에 저장
	  const now = new Date();
	  const currentYear = now.getFullYear();
	  const currentMonth = now.getMonth() + 1; // 현재 1월 (1)
	  
	  // 가져올 학기 배열
	  let semesters = [];

	  for (let year = startYear; year <= currentYear; year++) {
	    // [1학기 노출 조건]
	    // 1. 과거 연도이거나
	    // 2. 올해(2026)라면 8월이 지났어야 함
	    if (year < currentYear || (year === currentYear && currentMonth >= 8)) {
	      // 배열에 추가
	      semesters.push({value: year + "_1", text: year + "년 1학기" });
	    }

	    // [2학기 노출 조건]
	    // 1. 완전 과거(작년 이전) 연도이거나
	    // 2. 작년(2025) 2학기는 올해(2026) 2월이 지났어야 함
	    // (즉, target연도의 다음 해 2월이 현재 날짜보다 과거여야 함)
	    
	    // "2025_2"를 만들지 말지 결정하는 로직
	    const isPastYear = year < (currentYear - 1); // 2024년 이전
	    const isLastYearAndReady = (year === currentYear - 1 && currentMonth >= 2); // 2025년인데 현재 2월 이상
	    
	    if (isPastYear || isLastYearAndReady) {
	      semesters.push({ value: year + "_2", text: year + "년 2학기" });
	    }
	  }

	  // 최신순 정렬
	  semesters.sort((a, b) => b.value.localeCompare(a.value));

	  // 옵션 html 코드 생성 (기존과 동일)
	  semesters.forEach(s => {
	    const option = document.createElement("option");
	    option.value = s.value;
	    option.textContent = s.text;
	    // 가장 최근 학기 미리 selected 설정
	    if ("${currentSemester}" === s.value) option.selected = true;
	    select.appendChild(option);
	  });
	  
	  // 안내 문구 포함시 다음 인덱스 selected 되게끔
	  if (!select.value && select.options.length > 1) {
	    select.selectedIndex = 1;
	  }
	}
	
	// 전부 로드 후 함수 실행
	window.onload = generateSemesterOptions;

	document.addEventListener("DOMContentLoaded", function() {
    	const selectAll = document.getElementById("selectAll");
    	const switches = document.querySelectorAll(".custom-switch");

   		 if (selectAll) {
        	selectAll.addEventListener("change", function() {
            	switches.forEach(sw => {
            		// 확정되어 있는 학생은(disabled)는 적용x
               	 	if (!sw.disabled) {
                    sw.checked = selectAll.checked;
                	}
           		 });
       		 });
   		 }
	});
</script>

</head>

<body>
<%@ include file="/header.jsp" %>
<%@ include file="/sideNavbar.jsp" %>

<main class="content">
  <div class="container-fluid">
	<div class="card mb-3">
  	  <div class="card-body">
		<form action="<c:url value='/admin/confirmScholarship/list'/>"
		      method="get"
		      class="row g-2 align-items-end">
		
		  <div class="col-md-4">
		    <label class="form-label">조회 학기</label>
		
		    <div class="row g-2">
		      <div class="col-auto">
		        <select id="semesterSelect"
		                name="yearSemester"
		                class="form-select form-select-sm"
		                style="width: 200px"
		                required>
		          <option value="" selected disabled>-- 학기 선택 --</option>
		        </select>
		      </div>
		
		      <div class="col-auto d-grid">
		        <button class="btn btn-sm btn-secondary">
		          조회
		        </button>
		      </div>
		    </div>
		  </div>
		</form>
  	  </div>
	</div>

<div class="card">
  <form action="<c:url value='/admin/confirmScholarship/confirm'/>" method="post">
  <input type="hidden" name="yearSemester" value="${currentSemester}">   
    <div class="table-responsive" style="max-height: 500px; overflow-y: auto;">
      <table class="table table-sm align-middle table-hover mb-0">
        <thead class="table-light" style="position: sticky; top: 0; z-index: 1;">
          <tr>
            <th>학번</th>
            <th>이름</th>
            <th>학과</th>
            <th>평점 (4.5)</th>
            <th class="text-center">장학생</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="s" items="${scholarshipList}">
            <tr>
              <td>${s.studentNo}</td>
              <td>${s.studentName}</td>
              <td>${s.majorName}</td>
              <td>${s.gpa}</td>
              <td class="text-center">
				  <div class="form-check form-switch d-inline-block">
				    <input class="form-check-input custom-switch" 
				           type="checkbox" 
				           name="checkConfirmed" 
				           value="${s.studentId}" 
				           <%-- 각 행의 student id --%>
				           id="switch_${s.studentId}"
				           ${s.isConfirmed == 'Y' ? 'checked disabled' : ''}>
				    <label class="form-check-label" for="switch_${s.studentId}"></label>
				  </div>
			  </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <div class="card-footer text-end bg-white border-top-0">
    	<div class="form-check form-switch d-inline-block">
       		<input class="form-check-input" type="checkbox" id="selectAll">
        	<label class="form-check-label" for="selectAll" style="font-size: 0.8rem;">
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
      	</div>
      	<button type="submit" class="btn btn-sm btn-primary" onclick="return confirm('체크된 학생을 장학생으로 확정하시겠습니까?')">
        확정
      	</button>
    </div>  
  </form>
</div>
</div>
</main>
<%@ include file="/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
