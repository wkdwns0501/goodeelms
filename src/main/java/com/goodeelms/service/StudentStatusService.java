package com.goodeelms.service;

import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dao.StatusHistoryDAO;
import com.goodeelms.dao.StudentStatusUpdateDAO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentStatusHistoryDTO;

public class StudentStatusService {
	StatusHistoryDAO historyDAO = StatusHistoryDAO.getInstance();
	
	// 0119 임욱 / 재적 상태 이력 조회 
	public List<StudentStatusHistoryDTO> getStatusHistory(int studentId){
		return historyDAO.getStatusHistoryByStudentId(studentId);
	}
	
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
	
	// student 테이블 status 변경 및 history 작성
	public int processStatusUpdate (String studentId, String newStudentStatus, String statusReason, int adminId) {
		StudentStatusUpdateDAO dao = StudentStatusUpdateDAO.getInstance();
		int updateResult = dao.updateStudentStatus(studentId, newStudentStatus);
		
		// update 작업 정상 완료시 history 작성
		if (updateResult > 0) {
		return dao.writeStatusHistory(studentId, newStudentStatus, statusReason, adminId);
		}
		return 0;
	}
}
