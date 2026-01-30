package com.goodeelms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dao.StatusHistoryDAO;
import com.goodeelms.dao.StudentStatusUpdateDAO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentStatusHistoryDTO;
import com.goodeelms.util.DBUtil;

public class StudentStatusService {
	StatusHistoryDAO historyDAO = StatusHistoryDAO.getInstance();
	StudentStatusUpdateDAO dao = StudentStatusUpdateDAO.getInstance();
	// 0119 임욱 / 재적 상태 이력 조회 
	public List<StudentStatusHistoryDTO> getStatusHistory(int studentId){
		return historyDAO.getStatusHistoryByStudentId(studentId);
	}
	
	// 검색 조건 없는 모든 학생 목록
	public ArrayList<StudentDTO> getAllStudentList() {		
		return dao.getAllStudentList();
	}
	
	// 검색 조건 통한 학생 목록 조회 서비스
	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		return dao.getStudentList(studentName, majorName, studentNo);
	}
	
	// student 테이블 status 변경 및 history 작성
	public int processStatusUpdate (String studentId, String newStudentStatus, String statusReason, int adminId) {
		Connection conn = null;
	    int result = 0;
	    
	    try {
	    	conn = DBUtil.getConnection();
	    	conn.setAutoCommit(false);
	    	
	    	// 2. 학생 상태 업데이트
	        int updateResult = dao.updateStudentStatus(conn, studentId, newStudentStatus);
	        
	        if (updateResult > 0) {
	            // 3. 이력 작성
	            result = dao.writeStatusHistory(conn, studentId, newStudentStatus, statusReason, adminId);
	            
	            if (result > 0) {
	                conn.commit();         // 4. 모두 성공 시 커밋
	            } else {
	                conn.rollback();       // 이력 작성 실패 시 롤백
	            }
	        } else {
	            conn.rollback();           // 업데이트 실패 시 롤백
	        }

	    } catch (Exception e) {
	        try {
	            if (conn != null) conn.rollback(); // 예외 발생 시 무조건 롤백
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        try {
	            if (conn != null) {
	                conn.setAutoCommit(true); // 커넥션 풀을 사용한다면 상태 원복
	                conn.close();             // 자원 반납
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return result;
	}
		// update 작업 정상 완료시 history 작성
		
	}

