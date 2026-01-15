package com.goodeelms.service;

import java.util.List;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentMajorDTO;

public class StudentService {
	public List<StudentMajorDTO> getMajors(String studentId) {
		StudentDAO dao = new StudentDAO();
		return dao.getMajors(studentId);
	}
	
	public String getStudentId(String studentNo) {
		StudentDAO dao = new StudentDAO();
		return dao.getStudentId(studentNo);
	}
}
