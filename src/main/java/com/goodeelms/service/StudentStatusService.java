package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.StudentStatusUpdateDAO;
import com.goodeelms.dto.StudentDTO;

public class StudentStatusService {
	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		StudentStatusUpdateDAO dao = StudentStatusUpdateDAO.getInstance();
		return dao.getStudentList(studentName, majorName, studentNo);
	}
}
