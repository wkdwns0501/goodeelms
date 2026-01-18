package com.goodeelms.service;

import java.util.List;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.util.EncryptUtil;
import com.goodeelms.util.GenderUtil;

public class StudentService {
	private StudentDAO dao = StudentDAO.getInstance();
	public List<StudentMajorDTO> getMajors(String studentId) {
		return dao.getMajors(studentId);
	}
	
	public String getStudentId(String studentNo) {
		return dao.getStudentId(studentNo);
	}
	
	// 0118 임욱 추가 - 학생 정보를 업데이트 (학생 정보 수정, 초기 로그인 경우에 사용)
	public boolean updateStudent(StudentDTO studentDTO) {
		if(dao.existsStudentUniqueColumn(studentDTO)) {
			return false; 	// DB에 동일한 UNIQUE 컬럼이 존해할 경우 early return
		}
		
		studentDTO.setStudentGender(GenderUtil.getGenderByIdentityNumber(studentDTO.getStudentIdentityNumber())); // 주민번호에 따른 자동 성별 설정
	    studentDTO.setStudentPassword(EncryptUtil.encryptPassword(studentDTO.getStudentPassword())); // 비밀번호 암호화 후 저장
		
		return dao.updateStudent(studentDTO);
	}
	
	// 0118 임욱 추가 - 노출하지 않을 비밀번호를 제외한 정보를 획득
	public StudentDTO getStudentByNo(StudentDTO studentDTO) {
		StudentDTO dto = dao.getStudentByNo(studentDTO.getStudentNo());
		
		dto.setStudentPassword("");
		
		return dto;
	}
	
}
