package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.util.DBUtil;

public class ProfessorDAO {
	private static final ProfessorDAO instance = new ProfessorDAO();

	private ProfessorDAO() {
	}

	public static ProfessorDAO getInstance() {
		return instance;
	}
	
	public int addProfessor(ProfessorDTO dto) {
	    String sql = "INSERT INTO professor(professor_name, professor_email, professor_password, major_id) "
	    		+ "VALUES(?, ?, ?, ?)";

	    int result = 0;
	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, dto.getProfessorName());
	        pstmt.setString(2, dto.getProfessorEmail());
	        pstmt.setString(3, dto.getProfessorPassword());
	        pstmt.setInt(4, dto.getMajorId()); 

	        result = pstmt.executeUpdate();
	    } catch (Exception e) {  // 부모 테이블에 해당 ID가 없는 경우 예외가 발생합니다.
	    	System.out.println("addProfessor() 예외발생: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return result;
	}

	public ProfessorDTO getProfessorByEmail(String professor_email) {
	    String sql = "SELECT * FROM professor WHERE professor_email = ?";
	    ProfessorDTO dto = null;

	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, professor_email);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                dto = new ProfessorDTO();
	                dto.setProfessorId(rs.getInt("professor_id"));
	                dto.setProfessorName(rs.getString("professor_name"));
	                dto.setProfessorEmail(rs.getString("professor_email"));
	                dto.setProfessorPassword(rs.getString("professor_password"));
	                dto.setProfessorStatus(rs.getString("professor_status"));
	                dto.setMajorId(rs.getInt("major_id")); 
	            } 
	        }
	    } catch (Exception e) {
	    	System.out.println("getProfessorByEmail() 예외발생: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return dto;
	}

	public boolean existProfessorByEmail(String professor_email) {
		boolean result = false;
		String sql = "SELECT COUNT(*) as cnt FROM professor " 
	               + "WHERE professor_email = ? ";
		
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, professor_email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("cnt");
					if (count > 0) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("existProfessorByEmail() 예외: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
		
	
}
