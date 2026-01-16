package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.MajorDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentRegisterService;

@WebServlet("/addStudent/*")
public class AddStudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		if(command.equals("/addStudent/list")) {
			StudentRegisterService srs = new StudentRegisterService();
			
			ArrayList<StudentDTO> list = srs.getAllStudentList();
			request.setAttribute("studentList", list);
			
			ArrayList<MajorDTO> majorList = srs.getMajorList();
			request.setAttribute("majorList", majorList);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/addStudent.jsp");
			rd.forward(request, response);			
		}
		if(command.equals("/addStudent/search")) {
			String studentName = request.getParameter("studentName");
			String majorName = request.getParameter("majorName");
			String studentNo = request.getParameter("studentNo");
			
			StudentRegisterService srs = new StudentRegisterService();
			ArrayList<StudentDTO> list = srs.getStudentList(studentName,majorName,studentNo);
			
			request.setAttribute("studentList", list);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/addStudent.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		if(command.equals("/addStudent/register")) {
			StudentDTO studentDTO = new StudentDTO();
			studentDTO.setStudentName(request.getParameter("studentName")); 
			studentDTO.setStudentGender(request.getParameter("studentGender")); 
			studentDTO.setStudentIdentityNumber
			(request.getParameter("identityFront")+"-"+request.getParameter("identityBack")); 
			studentDTO.setStudentNo(request.getParameter("studentNo")); 
			studentDTO.setStudentPhone(request.getParameter("studentPhone"));
			int majorId = Integer.parseInt(request.getParameter("majorId"));
			StudentRegisterService srs = new StudentRegisterService();
			int checkResult = srs.studentExistCheck(request.getParameter("studentNo"));
			
			if(checkResult > 0) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
		        out.println("<script>");
		        out.println("alert('이미 존재하는 학번입니다. 다시 확인해주세요.');");
		        out.println("history.back();"); // 입력하던 내용 유지하며 뒤로가기
		        out.println("</script>");
		        out.flush();
		        out.close();
			} else {
				int registerResult = srs.studentRegister(studentDTO);
				
				int newStudentId = srs.getNewStudentId();
				System.out.println(newStudentId);
				int writeResult = srs.writeStudentMajor(newStudentId, majorId);
				if(registerResult > 0 && writeResult > 0) {
				response.sendRedirect(request.getContextPath() + "/addStudent/list");	
				}
				else {
					
				}
			}
			
		}
		
		
	}	
}


