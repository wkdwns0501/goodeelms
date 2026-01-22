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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.PreEnrollmentDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.service.LectureCartService;
import com.goodeelms.service.LectureService;
import com.goodeelms.service.LoadLectureService;
import com.goodeelms.service.StudentService;

/**
 * Servlet implementation class AddLectureCartServlet
 */
@WebServlet("/student/cart/*")
public class LectureCartController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// URL 관련 필드
	private String requestURI = null;
	private String contextPath = null;
	private String command = null;
	
    public LectureCartController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestURI = request.getRequestURI();
		contextPath = request.getContextPath();
		command = requestURI.substring(contextPath.length());
		
		HttpSession session = request.getSession();
		int studentId = (Integer)session.getAttribute("student_id");
		LectureCartService selectCartService = new LectureCartService();
		Set<String> enrollmentStatusSet = new HashSet<String>();
		List<PreEnrollmentDTO> cartList = new ArrayList<PreEnrollmentDTO>();
		if("/student/cart/loadCart".equals(command)) {
			// 한 학생이 장바구니에 갖고있는 강의ID 중에 progress 긁어오기
			enrollmentStatusSet.clear();
			enrollmentStatusSet.add("progress");
		}
		else if("/student/cart/loadComp".equals(command)) {
			// 한 학생이 장바구니에 갖고있는 강의ID 중에 completed, re_apply 긁어오기
			enrollmentStatusSet.clear();
			enrollmentStatusSet.add("completed");
			enrollmentStatusSet.add("re_apply");
		}
		// 공통
		cartList = selectCartService.getCartDataOfStudent(studentId, enrollmentStatusSet);
		
		// lecture_id 얻어온 걸로 강의 코드 가져오기
		Set<Integer> lectureIdSet = new HashSet<Integer>();
		Set<Integer> lectureCodeSet = LectureService.getInstance().getLectureCodeWithLectureId(cartList.stream().map(PreEnrollmentDTO::getLectureId).collect(Collectors.toSet()));
		
		if(lectureCodeSet == null) lectureCodeSet = new HashSet<Integer>();
		// UI에서 수강 신청 완료 vs 수강 신청 실패
		Map<Integer, String> enrollStat = new HashMap<Integer, String>();
		for(PreEnrollmentDTO dto:cartList) {
			if(dto != null) {
				int id = dto.getLectureId();
				lectureIdSet.add(id);
				enrollStat.put(id, dto.getPreEnrollmentStatus());
			}
		}
		List<LectureDTO> lectureList = new ArrayList<LectureDTO>();
		if(lectureIdSet.size() > 0) {
			// 강의ID로 강의 긁어올 때, 강의 아이디랑 일치하면서 지금 시간대인 것(년도, 학기)
			LoadLectureService selectLectureService = new LoadLectureService();
			lectureList = selectLectureService.getLectureOfStudentCart(lectureIdSet);
		}
		
//		Set<Integer> lectureCodes = new HashSet<Integer>(); 
		for(LectureDTO dto : lectureList) {
			// progress || re_apply
			// 수강신청 시 자동 신청 된건지 미신청 된건지
			dto.setPreEnrollmentStatus(enrollStat.get(dto.getLectureId()));
			
		}
		
		request.setAttribute("totalCount", lectureList.size());
		request.setAttribute("lectureList", lectureList);
		// Limit 학점
		request.setAttribute("limitCartCredit", 21);
		// 강의 리스트(LoadLectureController)에서 장바구니 담긴 강의 빼야해서 세션에 저장함
		// 수강신청일 때는 다른 걸 빼야해서 Skip
		if("/student/cart/loadCart".equals(command)) {
			// 다른 곳에서 같은 이름으로 setAttribute한거 지우기
			session.removeAttribute("lectureCodeSet");
			session.setAttribute("lectureCodeSet", lectureCodeSet);
		}
		
		if("/student/cart/loadCart".equals(command)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/student/loadCartList.jsp");
			rd.forward(request, response);
		}
		else if("/student/cart/loadComp".equals(command)) {
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/student/loadCartCompetition.jsp");
			rd.forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// URL 분기
		requestURI = request.getRequestURI();
		contextPath = request.getContextPath();
		command = requestURI.substring(contextPath.length());
		
		System.out.println(command);
		
		String lectureId = request.getParameter("lecture_id");
		String studentId = request.getParameter("student_id");
		HttpSession session = request.getSession();
		String sessionId = ((Integer)session.getAttribute("student_id")).toString();
		if(!"/student/cart/clearCart".equals(command)) {
			if(!invaildString(lectureId) || !invaildString(studentId)) {
				System.out.println("잘못된 접근");
				return;
			}
		}
		else if(!invaildString(studentId)) {
			System.out.println("잘못된 접근");
			return;
		}
		if(!invaildStudent(studentId, sessionId)) {
			System.out.println("잘못된 접근");
			return;
		}
		
		LectureCartService service = new LectureCartService();
		if("/student/cart/addCart".equals(command)) {
			List<StudentMajorDTO> majorIds = new StudentService().getMajors(studentId);
			if(majorIds == null || majorIds.size() == 0) {
				System.out.println("전공 ID 조회 실패");
				return;
			}
			LectureDTO lectureInfo = LectureService.getInstance().getMajorIdAndByLectureId(Integer.parseInt(lectureId));
			if(lectureInfo == null || lectureInfo.getLectureType() == null || lectureInfo.getLectureType().isBlank()) {
				System.out.println("말도 안된다.");
				return;
			}
			boolean inMajor = false;
			for(StudentMajorDTO dto : majorIds) {
				if(lectureInfo.getMajorId() == dto.getMajorId() || "교양".equals(lectureInfo.getLectureType())) {
					inMajor = true;
					break;
				}
			}
			if(!inMajor) {
				System.out.println("비전공 접근");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			int select = service.simpleSearchBeforeAdd(lectureId, studentId);
			if(select != 0) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			int result = service.insertLectureOnCart(lectureId, studentId);
			if(result < 1) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
			System.out.println("INSERT 성공");
		}
		else if("/student/cart/deleteCart".equals(command)) {
			if(service.deleteLectureOnCart(lectureId, studentId) < 1) {
				System.out.println("DELETE 실패!");
				return;
			}
			System.out.println("DELETE 성공");
		}
		else if("/student/cart/clearCart".equals(command)) {
			int intId = Integer.parseInt(studentId);
			Set<String> enrollmentStatusSet = new HashSet<String>();
			enrollmentStatusSet.add("progress");
			List<PreEnrollmentDTO> cartList = service.getCartDataOfStudent(intId, enrollmentStatusSet);
			if(cartList == null || cartList.size() == 0) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			if(service.clearCart(studentId, cartList) == 0) response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
