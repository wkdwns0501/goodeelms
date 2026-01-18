package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.goodeelms.dto.AdminDTO;
import com.goodeelms.util.DBUtil;

public class AdminDAO {
	private static final AdminDAO instance = new AdminDAO();

	private AdminDAO() {
	}

	public static AdminDAO getInstance() {
		return instance;
	}
	
	public AdminDTO getAdminBylogId(String log_id) {
		String sql = "SELECT * FROM admin WHERE admin_log_id = ?";
		AdminDTO dto = null;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, log_id);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					dto = new AdminDTO();

					dto.setAdminId(rs.getInt("admin_id"));
					dto.setAdminLoginId(rs.getString("admin_log_id"));
					dto.setAdminName(rs.getString("admin_name"));
					dto.setAdminPassword(rs.getString("admin_password"));
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
}
