package com.goodeelms.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;

import com.goodeelms.listener.LMSScheduleListener;
import com.goodeelms.util.StaticUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
		// 시간 설정
		Map<String, ZonedDateTime> timeMap = LMSScheduleListener.getEventTimeMap();
		switch(command) {
			// 장바구니
			case "/cart":
				// 현재 시간 로드
				ZonedDateTime nowCartTime = StaticUtils.getSettedTime();
				
				// 마감 시간 변수 선언
				ZonedDateTime endCartTime;
				
				// 장바구니 기간 외에 접속 시도 시 메인페이지로
				if(StaticUtils.isBetweenTime(nowCartTime, timeMap.get("student_first_lecture_cart_start"), timeMap.get("student_first_lecture_cart_end"))) {
					endCartTime = timeMap.get("student_first_lecture_cart_end");
				}
				else if(StaticUtils.isBetweenTime(nowCartTime, timeMap.get("student_second_lecture_cart_start"), timeMap.get("student_second_lecture_cart_end"))) {
					endCartTime = timeMap.get("student_second_lecture_cart_end");
				}
				else {
					response.sendRedirect("/main.jsp?error=NoAccessEnrollTime");
					return;
				}
				
				Instant endCartInstant = endCartTime.toInstant();
				long endCartTimeMS = endCartInstant.toEpochMilli();
				request.setAttribute("endTime", endCartTimeMS);
				rd = request.getRequestDispatcher("/WEB-INF/views/student/enrollmentCart.jsp");
				rd.forward(request, response);
				break;
				
			// 수강신청
			case "/competition":
				// 목표 시간 설정
				ZonedDateTime endComTime;
				
				// 현재 시간 로드
				ZonedDateTime nowComTime = StaticUtils.getSettedTime();
				
				// 수강신청 기간 외에 접속 시도 시 메인페이지로
				if(StaticUtils.isBetweenTime(nowComTime, timeMap.get("student_first_enrollment_start"), timeMap.get("student_first_enrollment_end"))) {
					endComTime = timeMap.get("student_first_enrollment_end");
				}
				else if(StaticUtils.isBetweenTime(nowComTime, timeMap.get("student_second_enrollment_start"), timeMap.get("student_second_enrollment_end"))) {
					endComTime = timeMap.get("student_second_enrollment_end");
				}
				else {
					response.sendRedirect("/main.jsp?error=NoAccessEnrollTime");
					return;
				}
				
				Instant endComInstant = endComTime.toInstant();
				long endComTimeMS = endComInstant.toEpochMilli();
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
