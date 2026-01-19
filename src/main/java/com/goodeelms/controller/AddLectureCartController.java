package com.goodeelms.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.List;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.PreEnrollmentDTO;
import com.goodeelms.service.AddLectureOnCartService;
import com.goodeelms.service.LoadLectureService;

@WebServlet("/addLectureCart")
public class AddLectureCartController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddLectureCartController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String studentId = (String)session.getAttribute("student_id");
		
		// 한 학생이 갖고있는 강의 다 긁어오기
		AddLectureOnCartService selectCartService = new AddLectureOnCartService();
		List<PreEnrollmentDTO> cartList = selectCartService.getCartDataOfStudent(studentId);
		
		// 강의 긁어올 때, 강의 아이디랑 일치하면서 지금 시간대인 것
		LoadLectureService selectLectureService = new LoadLectureService();
//		List<LectureDTO> lectureList = selectCartService.
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String lectureId = request.getParameter("lecture_id");
		String studentId = request.getParameter("student_id");
		System.out.println("호출됨");
		
		if(!invaildString(lectureId) || !invaildString(studentId)) {
			System.out.println("잘못된 접근");
			response.sendRedirect("/main.jsp");
			return;
		}
		
		AddLectureOnCartService service = new AddLectureOnCartService();

		int select = service.simpleSearchBeforeAdd(lectureId, studentId);
		System.out.println("select:" + select);
		if(select != 0) {
			System.out.println("이미 있는 데이터 추가 불가 버그 수정 필요");
			response.sendRedirect("/main.jsp");
			return;
		}
		
		int result = service.insertLectureOnCart(lectureId, studentId);
		if(result < 1) {
			System.out.println("INSERT 실패!");
			response.sendRedirect("/main.jsp");
			return;
		}
		
		System.out.println("INSERT 성공");
		// 다시 조회해서 담아야함
	}

	private boolean invaildString(String target) {
		if(target == null || target.isBlank()) return false;
		
		return true;
	}
}
