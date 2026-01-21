package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodeelms.dto.ChangeMajorHistoryDTO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.LectureHistoryDTO;
import com.goodeelms.dto.ScholarshipDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentStatusHistoryDTO;
import com.goodeelms.dto.TuitionPaymentDTO;
import com.goodeelms.service.MajorService;
import com.goodeelms.service.StudentService;
import com.goodeelms.service.StudentStatusService;
import com.goodeelms.service.TuitionService;

@WebServlet("/student/*")
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
		String path = request.getPathInfo();
		if (path == null)
			path = "";

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user_role") == null
				|| session.getAttribute("student_id") == null) {
			if (!path.equals("/student/login") && !path.equals("/student/signup")) {
				response.sendRedirect(request.getContextPath() + "/common/login");
				return;
			}
		}

		switch (path) {
		case "/signup":
			signup(request, response);
			break;
		case "/tuition":
			showTuitionAndScholarship(request, response);
			break;
		case "/my-lectures":
			showLecture(request, response);
			break;
		case "/grades":
			showGrade(request, response);
			break;
		case "/history/majorAndStatus":
			showStatusAndMajorHistory(request, response);
			break;
		case "/history/rewardAndPunishment":
			// 추가 필요
			break;
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

	protected void showTuitionAndScholarship(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 	// 0120 임욱(수정) / 장학 정보 조회 추가
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
		
		TuitionPaymentDTO dto = new TuitionService().readTuition(student_id);
		List<ScholarshipDTO> scholarship = new StudentService().getScholarshipByStudentId(student_id);
		
		
		if(dto == null || scholarship == null) {
			request.setAttribute("msg", "등록금 및 장학 정보를 조회할 수 없습니다.");
			request.getRequestDispatcher("/WEB-INF/views/student/tuition.jsp").forward(request, response);
			return;
		}
		
		request.setAttribute("tuition", dto);
		request.setAttribute("scholarship", scholarship);
		
		request.getRequestDispatcher("/WEB-INF/views/student/tuition.jsp").forward(request, response);
		return;
	}

	protected void showStatusAndMajorHistory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 	// 0120 임욱(수정) / 전공, 학적 변동 이력, 전공 변동 이력 출력
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
		
		List<String> majorList = new MajorService().getMajorCodeAndMajorName(student_id); // major_code, major_name
		ChangeMajorHistoryDTO dto = studentService.getChangedMajorHistory(student_id); // fromMajorName, toMajorName
		List<StudentStatusHistoryDTO> statusList = new StudentStatusService().getStatusHistory(student_id); // status_type, status_reason, status_at 		

		if (statusList != null) request.setAttribute("statusList", statusList);
		if (dto != null) request.setAttribute("majorHistory", dto);
		if (majorList == null) request.setAttribute("msg", "전공을 조회할 수 없습니다.");
		
		
		request.setAttribute("majorList", majorList);
		request.getRequestDispatcher("/WEB-INF/views/student/majorAndStatus.jsp").forward(request, response);
		return;
	}

	
	protected void showGrade(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	// 0120 임욱(추가) / 성적 조회 
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
		
		Map<Integer, LectureDTO> grade = studentService.getlectureAndLectureScoreByStudentId(student_id);
		
		request.setAttribute("grade", grade); // lecture_code, lecture_name, lecuture_year, lecture_semester, lecture_score ( lecture_history)
		request.getRequestDispatcher("/WEB-INF/views/student/grades.jsp").forward(request, response);
		return;
	}
	
	protected void showLecture(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {	// 0120 임욱(추가) / 수강중인 강의 조회 
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
		
		Map<Integer, LectureDTO> lectures = studentService.getProgressInfoByStudentId(student_id);
		
		request.setAttribute("lectures", lectures); 
		request.getRequestDispatcher("/WEB-INF/views/student/lecture.jsp").forward(request, response);
		return;
	}
	
}
