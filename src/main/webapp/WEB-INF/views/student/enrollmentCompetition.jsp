<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수강 신청</title>

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
			  <div class="d-flex align-items-center justify-content-between flex-wrap gap-2 mb-3">
			    <div>
			      <h5 class="mb-1">수강신청</h5>
			      <p class="text-muted mb-0">장바구니에서 신청완료와 정원초과를 확인 후, 신청해주세요.</p>
			      <p class="text-muted mb-0 fw-5 fw-bold">기간이 종료되면 더이상 수강신청이 불가능합니다.</p>
			    </div>
			  </div>
			
			  <!-- 카테고리 + 검색 + 남은 시간(같은 줄) -->
				<div class="row g-3 align-items-center mb-3">
				  <!-- 좌측: 카테고리 + 검색 -->
				  <div class="col-12 col-lg-8">
				    <div class="d-flex flex-wrap align-items-center justify-content-between gap-2">
				    	<!-- studentID 수신 방법 변경해야함 -->
				      <input type="hidden" id="sessionUserId" value="${sessionScope.student_id}">
				      <!-- 카테고리 버튼 -->
				      <div class="btn-group btn-group-sm" role="group" aria-label="강의 카테고리">
				        <!-- 접근성: 현재 선택 상태 표시 -->
				        <button type="button" class="btn btn-outline-dark is-cat active" data-cat="all" aria-pressed="true">전체 강의</button>
				        <button type="button" class="btn btn-outline-dark is-cat" data-cat="major" aria-pressed="false">전공</button>
				        <button type="button" class="btn btn-outline-dark is-cat" data-cat="minor" aria-pressed="false">부전공</button>
				        <button type="button" class="btn btn-outline-dark is-cat" data-cat="liberal" aria-pressed="false">교양</button>
				      </div>
				      <!-- 검색 바 -->
				      <form class="ms-lg-auto" id="lectureSearchForm" role="search">
				        <div class="input-group input-group-sm" style="max-width: 360px;">
				          <input type="search" class="form-control" id="lectureKeyword" name="search_word" placeholder="과목명/교수명 검색"
				            aria-label="과목명 또는 교수명 검색" autocomplete="off">
				          <button class="btn btn-primary" type="submit" id="btnLectureSearch">검색</button>
				        </div>
				      </form>
				    </div>
				  </div>
				
				  <!-- 우측: 남은 시간 -->
				  <div class="col-12 col-lg-4">
				    <div class="d-flex align-items-center justify-content-between gap-2">
				      <span class="text-muted small" id="remainTimeSet" data-end-time ="${endTime}">남은 시간</span>
				      <span class="badge text-bg-success px-3 py-2 me-lg-2" id="enrollTimer" aria-live="polite">--:--</span>
				    </div>
				  </div>
				</div>
			
			  <div class="row g-3">
			    <!-- 좌측: 강의 리스트 -->
			    <div class="col-12 col-lg-8" id="lectureListArea">
			    </div>
			
			    <!-- 우측: 장바구니 -->
			    <div class="col-12 col-lg-4" id="lectureCartArea">
			    </div>
			    
			    <!-- 우측: 수강신청 현황 -->
			    <div class="col-12 col-lg-4" id="lectureCompletedArea">
			    </div>
			    
			  </div>
			</div>
		 </div>
	</main>
  
	<%@ include file="/footer.jsp" %>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script type="text/javascript" src="/resources/js/enrollmentOnCompetition.js" defer="defer"></script>
	<script type="text/javascript" src="/resources/js/enrollmentRemainTimer.js" defer="defer"></script>
</body>
</html>