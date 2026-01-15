package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.service.ProfessorSignUpService;

@WebServlet("/signup")
public class ProfessorSignUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProfessorSignUpController() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/professor/professorSignUp.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String professor_email = request.getParameter("professor_email");
		String professor_password = request.getParameter("professor_password");
		String professor_name = request.getParameter("professor_name");
		String professor_status = request.getParameter("professor_status");

		if (professor_name == null || professor_name.isEmpty() || professor_email == null || professor_email.isBlank()
				|| professor_password == null || professor_password.isBlank()) {

			request.setAttribute("errorMessage", "필수 입력값이 누락되었습니다.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}

		ProfessorDTO dto = new ProfessorDTO();
		dto.setProfessorName(professor_name);
		dto.setProfessorEmail(professor_email);
		dto.setProfessorPassword(professor_password);
		dto.setProfessorStatus(professor_status);

		ProfessorSignUpService professorService = new ProfessorSignUpService();
		int result = professorService.addProfessor(dto);

		if (result == -2) {
			request.setAttribute("errorMessage", "이미 사용하는 메일입니다.");
			request.getRequestDispatcher("/WEB-INF/views/professor/professorSignUp.jsp").forward(request, response);
			return;
		} else if (result > 0) {
			response.sendRedirect(request.getContextPath() + "/main.jsp");
			return;
		} else {
			request.setAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");
			request.getRequestDispatcher("/WEB-INF/views/professor/professorSignUp.jsp").forward(request, response);
			return;
		}
	}

}
