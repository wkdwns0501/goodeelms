package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.goodeelms.dto.AdminDTO;
import com.goodeelms.dto.MajorDTO;
import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.CommonService;
import com.goodeelms.service.MajorService;
import com.goodeelms.service.ProfessorSignUpService;
import com.goodeelms.util.EncryptUtil;
import com.goodeelms.util.ExistUtil;

@WebServlet("/common/*")
public class CommonController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		checkPath(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		checkPath(request, response);
	}

	private void checkPath(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();

		if (ExistUtil.isNull(path) || path.equals("/")) {
			response.sendRedirect(request.getContextPath() + "/main.jsp");
			return;
		}

		switch (path) {
			case "/login":
				login(request, response);
				break;
			case "/logout":
				logout(request, response);
				break;
			case "/signup":
				signup(request, response);
				break;
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login_id = request.getParameter("login_id");
		String login_password = request.getParameter("login_password");
		
		if (login_id == null && login_password == null) { // 1. 페이지 첫 진입 시 (파라미터가 아예 없는 경우)
			request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
			return;
		}

		// 1. 로그인 버튼을 눌렀으나 값을 누락한 경우 (파라미터는 존재하나 비어있는 경우)
		if (ExistUtil.isNull(login_id) || ExistUtil.isNull(login_password)) {
			request.setAttribute("errorMessage", "아이디와 비밀번호를 입력해주세요");
			request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
			return;
		}

		// 2. 로그인 시도(아이디, 비밀번호 일치 확인)
		CommonService commonService = new CommonService();
		Object obj = commonService.checkUserRoleByIdAndPassword(login_id, login_password);

		// 3_1. 로그인 실패
		if (obj == null) {
			request.setAttribute("errorMessage", "아이디와 비밀번호가 일치하지 않습니다.");
			request.getRequestDispatcher("/WEB-INF/views/common/loginForm.jsp").forward(request, response);
			return;
		}

		// 3_2. 로그인 성공
		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();
		session = request.getSession(true);

		if (obj instanceof StudentDTO studentDTO) {
			session.setAttribute("user_role", "STUDENT");
			session.setAttribute("user_name", studentDTO.getStudentName());
			session.setAttribute("student_id", studentDTO.getStudentId());

			String identityNum = studentDTO.getStudentIdentityNumber();
			if (identityNum != null && identityNum.length() >= 7) { // 초기 로그인 여부 확인 (입력 비밀번호 == 주민번호 뒷자리)
				String initialPassword = identityNum.substring(identityNum.length() - 7);

				if (login_password.equals(initialPassword)) {
					request.setAttribute("studentDTO", studentDTO);
					request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
					return;
				}
			}
		} else if (obj instanceof ProfessorDTO professorDTO) {
			session.setAttribute("user_role", "PROFESSOR");
			session.setAttribute("user_name", professorDTO.getProfessorName());
			session.setAttribute("professor_id", professorDTO.getProfessorId());
		} else if (obj instanceof AdminDTO adminDTO) {
			session.setAttribute("user_role", "ADMIN");
			session.setAttribute("user_name", adminDTO.getAdminName());
			session.setAttribute("admin_id", adminDTO.getAdminId());
		}
		response.sendRedirect(request.getContextPath() + "/main.jsp");
		return;
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session != null)
			session.invalidate();

		response.sendRedirect(request.getContextPath() + "/main.jsp");
		return;
	}

	private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<MajorDTO> majorList = new MajorService().getAllMajor();
		request.setAttribute("majorList", majorList);
		
		String professor_email = request.getParameter("professor_email");
		String professor_password = request.getParameter("professor_password");
		String professor_name = request.getParameter("professor_name");
		String major_id = request.getParameter("major_id");

		if (ExistUtil.isNull(professor_email) || ExistUtil.isNull(professor_password)
				|| ExistUtil.isNull(professor_name) || ExistUtil.isNull(major_id)) {
			request.getRequestDispatcher("/WEB-INF/views/common/professorSignUp.jsp").forward(request, response);
			return;
		}

		ProfessorDTO dto = new ProfessorDTO();
		dto.setProfessorName(professor_name);
		dto.setProfessorEmail(professor_email);
		dto.setMajorId(Integer.parseInt(major_id));
		request.setAttribute("professorDTO", dto); 

		dto.setProfessorPassword(EncryptUtil.encryptPassword(professor_password));

		int result = new ProfessorSignUpService().signup(dto);

		if (result == -1) {
			request.setAttribute("errorMessage", "이미 사용하는 메일입니다.");
			request.getRequestDispatcher("/WEB-INF/views/common/professorSignUp.jsp").forward(request, response);
			return;
		} else if (result > 0) {
			response.sendRedirect(request.getContextPath() + "/main.jsp");
			return;
		} else {
			request.setAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");
			request.getRequestDispatcher("/WEB-INF/views/common/professorSignUp.jsp").forward(request, response);
			return;
		} 
	}
	
}
