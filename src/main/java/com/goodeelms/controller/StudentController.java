package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.TuitionPaymentDTO;
import com.goodeelms.service.StudentService;
import com.goodeelms.service.TuitionService;
import com.goodeelms.util.EncryptUtil;
import com.goodeelms.util.ExistUtil;

@WebServlet(urlPatterns = {
		"/student/signup", 
		"/student/mypage",
		"/student/tuition"
})
public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final StudentService studentService = new StudentService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		checkPath(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		checkPath(request, response);
	}

	private void checkPath(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getServletPath();

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("studentDTO") == null) {
			if (!path.equals("/student/login") && !path.equals("/student/signup")) {
	            response.sendRedirect(request.getContextPath() + "/common/login");
	            return;
	        }
		}

		switch (path) {
		case "/student/signup":
			signup(request, response);
			break;
		case "/student/mypage":
			showStudentProfile(request, response);
			break;	
		case "/student/tuition":
			showTuition(request, response);
			break;		
		default:
			// response.sendRedirect(request.getContextPath() + "/main.jsp");
			System.out.println("정의되지 않은 경로 요청됨: " + path);
		}
	}

	private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String originPw = request.getParameter("origin_student_password");
		String newPw = request.getParameter("new_student_password");
		String email = request.getParameter("student_email");
		String phone = request.getParameter("student_phone");
		String bank = request.getParameter("student_bank");
		String address = request.getParameter("student_address");

		HttpSession session = request.getSession();
		StudentDTO updateStudent = (StudentDTO) session.getAttribute("studentDTO");

		if(newPw.equals(originPw)){	// 기존 비밀번호와 동일하면 다시 입력하도록
			request.setAttribute("errorMessage", "기존 비밀번호와 동일합니다.");
			request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
			return;
		} 

		updateStudent.setStudentPassword(newPw); 
		updateStudent.setStudentEmail(email);
		updateStudent.setStudentPhone(phone);
		updateStudent.setStudentBank(bank);
		updateStudent.setStudentAddress(address);
		
		boolean isUpdated = studentService.updateStudent(updateStudent);

		if (isUpdated) {
	        session.setAttribute("studentDTO", updateStudent);
	        response.sendRedirect(request.getContextPath() + "/main.jsp");
	        return;
	    } else {
	        request.setAttribute("errorMessage", "이미 사용 중인 이메일/연락처 입니다.");
	        request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
	        return;
	    }
	}
	
	private void showStudentProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		StudentDTO dto = (StudentDTO)session.getAttribute("studentDTO");
		dto = new StudentService().getStudentByNo(dto);
		
		session.setAttribute("studentDTO", dto);
		
		request.getRequestDispatcher("/WEB-INF/views/student/mypage.jsp").forward(request, response);
		return;
	}
	
	protected void showTuition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    StudentDTO student = (StudentDTO) session.getAttribute("studentDTO");
	    
	    if (student == null) {
	        response.sendRedirect(request.getContextPath() + "/common/login");
	        return;
	    }
	    
	    TuitionService tuitionService = new TuitionService();
	    TuitionPaymentDTO tuition = tuitionService.readTuition(student.getStudentId());
	    
	    request.setAttribute("tuition", tuition);
	    request.getRequestDispatcher("/WEB-INF/views/student/tuition.jsp").forward(request, response);
	    return;
	}
	
}
