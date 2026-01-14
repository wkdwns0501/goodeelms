<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="sidebar py-2">
  <div class="px-3 pt-2 pb-1 small text-uppercase text-muted">메뉴</div>

  <nav class="nav flex-column">
    <a class="nav-link" href="<c:url value='/dashboard'/>"><span class="nav-label">대시보드</span></a>
    <a class="nav-link" href="<c:url value='/student/status'/>"><span class="nav-label">학사관리</span></a>
    <a class="nav-link" href="<c:url value='/my-lectures'/>"><span class="nav-label">계정관리</span></a>
    <a class="nav-link" href="<c:url value='/assignments'/>"><span class="nav-label">장학관리</span></a>

    <hr class="my-2 mx-3" />
    <a class="nav-link" href="<c:url value='/settings'/>"><span class="nav-label">설정</span></a>
  </nav>
  <div class="sidebar-handle" id="sidebarHandle" title="메뉴 열기/닫기"></div>
</aside>
