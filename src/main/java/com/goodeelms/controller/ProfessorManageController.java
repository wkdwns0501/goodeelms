package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.service.ProfessorManageService;

@WebServlet("/professorManage/*")
public class ProfessorManageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		// 내비바 통한 교수 관리 페이지 진입
		if(command.equals("/professorManage/page")) {
			ProfessorManageService pms = new ProfessorManageService();
			ArrayList<ProfessorDTO> list = pms.getAllProfessorList();
			
			request.setAttribute("professorList", list);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/professorManage.jsp");
			rd.forward(request, response);
		}
		
		// 검색 입력값에 따른 교수 목록
		if(command.equals("/professorManage/search")) {
			String professorName = request.getParameter("professorName");
			String majorName = request.getParameter("majorName");
			String professorEmail = request.getParameter("professorEmail");
			
			// 서비스 호출
			ProfessorManageService pms = new ProfessorManageService();
		 	ArrayList<ProfessorDTO> list = pms.getProfessorList(professorName, majorName, professorEmail);
			
			request.setAttribute("professorList", list);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/professorManage.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
			
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		// 교수 상태 업데이트 
		if(command.equals("/professorManage/updateStatus")) {

			String professorId = request.getParameter("professorId");
			String newProfessorStatus = request.getParameter("newProfessorStatus");
			
			// 현재 검색 값 저장
			String searchName = request.getParameter("searchName");
			String searchMajor = request.getParameter("searchMajor");
			String searchNo = request.getParameter("searchNo");
			
			ProfessorManageService pms = new ProfessorManageService();
			int result = pms.updateProfessorStatus(professorId, newProfessorStatus);
			
			String encName = (searchName != null) ? java.net.URLEncoder.encode(searchName, "UTF-8") : "";
		    String encMajor = (searchMajor != null) ? java.net.URLEncoder.encode(searchMajor, "UTF-8") : "";
		    String sNo = (searchNo != null) ? searchNo : "";

		    // 6. 리다이렉트 실행
		    // 결과(res)와 검색 조건들을 다시 붙여서 /professor/search로 보냄
		    String redirectUrl = contextPath + "/professorManage/search?res=" + result 
		                          + "&professorName=" + encName 
		                          + "&majorName=" + encMajor 
		                          + "&professorEmail=" + sNo;
		        
		    response.sendRedirect(redirectUrl);
	}

}
	
}
