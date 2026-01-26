<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GoodeeLMS</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

</head>
<body>
  <%@ include file="header.jsp" %>
  <%@ include file="sideNavbar.jsp" %>
  
  <main class="content">
	  <div class="container-fluid">
	    <div class="page-shell">
	      <h5 class="mb-2">본문 영역</h5>
				<p class="text-muted mb-3">여기에 각 페이지 내용을 넣으면 됩니다.</p>
				
				<div class="d-flex gap-2">
				  <a href="#" class="btn btn-sm btn-outline-success">예시 버튼 1</a>
				  <a href="#" class="btn btn-sm btn-outline-secondary">예시 버튼 2</a>
				</div>
	    </div>
	  </div>
	</main>
  
  <%@ include file="footer.jsp" %>
  
  <c:if test="${not empty param.error}">
	  <c:choose>
	  	<c:when test="${param.error == 'noDTO'}">
	  		<script type="text/javascript">
	  			alert("로그인 후 이용 가능합니다.");
	  		</script>
	  	</c:when>
	  	<c:when test="${param.error == 'NoAccessEnrollTime'}">
	  		<script type="text/javascript">
	  			alert("이용 가능한 기간이 아닙니다.");
	  		</script>
	  	</c:when>
	  </c:choose>
  </c:if>

  
</body>
</html>