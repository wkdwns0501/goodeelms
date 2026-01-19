package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.ChangeMajorHistoryDTO;
import com.goodeelms.dto.StudentStatusHistoryDTO;
import com.goodeelms.util.DBUtil;

public class ChangedMajorDAO {
	private static final ChangedMajorDAO instance = new ChangedMajorDAO();

	private ChangedMajorDAO() {
	}

	public static ChangedMajorDAO getInstance() {
		return instance;
	}
	
	public ChangeMajorHistoryDTO getChangeMajorHistoryById(int studentId) {
		String sql = "select * from change_major_history WHERE student_id = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
		
			change_major_id INT AUTO_INCREMENT PRIMARY KEY,
			changed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
			student_id INT NOT NULL,
			
			
			ChangeMajorHistoryDTO dto = null;
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					dto = new ChangeMajorHistoryDTO();
					dto.  (rs.getString("change_major_id"));
					dto.(rs.getString("status_reason"));
					dto.(rs.getObject("status_at", LocalDateTime.class));

				}
				return dto;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
		
		
	}
	
	
}
