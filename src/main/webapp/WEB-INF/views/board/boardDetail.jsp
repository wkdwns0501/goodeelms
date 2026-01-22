<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${board.boardTitle}</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  /* LMS 테마 컬러 정의 (초록색 계열) */
  :root {
    --lms-green: #198754;      /* 메인 초록 */
    --lms-light-green: #f0fdf4; /* 아주 연한 초록 배경 */
    --lms-border: #e9ecef;
  }

  .page-shell { background-color: #ffffff; border: 1px solid var(--lms-border); }

  /* 헤더 영역 */
  .detail-header {
    border-bottom: 2px solid #f8f9fa;
    padding-bottom: 20px;
    margin-bottom: 30px;
  }
  .board-title {
    font-size: 1.5rem;
    font-weight: 700;
    color: #212529;
    margin-top: 10px;
  }
  
  /* 배지 스타일 */
  .badge-important {
    background-color: #fff1f0;
    color: #e03131;
    border: 1px solid #ffc9c9;
    font-weight: 600;
    padding: 5px 12px;
  }

  /* 메타 정보 (작성자, 날짜 등) */
  .meta-group { font-size: 0.9rem; color: #6c757d; }
  .meta-item b { color: var(--lms-green); margin-right: 5px; }
  .meta-divider { margin: 0 15px; color: #dee2e6; }

  /* 본문 영역 */
  .board-content {
    line-height: 1.8;
    font-size: 1.05rem;
    color: #333;
    min-height: 300px;
  }
  .board-content img { max-width: 100%; height: auto; border-radius: 4px; }

  /* 버튼 커스텀 */
  .btn-lms-outline {
    border: 1px solid var(--lms-green);
    color: var(--lms-green);
    background-color: #fff;
    font-weight: 500;
    transition: 0.2s;
  }
  .btn-lms-outline:hover {
    background-color: var(--lms-green);
    color: #fff;
  }
  
  .btn-lms-list {
    border: 1px solid #ced4da;
    color: #495057;
    background-color: #fff;
  }
  .btn-lms-list:hover { background-color: #ced4da; color: #fff; }

  .btn-lms-delete {
    border: 1px solid #dc3545;
    color: #dc3545;
    background-color: #fff;
  }
  .btn-lms-delete:hover { border-color: #dc3545; color: #fff; background-color: #dc3545; }
</style>
</head>
<body>
  <%@ include file="/header.jsp" %>
  <%@ include file="/sideNavbar.jsp" %>
  
  <main class="content">
    <div class="container-fluid">
      <div class="page-shell shadow-sm p-4 p-md-5">
        
        <div class="detail-header">
          <div class="d-flex align-items-center justify-content-between mb-2">
            <div>
               <c:if test="${board.isImportant == 'Y'}">
                <span class="badge badge-important rounded-1 me-2">중요</span>
              </c:if>
              <span class="text-success fw-bold small">공지사항</span>
            </div>
            <div class="meta-group">
              <span class="meta-item"><b>조회수</b> ${board.hit}</span>
            </div>
          </div>
          
          <h2 class="board-title">${board.boardTitle}</h2>
          
          <div class="mt-3 d-flex align-items-center meta-group">
            <span class="meta-item"><b>작성자</b> 관리자</span>
            <span class="meta-divider">|</span>
            <span class="meta-item"><b>작성일</b> ${fn:substring(board.boardRegAt, 0, 10)}</span>
          </div>
        </div>

        <div class="board-content">
            ${board.boardContent}
        </div>

        <div class="d-flex justify-content-between align-items-center mt-5 pt-4 border-top">
		  <a href="<c:url value='/common/board/list'/>" class="btn btn-sm btn-lms-list px-3">
		    <i class="bi bi-justify me-1"></i> 목록
		  </a>
		  
		  <c:if test="${sessionScope.user_role == 'ADMIN'}">
		    <div class="d-flex gap-2">
		      <a href="<c:url value='/common/board/admin/edit?id=${board.boardId}'/>" class="btn btn-sm btn-lms-outline px-3">
		        수정
		      </a>
		      
		      <form action="<c:url value='/common/board/admin/delete'/>" method="post" onsubmit="return confirm('이 게시글을 삭제하시겠습니까?');" style="display:inline;">
		        <input type="hidden" name="id" value="${board.boardId}">
		        <button type="submit" class="btn btn-sm btn-lms-delete px-3">
		          삭제
		        </button>
		      </form>
		    </div>
		  </c:if>
		</div>

      </div> </div>
  </main>
  
  <%@ include file="/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>