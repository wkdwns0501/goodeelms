package com.goodeelms.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/student/mypage/*")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1, 
		maxFileSize = 1024 * 1024 * 10, 
		maxRequestSize = 1024 * 1024 * 50
)
public class UpdateStudentInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentService studentService = new StudentService();
    public UpdateStudentInfoController() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        // "" 나 "/" 만 허용
        
        // 사진 조회 요청
        if("/display".equals(pathInfo)) {
        	handleDisplayFile(request, response);
        	return;
        }
        
        if (pathInfo == null || "/".equals(pathInfo)) { // 마이페이지 조회
            handleMypageView(request, response);
            return;
        }
        // 그 외는 마이페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/student/mypage");
    }

    private void handleDisplayFile(HttpServletRequest request, HttpServletResponse response) {
		String fileName = request.getParameter("fileName");
		String uploadPath = "D:/goodeelmsfile";
		
		File file = new File(uploadPath, fileName);
		
		if (fileName == null || fileName.isEmpty() || !file.exists()) {
	        // 기본 이미지 쏴주기
	        String defaultPath = request.getServletContext().getRealPath("/resources/images/defaultUserProfile.jpg");
	        file = new File(defaultPath);
	    }
		
		// 파일 형식 라벨링 to 브라우저
		String mimeType = getServletContext().getMimeType(file.getName());
	    if (mimeType == null) {
	        mimeType = "application/octet-stream";
	    }
	    response.setContentType(mimeType);

	    // 파일을 읽어서 출력 스트림으로 복사
	    try {
			Files.copy(file.toPath(), response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    // 마이페이지 학생 정보 조회
    private void handleMypageView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Integer loginId = (Integer) request.getSession().getAttribute("student_id");
    	if (loginId == null) {
    		response.sendRedirect(request.getContextPath() + "/common/login");
    		return;
    	}
        try {
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
    
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if ("/update".equals(pathInfo)) { // 일반 정보 수정
            handleUpdateProfile(request, response);
            return;
        }
        if ("/password".equals(pathInfo)) { // 비밀번호 수정
            handleUpdatePassword(request, response);
            return;
        }
        // 그 외는 마이페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/student/mypage");
    }
    

    
    // 마이페이지 학생 정보 수정
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer loginId = (Integer) request.getSession().getAttribute("student_id");
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/common/login");
            return;
        }
        // 사진 저장
        Part filePart = request.getPart("uploadFile");
        String photoFile = ""; // 원본 파일명
        String photoUUID = ""; // 저장될 UUID 파일명
        boolean newFileUploaded = false;
        String lastPhotoUUID = ""; // 기존 사용하던 사진의 UUID
        
        if(filePart != null && filePart.getSize() > 0) {
        	//원본 파일명 추출
        	photoFile = filePart.getSubmittedFileName();
        	
        	//UUID 생성 및 저장용 파일명 만들기
        	String ext = photoFile.substring(photoFile.lastIndexOf("."));
        	photoUUID = UUID.randomUUID().toString() + ext;
        	
        	// 저장 경로 설정 (폴더 없다면 생성까지)
        	String uploadPath = "D:/goodeelmsfile";
        	File uploadDir = new File(uploadPath);
        	if (!uploadDir.exists()) uploadDir.mkdirs();
        	
        	// 실제 파일 UUID 이름으로 저장
        	filePart.write(uploadPath + File.separator + photoUUID);
        	
        	newFileUploaded = true; // 기존 파일 삭제를 위한 플래그
        	// 기존 사진의 uuid (삭제 목적)
        	lastPhotoUUID = request.getParameter("nowPhotoUUID");
        } 	else { // 파일을 선택하지 않았을 경우
        	photoFile = request.getParameter("nowPhotoFile");
        	photoUUID = request.getParameter("nowPhotoUUID");
        }
        
        String phone = request.getParameter("studentPhone");
        if (phone != null) phone = phone.trim();
        String email = request.getParameter("studentEmail");
        if (email != null) email = email.trim();
        String address = request.getParameter("student_address");
        String bankName = request.getParameter("bank_name");       // select
        String accountNo = request.getParameter("account_number"); // input
        String confirmPw = request.getParameter("confirmPassword");
        if (confirmPw != null) confirmPw = confirmPw.trim();

        if (confirmPw == null || confirmPw.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/student/mypage?err=profile_pw_mismatch");
            return;
        }

        if (accountNo != null) {
            accountNo = accountNo.replaceAll("\\s+", ""); // 계좌 번호 공백 정리
        }
        String studentBank = bankName + " " + accountNo;

        try {
            studentService.updateStudentProfile(loginId, phone, email, address, studentBank, confirmPw, photoFile, photoUUID);        
            if(newFileUploaded) {
            	studentService.deleteLastFile(lastPhotoUUID);
            }
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
            if ("PROFILE_PW_MISMATCH".equals(e.getMessage())) {
                response.sendRedirect(request.getContextPath() + "/student/mypage?err=profile_pw_mismatch");
                return;
            }
            throw e;
        } catch (Exception e) {
        	e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/student/mypage?err=profile_fail");
            return;
        }
    }
    
    // 마이페이지 학생 비밀번호 수정
    private void handleUpdatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Integer loginId = (Integer) request.getSession().getAttribute("student_id");
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/common/login");
            return;
        }

        String currentPw = request.getParameter("currentPassword");
        String newPw = request.getParameter("newPassword");

        try {
            boolean ok = studentService.changePassword(loginId, currentPw, newPw);
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
            if ("PROFILE_PW_MISMATCH".equals(e.getMessage())) {
                response.sendRedirect(request.getContextPath() + "/student/mypage?err=pw_mismatch");
                return;
            }
            if ("PW_SAME".equals(e.getMessage())) {
                response.sendRedirect(request.getContextPath() + "/student/mypage?err=pw_same");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/student/mypage?err=server");
            return;
        } catch (Exception e) {
            System.out.println("비밀번호 변경 실패: " + e);
            response.sendRedirect(request.getContextPath() + "/student/mypage?err=server");
            return;
        }
    }

}
