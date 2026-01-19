package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentStatusHistoryDTO;
import com.goodeelms.dto.TuitionPaymentDTO;
import com.goodeelms.service.StudentService;
import com.goodeelms.service.StudentStatusService;
import com.goodeelms.service.TuitionService;

@WebServlet(urlPatterns = { "/student/signup", "/student/tuition", "/student/status_history" })
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
		if (session == null || session.getAttribute("user_role") == null
				|| session.getAttribute("student_id") == null) {
			if (!path.equals("/student/login") && !path.equals("/student/signup")) {
				response.sendRedirect(request.getContextPath() + "/common/login");
				return;
			}
		}

		switch (path) {
		case "/student/signup":
			signup(request, response);
			break;
		case "/student/tuition":
			showTuition(request, response);
			break;
		case "/student/history":
			showStatusHistory(request, response);
			break;
		case "/student/major":
			showStudentMajor(request, response);
		default:
			System.out.println("정의되지 않은 경로 요청됨: " + path);
			response.sendRedirect(request.getContextPath() + "/main.jsp");
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
		StudentDTO updateStudent = (StudentDTO) request.getAttribute("studentDTO");

		if (newPw.equals(originPw)) { // 기존 비밀번호와 동일하면 다시 입력하도록
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

	protected void showTuition(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");

		TuitionPaymentDTO tuition = new TuitionService().readTuition(student_id);
		request.setAttribute("tuition", tuition);

		request.getRequestDispatcher("/WEB-INF/views/student/tuition.jsp").forward(request, response);
		return;
	}

	protected void showStatusHistory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
		
		List<StudentStatusHistoryDTO> list = new StudentStatusService().getStatusHistory(student_id);

		request.setAttribute("statusList", list);
		request.getRequestDispatcher("/WEB-INF/views/student/statusHistory.jsp").forward(request, response);
		return;
	}

	protected void showStudentMajor(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
	}

}
