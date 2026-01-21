
package com.goodeelms.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.goodeelms.dao.ChangedMajorDAO;
import com.goodeelms.dao.LectureHistoryDAO;
import com.goodeelms.dao.ScholarshipDAO;
import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.ChangeMajorHistoryDTO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.ScholarshipDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.util.EncryptUtil;
import com.goodeelms.util.GenderUtil;
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

	public boolean updateStudent(StudentDTO studentDTO) { 	// 0118 임욱 추가 - 학생 정보를 업데이트 (학생 정보 수정, 초기 로그인 경우에 사용)
		if(studentDAO.existsStudentUniqueColumn(studentDTO)) return false; 	
		
		studentDTO.setStudentGender(GenderUtil.getGenderByIdentityNumber(studentDTO.getStudentIdentityNumber())); // 주민번호에 따른 자동 성별 설정
	    studentDTO.setStudentPassword(EncryptUtil.encryptPassword(studentDTO.getStudentPassword())); // 비밀번호 암호화 후 저장
		
		return studentDAO.updateStudent(studentDTO);
	}
	

	public StudentDTO getStudentByNo(StudentDTO studentDTO) { 	// 0118 임욱 추가 - 노출하지 않을 비밀번호를 제외한 정보를 획득
		StudentDTO dto = studentDAO.getStudentByNo(studentDTO.getStudentNo());
		dto.setStudentPassword("");
		return dto;
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
	
	
	public Map<Integer, LectureDTO> getProgressInfoByStudentId(int studentId){ // 0120 임욱(추가) / 수강 중인 강의 정보 정회
		return LectureHistoryDAO.getInstance().getProgressInfoByStudentId(studentId);
	}
	
	public Map<Integer, LectureDTO> getlectureAndLectureScoreByStudentId(int studentId){ // 0120 임욱(추가) / 수강 중인 강의 점수 조회
		return LectureHistoryDAO.getInstance().getLectureHistoryByStudentId(studentId);
	}

	public ChangeMajorHistoryDTO getChangedMajorHistory(int studentId) { // 0120 임욱(추가) / 학생 전과 이력 조회
		return ChangedMajorDAO.getInstance().getChangeMajorHistoryNameByStudentId(studentId);
	}

	public List<ScholarshipDTO> getScholarshipByStudentId(int studentId){ // 0120 임욱(추가) / 학생 장학 정보 조회
		return ScholarshipDAO.getInstance().getSemesterAndAmountByStudentId(studentId);
	}
	
	public List<LectureDTO> getProbationByStudentId(int studentId){ // 0120 임욱(추가) / 학사경고(평균2.0이하) 조회
		return LectureHistoryDAO.getInstance().getProbationByStudentId(studentId);
	}
	
	public StudentDTO getIdentityNumAndNo(int studentId) {
		StudentDTO student = studentDAO.getIdentityNumAndNo(studentId);
		if (student == null) {
			System.out.println("studentService 예외 발생 (값이 null)");
			return null;
		} else {
			return student;
		}
	}
	
}
