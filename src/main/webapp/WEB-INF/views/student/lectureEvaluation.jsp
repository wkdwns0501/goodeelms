<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의 평가</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  /* 라디오 버튼 숨기기 */
  .score-group input[type="radio"] {
    display: none;
  }

  /* 라디오 버튼을 감싸는 라벨 스타일 */
  .score-group label {
    flex: 1;
    text-align: center;
    padding: 12px 5px;
    margin: 0 4px;
    border: 1px solid #dee2e6;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s ease;
    font-size: 0.9rem;
    color: #6c757d;
    background-color: #fff;
  }

  /* 마우스 호버 효과 */
  .score-group label:hover {
    background-color: #f8f9fa;
    border-color: #0d6efd;
    color: #0d6efd;
  }

  /* 선택되었을 때의 스타일 (매우만족~보통: 파란색, 불만족: 주황색 등 차별화 가능) */
  .score-group input[type="radio"]:checked + label {
    background-color: #0d6efd;
    border-color: #0d6efd;
    color: #fff;
    font-weight: bold;
    box-shadow: 0 4px 6px rgba(13, 110, 253, 0.2);
  }
</style>

</head>
<body>
	<%@ include file="/header.jsp" %>
 	<%@ include file="/sideNavbar.jsp" %>
  
	 <main class="content">
  <div class="container py-4">
    <div class="card shadow-sm">
      <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center py-2">
        <h5 class="mb-0">강의 평가 작성</h5>
        <div class="d-flex align-items-center">
      	 <label for="lectureSelect" class="me-2 mb-0 sm-text" style="font-size: 0.85rem; min-width: 80px;">평가 과목</label>
      	  <select id="lectureSelect" class="form-select form-select-sm" style="width: 350px;" onchange="changeLecture(this.value)">
        	<c:forEach var="lecture" items="${lectureList}">
          		<option value="${lecture.lectureId}" ${lecture.lectureId == currentLectureId ? 'selected' : ''}
          		${lecture.evaluated ? 'disabled style="color: #ccc; background-color: #f8f9fa;"' : ''}>
            	${lecture.lectureName} (${lecture.professorName}) [${lecture.lectureCode}] ${lecture.evaluated ? '(완료)' : ''}
          		</option>
        	</c:forEach>
        		<c:if test="${empty lectureList}">
          		<option disabled>평가 가능한 과목이 없습니다.</option>
        		</c:if>
      	  </select>
    	</div>
  	 </div>
      </div>
		<div class="card-body">
		
		  <form action="<c:url value='/student/evaluation/submit'/>" method="post" id="evalForm">
		    <input type="hidden" name="lectureId" value="${lecture.lectureId}">
		    <input type="hidden" id="sumScore" name="sumScore" value="0">
		
			<div class="mb-5 p-4 bg-white rounded shadow-sm border">
		      <p class="fw-bold mb-3 fs-5">1. 강의 시청각 자료 및 학습 첨부물이 수업 전 잘 준비되었습니까?</p>
		      <div class="d-flex score-group">
			    <input type="radio" name="q1" id="q1_4" value="4">
			    <label for="q1_4">매우만족</label>
			    
			    <input type="radio" name="q1" id="q1_3" value="3">
			    <label for="q1_3">만족</label>
			    
			    <input type="radio" name="q1" id="q1_2" value="2">
			    <label for="q1_2">보통</label>
			    
			    <input type="radio" name="q1" id="q1_1" value="1">
			    <label for="q1_1">불만족</label>
			    
			    <input type="radio" name="q1" id="q1_0" value="0">
			    <label for="q1_0">매우불만족</label>
			  </div>
			</div>
		
			<div class="mb-5 p-4 bg-white rounded shadow-sm border">
		      <p class="fw-bold mb-3 fs-5">2. 교수님의 강의 전달력과 설명은 명확하고 이해하기 쉬웠습니까?</p>
		      <div class="d-flex score-group">
			    <input type="radio" name="q2" id="q2_4" value="4">
			    <label for="q2_4">매우만족</label>
			    
			    <input type="radio" name="q2" id="q2_3" value="3">
			    <label for="q2_3">만족</label>
			    
			    <input type="radio" name="q2" id="q2_2" value="2">
			    <label for="q2_2">보통</label>
			    
			    <input type="radio" name="q2" id="q2_1" value="1">
			    <label for="q2_1">불만족</label>
			    
			    <input type="radio" name="q2" id="q2_0" value="0">
			    <label for="q2_0">매우불만족</label>
			  </div>
			</div>
			
			<div class="mb-5 p-4 bg-white rounded shadow-sm border">
		      <p class="fw-bold mb-3 fs-5">3. 강의 내용이 해당 과목의 학습 목표를 잘 달성하였습니까?</p>
		      <div class="d-flex score-group">
			    <input type="radio" name="q3" id="q3_4" value="4">
			    <label for="q3_4">매우만족</label>
			    
			    <input type="radio" name="q3" id="q3_3" value="3">
			    <label for="q3_3">만족</label>
			    
			    <input type="radio" name="q3" id="q3_2" value="2">
			    <label for="q3_2">보통</label>
			    
			    <input type="radio" name="q3" id="q3_1" value="1">
			    <label for="q3_1">불만족</label>
			    
			    <input type="radio" name="q3" id="q3_0" value="0">
			    <label for="q3_0">매우불만족</label>
			  </div>
			</div>
			
			<div class="mb-5 p-4 bg-white rounded shadow-sm border">
		      <p class="fw-bold mb-3 fs-5">4. 학생의 질문이나 의견에 대한 교수님의 피드백이 적절하였습니까?</p>
		      <div class="d-flex score-group">
			    <input type="radio" name="q4" id="q4_4" value="4">
			    <label for="q4_4">매우만족</label>
			    
			    <input type="radio" name="q4" id="q4_3" value="3">
			    <label for="q4_3">만족</label>
			    
			    <input type="radio" name="q4" id="q4_2" value="2">
			    <label for="q4_2">보통</label>
			    
			    <input type="radio" name="q4" id="q4_1" value="1">
			    <label for="q4_1">불만족</label>
			    
			    <input type="radio" name="q4" id="q4_0" value="0">
			    <label for="q4_0">매우불만족</label>
			  </div>
			</div>
			
			<div class="mb-5 p-4 bg-white rounded shadow-sm border">
		      <p class="fw-bold mb-3 fs-5">5. 이 강의를 통해 본인의 전공 지식이나 실무 능력이 향상되었습니까?</p>
		      <div class="d-flex score-group">
			    <input type="radio" name="q5" id="q5_4" value="4">
			    <label for="q5_4">매우만족</label>
			    
			    <input type="radio" name="q5" id="q5_3" value="3">
			    <label for="q5_3">만족</label>
			    
			    <input type="radio" name="q5" id="q5_2" value="2">
			    <label for="q5_2">보통</label>
			    
			    <input type="radio" name="q5" id="q5_1" value="1">
			    <label for="q5_1">불만족</label>
			    
			    <input type="radio" name="q5" id="q5_0" value="0">
			    <label for="q5_0">매우불만족</label>
			  </div>
			</div>
			
			<div class="mb-5 p-4 bg-white rounded shadow-sm border">
			    <label for="comment" class="fw-bold mb-3 fs-5">6. 기타 추가 건의사항</label>
			    <textarea 
			        class="form-control" 
			        id="comment" 
			        name="comment" 
			        rows="5" 
			        placeholder="강의에 대한 좋았던 점이나 개선이 필요한 점을 자유롭게 남겨주세요."
			        style="resize: none; border-radius: 10px;"
			    ></textarea>
			    <div class="form-text mt-2">
			        * 작성하신 내용은 익명으로 처리되며, 향후 강의 질 개선을 위한 자료로만 활용됩니다.
			    </div>
			</div>		
		    <div class="text-center mt-4">
		      <button type="submit" class="btn btn-primary btn-lg px-5">제출</button>
		    </div>
		  </form>
		</div>
    </div>
</main>
  
	<%@ include file="/footer.jsp" %>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script>
	// 페이지 로드 즉시 실행 함수
	document.addEventListener("DOMContentLoaded", function() {
        const evalForm = document.getElementById("evalForm");
        console.log("폼 찾기 결과:", evalForm); // 브라우저 콘솔(F12)에서 확인 가능

        if (evalForm) {
            evalForm.onsubmit = function(event) {
                console.log("제출 시도됨"); // 이벤트 작동 확인용
                
                let total = 0;
                const count = 5;
				
                // 전체 문항 체크되어있는지 확인
                for (let i = 1; i <= count; i++) {
                    const selected = document.querySelector('input[name="q' + i + '"]:checked');
                    
                    if (!selected) {
                        alert(i + "번 문항을 선택해 주세요.");
                        
                        // 선택 안 된 문항으로 화면 이동
                        const targetQuestion = document.getElementsByName("q" + i)[0].closest('.mb-5');
                        targetQuestion.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        
                        return false; // 제출 중단
                    }
                    total += parseInt(selected.value);
                }

                // 합계 대입
                const sumInput = document.getElementById("sumScore");
                if (sumInput) {
                    sumInput.value = total;
                    console.log("계산된 총점:", total);
                }
                
                return confirm("제출하시겠습니까?");
            };
        }
    });

    // select 박스 변경 함수도 스크립트 안에 같이 두세요
    function changeLecture(lectureId) {
        if(lectureId) {
            location.href = "<c:url value='/student/evaluation/list'/>?lectureId=" + lectureId;
        }
    }
	</script>
</body>
</html>