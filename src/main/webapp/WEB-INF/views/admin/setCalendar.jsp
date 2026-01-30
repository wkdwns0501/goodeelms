<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>일정 관리 - GoodeeLMS</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.10/index.global.min.js"></script>

<style>
    body { background-color: #f8fafc; font-family: 'Pretendard', sans-serif; }
    .content { padding: 100px 40px 40px 40px; }
    .page-header { margin-bottom: 30px; padding-bottom: 15px; border-bottom: 2px solid #e2e8f0; }
    .page-title { font-size: 1.8rem; font-weight: 800; color: #1e293b; }
    .scroll-container { max-height: 600px; overflow-y: auto; padding-right: 10px; margin-bottom: 20px; }
    .scroll-container::-webkit-scrollbar { width: 6px; }
    .scroll-container::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 10px; }
    .schedule-item { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 15px; margin-bottom: 12px; cursor: pointer; transition: 0.2s; border-left: 6px solid #cbd5e1; }
    .schedule-item.active { border-color: #1e293b; background-color: #f8fafc; border-left-width: 10px; }
    .item-title { font-weight: 700; color: #334155; font-size: 1rem; }
    .date-display { margin-top: 8px; font-size: 0.85rem; color: #64748b; }
    .date-display span { font-weight: 600; color: #3b82f6; }
    #calendar-card { background: #fff; border-radius: 20px; padding: 20px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); }
    .fc-daygrid-day-number {
    text-decoration: none !important;
    color: #1e293b;
    font-weight: 500;
    }
    /* 오늘 날짜 표시 커스텀 */
	.fc-day-today {
	    background-color: #f1f5f9 !important;
	}
	
	/* 클릭 시 강조될 배경색 (범위) */
	.selected-range {
	    background-color: #dbeafe !important;
	}
	
	/* 시작일로 선택된 칸 강조 */
	.selected-start {
	    background-color: #dbeafe !important;
	}
	
    .btn-save { background-color: #1e293b; color: white; border: none; padding: 15px; border-radius: 12px; width: 100%; font-weight: 700; margin-top: 10px; }
    /* 학기별 색상 구분 (선택사항) */
    /* 마우스 올렸을 때(hover) 스타일 추가 */
	.btn-save:hover {
	    background-color: solid white; /* 배경색을 약간 밝게 변경 */
	    border : solid black;
	    cursor: pointer;
	}
	
    .schedule-item[data-type*="first"] { border-left-color: #3b82f6; }
    .schedule-item[data-type*="second"] { border-left-color: #f59e0b; }
</style>
</head>

<body>
<%@ include file="/header.jsp" %>
<%@ include file="/sideNavbar.jsp" %>

<main class="content">
  <div class="container-fluid">
    <div class="page-header">
        <h2 class="page-title">학사 일정 설정</h2>
    </div>

    <form action="<c:url value='/admin/setCalendar/update'/>" method="post">
      <div class="row g-4">
        <div class="col-lg-4 col-xl-3">
          <div class="scroll-container" id="itemSelector">
            
            <%-- 2학기 성적 기입 (DB Key 기준 매핑) --%>
            <div class="schedule-item active" data-type="ac_first_grade_insert">
                <div class="item-title">2학기 성적 기입</div>
                <div class="date-display">일정: <span id="text_ac_first_grade_insert">${empty schedule.ac_first_grade_insert_start ? '미설정' : schedule.ac_first_grade_insert_start} ~ ${empty schedule.ac_first_grade_insert_end ? '미설정' : schedule.ac_first_grade_insert_end}</span></div>
                <input type="hidden" name="ac_first_grade_insert_start" id="ac_first_grade_insert_start" value="${schedule.ac_first_grade_insert_start}">
                <input type="hidden" name="ac_first_grade_insert_end" id="ac_first_grade_insert_end" value="${schedule.ac_first_grade_insert_end}">
            </div>

			<div class="schedule-item" data-type="ac_first_lecture_insert">
                <div class="item-title">1학기 강의 등록</div>
                <div class="date-display">일정: <span id="text_ac_first_lecture_insert">${empty schedule.ac_first_lecture_insert_start ? '미설정' : schedule.ac_first_lecture_insert_start} ~ ${empty schedule.ac_first_lecture_insert_end ? '미설정' : schedule.ac_first_lecture_insert_end}</span></div>
                <input type="hidden" name="ac_first_lecture_insert_start" id="ac_first_lecture_insert_start" value="${schedule.ac_first_lecture_insert_start}">
                <input type="hidden" name="ac_first_lecture_insert_end" id="ac_first_lecture_insert_end" value="${schedule.ac_first_lecture_insert_end}">
            </div>
            
            <div class="schedule-item" data-type="ac_first_lecture_evaluation">
                <div class="item-title">2학기 강의평가(장학생 선정)</div>
                <div class="date-display">일정: <span id="text_ac_first_lecture_evaluation">${empty schedule.ac_first_lecture_evaluation_start ? '미설정' : schedule.ac_first_lecture_evaluation_start} ~ ${empty schedule.ac_first_lecture_evaluation_end ? '미설정' : schedule.ac_first_lecture_evaluation_end}</span></div>
                <input type="hidden" name="ac_first_lecture_evaluation_start" id="ac_first_lecture_evaluation_start" value="${schedule.ac_first_lecture_evaluation_start}">
                <input type="hidden" name="ac_first_lecture_evaluation_end" id="ac_first_lecture_evaluation_end" value="${schedule.ac_first_lecture_evaluation_end}">
            </div>

            <div class="schedule-item" data-type="student_first_lecture_cart">
                <div class="item-title">1학기 수강바구니</div>
                <div class="date-display">일정: <span id="text_student_first_lecture_cart">${empty schedule.student_first_lecture_cart_start ? '미설정' : schedule.student_first_lecture_cart_start} ~ ${empty schedule.student_first_lecture_cart_end ? '미설정' : schedule.student_first_lecture_cart_end}</span></div>
                <input type="hidden" name="student_first_lecture_cart_start" id="student_first_lecture_cart_start" value="${schedule.student_first_lecture_cart_start}">
                <input type="hidden" name="student_first_lecture_cart_end" id="student_first_lecture_cart_end" value="${schedule.student_first_lecture_cart_end}">
            </div>
            
            <div class="schedule-item" data-type="student_first_enrollment">
                <div class="item-title">1학기 수강신청</div>
                <div class="date-display">일정: <span id="text_student_first_enrollment">${empty schedule.student_first_enrollment_start ? '미설정' : schedule.student_first_enrollment_start} ~ ${empty schedule.student_first_enrollment_end ? '미설정' : schedule.student_first_enrollment_end}</span></div>
                <input type="hidden" name="student_first_enrollment_start" id="student_first_enrollment_start" value="${schedule.student_first_enrollment_start}">
                <input type="hidden" name="student_first_enrollment_end" id="student_first_enrollment_end" value="${schedule.student_first_enrollment_end}">
            </div>

            <div class="schedule-item" data-type="ac_first_semester">
                <div class="item-title">1학기 개강 및 종강</div>
                <div class="date-display">일정: <span id="text_ac_first_semester">${empty schedule.ac_first_semester_start ? '미설정' : schedule.ac_first_semester_start} ~ ${empty schedule.ac_first_semester_end ? '미설정' : schedule.ac_first_semester_end}</span></div>
                <input type="hidden" name="ac_first_semester_start" id="ac_first_semester_start" value="${schedule.ac_first_semester_start}">
                <input type="hidden" name="ac_first_semester_end" id="ac_first_semester_end" value="${schedule.ac_first_semester_end}">
            </div>

            <div class="schedule-item" data-type="ac_second_grade_insert">
                <div class="item-title">1학기 성적 기입</div>
                <div class="date-display">일정: <span id="text_ac_second_grade_insert">${empty schedule.ac_second_grade_insert_start ? '미설정' : schedule.ac_second_grade_insert_start} ~ ${empty schedule.ac_second_grade_insert_end ? '미설정' : schedule.ac_second_grade_insert_end}</span></div>
                <input type="hidden" name="ac_second_grade_insert_start" id="ac_second_grade_insert_start" value="${schedule.ac_second_grade_insert_start}">
                <input type="hidden" name="ac_second_grade_insert_end" id="ac_second_grade_insert_end" value="${schedule.ac_second_grade_insert_end}">
            </div>

			<div class="schedule-item active" data-type="ac_second_lecture_insert">
                <div class="item-title">2학기 강의 등록</div>
                <div class="date-display">일정: <span id="text_ac_second_lecture_insert">${empty schedule.ac_second_lecture_insert_start ? '미설정' : schedule.ac_second_lecture_insert_start} ~ ${empty schedule.ac_second_lecture_insert_end ? '미설정' : schedule.ac_second_lecture_insert_end}</span></div>
                <input type="hidden" name="ac_second_lecture_insert_start" id="ac_second_lecture_insert_start" value="${schedule.ac_second_lecture_insert_start}">
                <input type="hidden" name="ac_second_lecture_insert_end" id="ac_second_lecture_insert_end" value="${schedule.ac_second_lecture_insert_end}">
            </div>
            
            <div class="schedule-item" data-type="ac_second_lecture_evaluation">
                <div class="item-title">1학기 강의평가(장학생 선정)</div>
                <div class="date-display">일정: <span id="text_ac_second_lecture_evaluation">${empty schedule.ac_second_lecture_evaluation_start ? '미설정' : schedule.ac_second_lecture_evaluation_start} ~ ${empty schedule.ac_second_lecture_evaluation_end ? '미설정' : schedule.ac_second_lecture_evaluation_end}</span></div>
                <input type="hidden" name="ac_second_lecture_evaluation_start" id="ac_second_lecture_evaluation_start" value="${schedule.ac_second_lecture_evaluation_start}">
                <input type="hidden" name="ac_second_lecture_evaluation_end" id="ac_second_lecture_evaluation_end" value="${schedule.ac_second_lecture_evaluation_end}">
            </div>

            <div class="schedule-item" data-type="student_second_lecture_cart">
                <div class="item-title">2학기 수강바구니</div>
                <div class="date-display">일정: <span id="text_student_second_lecture_cart">${empty schedule.student_second_lecture_cart_start ? '미설정' : schedule.student_second_lecture_cart_start} ~ ${empty schedule.student_second_lecture_cart_end ? '미설정' : schedule.student_second_lecture_cart_end}</span></div>
                <input type="hidden" name="student_second_lecture_cart_start" id="student_second_lecture_cart_start" value="${schedule.student_second_lecture_cart_start}">
                <input type="hidden" name="student_second_lecture_cart_end" id="student_second_lecture_cart_end" value="${schedule.student_second_lecture_cart_end}">
            </div>
            
            <div class="schedule-item" data-type="student_second_enrollment">
                <div class="item-title">2학기 수강신청</div>
                <div class="date-display">일정: <span id="text_student_second_enrollment">${empty schedule.student_second_enrollment_start ? '미설정' : schedule.student_second_enrollment_start} ~ ${empty schedule.student_second_enrollment_end ? '미설정' : schedule.student_second_enrollment_end}</span></div>
                <input type="hidden" name="student_second_enrollment_start" id="student_second_enrollment_start" value="${schedule.student_second_enrollment_start}">
                <input type="hidden" name="student_second_enrollment_end" id="student_second_enrollment_end" value="${schedule.student_second_enrollment_end}">
            </div>

            <div class="schedule-item" data-type="ac_second_semester">
                <div class="item-title">2학기 개강 및 종강</div>
                <div class="date-display">일정: <span id="text_ac_second_semester">${empty schedule.ac_second_semester_start ? '미설정' : schedule.ac_second_semester_start} ~ ${empty schedule.ac_second_semester_end ? '미설정' : schedule.ac_second_semester_end}</span></div>
                <input type="hidden" name="ac_second_semester_start" id="ac_second_semester_start" value="${schedule.ac_second_semester_start}">
                <input type="hidden" name="ac_second_semester_end" id="ac_second_semester_end" value="${schedule.ac_second_semester_end}">
            </div>
          </div>

          <button type="submit" class="btn btn-save">설정 저장하기</button>
        </div>

        <div class="col-lg-8 col-xl-9">
          <div id="calendar-card">
              <div id="calendar"></div>
          </div>
        </div>
      </div>
    </form>
  </div>
</main>

<%@ include file="/footer.jsp" %>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const calendarEl = document.getElementById('calendar');
    // 초기 활성화된 첫 번째 아이템의 data-type으로 설정
    let currentType = 'ac_first_grade_insert'; 
    let selectionStep = 1; 
    let tempStart = null;

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko',
        height: 650,
        dateClick: function(info) {
            var clickedDate = info.dateStr; // "2026-01-26" 형태
            var textDisplay = document.getElementById('text_' + currentType);
            var startInput = document.getElementById(currentType + '_start');
            var endInput = document.getElementById(currentType + '_end');

            if (selectionStep === 1) {
                // 1. 기존 선택색상 초기화
                var allDays = document.querySelectorAll('.fc-day');
                for (var i = 0; i < allDays.length; i++) {
                    allDays[i].classList.remove('selected-range');
                    allDays[i].classList.remove('selected-start');
                }

                tempStart = clickedDate;
                selectionStep = 2;
                
                // 시작일 색상 강조
                info.dayEl.classList.add('selected-start');
                
                textDisplay.innerText = clickedDate + " ~ 선택 중";
                textDisplay.style.color = "#ef4444";
            } else {
                // 2. 종료일 선택 로직
                var startD = new Date(tempStart);
                var endD = new Date(clickedDate);

                if (endD < startD) {
                    alert("종료일은 시작일보다 빠를 수 없습니다.");
                    return;
                }
                
                startInput.value = tempStart;
                endInput.value = clickedDate;
                textDisplay.innerText = tempStart + " ~ " + clickedDate;
                textDisplay.style.color = "#3b82f6";

                // 범위 칠하기 로직
                var curr = new Date(startD);
                while (curr <= endD) {
                    // 날짜를 YYYY-MM-DD 문자열로 변환
                    var y = curr.getFullYear();
                    var m = (curr.getMonth() + 1);
                    if (m < 10) m = '0' + m;
                    var d = curr.getDate();
                    if (d < 10) d = '0' + d;
                    
                    var dateStr = y + '-' + m + '-' + d;
                    
                    // 해당 날짜 칸 찾아서 클래스 추가
                    var dayEl = document.querySelector('.fc-day[data-date="' + dateStr + '"]');
                    if (dayEl) {
                        dayEl.classList.add('selected-range');
                    }
                    
                    // 날짜 1일 증가
                    curr.setDate(curr.getDate() + 1);
                }

                selectionStep = 1;
                tempStart = null;
            }
        }
    });
    calendar.render();

    document.querySelectorAll('.schedule-item').forEach(item => {
        item.addEventListener('click', function() {
            document.querySelectorAll('.schedule-item').forEach(i => i.classList.remove('active'));
            this.classList.add('active');
            currentType = this.getAttribute('data-type');
            selectionStep = 1; 
            tempStart = null;
        });
    });
});

document.querySelectorAll('.schedule-item').forEach(item => {
    item.addEventListener('click', function() {
        document.querySelectorAll('.schedule-item').forEach(i => i.classList.remove('active'));
        this.classList.add('active');
        currentType = this.getAttribute('data-type');
        selectionStep = 1; 
        tempStart = null;
        
        // 달력 색상 싹 지우기
        document.querySelectorAll('.fc-day').forEach(el => {
            el.classList.remove('selected-range', 'selected-start');
        });
    });
});
</script>
</body>
</html>