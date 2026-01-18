<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<header class="app-header d-flex align-items-center px-3">
	<button id="btnToggle" class="btn btn-sm btn-light me-2" type="button">
		☰</button>

	<div class="fw-semibold ms-2">
		<a class="brand-link" href="<c:url value='/main.jsp'/>">GoodeeLMS</a>
	</div>


	<div class="ms-auto d-flex align-items-center gap-3">
		<c:choose>
			<c:when test="${not empty user_role}">

				<span class="text-white small me-2"> <c:choose>
						<c:when test="${user_role eq 'STUDENT'}">
							<strong>${studentDTO.studentName}</strong>
						</c:when>

						<c:when test="${user_role eq 'PROFESSOR'}">
							<strong>${professorDTO.professorName}</strong>
						</c:when>

						<c:when test="${user_role eq 'ADMIN'}">
							<strong>${adminDTO.adminName}</strong>
						</c:when>
					</c:choose> 님 <span
					class="badge ${user_role eq 'ADMIN' ? 'bg-danger' : 'bg-secondary'} ms-1">${user_role}</span>
				</span>

				<%-- 학생 전용 버튼 --%>
				<c:if test="${user_role eq 'STUDENT'}">
					<a class="btn btn-sm btn-outline-light"
						href="<c:url value='/student/mypage'/>">마이페이지</a>
				</c:if>

				<a class="btn btn-sm btn-light"
					href="<c:url value='/common/logout'/>">로그아웃</a>
			</c:when>

			<c:otherwise>
				<a class="btn btn-sm btn-outline-light"
					href="<c:url value='/common/login'/>">로그인</a>
				<a class="btn btn-sm btn-light"
					href="<c:url value='/professor/signup'/>">회원가입</a>
			</c:otherwise>
		</c:choose>
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

<style>
/* 헤더 브랜드 링크: 파란색/밑줄 제거 + 크기 업 */
.app-header .brand-link {
	color: #fff;
	text-decoration: none;
	font-size: 1.15rem;
	letter-spacing: 0.2px;
}

/* 방문/호버/클릭 상태에서도 색 유지 */
.app-header .brand-link:visited, .app-header .brand-link:hover,
	.app-header .brand-link:active {
	color: #fff;
	text-decoration: none;
}

/* 호버 시만 살짝 티 나게 */
.app-header .brand-link:hover {
	opacity: 0.92;
}
</style>
