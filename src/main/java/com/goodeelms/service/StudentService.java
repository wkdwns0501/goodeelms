package com.goodeelms.service;

import java.sql.Connection;
import java.util.List;
import java.util.regex.Pattern;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.util.DBUtil;

public class StudentService {
	private StudentDAO studentDAO = StudentDAO.getInstance();
	// 비밀번호 영문 소문자 + 숫자, 6자 이상
	private static final Pattern PW_PATTERN =
	        Pattern.compile("^(?=.*[a-z])(?=.*\\d)[a-z\\d]{6,}$");

	public List<StudentMajorDTO> getMajors(String studentId) {
		return studentDAO.getMajors(studentId);
	}
	public String getStudentId(String studentNo) {
		return studentDAO.getStudentId(studentNo);
	}
	
	// 학생 정보 수정을 위한 조회
	public StudentDTO getStudentById(int studentId) throws Exception {
		try (Connection conn = DBUtil.getConnection()) {
            return studentDAO.selectById(conn, studentId);
        }
	}
	
	// 학생 일반 정보 수정
	public void updateStudentProfile(int studentId, String phone, String email,
	        						 String address, String studentBank) throws Exception {
		if (address == null || address.trim().isEmpty() || address.trim().length() > 255) {
		        throw new IllegalArgumentException("ADDR_RULE");
		}
		
		if (studentBank == null) {
		    throw new IllegalArgumentException("ACC_RULE");
		}
		
		String[] parts = studentBank.trim().split("\\s+", 2);
		if (parts.length < 2) {
		    throw new IllegalArgumentException("ACC_RULE");
		}
		
		String accountNo = parts[1].replaceAll("\\s+", "");
		if (!accountNo.matches("^\\d{3}-\\d{7}$")) {
		    throw new IllegalArgumentException("ACC_RULE");
		}
		
	    try (Connection conn = DBUtil.getConnection()) {
	        studentDAO.updateProfile(conn, studentId, phone, email, address, studentBank);
	    }
	}
	
	// 학생 비밀번호 수정
	public boolean changePassword(int studentId, String currentPw, String newPw) throws Exception {
	    if (newPw == null || !PW_PATTERN.matcher(newPw).matches()) {
	        throw new IllegalArgumentException("PW_RULE");
	    }
		
	    try (Connection conn = DBUtil.getConnection()) {
	        // 현재 비번이 맞는지 확인
	        String dbPw = studentDAO.selectPasswordById(conn, studentId);

	        if (dbPw == null) return false;
	        if (!dbPw.equals(currentPw)) return false;

	        // 맞으면 새 비번 업데이트
	        studentDAO.updatePassword(conn, studentId, newPw);
	        return true;
	    }
	}

	
}
