package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.service.LectureService;

@WebServlet("/lecture/*")
public class LectureController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LectureService service = LectureService.getInstance();
       
    public LectureController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 조회
	    String path = request.getPathInfo(); // /add /list
	    if (path == null) path = "/list";
	    
//        // 로그인 체크 
//        Integer professorId = (Integer) request.getSession().getAttribute("professor_id");
	      Integer professorId = 4; // 테스트용
//        if (professorId == null) {
//            response.sendRedirect(request.getContextPath() + "/login.jsp"); // 로그인 페이지 수정
//            return;
//        }
	    
	    if (path.equals("/add")) {
	        request.getRequestDispatcher("/WEB-INF/views/lecture/insert.jsp")
	               .forward(request, response);
	        return;
	    }

	    if (path.equals("/list")) {
	    	String keyword = request.getParameter("keyword");
	        String pageParam = request.getParameter("page");
	        int page = 1;
	        
	        try {
	            if (pageParam != null && !pageParam.isBlank()) {
	                page = Integer.parseInt(pageParam);
	            }
	        } catch (NumberFormatException e) {
	            page = 1;
	        }

	        int limit = 5;
	        if (page < 1) page = 1;

	        // professorId로 majorId 구해서 DAO 호출
	        int totalCount = service.getLectureTotalCount(professorId, keyword);
	        int totalPage = (int) Math.ceil(totalCount / (double) limit);

	        if (totalPage == 0) totalPage = 1;
	        if (page > totalPage) page = totalPage;
	        
	        request.setAttribute("lectures",
	                service.getLecturePage(professorId, page, limit, keyword));
	        request.setAttribute("page", page);
	        request.setAttribute("limit", limit);
	        request.setAttribute("totalCount", totalCount);
	        request.setAttribute("totalPage", totalPage);
	        request.setAttribute("keyword", keyword);

	        request.getRequestDispatcher("/WEB-INF/views/lecture/list.jsp")
	               .forward(request, response);
	        return;
	    }
	    response.sendRedirect(request.getContextPath() + "/lecture/list");		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo(); // /insert
        if (path == null) path = "";

        // 강의 등록 처리
        if (path.equals("/insert")) {
//        	Integer professorId = (Integer) request.getSession().getAttribute("professor_id");
        	Integer professorId = 4; // 테스트용
//            if (professorId == null) {
//                response.sendRedirect(request.getContextPath() + "/list.jsp");
//                return;
//            }
            LectureDTO lecture = new LectureDTO();
            lecture.setLectureName(request.getParameter("lecture_name"));
            lecture.setLectureDescription(request.getParameter("lecture_description"));
            lecture.setLectureRoom(request.getParameter("lecture_room"));
            lecture.setLectureType(request.getParameter("lecture_type"));
            lecture.setLectureYear(request.getParameter("lecture_year"));
            lecture.setLectureSection(request.getParameter("lecture_section"));

            // professor_id는 세션 => Service
            lecture.setProfessorId(professorId);
            try {
                lecture.setLectureCredit(Integer.parseInt(request.getParameter("lecture_credit")));
                lecture.setLectureSemester(Integer.parseInt(request.getParameter("lecture_semester")));
                lecture.setLectureCapacity(Integer.parseInt(request.getParameter("lecture_capacity")));
                service.insertLecture(lecture);
                response.sendRedirect(request.getContextPath() + "/lecture/list");
                return;
            } catch (NumberFormatException e) {
                request.setAttribute("error", "숫자 입력값이 올바르지 않습니다.");
                request.setAttribute("form", lecture);
                request.getRequestDispatcher("/WEB-INF/views/lecture/insert.jsp").forward(request, response);
                return;
            } catch (Exception e) {
            	System.out.println("강의 등록 실패: " + e);
                request.setAttribute("error", "강의 등록에 실패했습니다. (중복 또는 입력값 오류)");
                request.setAttribute("form", lecture);
                request.getRequestDispatcher("/WEB-INF/views/lecture/insert.jsp").forward(request, response);
                return;
            }
        } else response.sendRedirect(request.getContextPath() + "/lecture/list");
	}

}
