<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의 등록</title>

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
	      <div class="d-flex align-items-center justify-content-between mb-3">
				  <div>
				    <h4 class="mb-0">강의 등록</h4>
				    <small class="text-muted">필수 항목을 입력하고 등록 버튼을 눌러주세요.</small>
				  </div>
				  <a class="btn btn-outline-secondary btn-sm" href="<c:url value='/lecture/list'/>">목록</a>
				</div>
				
				<!-- 에러 메시지 영역 (컨트롤러에서 request.setAttribute("error", "...") 했을 때) -->
				<c:if test="${not empty error}">
				  <div class="alert alert-danger mb-3 py-2" role="alert">
				    ${error}
				  </div>
				</c:if>
				
				<div class="card shadow-sm border-0">
				  <div class="card-body">
				
				    <form method="post" action="<c:url value='/lecture/insert'/>">
				      <div class="row g-3">
				
				        <!-- 강의명 -->
				        <div class="col-md-6">
				          <label class="form-label">강의명 <span class="text-danger">*</span></label>
				          <input type="text" class="form-control"
				                 name="lecture_name"
				                 placeholder="예: 자료구조"
				                 required>
				        </div>
				
				        <!-- 강의유형 -->
				        <div class="col-md-6">
				          <label class="form-label">강의유형 <span class="text-danger">*</span></label>
				          <select class="form-select" name="lecture_type" required>
				            <option value="" selected disabled>선택</option>
				            <option value="전공">전공</option>
				            <option value="교양">교양</option>
				          </select>
				        </div>
				
				        <!-- 학점 -->
				        <div class="col-md-3">
				          <label class="form-label">학점 <span class="text-danger">*</span></label>
				          <input type="number" class="form-control"
				                 name="lecture_credit"
				                 min="1" max="6"
				                 value="3"
				                 required>
				           <div class="form-text">최대 6학점</div>
				        </div>
				
				        <!-- 연도 -->
				        <div class="col-md-3">
				          <label class="form-label">연도 <span class="text-danger">*</span></label>
				          <input type="text" class="form-control"
				                 name="lecture_year"
				                 placeholder="예: 2026"
				                 maxlength="4"
				                 required>
				          <div class="form-text">4자리 숫자로 입력</div>
				        </div>
				
				        <!-- 학기 -->
				        <div class="col-md-3">
				          <label class="form-label">학기 <span class="text-danger">*</span></label>
				          <select class="form-select" name="lecture_semester" required>
				            <option value="1">1학기</option>
				            <option value="2">2학기</option>
				          </select>
				        </div>
				
				        <!-- 분반 -->
				        <div class="col-md-3">
				          <label class="form-label">분반 <span class="text-danger">*</span></label>
				          <input type="text" class="form-control"
				                 name="lecture_section"
				                 placeholder="예: 01"
				                 maxlength="10"
				                 required>
				        </div>
				
				        <!-- 강의실 -->
				        <div class="col-md-6">
				          <label class="form-label">강의실 <span class="text-danger">*</span></label>
				          <input type="text" class="form-control"
				                 name="lecture_room"
				                 placeholder="예: 101" 
				                 required>
				        </div>
				
				        <!-- 정원 -->
				        <div class="col-md-6">
				          <label class="form-label">정원 <span class="text-danger">*</span></label>
				          <input type="number" class="form-control"
				                 name="lecture_capacity"
				                 min="1"
				                 placeholder="예: 30"
				                 required>
				        </div>
				
				        <!-- 강의 설명 -->
				        <div class="col-12">
				          <label class="form-label">강의 설명</label>
				          <textarea class="form-control"
				                    name="lecture_description"
				                    rows="4"
				                    placeholder="강의 개요, 평가 방식, 참고 사항 등을 간단히 작성"></textarea>
				        </div>
				
				      </div>
				
				      <hr class="my-4">
				
				      <div class="d-flex gap-2 justify-content-end">
				        <a class="btn btn-light" href="<c:url value='/lecture/list'/>">취소</a>
				        <button type="submit" class="btn btn-success">등록</button>
				      </div>
				    </form>
				
				  </div>
				</div>
	      
	    </div>
	  </div>
	</main>
  
  <%@ include file="/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>