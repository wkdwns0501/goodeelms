package com.goodeelms.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.LectureHistoryDTO;
import com.goodeelms.service.ProfessorGradeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/professor/grade/*")
public class ProfessorGradeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 페이지당 학생 수
    private static final int PAGE_SIZE = 10;
    // 시간대 고정(ZonedDateTime)
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private final ProfessorGradeService gradeService = ProfessorGradeService.getInstance();
    
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
        response.sendRedirect(request.getContextPath() + "/professor/grade/list");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = getPath(request);

        if ("/update".equals(path)) {
            handleUpdate(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/professor/grade/list");
    }
    
    // 수강생 목록
    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Integer professorId = (Integer) request.getSession().getAttribute("professor_id");
    	if (professorId == null) {
    		response.sendRedirect(request.getContextPath() + "/common/login");
    		return;
    	}

        // 직전학기(종강) 강의 대상 학기 계산
        SemesterKey target = calcTargetSemester(ZonedDateTime.now(KST));

        // 수정 가능 여부(성적 기입 기간 = 1월/7월)
        boolean isEditable = isGradeInputPeriod(ZonedDateTime.now(KST));
        
        // 성적 기간 테스트
    	// 1월, 7월만 성적 기입 가능 / 나머지 달은 성적 기입 불가능
//        ZonedDateTime now = ZonedDateTime.of(2026, 3, 1, 10, 0, 0, 0, KST); // 두번째 값 만 변경
//        SemesterKey target = calcTargetSemester(now);
//        boolean isEditable = isGradeInputPeriod(now);
        
        // 교수의 직전학기 종강 강의 목록
        List<LectureDTO> lectureList =
                gradeService.listCompletedLectures(professorId, target.year, target.semester);
        
        // 선택된 lectureId
        Integer lectureId = parseIntOrNull(request.getParameter("lectureId"));
        if (lectureId == null) {
            // 초기에는 첫 강의 자동 선택
            if (lectureList != null && !lectureList.isEmpty()) {
                lectureId = lectureList.get(0).getLectureId();
            }
        }
        
        // 페이징 / 검색
        int page = parseIntOrDefault(request.getParameter("page"), 1);
        if (page < 1) page = 1;
        String keyword = request.getParameter("keyword");
        if (keyword != null && keyword.isBlank()) keyword = null;

        // 학생 목록 조회(lectureId 없으면 빈 리스트 처리)
        List<LectureHistoryDTO> historyList = List.of();
        int totalCount = 0;

        if (lectureId != null) {
            totalCount = gradeService.countStudents(lectureId, keyword);
            historyList = gradeService.listStudents(lectureId, keyword, page, PAGE_SIZE);
        }

        int lastPage = (int) Math.ceil(totalCount / (double) PAGE_SIZE);
        if (lastPage == 0) lastPage = 1;
        if (page > lastPage) page = lastPage;

        // 네비게이터
        int navSize = 10;
        int navStart = ((page - 1) / navSize) * navSize + 1;
        int navEnd = Math.min(navStart + navSize - 1, lastPage);

        request.setAttribute("targetYear", target.year);
        request.setAttribute("targetSemester", target.semester);
        request.setAttribute("isEditable", isEditable);

        request.setAttribute("lectureList", lectureList);
        request.setAttribute("selectedLectureId", lectureId);
        
        // 선택된 강의 아이디
        System.out.println("lecture_id = " + lectureId);
        
        request.setAttribute("keyword", keyword);
        request.setAttribute("page", page);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("lastPage", lastPage);
        request.setAttribute("navStart", navStart);
        request.setAttribute("navEnd", navEnd);

        request.setAttribute("historyList", historyList);

        request.getRequestDispatcher("/WEB-INF/views/professor/gradeManage.jsp")
               .forward(request, response);
    }
    
    // 성적 수정(성적 기입)
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	Integer professorId = (Integer) request.getSession().getAttribute("professor_id");
    	if (professorId == null) {
    		response.sendRedirect(request.getContextPath() + "/common/login");
    		return;
    	}

        // lectureId / page 유지
        int lectureId = parseIntOrDefault(request.getParameter("lectureId"), 0);
        int page = parseIntOrDefault(request.getParameter("page"), 1);
        String keyword = request.getParameter("keyword");
        if (keyword != null && keyword.isBlank()) keyword = null;

        // 성적 기입 기간이 아니면 서버에서 차단해야 함

        // 배열 파라미터(페이지당 10명)
        // studentId[], oldScore[], newScore[]
        String[] studentIdArr = request.getParameterValues("studentId");
        String[] oldScoreArr = request.getParameterValues("oldScore");
        String[] newScoreArr = request.getParameterValues("newScore");

        try {
        	int updated = gradeService.updateGrades(professorId, lectureId,
            			studentIdArr,oldScoreArr,newScoreArr
            );
            // 수정 성공
        	String msg;
            if (updated > 0) {
                msg = "저장에 성공했습니다. (" + updated + "건)";
            } else {
                msg = "저장할 내용이 없습니다.";
            }
            response.sendRedirect(request.getContextPath()
                    + "/professor/grade/list?lectureId=" + lectureId
                    + "&page=" + page
                    + (keyword != null ? "&keyword=" + urlEncode(keyword) : "")
                    + "&msg=" + urlEncode(msg));
        } catch (Exception e) {
            // 수정 실패
        	String errMsg = (e.getMessage() != null && !e.getMessage().isBlank())
                    ? e.getMessage()
                    : "성적 저장에 실패했습니다.";

            response.sendRedirect(request.getContextPath()
                    + "/professor/grade/list?lectureId=" + lectureId
                    + "&page=" + page
                    + (keyword != null ? "&keyword=" + urlEncode(keyword) : "")
                    + "&error=" + urlEncode(errMsg));
        }
    }

    /**
     *   직전학기(종강) 대상 학기 계산 규칙
     * - 1월(성적기입), 2월(학생조회): 전년도 2학기
     * - 7월(성적기입), 8월(학생조회): 해당년도 1학기
     * - 그 외(3~6, 9~12): 직전학기 기준으로는
     *   3~6이면 전년도 2학기,
     *   9~12이면 해당년도 1학기
     */
    // => 년/월로 학기 계산
    private SemesterKey calcTargetSemester(ZonedDateTime now) {
        int year = now.getYear();
        int month = now.getMonthValue();

        // 7~12: 해당년도 1학기(직전 종강 = 1학기)
        if (month >= 7 && month <= 12) {
            return new SemesterKey(year, 1);
        }

        // 1~6: 전년도 2학기(직전 종강 = 2학기)
        return new SemesterKey(year - 1, 2);
    }
    
    // 성적 입력 기간인지
    private boolean isGradeInputPeriod(ZonedDateTime now) {
        int m = now.getMonthValue();
        return (m == 1 || m == 7);
    }
    
    // int 변환 아니면 NULL 반환
    private Integer parseIntOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
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
    
    // 쿼리 파라미터 인코딩
    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s;
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
