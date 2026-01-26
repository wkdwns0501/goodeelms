package com.goodeelms.controller;

import java.io.IOException;
import java.util.List;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.service.BuildingService;
import com.goodeelms.service.LectureService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/professor/lecture/*")
public class ProfessorLectureController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LectureService lectureService = LectureService.getInstance();
	private final BuildingService buildingService = BuildingService.getInstance();  
    public ProfessorLectureController() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String path = request.getPathInfo(); // /add /list
	    if (path == null) path = "/list";
	    
	    Integer professorId = (Integer) request.getSession().getAttribute("professor_id");
        if (professorId == null) {
            response.sendRedirect(request.getContextPath() + "/common/login");
            return;
        }
	    
	    if (path.equals("/add")) { // 강의 등록 페이지 조회
	    	if (!lectureService.isLectureInsertPeriod()) {
	            response.sendRedirect(request.getContextPath() 
	                + "/professor/lecture/list?error=NoLectureInsertPeriod");
	            return;
	        }
	    	request.setAttribute("buildingList", buildingService.getAll());
	        request.getRequestDispatcher("/WEB-INF/views/professor/lectureInsert.jsp")
	               .forward(request, response);
	        return;
	    } else if (path.equals("/list")) { // 강의 목록 조회
	    	boolean isLectureInsertPeriod = lectureService.isLectureInsertPeriod(); 
	    	request.setAttribute("isLectureInsertPeriod", isLectureInsertPeriod);
	    	
	    	String keyword = request.getParameter("keyword");
	    	String statusFilter = request.getParameter("statusFilter");
	    	if (statusFilter == null || statusFilter.isBlank()) statusFilter = "ACTIVE";

	    	request.setAttribute("statusFilter", statusFilter);

	        String pageParam = request.getParameter("page");
	        int page = 1;
	        
	        try {
	            if (pageParam != null && !pageParam.isBlank()) {
	                page = Integer.parseInt(pageParam);
	            }
	        } catch (NumberFormatException e) {
	            page = 1;
	        }

		    final int limit = 10;
		    final int blockSize = 10;

		    // 입력 page 최소 보정
		    page = Math.max(page, 1);

		    // totalCount / totalPage 계산 (0건이면 totalPage=1 보장)
		    final int totalCount = lectureService.getLectureTotalCount(professorId, keyword, statusFilter);
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
	        		lectureService.getLecturePage(professorId, page, limit, keyword, statusFilter));
	        
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

	        request.getRequestDispatcher("/WEB-INF/views/professor/lectureList.jsp")
	               .forward(request, response);
	        return;
	    }  else if (path.equals("/rooms")) { // 강의 등록시 강의실 호수 조회용
	        String year = request.getParameter("lecture_year");
	        String semesterParam = request.getParameter("lecture_semester");
	        String buildingParam = request.getParameter("building_id");
	        response.setContentType("application/json; charset=UTF-8");

	        try {
	            int semester = Integer.parseInt(semesterParam);
	            int buildingId = Integer.parseInt(buildingParam);
	            List<String> occupiedRooms = lectureService.getOccupiedRooms(buildingId, year, semester);

	            StringBuilder sb = new StringBuilder();
	            sb.append("[");
	            for (int i = 0; i < occupiedRooms.size(); i++) {
	                if (i > 0) sb.append(",");
	                sb.append("\"").append(occupiedRooms.get(i)).append("\"");
	            }
	            sb.append("]");

	            response.getWriter().write(sb.toString());
	        } catch (Exception e) {
	            // 파라미터 이상하면 빈 배열로 응답
	            response.getWriter().write("[]");
	        }
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
        	Integer professorId = (Integer) request.getSession().getAttribute("professor_id");
            if (professorId == null) {
                response.sendRedirect(request.getContextPath() + "/common/login");
                return;
            }
            
            LectureDTO lecture = new LectureDTO();
            lecture.setLectureName(request.getParameter("lecture_name"));
            lecture.setLectureDescription(request.getParameter("lecture_description"));
            lecture.setLectureRoom(request.getParameter("lecture_room"));
            lecture.setLectureType(request.getParameter("lecture_type"));
            lecture.setLectureYear(request.getParameter("lecture_year"));
            lecture.setProfessorId(professorId);
            
            try {
                lecture.setLectureCredit(Integer.parseInt(request.getParameter("lecture_credit")));
                lecture.setLectureSemester(Integer.parseInt(request.getParameter("lecture_semester")));
                lecture.setLectureCapacity(Integer.parseInt(request.getParameter("lecture_capacity")));
                lecture.setBuildingId(Integer.parseInt(request.getParameter("building_id")));
                lectureService.insertLecture(lecture);
                response.sendRedirect(request.getContextPath() + "/professor/lecture/list?msg=insert_ok");
                return;
            } catch (Exception e) {
                System.out.println("강의 등록 실패: " + e);
                request.setAttribute("buildingList", buildingService.getAll());
                String msg = (e.getMessage() != null && !e.getMessage().isBlank())
		                        ? e.getMessage()
		                        : "강의 등록에 실패했습니다.";
                request.setAttribute("error", msg);
                request.setAttribute("form", lecture);
                request.getRequestDispatcher("/WEB-INF/views/professor/lectureInsert.jsp")
                       .forward(request, response);
                return;
            }
        }
	}

}
