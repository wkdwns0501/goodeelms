package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.util.DBUtil;

public class ProfessorManageDAO {
	
	private static final ProfessorManageDAO instance = new ProfessorManageDAO();
	
	private ProfessorManageDAO() {
	}
	
	public static ProfessorManageDAO getInstance() {
		return instance;
	}
	
	// 교수 전체 목록 조회
	public ArrayList<ProfessorDTO> getAllProfessorList() {
		String sql = "SELECT professor_id, professor_name, m.major_name, professor_email, professor_status " +
					 "FROM professor p JOIN major m ON p.major_id = m.major_id " +
					 "ORDER BY m.major_name, professor_name";
		
		ArrayList<ProfessorDTO> list = new ArrayList<ProfessorDTO>();
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			try(ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					ProfessorDTO professorDTO = new ProfessorDTO();
					professorDTO.setProfessorId(rs.getInt("professor_id"));
					professorDTO.setProfessorName(rs.getString("professor_name"));
					professorDTO.setMajorName(rs.getString("m.major_name"));
					professorDTO.setProfessorEmail(rs.getString("professor_email"));
					professorDTO.setProfessorStatus(rs.getString("professor_status"));
					list.add(professorDTO);
				}
			}			
		} catch (Exception e) {
			System.out.println("getAllProfessorList() 예외 발생: " + e);
		} return list;
	}
	
	// 검색 조건에 따른 교수 목록 조회(학생 학적 변경에서의 로직과 동일)
	public ArrayList<ProfessorDTO> getProfessorList(String professorName, String majorName, String professorEmail) {
		String sql = "SELECT professor_id, professor_name, professor_email, professor_status, m.major_name " +
					 "FROM professor p JOIN major m ON p.major_id = m.major_id " +
					 "WHERE 1=1 ";
		
		boolean isName = false;
		boolean isMajor = false;
		boolean isEmail = false;
		
		if(professorName != null && !professorName.trim().isEmpty()) {
			sql += "AND p.professor_name LIKE ? ";
			isName = true;
		} 
		if (majorName != null && !majorName.trim().isEmpty()) {
			sql += "AND m.major_name LIKE ? ";
			isMajor = true;
		} 
		if (professorEmail != null && !professorEmail.trim().isEmpty()) {
			sql += "AND p.professor_email LIKE ? ";
			isEmail = true;
		}
		
			sql += "ORDER BY m.major_name, professor_name ";
		
		ArrayList<ProfessorDTO> list = new ArrayList<ProfessorDTO>();
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {	 
			
			int index = 1;
			if(isName) {
				pstmt.setString(index++, "%" + professorName + "%");
			}
			if(isMajor) {
				pstmt.setString(index++, "%" + majorName + "%");
			}
			if(isEmail) {
				pstmt.setString(index++, "%" + professorEmail + "%");
			}
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					ProfessorDTO professorDTO = new ProfessorDTO();
					professorDTO.setProfessorId(rs.getInt("professor_id"));
					professorDTO.setProfessorName(rs.getString("professor_name"));
					professorDTO.setMajorName(rs.getString("m.major_name"));
					professorDTO.setProfessorEmail(rs.getString("professor_email"));
					professorDTO.setProfessorStatus(rs.getString("professor_status"));
					list.add(professorDTO);
				}
			}
		} catch (Exception e) {
			System.out.println("getProfessorList() 예외 발생: " + e);
		} return list;
	}
	
	// professor 테이블의 status 변경
	public int updateProfessorStatus(String professorId, String newProfessorStatus) {
		String sql = "UPDATE professor SET professor_status = ? WHERE professor_id = ? ";
		
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {	
			pstmt.setString(1, newProfessorStatus);
			pstmt.setInt(2, Integer.parseInt(professorId));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateProfessorStatus() 예외 발생: " + e);
			return 0;
		} 
	}


}
	
	
	
	
	



