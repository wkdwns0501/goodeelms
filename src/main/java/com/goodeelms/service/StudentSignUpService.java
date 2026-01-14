package com.goodeelms.service;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentDTO;

public class StudentSignUpService {

	public void signUpStudent(String login_student_no, String student_no, String student_password, String student_name, String student_phone, 
			String student_identity_number, String student_gender, String student_address, String student_email, String student_bank) {

		StudentDAO dao = new StudentDAO();
		dao.addStudent(login_student_no, student_no, student_password, student_name, student_phone, 
				student_identity_number, student_gender, student_address, student_email, student_bank);
		
	}
}
