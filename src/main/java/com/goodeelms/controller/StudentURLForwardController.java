package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Servlet implementation class StudentURLForwardServlet
 */
@WebServlet("/student/page/*")
public class StudentURLForwardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public StudentURLForwardController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath() + "/student/page";
		String command = requestURI.substring(contextPath.length());
		
		System.out.println(command);
		RequestDispatcher rd = null;
		switch(command) {
			case "/enrollment":
				// 목표 시간 설정
				LocalDateTime targetTime = LocalDateTime.of(2026, 01, 20, 0, 0);
				ZoneId timeZone = ZoneId.of("Asia/Seoul");
				ZonedDateTime endTime = ZonedDateTime.of(targetTime, timeZone);
				
				// 현재 시간 로드
				ZonedDateTime nowTime = ZonedDateTime.now(timeZone);
				
				if(nowTime.isAfter(endTime)) {
					response.sendRedirect("/main.jsp?error=NoAccessEnrollTime");
					return;
				}
				
				Instant endInstant = endTime.toInstant();
				long endTimeMS = endInstant.toEpochMilli();
				System.out.println("설정 된 시간: " + endTime);
				request.setAttribute("endTime", endTimeMS);
				rd = request.getRequestDispatcher("/WEB-INF/views/student/enrollment.jsp");
				rd.forward(request, response);
				break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
	}

}
