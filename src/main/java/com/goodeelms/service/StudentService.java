package com.goodeelms.service;

import java.util.List;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentMajorDTO;

public class StudentService {
	private StudentDAO dao = StudentDAO.getInstance();
	public List<StudentMajorDTO> getMajors(String studentId) {
		return dao.getMajors(studentId);
	}
	
	public String getStudentId(String studentId) {
		return dao.getStudentId(studentId);
	}
}
