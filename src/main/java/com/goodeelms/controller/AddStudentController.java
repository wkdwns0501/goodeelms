package com.goodeelms.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.goodeelms.dto.MajorDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentRegisterService;
import com.goodeelms.util.EncryptUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/addStudent/*")
public class AddStudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		// 첫 페이지 진입시 
		if(command.equals("/admin/addStudent/list")) {
			StudentRegisterService srs = new StudentRegisterService();
			
			// 전체 학생 조회
			ArrayList<StudentDTO> list = srs.getAllStudentList();
			request.setAttribute("studentList", list);
			
			// 학생 등록에 학과 목록 호출
			ArrayList<MajorDTO> majorList = srs.getMajorList();
			request.setAttribute("majorList", majorList);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/addStudent.jsp");
			rd.forward(request, response);			
		}
		
		// 검색 조건에 따른 학생 목록 조회
		if(command.equals("/admin/addStudent/search")) {
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
		
		// 신입생 기초 정보 등록
		if(command.equals("/admin/addStudent/register")) {
			StudentDTO studentDTO = new StudentDTO();
			studentDTO.setStudentName(request.getParameter("studentName")); 
			studentDTO.setStudentGender(request.getParameter("studentGender")); 
			studentDTO.setStudentIdentityNumber
			// 주민번호 연결
			(request.getParameter("identityFront")+"-"+request.getParameter("identityBack")); 
			studentDTO.setStudentNo(request.getParameter("studentNo")); 
			studentDTO.setStudentPhone(request.getParameter("studentPhone"));
			
			String passWord = EncryptUtil.encryptPassword(request.getParameter("identityBack"));
			studentDTO.setStudentPassword(passWord);
			System.out.println(passWord);
			int majorId = Integer.parseInt(request.getParameter("majorId"));
			StudentRegisterService srs = new StudentRegisterService();
			
			// 사용중인 학번인지 확인
			int checkResult = srs.studentExistCheck(request.getParameter("studentNo"));
			// 1. 학번 중복 체크
			if(checkResult > 0) {
			    response.setContentType("text/html; charset=UTF-8");
			    PrintWriter out = response.getWriter();
			    out.println("<script>");
			    out.println("alert('이미 존재하는 학번입니다. 다시 확인해주세요.');");
			    out.println("history.back();");
			    out.println("</script>");
			    out.flush();
			    return; // ⬅️ 로직 중단 (매우 중요)
			} 
			// 2. 핸드폰 번호 중복 체크 (학번이 통과된 경우만 실행)
			int checkPhone = srs.studentPhoneCheck(request.getParameter("studentPhone"));
			System.out.println(checkPhone);
			if(checkPhone > 0) {
			    response.setContentType("text/html; charset=UTF-8");
			    PrintWriter out = response.getWriter();
			    out.println("<script>");
			    out.println("alert('이미 존재하는 핸드폰 번호입니다. 다시 확인해주세요.');");
			    out.println("history.back();");
			    out.println("</script>");
			    out.flush();
			    return; // ⬅️ 로직 중단
			} 
			// 3. 둘 다 통과 시 등록 진행
			    int registerResult = srs.studentRegister(studentDTO);
			    int newStudentId = srs.getNewStudentId();
			    int writeResult = srs.writeStudentMajor(newStudentId, majorId);
			    
			    if(registerResult > 0 && writeResult > 0) {
			        response.sendRedirect(request.getContextPath() + "/admin/addStudent/list"); 
			    } else {
			        System.out.println("학생 등록 실패");
			    }
			}
			
		}
		
		
	}	



