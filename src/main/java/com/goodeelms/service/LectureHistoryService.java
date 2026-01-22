package com.goodeelms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.goodeelms.dao.LectureDAO;
import com.goodeelms.dao.LectureHistoryDAO;
import com.goodeelms.dto.LectureHistoryDTO;
import com.goodeelms.util.DBUtil;

public class LectureHistoryService {
	LectureDAO lecDAO = LectureDAO.getInstance();
	LectureHistoryDAO historyDAO = LectureHistoryDAO.getInstance();
	
	// 수강 강의에 해당 강의가 있는지 COUNT로 확인
	public int searchLectureIdOfHistory(String student_id, String lecture_id) {
		return historyDAO.searchLectureIdOfHistory(student_id, lecture_id);
	}
	
	public int insertNewLecutreToHistory(String student_id, String lecture_id) {
		
		Connection conn = DBUtil.getConnection();
		try {
			// lecture_history 등록
			int insertResult = historyDAO.insertNewLectureToHistory(conn, student_id, lecture_id);
			if(insertResult < 1) throw new SQLException("INSERT lecture_history NEW lecture failed");
			// lecture_current_people 값 변경
			int updateResult = lecDAO.updateLectureCurrentPeople(conn, lecture_id);
			if(updateResult < 1) throw new SQLException("UPDATE lecture_current_people failed");
			
			return 1;
		}
		catch (SQLException e) {
			if(conn != null) {
				try {
					conn.rollback();
				}
				catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}
}
