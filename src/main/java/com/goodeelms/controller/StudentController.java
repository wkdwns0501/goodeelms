package com.goodeelms.controller;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.goodeelms.dto.ChangeMajorHistoryDTO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.ScholarshipDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentStatusHistoryDTO;
import com.goodeelms.dto.TuitionPaymentDTO;
import com.goodeelms.service.LectureService;
import com.goodeelms.service.MajorService;
import com.goodeelms.service.StudentService;
import com.goodeelms.service.StudentStatusService;
import com.goodeelms.service.TuitionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/student/*")
public class StudentController extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private final StudentService studentService = new StudentService();
   private final LectureService lectureService = LectureService.getInstance();

   protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      checkPath(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      checkPath(request, response);
   }
		
   private void checkPath(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
	  String path = request.getPathInfo(); // /student 뒤의 경로
	  if (path == null) path = "";
	  
      HttpSession session = request.getSession(false);
      if (session == null || session.getAttribute("user_role") == null
            || session.getAttribute("student_id") == null) {
         if (!path.equals("/student/login") && !path.equals("/student/signup")) {
            response.sendRedirect(request.getContextPath() + "/common/login");
            return;
         }
      }
      
      switch (path) {
		case "/signup":
			signup(request, response);
			break;
		case "/tuition":
			showTuitionAndScholarship(request, response);
			break;
		case "/tuition/pay":
	        payTuition(request, response);
	        break;
		case "/lecture":
	    		showLectureList(request, response);
	    	break;
		case "/grades":
			showGrade(request, response);
			break;
		case "/history/majorAndStatus":
			showStatusAndMajorHistory(request, response);
			break;
		case "/history/rewardAndPunishment":
			showReawrdAndPunishments(request, response);
			break;
		case "/myLectures":	
			showLecture(request, response);
			break;
		default:
			System.out.println("정의되지 않은 경로 요청됨: " + path);
			response.sendRedirect(request.getContextPath() + "/main.jsp");
      }
   }

   private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String originPw = request.getParameter("origin_student_password");
      String newPw = request.getParameter("new_student_password");
      String email = request.getParameter("student_email");
      String phone = request.getParameter("student_phone");
      String bank = request.getParameter("student_bank");
      String address = request.getParameter("student_address");

      HttpSession session = request.getSession();
      StudentDTO updateStudent = (StudentDTO) session.getAttribute("studentDTO");

		if (newPw.equals(originPw)) { // 기존 비밀번호와 동일하면 다시 입력하도록
			request.setAttribute("errorMessage", "기존 비밀번호와 동일합니다.");
			request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
			return;
		}
		
		updateStudent.setStudentPassword(newPw);
		updateStudent.setStudentEmail(email);
		updateStudent.setStudentPhone(phone);
		updateStudent.setStudentBank(bank);
		updateStudent.setStudentAddress(address);
		
		boolean isUpdated = studentService.updateStudent(updateStudent);

		if (isUpdated) {
			session.setAttribute("studentDTO", updateStudent);
			response.sendRedirect(request.getContextPath() + "/main.jsp");
			return;
		} else {
			request.setAttribute("errorMessage", "이미 사용 중인 이메일/연락처 입니다.");
			request.getRequestDispatcher("/WEB-INF/views/student/studentSignUp.jsp").forward(request, response);
			return;
		}
	}

	protected void showTuitionAndScholarship(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    int student_id = (Integer) session.getAttribute("student_id");
	    
	    try {
			String status = studentService.getStudentById(student_id).getStudentStatus();
			if (status.equals("졸업") || status.equals("휴학")) {
		        response.setContentType("text/html; charset=UTF-8");
		        PrintWriter out = response.getWriter();
		        
		        out.println("<script>");
		        out.println("alert('졸업생 또는 휴학생 접근할 수 없습니다.');");
		        out.println("location.href='" + request.getContextPath() + "/main.jsp';");
		        out.println("</script>");
		        
		        out.flush();
		        return; 
		    }
		} catch (Exception e) {
			System.out.println("졸업생 또는 휴학생의 접근차단 실패");
		} 
	    
	    TuitionPaymentDTO dto = new TuitionService().readTuition(student_id);
	    List<ScholarshipDTO> scholarship = new StudentService().getScholarshipByStudentId(student_id);
	    
	    if(dto == null) {
	        request.setAttribute("msg", "등록금 정보를 조회할 수 없습니다.");
	        request.getRequestDispatcher("/WEB-INF/views/student/tuition.jsp").forward(request, response);
	        return;
	    }
	    
	    String msgParam = request.getParameter("msg");
	    if ("success".equals(msgParam)) {
	        request.setAttribute("msg", "등록금 납부가 정상적으로 처리되었습니다.");
	    }
	    
	    StudentDTO student = studentService.getIdentityNumAndNo(student_id); 
	    String virtualAccount = "정보 없음"; 
	    
	    if (student != null && student.getStudentIdentityNumber() != null && student.getStudentNo() != null) {
	        try {
	            String identityNum = student.getStudentIdentityNumber(); 
	            String studentNo = student.getStudentNo();
	            
	            String frontNum = identityNum.substring(7, 10); 
	            String backNum = studentNo.substring(studentNo.length() - 7);
	            
	            virtualAccount = frontNum + "-" + backNum;
	        } catch (Exception e) {
	            System.err.println("계좌 번호 생성 중 인덱스 오류: " + e.getMessage());
	        }
	    }
	    
	    request.setAttribute("account", virtualAccount);
	    request.setAttribute("tuition", dto);
	    request.setAttribute("scholarship", scholarship);
	    
	    request.getRequestDispatcher("/WEB-INF/views/student/tuition.jsp").forward(request, response);
	}

	protected void payTuition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    int student_id = (Integer) session.getAttribute("student_id");
	     
	    
	    String payStr = request.getParameter("payment");
	    int paymentTuition = (payStr != null) ? Integer.parseInt(payStr) : 0;
	    
	    TuitionService tService = new TuitionService();
	    
	    if(paymentTuition > 0) { tService.getTuitionPaymentAfterPay(student_id, paymentTuition); } 
	    
	    response.sendRedirect(request.getContextPath() + "/student/tuition?msg=success");
	}

	
	protected void showStatusAndMajorHistory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 	// 0120 임욱(수정) / 전공, 학적 변동 이력, 전공 변동 이력 출력
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
		
		List<String> majorList = new MajorService().getMajorCodeAndMajorName(student_id); // major_code, major_name
		ChangeMajorHistoryDTO dto = studentService.getChangedMajorHistory(student_id); // fromMajorName, toMajorName
		List<StudentStatusHistoryDTO> statusList = new StudentStatusService().getStatusHistory(student_id); // status_type, status_reason, status_at 		

		if (statusList != null) request.setAttribute("statusList", statusList);
		if (dto != null) request.setAttribute("majorHistory", dto);
		if (majorList == null) request.setAttribute("msg", "전공을 조회할 수 없습니다.");
		
		
		request.setAttribute("majorList", majorList);
		request.getRequestDispatcher("/WEB-INF/views/student/majorAndStatus.jsp").forward(request, response);
		return;
	}
	
	protected void showGrade(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	// 0120 임욱(추가) / 성적 조회 
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
		
		Map<Integer, LectureDTO> grade = studentService.getlectureAndLectureScoreByStudentId(student_id);
		
		request.setAttribute("grade", grade); // lecture_code, lecture_name, lecuture_year, lecture_semester, lecture_score ( lecture_history)
		request.getRequestDispatcher("/WEB-INF/views/student/grades.jsp").forward(request, response);
		return;
	}
	
	protected void showLecture(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	// 0120 임욱(추가) / 수강중인 강의 조회 
		HttpSession session = request.getSession();
		int student_id = (Integer) session.getAttribute("student_id");
		
		Map<Integer, LectureDTO> lectures = studentService.getProgressInfoByStudentId(student_id);
		
		request.setAttribute("lectures", lectures); 
		request.getRequestDispatcher("/WEB-INF/views/student/myLectures.jsp").forward(request, response);
		return;
	}
	
	protected void showReawrdAndPunishments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session = request.getSession(); // 0121 임욱(추가) / 학사경고, 장학 정보 조회
		int student_id = (Integer) session.getAttribute("student_id");
	
		List<LectureDTO> probagation = studentService.getProbationByStudentId(student_id);
		List<ScholarshipDTO> scholarship = new StudentService().getScholarshipByStudentId(student_id);
		
		request.setAttribute("probagation", probagation); 
		request.setAttribute("scholarship", scholarship); 
		request.getRequestDispatcher("/WEB-INF/views/student/rewardAndPunishment.jsp").forward(request, response);
		return;
	}

   protected void showTuition(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      HttpSession session = request.getSession();
      int studentId = (Integer) session.getAttribute("student_id");

      TuitionPaymentDTO tuition = new TuitionService().readTuition(studentId);
      request.setAttribute("tuition", tuition);

      request.getRequestDispatcher("/WEB-INF/views/student/tuition.jsp").forward(request, response);
      return;
   }
  
   
   // 학생 강의 목록
   private void showLectureList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   Integer loginId = (Integer) request.getSession().getAttribute("student_id");
       if (loginId == null) {
           response.sendRedirect(request.getContextPath() + "/common/login");
           return;
       }
       
       String keyword = request.getParameter("keyword");
       String pageParam = request.getParameter("page");
       int page = 1;

       try {
           if (pageParam != null && !pageParam.isBlank()) {
               page = Integer.parseInt(pageParam);
           }
       } catch (NumberFormatException e) {
           page = 1;
       }

       final int limit = 10;
       final int blockSize = 10;

       page = Math.max(page, 1);

       final int totalCount = lectureService.getLectureTotalCountForStudent(keyword);
       final int totalPage = Math.max(1, (int) Math.ceil(totalCount / (double) limit));

       page = Math.min(page, totalPage);

       final int startPage = ((page - 1) / blockSize) * blockSize + 1;
       final int endPage = Math.min(startPage + blockSize - 1, totalPage);

       final int prevBlockPage = (startPage > 1) ? (startPage - 1) : 1;
       final int nextBlockPage = (endPage < totalPage) ? (endPage + 1) : totalPage;

       request.setAttribute("lectures",
               lectureService.getLecturePageForStudent(page, limit, keyword));

       request.setAttribute("page", page);
       request.setAttribute("limit", limit);
       request.setAttribute("totalCount", totalCount);
       request.setAttribute("totalPage", totalPage);
       request.setAttribute("keyword", keyword);

       request.setAttribute("blockSize", blockSize);
       request.setAttribute("startPage", startPage);
       request.setAttribute("endPage", endPage);
       request.setAttribute("prevBlockPage", prevBlockPage);
       request.setAttribute("nextBlockPage", nextBlockPage);

       request.getRequestDispatcher("/WEB-INF/views/student/lectureList.jsp")
              .forward(request, response);
   }

}
