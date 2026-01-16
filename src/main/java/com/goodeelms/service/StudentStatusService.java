package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.StudentStatusUpdateDAO;
import com.goodeelms.dto.StudentDTO;

public class StudentStatusService {
	
	// 검색 조건 없는 모든 학생 목록
	public ArrayList<StudentDTO> getAllStudentList() {
		StudentStatusUpdateDAO dao = StudentStatusUpdateDAO.getInstance();
		return dao.getAllStudentList();
	}
	
	// 검색 조건 통한 학생 목록 조회 서비스
	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		StudentStatusUpdateDAO dao = StudentStatusUpdateDAO.getInstance();
		return dao.getStudentList(studentName, majorName, studentNo);
	}
	
	// 
	public int processStatusUpdate (String studentId, String newStudentStatus, String statusReason, String adminId) {
		StudentStatusUpdateDAO dao = StudentStatusUpdateDAO.getInstance();
		int updateResult = dao.updateStudentStatus(studentId, newStudentStatus);
		if (updateResult > 0) {
		return dao.writeStatusHistory(studentId, newStudentStatus, statusReason, adminId);
		}
		return 0;
	}
}
