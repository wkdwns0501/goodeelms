package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import com.goodeelms.dto.ChangeMajorHistoryDTO;
import com.goodeelms.util.DBUtil;

public class ChangedMajorDAO {
	private static final ChangedMajorDAO instance = new ChangedMajorDAO();

	private ChangedMajorDAO() {
	}

	public static ChangedMajorDAO getInstance() {
		return instance;
	}

	// 0120 임욱 / studentId로 이전 학과 이름, 현재 학과 이름 조회
	public ChangeMajorHistoryDTO getChangeMajorHistoryNameByStudentId(int studentId) { 
		String sql = "SELECT CHM.change_major_id, CHM.student_id, CHM.changed_at, "
				+ "M1.major_name AS 'beforeMajor', M2.major_name AS 'currentMajor' "
				+ "FROM change_major_history CHM "
				+ "JOIN major M1 ON CHM.from_major_id = M1.major_id "
				+ "JOIN major M2 ON CHM.to_major_id = M2.major_id " 
				+ "WHERE CHM.student_id = ? ";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);

			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					ChangeMajorHistoryDTO dto = new ChangeMajorHistoryDTO();
					dto.setFromMajorName(rs.getString("beforeMajor"));
					dto.setToMajorName(rs.getString("currentMajor"));
					dto.setChangedAt(rs.getObject("changed_at", LocalDateTime.class));
					return dto;
				}
				return null;
			} catch (Exception e) {
				System.out.println("getChangeMajorHistoryAndNameByStudentId() 쿼리 예외발생: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getChangeMajorHistoryAndNameByStudentId() 예외발생: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
