package com.goodeelms.service;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentDTO;

public class StudentSignUpService {

	public int signUpStudent(StudentDTO dto, String orgin_student_password) {
		StudentDAO dao = StudentDAO.getInstance();
		
		if(dto.getStudentPassword().equals(orgin_student_password)) {
			System.out.println("서로 다른 비밀번호를 입력하세요.");
			return -1;
		}
		
		return dao.updateStudent(dto, orgin_student_password);
	}
}
