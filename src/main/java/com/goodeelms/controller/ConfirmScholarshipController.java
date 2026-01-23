package com.goodeelms.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import com.goodeelms.dto.LectureHistoryDTO;
import com.goodeelms.service.ConfirmScholarshipService;
import com.goodeelms.util.AlertUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/confirmScholarship/*")
public class ConfirmScholarshipController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
				
		// 내비바 통한 장학 관리 페이지 진입
		if(command.equals("/admin/confirmScholarship/list")) {
			ConfirmScholarshipService ssc = new ConfirmScholarshipService();
			
			if (!ssc.isAccessPeriod()) {
				// alert 유틸 호출
				AlertUtil.alertAndRedirect(response, "장학 관리 기간이 아닙니다.", contextPath + "/main.jsp");
				return;
			}
	        
			String yearSemester = request.getParameter("yearSemester");
			
			// 
			String[] parts = ssc.lastestSemester(yearSemester);

			String year = parts[0];   // 2025
			int semester = Integer.parseInt(parts[1]);   // 1
			
			// null 값 대비하여 실제 
			String actualSemester = year + "_" + parts[1];
			
			ArrayList<LectureHistoryDTO> list = ssc.getScholarshipList(year, semester);
			
			request.setAttribute("scholarshipList", list);
			request.setAttribute("currentSemester", actualSemester);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/confirmScholarship.jsp");
			rd.forward(request, response);
		}
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		if(command.equals("/admin/confirmScholarship/confirm")) {
			String[] confirmedStudentsId = request.getParameterValues("checkConfirmed");
			String yearSemester = request.getParameter("yearSemester");
			int yearSemestertoInt = Integer.parseInt(yearSemester.replace("_", ""));
			
			// 추후 insert 되어야 할 학생 수
			int studentNumbers = confirmedStudentsId.length;
			
			ConfirmScholarshipService ssc = new ConfirmScholarshipService();
			int insertResult = ssc.writeScholarshipHistory(confirmedStudentsId, yearSemestertoInt);
			
			if(insertResult >= studentNumbers) {
				System.out.println("정상 작성 완료");
			} else {
				System.out.println("history 작성 문제 발생");
			}
			
			String redirectUrl = contextPath + "/admin/confirmScholarship/list?yearSemester=" + yearSemester;
  
			response.sendRedirect(redirectUrl);
		}
	}

}
