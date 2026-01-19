package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.util.DBUtil;

public class ProfessorDAO {
	private static final ProfessorDAO instance = new ProfessorDAO();

	private ProfessorDAO() {
	}

	public static ProfessorDAO getInstance() {
		return instance;
	}

	public ProfessorDTO checkProfessor(String professor_email, String professor_password) {
		String sql = "SELECT * FROM professor WHERE professor_email = ? AND professor_password = ? ";

		ProfessorDTO dto = null;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, professor_email);
			pstmt.setString(2, professor_password);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					dto = new ProfessorDTO();

					dto.setProfessorId(rs.getInt("professor_id"));
					dto.setProfessorName(rs.getString("professor_name"));
					dto.setProfessorEmail(rs.getString("professor_email"));
					dto.setProfessorPassword(rs.getString("professor_password"));
					dto.setProfessorStatus(rs.getString("professor_status"));
					
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	// 이름으로 ID 찾기 -> 검색어
	// 동명이인 때문에 LIST
	public List<ProfessorDTO> getProfessorIdsFromName(String name) {
		String sql = "SELECT * FROM professor WHERE professor_name LIKE ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, "%" + name.trim() + "%");
			try(ResultSet rs = pstmt.executeQuery()){
				List<ProfessorDTO> list = new ArrayList<ProfessorDTO>();
				while(rs.next()) {
					ProfessorDTO dto = new ProfessorDTO();
					dto.setProfessorId(rs.getInt("professor_id"));
					dto.setProfessorName(rs.getString("professor_name"));
					
					list.add(dto);
				}
				return list;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<ProfessorDTO> getTargetProfessor(Set<Integer> professorIdSet){
		String sql = "SELECT * FROM professor WHERE professor_id IN ("
				+ professorIdSet.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			int index = 1;
			for(Integer id : professorIdSet) {
				pstmt.setInt(index++, id);
			}
			
			try(ResultSet rs = pstmt.executeQuery()){
				List<ProfessorDTO> list = new ArrayList<ProfessorDTO>();
				while(rs.next()) {
					ProfessorDTO dto = new ProfessorDTO();
					dto.setProfessorId(rs.getInt("professor_id"));
					dto.setProfessorName(rs.getString("professor_name"));
					dto.setProfessorEmail(rs.getString("professor_email"));
					dto.setProfessorPassword(rs.getString("professor_password"));
					dto.setProfessorStatus(rs.getString("professor_status"));
					
					list.add(dto);
				}
				return list;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public int addProfessor(ProfessorDTO dto) {
		String sql = "INSERT INTO professor(professor_name, professor_email, professor_password, professor_status) VALUES( ?, ?, ?, ?)";

		int result = 0;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, dto.getProfessorName());
			pstmt.setString(2, dto.getProfessorEmail());
			pstmt.setString(3, dto.getProfessorPassword());
			pstmt.setString(4, dto.getProfessorStatus());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isEmailExist(String email) {
		String sql = "SELECT COUNT(*) as cnt FROM professor WHERE professor_email = ?";
		boolean result = false;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("cnt");
					if (count > 0) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
