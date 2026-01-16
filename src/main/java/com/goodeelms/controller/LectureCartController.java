package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.PreEnrollmentDTO;
import com.goodeelms.service.LectureCartService;
import com.goodeelms.service.LoadLectureService;

/**
 * Servlet implementation class AddLectureCartServlet
 */
@WebServlet("/student/cart/*")
public class LectureCartController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LectureCartController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int studentId = (Integer)session.getAttribute("student_id");
		
		System.out.println("cartDoGet 호출됨");
		
		// 한 학생이 갖고있는 강의ID 다 긁어오기
		LectureCartService selectCartService = new LectureCartService();
		List<PreEnrollmentDTO> cartList = selectCartService.getCartDataOfStudent(studentId);
		
		// lecture_id 얻어온 걸로 강의 정보 불러오기
		Set<Integer> lectureIdSet = new HashSet<Integer>();
		for(PreEnrollmentDTO dto:cartList) {
			if(dto != null) {
				int id = dto.getLectureId();
				lectureIdSet.add(id);
			}
		}
		List<LectureDTO> lectureList = new ArrayList<LectureDTO>();
		if(lectureIdSet.size() > 0) {
			// 강의ID로 강의 긁어올 때, 강의 아이디랑 일치하면서 지금 시간대인 것(년도, 학기)
			LoadLectureService selectLectureService = new LoadLectureService();
			lectureList = selectLectureService.getLectureOfStudent(lectureIdSet);
		}
		
		request.setAttribute("totalCount", lectureList.size());
		request.setAttribute("lectureList", lectureList);
		// 강의 리스트에서 장바구니 담긴 강의 빼야해서 세션에 저장함
		session.setAttribute("lectureIdSet", lectureIdSet);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/student/loadCartList.jsp");
		rd.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// URL 분기
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		
		System.out.println(command);
		
		String lectureId = request.getParameter("lecture_id");
		String studentId = request.getParameter("student_id");
		HttpSession session = request.getSession();
		String sessionId = ((Integer)session.getAttribute("student_id")).toString();
		
		if(!invaildString(lectureId) || !invaildString(studentId)) {
			System.out.println("잘못된 접근");
			return;
		}
		if(!invaildStudent(studentId, sessionId)) {
			System.out.println("잘못된 접근");
			return;
		}
		
		LectureCartService service = new LectureCartService();
		if("/student/cart/addCart".equals(command)) {
			int select = service.simpleSearchBeforeAdd(lectureId, studentId);
			System.out.println("select:" + select);
			if(select != 0) {
				System.out.println("이미 있는 데이터 추가 불가 버그 수정 필요");
				return;
			}
			
			int result = service.insertLectureOnCart(lectureId, studentId);
			if(result < 1) {
				System.out.println("INSERT 실패!");
				return;
			}
			System.out.println("INSERT 성공");
		}
		else if("/student/cart/deleteCart".equals(command)) {
			System.out.println("DELETE 진입");
			if(service.deleteLectureOnCart(lectureId, studentId) < 1) {
				System.out.println("DELETE 실패!");
				return;
			}
			System.out.println("DELETE 성공");
		}
	}

	private boolean invaildString(String target) {
		if(target == null || target.isBlank()) return false;
		
		return true;
	}
	private boolean invaildStudent(String paramId, String sessionId) {
		if(paramId.equals(sessionId)) return true;
		return false;
	}
}
