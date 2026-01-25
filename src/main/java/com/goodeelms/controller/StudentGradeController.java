package com.goodeelms.controller;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.goodeelms.dto.StudentGradeDTO;
import com.goodeelms.listener.LMSScheduleListener;
import com.goodeelms.service.StudentGradeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/student/grade/*")
public class StudentGradeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;
    private final StudentGradeService gradeService = StudentGradeService.getInstance();

    private String getPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        return (path == null) ? "" : path;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = getPath(request);
        if ("/list".equals(path) || "/".equals(path) || "".equals(path)) {
            handleList(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/student/grade/list");
    }
    
    // 성적 조회 화면 (목록)
    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer studentId = (Integer) request.getSession().getAttribute("student_id");
        if (studentId == null) {
            response.sendRedirect(request.getContextPath() + "/common/login");
            return;
        }
        
        // 어떤 탭인지
        String tab = request.getParameter("tab");
        if (tab == null || tab.isBlank()) tab = "recent"; // 기본: 직전학기
        
        ZonedDateTime now = ZonedDateTime.now(LMSScheduleListener.getZONE_ID());
        // 성적 조회 기간 테스트
//        ZonedDateTime now = ZonedDateTime.of(2026, 8, 1, 10, 0, 0, 0, LMSScheduleListener.getZONE_ID()); // 두번째 값 만 변경
        // 직전학기 계산
        SemesterKey target = calcTargetSemester(now);
        // 평가 기간 여부(2월/8월)
        boolean isEvalPeriod = gradeService.isEvaluationPeriod(now);
        
        // 검색 / 페이징
        int page = parseIntOrDefault(request.getParameter("page"), 1);
        if (page < 1) page = 1;
        
        String keyword = request.getParameter("keyword");
        if (keyword != null && keyword.isBlank()) keyword = null;
        
        request.setAttribute("tab",	tab);
        request.setAttribute("targetYear", target.year);
        request.setAttribute("targetSemester", target.semester);
        request.setAttribute("isEvalPeriod", isEvalPeriod);
        
        // recent 탭
        if ("recent".equals(tab)) {
        	boolean unlocked = true;
        	int missingEvalCount = 0;
        	
        	 // 평가 기간(2,8월)에는 직전학기 종강 강의 전부 평가 완료해야 공개
        	if (isEvalPeriod) {
        		missingEvalCount = gradeService.countMissEval(studentId, target.year, target.semester);
        		unlocked = (missingEvalCount == 0);
        	}
        	request.setAttribute("recentUnlocked", unlocked);
        	request.setAttribute("missingEvalCount", missingEvalCount);
        	
        	 // 잠금이면 리스트는 안 내려도 됨
        	if (unlocked) {
        		 // 직전학기 성적 리스트
                List<StudentGradeDTO> recentList =
                        gradeService.listRecentGrades(studentId, target.year, target.semester);
                request.setAttribute("recentList", recentList);
        	}
        	 request.getRequestDispatcher("/WEB-INF/views/student/grade.jsp")
		            .forward(request, response);
		     return;
        }
        
        // all 탭
        if ("all".equals(tab)) {
            int totalCount = gradeService.countAllHistory(studentId, target.year, target.semester, keyword);

            int lastPage = (int) Math.ceil(totalCount / (double) PAGE_SIZE);
            if (lastPage == 0) lastPage = 1;
            if (page > lastPage) page = lastPage;

            List<StudentGradeDTO> allList =
                    gradeService.listAllHistory(studentId, target.year, target.semester, keyword, page, PAGE_SIZE);
            
            int navSize = 10;
            int navStart = ((page - 1) / navSize) * navSize + 1;
            int navEnd = Math.min(navStart + navSize - 1, lastPage);

            request.setAttribute("keyword", keyword);
            request.setAttribute("page", page);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("lastPage", lastPage);
            request.setAttribute("navStart", navStart);
            request.setAttribute("navEnd", navEnd);

            request.setAttribute("allList", allList);

            request.getRequestDispatcher("/WEB-INF/views/student/grade.jsp")
                   .forward(request, response);
            return;
        }
        
     	// tab이 이상하면 recent로
        response.sendRedirect(request.getContextPath() + "/student/grade/list?tab=recent");
    }
    
    /**
     * 학생용 직전학기(종강) 대상 학기 계산 규칙
     * - 1~6월  : 전년도 2학기 (전년도 12월 종강이 직전학기)
     * - 7~12월 : 해당년도 1학기 (해당년도 6월 종강이 직전학기)
     */
    private SemesterKey calcTargetSemester(ZonedDateTime now) {
        Map<String, ZonedDateTime> map = LMSScheduleListener.getEventTimeMap();

        ZonedDateTime closeFirst = map.get("ac_close_first_semester");   // 1학기 종강
        ZonedDateTime closeSecond = map.get("ac_close_second_semester"); // 2학기 종강

        if (closeFirst == null || closeSecond == null) {
            throw new IllegalStateException("학사 일정(종강일) 설정이 누락되었습니다. 관리자에게 문의하세요.");
        }

        int year = now.getYear();

        // 2학기 종강 이후(=겨울방학~다음해 1월 포함): 직전학기 = 해당년도 2학기
        if (!now.isBefore(closeSecond)) {
            return new SemesterKey(year, 2);
        }

        // 1학기 종강 이후 ~ 2학기 종강 이전(=여름방학~2학기 중): 직전학기 = 해당년도 1학기
        if (!now.isBefore(closeFirst)) {
            return new SemesterKey(year, 1);
        }

        // 1학기 종강 전(=1학기 진행 중): 직전학기 = 전년도 2학기
        return new SemesterKey(year - 1, 2);
    }




    // int 변환 아니면 기본값 반환
    private int parseIntOrDefault(String s, int def) {
        if (s == null || s.isBlank()) return def;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    // 년도, 학기를 세트로 사용하기 위한 객체
    private static class SemesterKey {
        final int year;
        final int semester;
        SemesterKey(int year, int semester) {
            this.year = year;
            this.semester = semester;
        }
    }

}
