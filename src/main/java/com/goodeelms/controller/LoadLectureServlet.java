package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.service.LoadLectureService;
import com.goodeelms.service.StudentService;

/**
 * Servlet implementation class LoadLectureServlet
 */
@WebServlet("/loadLecture")
public class LoadLectureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoadLectureServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cat = request.getParameter("cat");
		System.out.println("cat: "+ cat);
		String reqId = request.getParameter("id");
		System.out.println("reqId: "+ reqId);
		
		HttpSession session = request.getSession(false);
		
		String sessionId = (String)session.getAttribute("login_student_id");
		if(sessionId == null) sessionId = "20230051";
		System.out.println("sessionId: "+ sessionId);
		/*
		if(session == null ||
			reqId == null || reqId.isBlank() ||
			sessionId == null || sessionId.isBlank()) {
			System.out.println("all null");
			response.sendRedirect("/main.jsp");
			return;
		}
		*/
		
		if(!sessionId.equals(reqId)) {
			response.sendRedirect("/main.jsp");
			System.out.println("잘못된 접근");
			return;
		}
		
		if(cat == null || cat.isBlank()) cat = "all";
		
		StudentService stuS = new StudentService();
		LoadLectureService loadS = new LoadLectureService();
		String student_id = stuS.getStudentId(reqId);
		if(student_id == null) {
			System.out.println("등록되지 않은 학생입니다.");
			response.sendRedirect("/main.jsp");
			return;
		}
		
		List<StudentMajorDTO> majorList = stuS.getMajors(student_id);
		
		if(majorList == null || majorList.size() == 0) {
			System.out.println("알 수 없는 오류");
			response.sendRedirect("/main.jsp");
			return;
		}
		
		int[] majorIds = new int[majorList.size()];
		for(int i = 0; i < majorIds.length; i++) {
			majorIds[i] = majorList.get(i).getMajorId();
		}
		
		List<LectureDTO> list = loadS.getLectureList(cat, majorIds);
		
		request.setAttribute("lectureList", list);
		request.setAttribute("totalCount", list.size());
		System.out.println("lectureListSize: " + list.size());
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/student/loadLectureList.jsp");
		rd.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
	}

}
