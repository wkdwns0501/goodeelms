package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentLoginService;

@WebServlet("/ProcessLogin")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginController() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		StudentLoginService service = new StudentLoginService();

		String student_no = request.getParameter("student_no");
		String student_password = request.getParameter("student_password");

		StudentDTO login_student = service.checkIdAndPass(student_no, student_password);

		if (login_student != null) {
	        HttpSession session = request.getSession();
	        session.setAttribute("login_student", login_student);

	        if (login_student.getStudentStatus() == null || login_student.getStudentStatus().isEmpty()) {
	            response.sendRedirect("studentSignUp.jsp");
	        } else {
	            response.sendRedirect("main.jsp");
	        }
	    } else {
	        response.sendRedirect("loginForm.jsp?error=1");
	    }
		
	}

	
	
}
