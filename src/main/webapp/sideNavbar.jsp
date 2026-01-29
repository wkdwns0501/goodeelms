<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<style>
	  /* 기본 상태: 텍스트 색상을 약간 부드럽게 */
	  .nav-link {
	    color: #495057;
	    transition: all 0.2s ease;
	    border-left: 4px solid transparent; /* 좌측에 투명한 선 미리 확보 */
	  }
	
	  /* 활성화(Active) 상태: 깔끔한 초록색 포인트 */
	  .nav-link.active {
	    background-color: #f1f8f4 !important; /* 아주 연한 초록색 배경 */
	    color: #2c5f3c !important;           /* 헤더와 맞춘 진한 초록색 글씨 */
	    font-weight: 600 !important;         /* 글자 살짝 두껍게 */
	    border-left: 4px solid #2c5f3c !important; /* 좌측에 포인트 선 */
	  }
	
	  /* 마우스 올렸을 때도 살짝 반응 */
	  .nav-link:hover {
	    background-color: #f8f9fa;
	    color: #2c5f3c;
	  }
	</style>

<script>
  document.addEventListener("DOMContentLoaded", function() {
    const currentPath = window.location.pathname; // 현재 접속한 경로 (예: /professor/lecture/list)
    const navLinks = document.querySelectorAll('.sidebar .nav-link');

    navLinks.forEach(link => {
      // link.pathname은 <a> 태그의 href에서 경로 부분만 추출합니다.
      // 현재 경로가 메뉴의 경로를 포함하고 있다면 active 클래스 추가
      if (currentPath.includes(link.getAttribute('href'))) {
        link.classList.add('active');
      }
    });
  });
</script>

<aside class="sidebar py-2">
  <div class="px-3 pt-2 pb-1 small text-uppercase text-muted">메뉴</div>
  <nav class="nav flex-column">
		<a class="nav-link" href="<c:url value='/common/dashboard'/>"><span class="nav-label">대시보드</span></a>
    <a class="nav-link" href="<c:url value='/common/board/list'/>"><span class="nav-label">공지사항</span></a> 
    <hr class="my-2 mx-3" /> 
  	<c:choose>
	  	<c:when test="${sessionScope.user_role == 'STUDENT'}">
		    <a class="nav-link" href="<c:url value='/student/enrollment/cart'/>"><span class="nav-label">수강신청 장바구니</span></a>
		    <a class="nav-link" href="<c:url value='/student/enrollment/competition'/>"><span class="nav-label">수강신청</span></a>
		    <a class="nav-link" href="<c:url value='/student/myLectures'/>"><span class="nav-label">내 강의</span></a>
		    <a class="nav-link" href="<c:url value='/student/lecture'/>"><span class="nav-label">강의목록</span></a>
		    <a class="nav-link" href="<c:url value='/student/grade/list'/>"><span class="nav-label">성적조회</span></a>
		    <a class="nav-link" href="<c:url value='/student/evaluation/list'/>"><span class="nav-label">강의평가</span></a>
		    <a class="nav-link" href="<c:url value='/student/tuition'/>"><span class="nav-label">등록금 납부 이력 </span></a>
		    <a class="nav-link" href="<c:url value='/student/history/majorAndStatus'/>"><span class="nav-label">전공/학과변경/학적변동</span></a>
   			<a class="nav-link" href="<c:url value='/student/history/rewardAndPunishment'/>"><span class="nav-label">학사경고/우등 이력 조회</span></a>
	    </c:when>	
	  	<c:when test="${sessionScope.user_role == 'PROFESSOR'}">
			  <a class="nav-link" href="<c:url value='/professor/lecture/list'/>">
			    <span class="nav-label">강의관리</span>
			  </a>
			  <c:if test="${sessionScope.professor_status != '휴직'}">
			    <a class="nav-link" href="<c:url value='/professor/grade/list'/>">
			      <span class="nav-label">성적관리</span>
			    </a>
			  </c:if>
			</c:when>

    	<c:when test="${sessionScope.user_role == 'ADMIN'}">
	   		<a class="nav-link" href="<c:url value='/admin/professorManage/page'/>"><span class="nav-label">계정관리</span></a>
	   		<a class="nav-link" href="<c:url value='/admin/addStudent/list'/>"><span class="nav-label">학생등록</span></a>
	    	<a class="nav-link" href="<c:url value='/admin/studentStatus/page'/>"><span class="nav-label">학사관리</span></a>
	    	<a class="nav-link" href="<c:url value='/admin/confirmScholarship/list'/>"><span class="nav-label">장학관리</span></a>
	    	<a class="nav-link" href="<c:url value='/admin/setCalendar'/>"><span class="nav-label">일정관리</span></a>
	    </c:when>
		</c:choose>
  </nav>
  <div class="sidebar-handle" id="sidebarHandle" title="메뉴 열기/닫기"></div>

</aside>

