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

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentStatusService;

@WebServlet("/admin/studentStatus/*")
public class StatusController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		// 내비바 통한 학사관리 페이지 입장
		if(command.equals("/admin/studentStatus/page")) {
			// 최초 보여줄 전체 학생 list 가져와서 출력
			StudentStatusService ss = new StudentStatusService();
			ArrayList<StudentDTO> list = ss.getAllStudentList();
			
			request.setAttribute("studentList", list);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/status.jsp");
			rd.forward(request, response);
		}
		
		// 검색 조건 통한 학생 목록 조회
		if(command.equals("/admin/studentStatus/search")) {
			String studentName = request.getParameter("studentName");
			String majorName = request.getParameter("majorName");
			String studentNo = request.getParameter("studentNo");
			
			// 검색 조건을 통한 list 가져오기
			StudentStatusService ss = new StudentStatusService();
			ArrayList<StudentDTO> list = ss.getStudentList(studentName,majorName,studentNo);
			
			request.setAttribute("studentList", list);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/status.jsp");
			rd.forward(request, response);
		}
		
	}
	
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		// 학생 학적 상태 변경 하기
		if(command.equals("/admin/studentStatus/updateStatus")) {
			
			int adminId = (Integer)session.getAttribute("admin_id");
			
			// 
			String studentNo = request.getParameter("studentNo");
			String newStudentStatus = request.getParameter("newStudentStatus");
			String statusReason = request.getParameter("statusReason");
			String studentId = request.getParameter("studentId");
			String studentName = request.getParameter("studentName");
			String majorName = request.getParameter("majorName");
			
			// 검색 입력값 유지 위한 파라미터값 저장
			String searchName = request.getParameter("searchName");
			String searchMajor = request.getParameter("searchMajor");
			String searchNo = request.getParameter("searchNo");
			
			// update 서비스 호출 (변경, 등록 한번에)
			StudentStatusService ss = new StudentStatusService();
			int result = ss.processStatusUpdate(studentId, newStudentStatus, statusReason, adminId);
			
			// 현재 검색 조건 및 목록 유지 위한 파라미터 저장
			String encName = (searchName != null) ? java.net.URLEncoder.encode(searchName, "UTF-8") : "";
		    String encMajor = (searchMajor != null) ? java.net.URLEncoder.encode(searchMajor, "UTF-8") : "";
		    String sNo = (searchNo != null) ? searchNo : "";

		    // 6. 리다이렉트 실행
		    // 결과(res)와 검색 조건들을 다시 붙여서 /student/search로 보냅니다.
		    String redirectUrl = contextPath + "/admin/studentStatus/search?res=" + result 
		                          + "&studentName=" + encName 
		                          + "&majorName=" + encMajor 
		                          + "&studentNo=" + sNo;
		        
		    response.sendRedirect(redirectUrl);
			}
		}
	

}
