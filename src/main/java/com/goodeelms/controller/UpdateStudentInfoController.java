package com.goodeelms.controller;

import java.io.IOException;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/student/mypage/*")
public class UpdateStudentInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentService studentService = new StudentService();
    public UpdateStudentInfoController() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        // "" 나 "/" 만 허용
        if (pathInfo == null || "/".equals(pathInfo)) { // 마이페이지 조회
            handleMypageView(request, response);
            return;
        }
        // 그 외는 마이페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/student/mypage");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if ("/update".equals(pathInfo)) { // 일반 정보 수정
            handleUpdateProfile(request, response);
            response.sendRedirect(request.getContextPath() + "/student/mypage");
            return;
        }
        if ("/password".equals(pathInfo)) { // 비밀번호 수정
            handleUpdatePassword(request, response);
            response.sendRedirect(request.getContextPath() + "/student/mypage");
            return;
        }
        // 그 외는 마이페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/student/mypage");
    }
    
    // 마이페이지 학생 정보 조회
    private void handleMypageView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	StudentDTO loginStudent = (StudentDTO) request.getSession().getAttribute("studentDTO");
//    	if (loginStudent == null) {
//    		response.sendRedirect(request.getContextPath() + "/login");
//    		return;
//    	}
    	Integer loginId = (Integer) request.getSession().getAttribute("student_id");
        try {
//        	int studentId = loginStudent.getStudentId();
//            StudentDTO student = studentService.getStudentById(studentId);
            StudentDTO student = studentService.getStudentById(loginId);
            request.setAttribute("student", student);
            
            // 전화번호 기본값 분리
            String phone1 = "010", phone2 = "", phone3 = "";
            if (student != null && student.getStudentPhone() != null) {
                String[] p = student.getStudentPhone().trim().split("-");
                if (p.length == 3) {
                    phone1 = p[0];
                    phone2 = p[1];
                    phone3 = p[2];
                }
            }
            request.setAttribute("phone1", phone1);
            request.setAttribute("phone2", phone2);
            request.setAttribute("phone3", phone3);

            // 이메일 기본값 분리 (id@domain)
            String emailId = "";
            String emailDomain = "naver.com";
            if (student != null && student.getStudentEmail() != null) {
                String[] e = student.getStudentEmail().trim().split("@");
                if (e.length == 2) {
                    emailId = e[0];
                    emailDomain = e[1];
                }
            }
            request.setAttribute("emailId", emailId);
            request.setAttribute("emailDomain", emailDomain);
            
            // 은행명, 계좌번호 분리
            String bankName = "";
            String accountNumber = "";
            if (student != null && student.getStudentBank() != null) {
                String bank = student.getStudentBank().trim();
                String[] parts = bank.split("\\s+", 2);
                bankName = parts[0];
                if (parts.length > 1) {
                    accountNumber = parts[1];
                }
            }
            request.setAttribute("bankName", bankName);
            request.setAttribute("accountNumber", accountNumber);
            
            // 에러 메세지
            String msg = request.getParameter("msg");
            String err = request.getParameter("err");
            request.setAttribute("msg", msg);
            request.setAttribute("err", err);
            
            request.getRequestDispatcher("/WEB-INF/views/student/mypage.jsp")
            	   .forward(request, response);
        } catch (Exception e) {
            System.out.println("마이페이지 조회 실패: " + e);
            request.getRequestDispatcher("/WEB-INF/views/student/mypage.jsp")
            	   .forward(request, response);
        }
    }
    
    // 마이페이지 학생 정보 수정
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentDTO loginStudent = (StudentDTO) request.getSession().getAttribute("studentDTO");
        if (loginStudent == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String phone = request.getParameter("student_phone");
        String email = request.getParameter("student_email");
        String address = request.getParameter("student_address");
        String bankName = request.getParameter("bank_name");       // select
        String accountNo = request.getParameter("account_number"); // input

        if (accountNo != null) {
            accountNo = accountNo.replaceAll("\\s+", ""); // 계좌 번호 공백 정리
        }
        String studentBank = bankName + " " + accountNo;

        try {
            studentService.updateStudentProfile(
                loginStudent.getStudentId(),
                phone, email, address, studentBank
            );
            // 세션 studentDTO 갱신
            StudentDTO refreshed = studentService.getStudentById(loginStudent.getStudentId());
            request.getSession().setAttribute("studentDTO", refreshed);
            response.sendRedirect(request.getContextPath() + "/student/mypage?msg=profile_ok");
            return;
        } catch (IllegalArgumentException e) {
            if ("ADDR_RULE".equals(e.getMessage())) {
                response.sendRedirect(request.getContextPath() + "/student/mypage?err=addr_rule");
                return;
            }
            if ("ACC_RULE".equals(e.getMessage())) {
                response.sendRedirect(request.getContextPath() + "/student/mypage?err=acc_rule");
                return;
            }
            throw e;
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/student/mypage?err=profile_dup");
            return;
        }
    }
    
    // 마이페이지 학생 비밀번호 수정
    private void handleUpdatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentDTO loginStudent = (StudentDTO) request.getSession().getAttribute("studentDTO");
        if (loginStudent == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String currentPw = request.getParameter("currentPassword");
        String newPw = request.getParameter("newPassword");

        try {
            boolean ok = studentService.changePassword(
                    loginStudent.getStudentId(),
                    currentPw,
                    newPw
            );
            if (ok) { // 현재 비번 일치
            	response.sendRedirect(request.getContextPath() + "/student/mypage?msg=pw_ok");
            	return;
            } else { // 현재 비번 불일치
                response.sendRedirect(request.getContextPath() + "/student/mypage?err=pw_mismatch");
                return;
            }
        } catch (IllegalArgumentException e) {
            if ("PW_RULE".equals(e.getMessage())) {
                response.sendRedirect(request.getContextPath() + "/student/mypage?err=pw_rule");
                return;
            }
            throw e;
        } catch (Exception e) {
            System.out.println("비밀번호 변경 실패: " + e);
            response.sendRedirect(request.getContextPath() + "/student/mypage?err=server");
            return;
        }
    }

}
