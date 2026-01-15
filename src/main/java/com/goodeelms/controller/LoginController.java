package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.ProfessorLoginService;
import com.goodeelms.service.StudentLoginService;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userType = request.getParameter("userType");

		if (userType == null || userType.isBlank()) { 
			request.setAttribute("errorMessage", "unchecked_userType");
			request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
			return;
		}

		if ("STUDENT".equals(userType)) {
			String student_no = request.getParameter("student_no");
			String student_password = request.getParameter("student_password");

			if (student_no == null || student_no.isBlank() || student_password == null || student_password.isBlank()) {
				request.setAttribute("errorMessage", "data_required");
				request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
				return;
			}

			StudentLoginService studentLoginService = new StudentLoginService();
			StudentDTO login_student = studentLoginService.checkIdAndPass(student_no, student_password);

			if (login_student != null) {
				session.invalidate();
				session = request.getSession(true);

				session.setAttribute("user_role", "STUDENT");
				session.setAttribute("student_id", login_student.getStudentId());
				session.setAttribute("student_no", login_student.getStudentNo());
				session.setAttribute("student_name", login_student.getStudentName());

				session.setAttribute("login_student", login_student); // 세션에 student 속성 추가를 위해 사용

				if (student_no.equals(student_password)) { // 최초 로그인 확인
					request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
					return;
				}

				response.sendRedirect(request.getContextPath() + "/main.jsp");
				return;
			} else {
				request.setAttribute("errorMessage", "login_fail");
				request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
				return;
			}

		} else if ("PROFESSOR".equals(userType)) {
			String professor_email = request.getParameter("professor_email");
			String professor_password = request.getParameter("professor_password");

			if (professor_email == null || professor_email.isBlank() || professor_password == null
					|| professor_password.isBlank()) {
				request.setAttribute("errorMessage", "data_required");
				request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
				return;
			}
			
			ProfessorLoginService professService = new ProfessorLoginService();
			ProfessorDTO login_professor = professService.checkIdAndPass(professor_email, professor_password);

			if (login_professor != null) {
				session.invalidate();
				session = request.getSession(true);

				session.setAttribute("user_role", "PROFESSOR");
				session.setAttribute("professor_id", login_professor.getProfessorId());
				session.setAttribute("professor_name", login_professor.getProfessorName());
				session.setAttribute("professor_email", login_professor.getProfessorEmail());
				session.setAttribute("professor_status", login_professor.getProfessorStatus());

				response.sendRedirect(request.getContextPath() + "/main.jsp");
				return;
			} else {
				request.setAttribute("errorMessage", "login_fail");
				request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
				return;
			}
		}
	}
}
