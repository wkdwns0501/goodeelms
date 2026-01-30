package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.service.LectureHistoryService;
import com.goodeelms.service.LectureService;
import com.goodeelms.service.LoadLectureService;
import com.goodeelms.service.StudentService;
import com.goodeelms.util.DBUtil;

/**
 * Servlet implementation class StudentLectureController
 */
@WebServlet("/student/addlecture/*")
public class StudentLectureController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// URL 관련 필드
	private String requestURI = null;
	private String contextPath = null;
	private String command = null;
		
    public StudentLectureController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestURI = request.getRequestURI();
		contextPath = request.getContextPath() + "/student/addlecture";
		command = requestURI.substring(contextPath.length());
		
		System.out.println(command);
		HttpSession session = request.getSession();
		int sessionId = (Integer)session.getAttribute("student_id");
		
		String requestId = sessionId + "";
		
		if("/nowEnroll".equals(command)) {
			// 강의 예정인 것들 중 학생 앞으로 데이터가 있는 강의 목록
			List<LectureDTO> list = LectureService.getInstance().getLectureOfStudentId(requestId);
			
			if(list == null || list.size() == 0) {
				System.out.println("/nowEnroll에서 list 의도적으로 비어있는지 확인");
				list = new ArrayList<LectureDTO>();
			}
			
			// for문 돌 때 강의 code 저장할 set 추가 -> 강의 조회 시 중복 없애기
			Set<Integer> lectureCodes = new HashSet<Integer>();
			// for문 돌면서 id들 건져와
			Set<Integer> majorIds = new HashSet<Integer>();
			Set<Integer> professorIds = new HashSet<Integer>();
			for(LectureDTO dto : list) {
				int major = dto.getMajorId();
				int professor = dto.getProfessorId();
				
				majorIds.add(major);
				professorIds.add(professor);
				lectureCodes.add(dto.getLectureCode());
			}
			
			// major랑 professor name 얻어오기
			Map<Integer, String> majorMap = new HashMap<Integer, String>();
			Map<Integer, String> professorMap = new HashMap<Integer, String>();
			
			if(majorIds.size() > 0) {
				majorMap = new LoadLectureService().getMajorNames(majorIds);
			}
			if(professorIds.size() > 0) {
				professorMap = new LoadLectureService().getprofessorNames(professorIds);
			}
			// 부여하기 => 강의 id, code, name, credit, type, major, professor
			for(LectureDTO dto : list) {
				dto.setMajorName(majorMap.getOrDefault(dto.getMajorId(),"미지정"));
				dto.setProfessorName(professorMap.getOrDefault(dto.getProfessorId(),"미지정"));
			}
			
			// 페이지에 표시 할 수강신청 완료 목록
			request.setAttribute("totalCount", list.size());
			request.setAttribute("lectureList", list);
			
			// Limit 학점
			request.setAttribute("limitCartCredit", 21);
			session.removeAttribute("lectureCodeSet");
			session.setAttribute("lectureCodeSet", lectureCodes);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/student/loadHistoryCompetition.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int sessionId = (Integer)session.getAttribute("student_id");
		
		String requestId = request.getParameter("student_id");
		String lectureId = request.getParameter("lecture_id");
		// 검증
		try {
			int id = Integer.parseInt(requestId);
			if(id != sessionId) return;
		}
		catch(NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			return;
		}
		
		LectureHistoryService service = new LectureHistoryService();
		// lecture_id 랑 lecture_history의 lecture_id랑 확인(중복 수강신청 방지)
		int result = service.searchLectureIdOfHistory(requestId, lectureId);
		if(result != 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		// 전공 아닌 전공강의 선택 불가
		List<StudentMajorDTO> majorIds = new StudentService().getMajors(requestId);
		if(majorIds == null || majorIds.size() == 0) {
			System.out.println("전공 ID 조회 실패");
			return;
		}
		LectureDTO lectureInfo = LectureService.getInstance().getMajorIdAndByLectureId(Integer.parseInt(lectureId));
		if(lectureInfo == null || lectureInfo.getLectureType() == null || lectureInfo.getLectureType().isBlank()) {
			System.out.println("말도 안된다.");
			return;
		}
		boolean inMajor = false;
		for(StudentMajorDTO dto : majorIds) {
			if(lectureInfo.getMajorId() == dto.getMajorId() || "교양".equals(lectureInfo.getLectureType())) {
				inMajor = true;
				break;
			}
		}
		if(!inMajor) {
			System.out.println("비전공 접근");
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		
		// 수강신청 등록(수용인원 확인 포함)
		int insertResult = service.insertNewLecutreToHistory(requestId, lectureId);
		System.out.println(insertResult);
		if(insertResult < 1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
	}

}
