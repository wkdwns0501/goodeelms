package com.goodeelms.controller;

import java.io.IOException;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.service.BuildingService;
import com.goodeelms.service.LectureService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/professor/lecture/*")
public class LectureController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LectureService lectureService = LectureService.getInstance();
	private final BuildingService buildingService = BuildingService.getInstance();  
    public LectureController() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String path = request.getPathInfo(); // /add /list
	    if (path == null) path = "/list";
	    
	    Integer loginId = (Integer) request.getSession().getAttribute("professor_id");
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/common/login");
            return;
        }
	    
	    if (path.equals("/add")) { // 강의 등록 페이지 조회
	    	request.setAttribute("buildingList", buildingService.getAll());
	        request.getRequestDispatcher("/WEB-INF/views/lecture/insert.jsp")
	               .forward(request, response);
	        return;
	    } else if (path.equals("/list")) { // 강의 목록 조회
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

		    final int limit = 5;
		    final int blockSize = 10;

		    // 입력 page 최소 보정
		    page = Math.max(page, 1);

		    // totalCount / totalPage 계산 (0건이면 totalPage=1 보장)
		    final int totalCount = lectureService.getLectureTotalCount(loginId, keyword);
		    final int totalPage = Math.max(1, (int) Math.ceil(totalCount / (double) limit));

		    // page 최대 보정
		    page = Math.min(page, totalPage);

		    // 블록 시작/끝 계산
		    final int startPage = ((page - 1) / blockSize) * blockSize + 1;
		    final int endPage = Math.min(startPage + blockSize - 1, totalPage);

		    // 블록 이동 (없으면 1)
		    final int prevBlockPage = (startPage > 1) ? (startPage - 1) : 1;
		    final int nextBlockPage = (endPage < totalPage) ? (endPage + 1) : totalPage;
	        
	        request.setAttribute("lectures",
	        		lectureService.getLecturePage(loginId, page, limit, keyword));
	        
	        request.setAttribute("page", page);
	        request.setAttribute("limit", limit);
	        request.setAttribute("totalCount", totalCount);
	        request.setAttribute("totalPage", totalPage);
	        request.setAttribute("keyword", keyword);

	        request.setAttribute("blockSize", blockSize);
	        request.setAttribute("startPage", startPage);
	        request.setAttribute("endPage", endPage);
	        request.setAttribute("prevBlockPage", prevBlockPage);
	        request.setAttribute("nextBlockPage", nextBlockPage);

	        request.getRequestDispatcher("/WEB-INF/views/lecture/list.jsp")
	               .forward(request, response);
	        return;
	    } else {
	    	response.sendRedirect(request.getContextPath() + "/professor/lecture/list");
	    	return;
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo(); // /insert
        if (path == null) path = "";

        if (path.equals("/insert")) {// 강의 등록
        	Integer loginId = (Integer) request.getSession().getAttribute("professor_id");
            if (loginId == null) {
                response.sendRedirect(request.getContextPath() + "/common/login");
                return;
            }
            LectureDTO lecture = new LectureDTO();
            lecture.setLectureName(request.getParameter("lecture_name"));
            lecture.setLectureDescription(request.getParameter("lecture_description"));
            lecture.setLectureRoom(request.getParameter("lecture_room"));
            lecture.setLectureType(request.getParameter("lecture_type"));
            lecture.setLectureYear(request.getParameter("lecture_year"));
            lecture.setProfessorId(loginId);
            
            try {
                lecture.setLectureCredit(Integer.parseInt(request.getParameter("lecture_credit")));
                lecture.setLectureSemester(Integer.parseInt(request.getParameter("lecture_semester")));
                lecture.setLectureCapacity(Integer.parseInt(request.getParameter("lecture_capacity")));
                lecture.setBuildingId(Integer.parseInt(request.getParameter("building_id")));
                lectureService.insertLecture(lecture);
                response.sendRedirect(request.getContextPath() + "/lecture/list?msg=insert_ok");
                return;
            } catch (Exception e) {
                System.out.println("강의 등록 실패: " + e);
                request.setAttribute("buildingList", buildingService.getAll());
                String msg = (e.getMessage() != null && !e.getMessage().isBlank())
		                        ? e.getMessage()
		                        : "강의 등록에 실패했습니다.";
                request.setAttribute("error", msg);
                request.setAttribute("form", lecture);
                request.getRequestDispatcher("/WEB-INF/views/lecture/insert.jsp")
                       .forward(request, response);
                return;
            }
        }
	}

}
