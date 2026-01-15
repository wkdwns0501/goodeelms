package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentStatusService;

@WebServlet("/student/*")
public class StatusController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		if(command.equals("/student/status")) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/status.jsp");
			rd.forward(request, response);
		}
		if(command.equals("/student/search")) {
			String studentName = request.getParameter("studentName");
			String majorName = request.getParameter("majorName");
			String studentNo = request.getParameter("studentNo");
			
			// 서비스 호출
			StudentStatusService ss = new StudentStatusService();
			ArrayList<StudentDTO> list = ss.getStudentList(studentName,majorName,studentNo);
			
			request.setAttribute("studentList", list);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/status.jsp");
			rd.forward(request, response);
		}
		
	}
	
	
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
			
//		if(command.equals("/student/updateStatus")) {
//			if (session == null || session.getAttribute("adminId") == null) {
//	            // 로그인 페이지로 리다이렉트 하거나 에러 처리
//	            response.sendRedirect(contextPath + "/login");
//	            return;
//	        }
			
			String studentNo = request.getParameter("studentNo");
			String newStudentStatus = request.getParameter("newStudentStatus");
			String statusReason = request.getParameter("statusReason");
			String studentId = request.getParameter("studentId");
			String studentName = request.getParameter("studentName");
			String majorName = request.getParameter("majorName");
			String adminId = (String) session.getAttribute("adminId");
			
			String searchName = request.getParameter("searchName");
			String searchMajor = request.getParameter("searchMajor");
			String searchNo = request.getParameter("searchNo");
			adminId = "1";
			StudentStatusService ss = new StudentStatusService();
			int result = ss.processStatusUpdate(studentId, studentNo, newStudentStatus, statusReason, adminId);
			
			String encName = (searchName != null) ? java.net.URLEncoder.encode(searchName, "UTF-8") : "";
		    String encMajor = (searchMajor != null) ? java.net.URLEncoder.encode(searchMajor, "UTF-8") : "";
		    String sNo = (searchNo != null) ? searchNo : "";

		    // 6. 리다이렉트 실행
		    // 결과(res)와 검색 조건들을 다시 붙여서 /student/search로 보냅니다.
		    String redirectUrl = contextPath + "/student/search?res=" + result 
		                          + "&studentName=" + encName 
		                          + "&majorName=" + encMajor 
		                          + "&studentNo=" + sNo;
		        
		    response.sendRedirect(redirectUrl);
			
		}
	

}
