<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학사 일정 관리</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.10/index.global.min.js"></script>

<style>
    body { background-color: #f8fafc; font-family: 'Pretendard', sans-serif; }
    .content { padding: 100px 40px 40px 40px; }

    /* 제목 영역 */
    .page-header { 
        margin-bottom: 30px; 
        padding-bottom: 15px; 
        border-bottom: 2px solid #e2e8f0; 
    }
    .page-title { font-size: 1.8rem; font-weight: 800; color: #1e293b; }

    /* [수정] 일정 리스트 스크롤 영역 */
    .scroll-container {
        max-height: 500px; /* 고정 높이 */
        overflow-y: auto;  /* 세로 스크롤 활성화 */
        padding-right: 10px;
        margin-bottom: 20px;
    }
    /* 스크롤바 디자인 (세련되게) */
    .scroll-container::-webkit-scrollbar { width: 6px; }
    .scroll-container::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 10px; }

    /* 일정 항목 카드 */
    .schedule-item {
        background: #fff;
        border: 1px solid #e2e8f0;
        border-radius: 12px;
        padding: 15px;
        margin-bottom: 12px;
        cursor: pointer;
        transition: 0.2s;
        border-left: 6px solid #cbd5e1;
    }
    .schedule-item.active { border-color: #1e293b; background-color: #f8fafc; }
    
    /* 항목별 컬러 포인트 */
    .item-scholar.active { border-left-color: #3b82f6 !important; }
    .item-registration.active { border-left-color: #10b981 !important; }
    .item-grade.active { border-left-color: #f59e0b !important; }
    .item-extra.active { border-left-color: #8b5cf6 !important; } /* 추가 항목 예시 */

    .item-title { font-weight: 700; color: #334155; font-size: 1rem; }
    .date-display { 
        margin-top: 8px;
        font-size: 0.85rem;
        color: #64748b;
    }
    .date-display span { font-weight: 600; color: #3b82f6; }

    /* 달력 카드 */
    #calendar-card {
        background: #fff;
        border-radius: 20px;
        padding: 20px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.03);
    }
    
    .btn-save {
        background-color: #1e293b;
        color: white; border: none; padding: 15px;
        border-radius: 12px; width: 100%; font-weight: 700;
    }
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

    <form action="<c:url value='/admin/academicSchedule/update'/>" method="post">
      <div class="row g-4">
        <div class="col-lg-4 col-xl-3">
          <div class="scroll-container" id="itemSelector">
            
            <div class="schedule-item item-scholar active" data-type="scholar">
                <div class="item-title">장학생 선발 기간</div>
                <div class="date-display">일정: <span id="text_scholar">${schedule.scholar_start != null ? schedule.scholar_start : '미설정'}</span></div>
                <input type="hidden" name="scholar_start" id="scholar_start" value="${schedule.scholar_start}">
                <input type="hidden" name="scholar_end" id="scholar_end" value="${schedule.scholar_end}">
            </div>

            <div class="schedule-item item-registration" data-type="registration">
                <div class="item-title">수강 신청 기간</div>
                <div class="date-display">일정: <span id="text_registration">미설정</span></div>
                <input type="hidden" name="registration_start" id="registration_start">
                <input type="hidden" name="registration_end" id="registration_end">
            </div>

            <div class="schedule-item item-grade" data-type="grade">
                <div class="item-title">성적 정정 기간</div>
                <div class="date-display">일정: <span id="text_grade">미설정</span></div>
                <input type="hidden" name="grade_start" id="grade_start">
                <input type="hidden" name="grade_end" id="grade_end">
            </div>

            <div class="schedule-item item-extra" data-type="extra">
                <div class="item-title">신입생 오리엔테이션</div>
                <div class="date-display">일정: <span id="text_extra">미설정</span></div>
                <input type="hidden" name="extra_start" id="extra_start">
                <input type="hidden" name="extra_end" id="extra_end">
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

<script>
document.addEventListener('DOMContentLoaded', function() {
    const calendarEl = document.getElementById('calendar');
    let currentType = 'scholar'; 
    let selectionStep = 1; 
    let tempStart = null;

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko',
        height: 650,
        dateClick: function(info) {
            const clickedDate = info.dateStr;
            const textDisplay = document.getElementById('text_' + currentType);
            const startInput = document.getElementById(currentType + '_start');
            const endInput = document.getElementById(currentType + '_end');

            if (selectionStep === 1) {
                tempStart = clickedDate;
                selectionStep = 2;
                textDisplay.innerText = clickedDate + " ~ 선택 중";
                textDisplay.style.color = "#ef4444";
            } else {
                if (new Date(clickedDate) < new Date(tempStart)) {
                    alert("종료일은 시작일보다 빠를 수 없습니다.");
                    return;
                }
                // input 값 할당 (Controller에서 이 name들로 받음)
                startInput.value = tempStart;
                endInput.value = clickedDate;
                
                textDisplay.innerText = tempStart + " ~ " + clickedDate;
                textDisplay.style.color = "#3b82f6";
                selectionStep = 1;
                tempStart = null;
            }
        }
    });
    calendar.render();

    // 항목 클릭 시 currentType 변경 로직
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
</script>

</body>
</html>