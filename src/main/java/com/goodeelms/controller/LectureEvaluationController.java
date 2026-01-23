package com.goodeelms.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.LectureEvaluationDTO;
import com.goodeelms.service.LectureEvaluationService;
import com.goodeelms.util.AlertUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/student/evaluation/*")
public class LectureEvaluationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
				
		// 내비바 통한 강의 평가 페이지 진입
		if(command.equals("/student/evaluation/list")) {
			int studentId = (Integer)session.getAttribute("student_id");
			LectureEvaluationService lec = new LectureEvaluationService();
			if (!lec.isAccessPeriod()) {
				AlertUtil.alertAndRedirect(response, "강의 평가 기간이 아닙니다.", contextPath + "/main.jsp");
				return;
			}
			
			// 타겟 학기 정보 한 번에 받아오기
		    String[] term = lec.getEvaluationTargetTerm();
		    if (term == null) {
		    	System.out.println("학기 정보 로드 오류");
		    }
		    String targetYear = term[0];
		    int targetSemester = Integer.parseInt(term[1]);
		    System.out.println(term[0]);
		    System.out.println(term[1]);
		    
			ArrayList<LectureDTO> lectureList = lec.getLectureList(studentId, targetYear, targetSemester);
			
			LectureDTO targetLecture = null;
			
			// 평가 안된 과목 확인
			for (LectureDTO dto : lectureList) {
			    if (!dto.isEvaluated()) { // 아직 평가 안 된 과목 발견!
			        targetLecture = dto;
			        break; 
			    }
			}

			if (targetLecture == null && !lectureList.isEmpty()) {
			    // [추가] 모든 과목 평가 완료 시 처리
				AlertUtil.alertAndRedirect(response, "강의 평가를 완료했습니다.", contextPath + "/main.jsp"); 
				return;
				// target 기간에 해당하는 과목이 없을 때
			} else if (lectureList.isEmpty()) {
				AlertUtil.alertAndRedirect(response, "지난 학기 수강 과목이 없습니다.", contextPath + "/main.jsp");
				return;
			}

			// targetLecture가 있을 때만 아래로 내려가서 JSP 실행
			request.setAttribute("lecture", targetLecture);
			request.setAttribute("lectureList", lectureList);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/student/lectureEvaluation.jsp");
			rd.forward(request, response);
		}
		
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		if(command.equals("/student/evaluation/submit")) {
			LectureEvaluationDTO evaDTO = new LectureEvaluationDTO();
			evaDTO.setStudentId((Integer)session.getAttribute("student_id"));
			evaDTO.setRating(Integer.parseInt(request.getParameter("sumScore")));
			evaDTO.setLectureId(Integer.parseInt(request.getParameter("lectureId")));
			evaDTO.setComment(request.getParameter("comment"));
			LectureEvaluationService lec = new LectureEvaluationService();
			
			int writeResult = lec.writeEvaluation(evaDTO);
			
			if(writeResult < 1) {
				System.out.println("성적 기입 오류");
				System.out.println(writeResult);
			} else {
				response.sendRedirect(contextPath + "/student/evaluation/list");
			}
		}
	}

}
