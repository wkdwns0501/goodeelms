package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.goodeelms.dto.MajorDTO;
import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.service.MajorService;
import com.goodeelms.service.ProfessorSignUpService;
import com.goodeelms.util.EncryptUtil;
import com.goodeelms.util.ExistUtil;

@WebServlet("/professor/signup")
public class ProfessorSignUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProfessorSignUpController() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<MajorDTO> majorList = new MajorService().getAllMajor();
		request.setAttribute("majorList", majorList);
		request.getRequestDispatcher("/WEB-INF/views/professor/professorSignUp.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String professor_email = request.getParameter("professor_email");
		String professor_password = request.getParameter("professor_password");
		String professor_name = request.getParameter("professor_name");
		String major_id = request.getParameter("major_id");

		if (ExistUtil.isNull(professor_email) || ExistUtil.isNull(professor_password)
				|| ExistUtil.isNull(professor_name) || ExistUtil.isNull(major_id)) {
			request.setAttribute("errorMessage", "필수 입력값이 누락되었습니다.");
			doGet(request, response);
			return;
		}

		ProfessorDTO dto = new ProfessorDTO();
		dto.setProfessorName(professor_name);
		dto.setProfessorEmail(professor_email);
		dto.setMajorId(Integer.parseInt(major_id));
		request.setAttribute("professorDTO", dto); // 회원가입 실패시 사용할 value로 사용할 데이터 
		
		dto.setProfessorPassword(EncryptUtil.encryptPassword(professor_password));
		
		int result = new ProfessorSignUpService().signup(dto);
		
		if (result == -1) {
			request.setAttribute("errorMessage", "이미 사용하는 메일입니다.");
			doGet(request, response);
		} else if (result > 0) {
			response.sendRedirect(request.getContextPath() + "/main.jsp");
			return;
		} else {
			request.setAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");
			doGet(request, response);
		}
	}

}
