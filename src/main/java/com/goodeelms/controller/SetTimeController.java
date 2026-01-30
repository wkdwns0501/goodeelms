package com.goodeelms.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import com.goodeelms.util.StaticUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class SetTimeController
 */
@WebServlet("/setTime")
public class SetTimeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetTimeController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ZonedDateTime time = StaticUtils.getSettedTime();
		HttpSession session = request.getSession(); 
		session.setAttribute("time", time);
		RequestDispatcher rd = request.getRequestDispatcher("/footer.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String date = request.getParameter("date");
		
		if(date == null || date.isBlank()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		LocalDate localDate = LocalDate.parse(date);
		StaticUtils.setSettedTime(localDate);
		
	}

}
