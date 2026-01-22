package com.goodeelms.controller;

import java.io.IOException;
import java.util.List;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentStatusHistoryDTO;
import com.goodeelms.dto.TuitionPaymentDTO;
import com.goodeelms.service.LectureService;
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
         showTuition(request, response);
         break;
      case "/history":
         showStatusHistory(request, response);
         break;
      case "/major":
         showStudentMajor(request, response);
         break;
      case "/lecture":
    	  showLectureList(request, response);
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
      StudentDTO updateStudent = (StudentDTO) request.getAttribute("studentDTO");

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

   protected void showTuition(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      HttpSession session = request.getSession();
      int studentId = (Integer) session.getAttribute("student_id");

      TuitionPaymentDTO tuition = new TuitionService().readTuition(studentId);
      request.setAttribute("tuition", tuition);

      request.getRequestDispatcher("/WEB-INF/views/student/tuition.jsp").forward(request, response);
      return;
   }

   protected void showStatusHistory(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      HttpSession session = request.getSession();
      int studentId = (Integer) session.getAttribute("student_id");
      
//      List<StudentStatusHistoryDTO> list = new StudentStatusService().getStatusHistory(studentId);

//      request.setAttribute("statusList", list);
      request.getRequestDispatcher("/WEB-INF/views/student/statusHistory.jsp").forward(request, response);
      return;
   }

   protected void showStudentMajor(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      HttpSession session = request.getSession();
      int studentId = (Integer) session.getAttribute("studentId");
      
      
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
