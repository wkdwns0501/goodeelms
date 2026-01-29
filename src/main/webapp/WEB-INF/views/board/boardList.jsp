<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 - GoodeeLMS</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  /* 기존 테마와 맞춘 커스텀 스타일 */
  .notice-table thead {
    background-color: #f8f9fa;
    border-top: 2px solid #0d6efd;
  }
  .important-row {
    background-color: #fff9db; /* 중요 공지 강조 */
  }
  .notice-title-link {
    text-decoration: none;
    color: #333;
    font-weight: 500;
  }
  .notice-title-link:hover {
    color: #0d6efd;
    text-decoration: underline;
  }
  .page-link {
    color: #0d6efd;
  }
 
  /* 기존 스타일 유지 */
  .write-container { max-width: 900px; margin: 0 auto; }
  .note-editable { background-color: white; font-size: 1rem; }
  .card-header { font-weight: bold; }
</style>
</head>
<body>
    <%@ include file="/header.jsp" %>
    <%@ include file="/sideNavbar.jsp" %>
  
    <main class="content">
        <div class="container py-4">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center py-3">
                    <h5 class="mb-0"><i class="bi bi-megaphone-fill me-2"></i>공지사항</h5>
                    <div class="d-flex gap-2">
                        <form action="<c:url value='/common/board/list'/>" method="get" class="d-flex">
                            <div class="input-group input-group-sm">
                                <input type="text" name="searchKeyword" class="form-control" placeholder="제목+내용 검색"
                                value="<c:out value="${param.searchKeyword}"/>">
                                <button class="btn btn-light btn-sm" type="submit text-primary">
                                    <i class="bi bi-search text-primary"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="card-body p-0"> <table class="table table-hover notice-table mb-0">
                        <thead class="text-center small text-muted">
                            <tr>
                                <th style="width: 8%">번호</th>
                                <th style="width: 50%">제목</th>
                                <th style="width: 12%">작성자</th>
                                <th style="width: 15%">날짜</th>
                                <th style="width: 10%">조회수</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="content" items="${boardList}">
						        <%-- 중요 공지('Y')일 경우 'important-row' 클래스 추가 --%>
						        <tr class="text-center ${content.isImportant == 'Y' ? 'important-row' : ''}">
						            <td>
						                <c:choose>
						                    <c:when test="${content.isImportant == 'Y'}">
						                        <span class="badge bg-danger">중요</span>
						                    </c:when>
						                    <c:otherwise>
						                        <c:out value="${content.boardId}"/>
						                    </c:otherwise>
						                </c:choose>
						            </td>
						            <td class="text-start ps-4">
						                <a href="<c:url value='/common/board/detail?id=${content.boardId}'/>" 
						                   class="notice-title-link ${content.isImportant == 'Y' ? 'text-danger fw-bold' : ''}">
						                    <c:out value='${content.boardTitle}'/>
						                </a>
						            </td>
						            <td><c:out value="관리자"/></td>
						            <td><c:out value="${fn:substring(content.boardRegAt, 0, 10)}"/></td>
						            <td><c:out value="${content.hit}"/></td>
						        </tr>
						    </c:forEach>

                            <c:if test="${empty boardList}">
                                <tr>
                                    <td colspan="5" class="text-center py-5 text-muted">
                                        등록된 공지사항이 없습니다.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                	<div class="card-footer bg-white py-3 d-flex justify-content-between align-items-center">
                    <div class="small text-muted">
                        Total: <strong>${totalCount != null ? totalCount : 0}</strong>건
                    </div>

                    <nav>
					    <ul class="pagination pagination-sm mb-0">
					        <li class="page-item ${pageNum <= 1 ? 'disabled' : ''}">
					            <a class="page-link" href="<c:url value='/common/board/list?pageNum=${pageNum - 1}&searchKeyword=${fn:escapeXml(keyword)}'/>">
					                <i class="bi bi-chevron-left"></i>
					            </a>
					        </li>
					
					        <c:forEach var="i" begin="${startPage }" end="${endPage}">
					            <li class="page-item ${pageNum == i ? 'active' : ''}">
					                <a class="page-link" href="<c:url value='/common/board/list?pageNum=${i}&searchKeyword=${fn:escapeXml(keyword)}'/>">
					                    ${i}
					                </a>
					            </li>
					        </c:forEach>
					
					        <li class="page-item ${endPage >= totalPage ? 'disabled' : ''}">
					            <a class="page-link" href="<c:url value='/common/board/list?pageNum=${pageNum + 1}&searchKeyword=${fn:escapeXml(keyword)}'/>">
					                <i class="bi bi-chevron-right"></i>
					            </a>
					        </li>
					    </ul>
					</nav>

                    <div>
                        <c:if test="${sessionScope.user_role == 'ADMIN'}">
                            <a href="<c:url value='/common/board/admin/write'/>" class="btn btn-primary btn-sm px-3">
                                <i class="bi bi-pencil-fill me-1"></i> 글쓰기
                            </a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="/footer.jsp" %>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>