package com.goodeelms.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.service.LoadLectureService;
import com.goodeelms.service.StudentService;
import com.goodeelms.util.StaticUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class LoadLectureServlet
 */
@WebServlet("/student/loadLecture/*")
public class LoadLectureController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// URL 관련 필드
	private String requestURI = null;
	private String contextPath = null;
	private String command = null;
		
    public LoadLectureController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestURI = request.getRequestURI();
		contextPath = request.getContextPath()+"/student/loadLecture";
		command = requestURI.substring(contextPath.length());
		
		String cat = request.getParameter("cat");
		String reqId = request.getParameter("id");
		
		HttpSession session = request.getSession(false);
		int sessionId = (Integer)session.getAttribute("student_id");
		
		if(cat == null || cat.isBlank()) cat = "all";
		
		StudentService stuS = new StudentService();
		
		// 학생의 학과 정보 불러오기
		List<StudentMajorDTO> majorList = stuS.getMajors(reqId);
		// 학과 정보가 없으면 안됨
		if(majorList == null || majorList.size() == 0) {
			System.out.println("알 수 없는 오류");
			response.sendRedirect("/main.jsp");
			return;
		}
		
		// 한번에 표시 할 길이
		int viewLen = 10;
		// 어느 페이지 호출했는지
		int viewPage = 1;
		String pageString = request.getParameter("viewPage");
		try {
			if(pageString != null && !pageString.isBlank()) {
				viewPage = Integer.parseInt(pageString);
			}
		}
		catch(NumberFormatException e) {
			viewPage = 1;
		}
		if (viewPage < 1) viewPage = 1;
		
		int[] majorIds = new int[majorList.size()];
		for(int i = 0; i < majorIds.length; i++) {
			majorIds[i] = majorList.get(i).getMajorId();
		}
		
		// 어떤 단어 검색했는지
		String searchWord = request.getParameter("search_word");
		if(!StaticUtils.checkEscapeString(searchWord)) {
//			response.sendRedirect("/main.jsp?error=EscapeString");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
		}
		// 장바구니에 등록 된 강의 codeSet 가져오기
		Set<Integer> inLectureCodeSet = (Set<Integer>)session.getAttribute("lectureCodeSet");
		// 로드 서비스 생성
		LoadLectureService loadS = new LoadLectureService();
		// 3.5점 이상인 강의 code codeSet에 넣기
		Set<Integer> notAbleReEnrollment = loadS.getNotAbleReEnrollmentCodes(sessionId);
		inLectureCodeSet.addAll(notAbleReEnrollment);
		
		// 카테고리, 검색어 조건으로 전공인지 확인, 이미 장바구니에 있거나 학점 3.5점 이상이면 노출 안되게
		int total_record = loadS.getLecturesCount(cat, searchWord, inLectureCodeSet, majorIds);
		int pageNums = (int)Math.ceil((double) total_record / viewLen);
		if (pageNums == 0) pageNums = 1;
		if (viewPage > pageNums) viewPage = pageNums;
		
		// 블록 시작/끝 계산
		final int pageLen = 10;
		final int startPage = ((viewPage - 1) / pageLen) * pageLen + 1;
		final int endPage = Math.min(startPage + pageLen - 1, pageNums);
		
		System.out.println("StartPage: " + startPage);
		System.out.println("EndPage: " + endPage);
		// 블록 이동 (없으면 1)
	    final int prevBlockPage = (startPage > 1) ? (startPage - 1) : 1;
	    final int nextBlockPage = (endPage < pageNums) ? (endPage + 1) : pageNums;
	    
//		if(pageNums > 1 && viewPage == 1) viewLen = total_record % viewLen;
		
		List<LectureDTO> list = loadS.getLectureList(cat, searchWord, viewPage, viewLen, inLectureCodeSet, majorIds);
		
		request.setAttribute("id", sessionId);
		request.setAttribute("cat", cat);
		request.setAttribute("viewPage", viewPage);
		request.setAttribute("pageNums", pageNums);
		request.setAttribute("startPage", startPage);	
		request.setAttribute("endPage", endPage);
        request.setAttribute("prevBlockPage", prevBlockPage);
        request.setAttribute("nextBlockPage", nextBlockPage);
		request.setAttribute("lectureList", list);
		request.setAttribute("totalCount", total_record);
		
		if(command.equals("/cart")) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/student/loadLectureList.jsp");
			rd.forward(request, response);
		}
		else if(command.equals("/comp")) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/student/loadLectureCompetition.jsp");
			rd.forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
	}

}
