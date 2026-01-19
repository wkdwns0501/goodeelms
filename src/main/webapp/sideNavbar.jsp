<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="sidebar py-2">
  <div class="px-3 pt-2 pb-1 small text-uppercase text-muted">메뉴</div>

  <nav class="nav flex-column">
  	<c:choose>
    <a class="nav-link" href="<c:url value='/dashboard'/>"><span class="nav-label">대시보드</span></a>
    <a class="nav-link" href="<c:url value='/student?page=enrollment'/>"><span class="nav-label">수강신청</span></a>
    <a class="nav-link" href="<c:url value='/my-lectures'/>"><span class="nav-label">내 강의</span></a>
    <a class="nav-link" href="<c:url value='/assignments'/>"><span class="nav-label">과제</span></a>
    <a class="nav-link" href="<c:url value='/exams'/>"><span class="nav-label">시험</span></a>
    <a class="nav-link" href="<c:url value='/grades'/>"><span class="nav-label">성적조회</span></a>
    <a class="nav-link" href="<c:url value='/notice'/>"><span class="nav-label">공지사항</span></a>
    <%-- <c:if test="${not empty sessionScope.adminId}"> --%>
			<a class="nav-link" href="<c:url value='/dashboard'/>"><span class="nav-label">대시보드</span></a>
	    <a class="nav-link" href="<c:url value='/studentStatus/page'/>"><span class="nav-label">학사관리</span></a>
	    <a class="nav-link" href="<c:url value='/professorManage/page'/>"><span class="nav-label">계정관리</span></a>
	    <a class="nav-link" href="<c:url value='/addStudent/list'/>"><span class="nav-label">학생등록</span></a>
	    <a class="nav-link" href="<c:url value='/assignments'/>"><span class="nav-label">장학관리</span></a>
		<%-- </c:if>--%>
    <hr class="my-2 mx-3" /> 
    <a class="nav-link" href="<c:url value='/settings'/>"><span class="nav-label">설정</span></a>
	  	<c:when test="${sessionScope.user_role == 'PROFESSOR'}">
			  <a class="nav-link" href="<c:url value='/professor/lecture/add'/>"><span class="nav-label">강의 등록</span></a>
			  <a class="nav-link" href="<c:url value='/professor/lecture/list'/>"><span class="nav-label">강의 목록</span></a>
			</c:when>
		</c:choose>
  </nav>
  <div class="sidebar-handle" id="sidebarHandle" title="메뉴 열기/닫기"></div>
</aside>
