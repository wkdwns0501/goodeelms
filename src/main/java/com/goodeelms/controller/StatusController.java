package com.goodeelms.controller;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
		System.out.println(command);
		if(command.equals("/student/status")) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/status.jsp");
			rd.forward(request, response);
		}
		if(command.equals("/student/search")) {
			String studentName = request.getParameter("studentName");
			String majorName = request.getParameter("majorName");
			String studentNo = request.getParameter("studentNo");
			
			StudentStatusService ss = new StudentStatusService();
			ArrayList<StudentDTO> list = ss.getStudentList(studentName,majorName,studentNo);
			
			request.setAttribute("studentList", list);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/status.jsp");
			rd.forward(request, response);
		}
	}
	
	
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
