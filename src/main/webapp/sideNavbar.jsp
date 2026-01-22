<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="sidebar py-2">
  <div class="px-3 pt-2 pb-1 small text-uppercase text-muted">메뉴</div>
  <nav class="nav flex-column">
  	<c:choose>
	  	<c:when test="${sessionScope.user_role == 'STUDENT'}">
		    <a class="nav-link" href="<c:url value='/student/page/enrollment'/>"><span class="nav-label">수강신청</span></a>
		    <a class="nav-link" href="<c:url value='/student/tuition'/>"><span class="nav-label">등록금 납부 이력 </span></a>
		    <a class="nav-link" href="<c:url value='/student/page/enrollment'/>"><span class="nav-label">수강신청</span></a>
		    <a class="nav-link" href="<c:url value='/student/lecture'/>"><span class="nav-label">강의목록</span></a>
		    <a class="nav-link" href="<c:url value='/student/assignments'/>"><span class="nav-label">과제</span></a>
		    <a class="nav-link" href="<c:url value='/student/exams'/>"><span class="nav-label">시험</span></a>
		    <a class="nav-link" href="<c:url value='/student/grade/list'/>"><span class="nav-label">성적조회</span></a>
	    </c:when>	
    	<c:when test="${sessionScope.user_role == 'ADMIN'}">
	    	<a class="nav-link" href="<c:url value='/admin/studentStatus/page'/>"><span class="nav-label">학사관리</span></a>
	   		<a class="nav-link" href="<c:url value='/admin/professorManage/page'/>"><span class="nav-label">계정관리</span></a>
	   		<a class="nav-link" href="<c:url value='/admin/addStudent/list'/>"><span class="nav-label">학생등록</span></a>
	    	<a class="nav-link" href="<c:url value='/admin/assignments'/>"><span class="nav-label">장학관리</span></a>
	    </c:when>
	  	<c:when test="${sessionScope.user_role == 'PROFESSOR'}">
			  <hr class="my-2 mx-3" />
			  <a class="nav-link" href="<c:url value='/professor/lecture/list'/>"><span class="nav-label">강의관리</span></a>
			  <a class="nav-link" href="<c:url value='/professor/grade/list'/>"><span class="nav-label">성적관리</span></a>
			</c:when>
			<c:otherwise>
				<a class="nav-link" href="<c:url value='/common/dashboard'/>"><span class="nav-label">대시보드</span></a>
	    	<a class="nav-link" href="<c:url value='/common/notice'/>"><span class="nav-label">공지사항</span></a> 
			</c:otherwise>
		</c:choose>
  </nav>
  <div class="sidebar-handle" id="sidebarHandle" title="메뉴 열기/닫기"></div>
</aside>
