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

import com.goodeelms.listener.LMSScheduleListener;

/**
 * Servlet implementation class StudentURLForwardServlet
 */
@WebServlet("/student/enrollment/*")
public class StudentURLForwardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public StudentURLForwardController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath() + "/student/enrollment";
		String command = requestURI.substring(contextPath.length());
		
		System.out.println(command);
		RequestDispatcher rd = null;
		ZoneId timeZone = LMSScheduleListener.getZONE_ID();
		switch(command) {
			case "/cart":
				// 목표 시간 설정
				ZonedDateTime startCartTime = LMSScheduleListener.getEventTimeMap().get("student_first_lecture_cart_start");
				ZonedDateTime endCartTime = LMSScheduleListener.getEventTimeMap().get("student_first_lecture_cart_end");
				
				// 현재 시간 로드
				ZonedDateTime nowCartTime = ZonedDateTime.now(timeZone);
				
				// 장바구니 기간 외에 접속 시도 시 메인페이지로
				if(nowCartTime.isBefore(startCartTime) || nowCartTime.isAfter(endCartTime)) {
					response.sendRedirect("/main.jsp?error=NoAccessEnrollTime");
					return;
				}
				
				Instant endCartInstant = endCartTime.toInstant();
				long endCartTimeMS = endCartInstant.toEpochMilli();
				System.out.println("설정 된 시간: " + endCartTime);
				request.setAttribute("endTime", endCartTimeMS);
				rd = request.getRequestDispatcher("/WEB-INF/views/student/enrollmentCart.jsp");
				rd.forward(request, response);
				break;
				
			case "/competition":
				// 목표 시간 설정
				ZonedDateTime startComTime = LMSScheduleListener.getEventTimeMap().get("student_first_lecture_cart_start");
				ZonedDateTime endComTime = LMSScheduleListener.getEventTimeMap().get("student_first_lecture_cart_end");
				
				// 현재 시간 로드
				ZonedDateTime nowComTime = ZonedDateTime.now(timeZone);
				
				// 수강신청 기간 외에 접속 시도 시 메인페이지로
				if(nowComTime.isBefore(startComTime) || nowComTime.isAfter(endComTime)) {
					response.sendRedirect("/main.jsp?error=NoAccessEnrollTime");
					return;
				}
				
				Instant endComInstant = endComTime.toInstant();
				long endComTimeMS = endComInstant.toEpochMilli();
				System.out.println("설정 된 시간: " + endComTime);
				request.setAttribute("endTime", endComTimeMS);
				
				rd = request.getRequestDispatcher("/WEB-INF/views/student/enrollmentCompetition.jsp");
				rd.forward(request, response);
				break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
	}

}
