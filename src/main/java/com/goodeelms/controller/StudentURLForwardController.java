package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class StudentURLForwardServlet
 */
@WebServlet("/student/page/*")
public class StudentURLForwardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public StudentURLForwardController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath() + "/student/page";
		String command = requestURI.substring(contextPath.length());
		
		System.out.println(command);
		RequestDispatcher rd = null;
		switch(command) {
			case "/enrollment":
				rd = request.getRequestDispatcher("/WEB-INF/views/student/enrollment.jsp");
				rd.forward(request, response);
				break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
	}

}
