package com.goodeelms.service;

import java.util.List;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.util.EncryptUtil;

public class StudentService {
	private StudentDAO dao = StudentDAO.getInstance();
	public List<StudentMajorDTO> getMajors(String studentId) {
		return dao.getMajors(studentId);
	}
	
	public String getStudentId(String studentNo) {
		return dao.getStudentId(studentNo);
	}
	
	// 0118 임욱 추가 - 입력받은 정보로 학생 상태 업데이트
	public Boolean updateStudent(StudentDTO studentDTO) {
		if(dao.existsStudentUniqueColumn(studentDTO)) {
			return false; 	// DB에 동일한 UNIQUE 컬럼이 존해할 경우 early return
		}
	
	    studentDTO.setStudentPassword(EncryptUtil.encryptPassword(studentDTO.getStudentPassword())); // 비밀번호 암호화 후 저장
		
		return dao.updateStudent(studentDTO);
	}
}
