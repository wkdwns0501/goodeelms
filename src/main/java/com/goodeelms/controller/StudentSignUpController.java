package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentSignUpService;

@WebServlet("/ProcessSignUp")
public class StudentSignUpController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        StudentDTO loginDTO = (StudentDTO)session.getAttribute("login_student"); 
        
        StudentDTO dto = new StudentDTO();
        String orgin_student_password = request.getParameter("origin_student_password");
        dto.setStudentPassword(request.getParameter("new_student_password"));
        dto.setStudentName(request.getParameter("student_name"));
        dto.setStudentPhone(request.getParameter("student_phone"));
        dto.setStudentIdentityNumber(request.getParameter("student_identity_number"));
        dto.setStudentGender(request.getParameter("student_gender"));
        dto.setStudentAddress(request.getParameter("student_address"));
        dto.setStudentEmail(request.getParameter("student_email"));
        dto.setStudentBank(request.getParameter("student_bank"));
        
        StudentSignUpService service = new StudentSignUpService();
        int result = service.signUpStudent(dto, orgin_student_password);
        
        if (result > 0) {
            session.setAttribute("login_student", dto);
            
            response.sendRedirect("main.jsp");
        } else {
        		System.out.println("정보 변경 실패");
            response.sendRedirect("studentSignUp.jsp?error=update_fail");
        }
    }
}