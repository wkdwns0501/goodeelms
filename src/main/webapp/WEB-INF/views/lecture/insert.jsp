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
				    <h4 class="mb-0"><b>강의 등록</b></h4>
				    <small class="text-muted">필수 항목을 입력하고 등록 버튼을 눌러주세요.</small>
				  </div>
				  <a class="btn btn-outline-secondary btn-sm" href="<c:url value='/professor/lecture/list'/>">목록</a>
				</div>
				
				<!-- 에러 메시지 영역 (컨트롤러에서 request.setAttribute("error", "...") 했을 때) -->
				<c:if test="${not empty error}">
				  <div id="errorAlert" class="alert alert-danger mb-3 py-2" role="alert">
				    <c:out value="${error}"/>
				  </div>
				</c:if>

				
				<div class="card shadow-sm border-0">
				  <div class="card-body">
				
				    <form id="lectureForm" method="post" action="<c:url value='/professor/lecture/insert'/>" novalidate>
				      <div class="row g-3">
				
				        <!-- 강의명 -->
				        <div class="col-md-6">
				          <label class="form-label">강의명 <span class="text-danger">*</span></label>
				          <input type="text" class="form-control"
				                 name="lecture_name" id="lecture_name"
				                 placeholder="예: 객체지향 프로그래밍" 
				                 value="<c:out value='${form.lectureName}'/>"
				                 required/>
				        	  <small id="lectureNameError" class="text-danger d-none">
									    강의명은 4자 이상 20자 이하로 입력해주세요.
									  </small>
				        </div>
				
				        <!-- 강의유형 -->
				        <div class="col-md-6">
				          <label class="form-label">강의 유형 <span class="text-danger">*</span></label>
				          <select class="form-select" name="lecture_type" id="lecture_type" required>
				            <option value="">-- 선택 --</option>
									  <option value="전공" <c:if test="${form.lectureType == '전공'}">selected</c:if>>전공</option>
									  <option value="교양" <c:if test="${form.lectureType == '교양'}">selected</c:if>>교양</option>
				          </select>
				          <small id="typeError" class="text-danger d-none">유형을 선택하세요</small>
				        </div>
				
				        <!-- 학점 -->
				        <div class="col-md-3">
				          <label class="form-label">학점 <span class="text-danger">*</span></label>
				          <input type="number" class="form-control"
				                 name="lecture_credit" 
				                 id="lecture_credit" 
				                 min="1" max="6"
				                 value="<c:out value='${form.lectureCredit}'/>"
				                 required/>
				          <small id="creditError" class="text-danger d-none">1~6만 입력 가능합니다</small>
				        </div>
				
				        <!-- 연도 -->
				        <div class="col-md-3">
				          <label class="form-label">연도 <span class="text-danger">*</span></label>
				          <input type="text" class="form-control"
				                 name="lecture_year" 
				                 id="lecture_year" 
				                 placeholder="예: 2026"
				                 maxlength="4" 
				                 value="<c:out value='${form.lectureYear}'/>" 
				                 required/>
				          <small id="yearError" class="text-danger d-none">YYYY 형식만 가능합니다(1900~2099)</small>
				        </div>
				
				        <!-- 학기 -->
				        <div class="col-md-3">
				          <label class="form-label">학기 <span class="text-danger">*</span></label>
				          <select class="form-select" name="lecture_semester" id="lecture_semester" required>
				            <option value="">-- 선택 --</option>
									  <option value="1" <c:if test="${form.lectureSemester == 1}">selected</c:if>>1학기</option>
									  <option value="2" <c:if test="${form.lectureSemester == 2}">selected</c:if>>2학기</option>
				          </select>
				          <small id="semesterError" class="text-danger d-none">학기를 선택하세요</small>
				        </div>
				
				        <!-- 분반 -->
				        <div class="col-md-3">
				          <label class="form-label">분반</label>
								  <div class="form-control bg-light text-muted">
								    자동 배정 (최대 2개)
								  </div>
				        </div>
								
								<!-- 건물명 -->
								<div class="col-md-6">
								  <label class="form-label">건물 <span class="text-danger">*</span></label>
								  <select class="form-select" name="building_id" id="building_id" required>
								    <option value="">-- 건물 선택 --</option>
								    <c:forEach var="b" items="${buildingList}">
							        <option value="${b.buildingId}"
									      <c:if test="${form.buildingId == b.buildingId}">selected</c:if>>
									      <c:out value="${b.buildingName}"/>
									    </option>
								    </c:forEach>
								  </select>
								  <small id="buildingError" class="text-danger d-none">건물을 선택하세요</small>
								</div>
								
				        <!-- 강의실 -->
				        <div class="col-md-3">
				          <label class="form-label">강의실 <span class="text-danger">*</span></label>
				          <input type="text" class="form-control"
				                 name="lecture_room"
				                 id="lecture_room" 
				                 placeholder="예: 101" 
				                 value="<c:out value='${form.lectureRoom}'/>" 
				                 required/>
				        </div>
				
				        <!-- 정원 -->
				        <div class="col-md-3">
				          <label class="form-label">정원 <span class="text-danger">*</span></label>
				          <input type="number" class="form-control"
				                 name="lecture_capacity" 
				                 id="lecture_capacity" 
				                 min="1" max="50" 
				                 placeholder="예: 30" 
				                 value="<c:out value='${form.lectureCapacity}'/>"
				                 required/>
				          <small id="capacityError" class="text-danger d-none">1~50만 입력 가능합니다</small>
				        </div>
				
				        <!-- 강의 설명 -->
				        <div class="col-12">
				          <label class="form-label">강의 설명</label>
				          <textarea class="form-control"
				                    name="lecture_description" 
				                    id="lecture_description" 
				                    rows="4" 
				                    maxlength="1000" 
				                    placeholder="최대 1000자까지 작성 가능합니다."><c:out value="${form.lectureDescription}"/></textarea>
				          <div class="d-flex justify-content-between">
								    <small id="descError" class="text-danger d-none">최대 1000자까지만 작성 가능합니다</small>
								    <small class="text-muted"><span id="descCount">0</span>/1000</small>
								  </div>
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
  
  <script src="<c:url value='/resources/js/insertLecture.js'/>"></script>
</body>
</html>