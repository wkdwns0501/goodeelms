package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.goodeelms.service.StudentSignUpService;

@WebServlet("/ProcessSignUp")
public class ProcessSignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProcessSignUpServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		StudentSignUpService service = new StudentSignUpService();
		
		HttpSession session = request.getSession();
		String login_student_no = (String)session.getAttribute("login_student_no");
		String student_no = request.getParameter("student_no");
		String student_password = request.getParameter("student_password");
		String student_name = request.getParameter("student_name");
		String student_phone = request.getParameter("student_phone");
		String student_identity_number = request.getParameter("student_identity_number");
		String student_gender = request.getParameter("student_gender");
		String student_address = request.getParameter("student_address");
		String student_email = request.getParameter("student_email");
		String student_bank = request.getParameter("student_bank");
		
		service.signUpStudent(login_student_no, student_no, student_password, student_name, student_phone, 
				student_identity_number, student_gender, student_address, student_email, student_bank);

	    response.sendRedirect("loginForm.jsp");
	}
	
	
}
