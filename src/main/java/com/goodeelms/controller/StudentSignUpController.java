package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentSignUpService;

@WebServlet("/student/signup")
public class StudentSignUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    if (session == null || session.getAttribute("student_id") == null) {
	        response.sendRedirect(request.getContextPath() + "/login");
	        return;
	    }
	    
	    request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String originPw = request.getParameter("origin_student_password");
		String newPw = request.getParameter("new_student_password");

		if (originPw == null || originPw.isBlank() || newPw == null || newPw.isBlank()) {
			request.setAttribute("errorMessage", "필수 입력값이 누락되었습니다.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}

		StudentDTO dto = new StudentDTO();
		dto.setStudentPassword(newPw);
		dto.setStudentPhone(request.getParameter("student_phone"));
		dto.setStudentIdentityNumber(request.getParameter("student_identity_number"));
		dto.setStudentAddress(request.getParameter("student_address"));
		dto.setStudentEmail(request.getParameter("student_email"));
		dto.setStudentBank(request.getParameter("student_bank"));

		StudentSignUpService service = new StudentSignUpService();
		HttpSession session = request.getSession();

		Integer student_id = (Integer)session.getAttribute("student_id");
		if (student_id == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		try {
			int result = service.signUpStudent(dto, originPw, student_id);
			
			if (result == -1) {	// 기존과 동일한 비밀번호 입력
                request.setAttribute("errorMessage", "samePassword");
                request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
                return;
            } else if (result > 0) {	
                response.sendRedirect(request.getContextPath() + "/main.jsp");
                return;
            } else {	// student 정보 갱신 실패 	
                request.setAttribute("errorMessage", "updateStudent_fail");
                request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
                return;
            }
		} catch(SQLException e) {
			String errorMessage = "데이터 갱신에 예외가 발생했습니다.";	
			request.setAttribute("errorMessage", errorMessage);
	        request.getRequestDispatcher("/error.jsp").forward(request, response);
	        return;
		} 
		
	}

}