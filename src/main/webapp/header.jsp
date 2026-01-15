<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="app-header d-flex align-items-center px-3">
  <button id="btnToggle" class="btn btn-sm btn-light me-2" type="button">
    ☰
  </button>

  <div class="fw-semibold">LMS</div>

  <div class="ms-auto d-flex align-items-center gap-2">
    <a class="btn btn-sm btn-outline-light" href="<c:url value='/mypage'/>">마이페이지</a>
    <a class="btn btn-sm btn-light" href="<c:url value='/logout'/>">로그아웃</a>
  </div>
</header>

<script>
(function(){
  const btn = document.getElementById("btnToggle");

  function isMobile(){
    return window.matchMedia("(max-width: 991.98px)").matches;
  }

  function toggleSidebar(){
    if (isMobile()){
      // 모바일: 오버레이 열기/닫기
      document.body.classList.toggle("sidebar-open");
    } else {
      // 데스크톱: 피크(핸들만) <-> 펼침
      document.body.classList.toggle("sidebar-collapsed");
      document.body.classList.remove("sidebar-open"); // 데스크톱에선 sidebar-open 의미 X
    }
  }

  if(btn){
    btn.addEventListener("click", toggleSidebar);
  }

  // 핸들 클릭으로도 토글
  document.addEventListener("click", (e) => {
    const handle = e.target.closest("#sidebarHandle");
    if(handle){
      toggleSidebar();
    }
  });

  // 리사이즈 시 모바일 오버레이 상태 정리
  window.addEventListener("resize", () => {
    if(!isMobile()){
      document.body.classList.remove("sidebar-open");
    }
  });
})();
</script>

