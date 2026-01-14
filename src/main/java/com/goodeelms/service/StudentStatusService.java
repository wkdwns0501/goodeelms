package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.StudentStatusUpdateDAO;
import com.goodeelms.dto.StudentDTO;

public class StudentStatusService {
	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		StudentStatusUpdateDAO dao = StudentStatusUpdateDAO.getInstance();
		return dao.getStudentList(studentName, majorName, studentNo);
	}
	
	public int processStatusUpdate (String studentId, String studentNo, String newStudentStatus, String statusReason, String adminId) {
		StudentStatusUpdateDAO dao = StudentStatusUpdateDAO.getInstance();
		int updateResult = dao.updateStudentStatus(studentId, newStudentStatus);
		if (updateResult > 0) {
		return dao.writeStatusHistory(studentId, studentNo, newStudentStatus, statusReason, adminId);
		}
		return 0;
	}
}
