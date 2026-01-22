package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.dto.StudentStatusHistoryDTO;
import com.goodeelms.util.DBUtil;

public class StatusHistoryDAO {
	private static final StatusHistoryDAO instance = new StatusHistoryDAO();

	private StatusHistoryDAO() {
	}

	public static StatusHistoryDAO getInstance() {
		return instance;
	}

	public List<StudentStatusHistoryDTO> getStatusHistoryByStudentId(int studentId) {
		String sql = "select * from student_status_history WHERE student_id = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			
			try (ResultSet rs = pstmt.executeQuery()) {  
				List<StudentStatusHistoryDTO> list = new ArrayList<StudentStatusHistoryDTO>();
				
				while (rs.next()) {
					StudentStatusHistoryDTO dto = new StudentStatusHistoryDTO();
					dto.setStatusReason(rs.getString("status_reason"));
					dto.setStatusType(rs.getString("status_type"));
					dto.setStatusAt(rs.getObject("status_at", LocalDateTime.class));
					list.add(dto);
				}

				return list;
			} catch (Exception e) {
				System.out.println("getStatusHistoryByStudentId() 쿼리 실행 예외: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getStatusHistoryByStudentId() DB 연결 예외: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
