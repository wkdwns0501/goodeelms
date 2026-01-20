package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodeelms.dto.MajorDTO;
import com.goodeelms.util.DBUtil;

public class MajorDAO {
	private static final MajorDAO instance = new MajorDAO();

	public static MajorDAO getInstance() {
		return instance;
	}

	public List<MajorDTO> getTargetMajor(Set<Integer> majorIdSet) {
		String sql = "SELECT * FROM major WHERE major_id IN ("
				+ majorIdSet.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			int index = 1;
			for (Integer id : majorIdSet) {
				pstmt.setInt(index++, id);
			}

			try (ResultSet rs = pstmt.executeQuery()) {
				List<MajorDTO> list = new ArrayList<MajorDTO>();
				while (rs.next()) {
					MajorDTO dto = new MajorDTO();
					dto.setMajorId(Integer.parseInt(rs.getString("major_id")));
					dto.setMajorCode(rs.getString("major_code"));
					dto.setMajorName(rs.getString("major_name"));

					list.add(dto);
				}
				return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 0119 임욱 / 전체 전공 조회
	public List<MajorDTO> findAll() {
		String sql = "SELECT * FROM major";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			try (ResultSet rs = pstmt.executeQuery()) {
				List<MajorDTO> list = new ArrayList<MajorDTO>();
				while (rs.next()) {
					MajorDTO dto = new MajorDTO();
					dto.setMajorId(Integer.parseInt(rs.getString("major_id")));
					dto.setMajorCode(rs.getString("major_code"));
					dto.setMajorName(rs.getString("major_name"));

					list.add(dto);
				}
				return list;
			}
		} catch (Exception e) {
			System.out.println("findAll() 예외 발생: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	// 0120 임욱(추가) / student_id 해당하는 전공, 전공이름 
	public List<String> getCodeAndNameByStudentId(int studentId){
		String sql = "SELECT m.major_code, m.major_name FROM major M "
				+ "JOIN student_major SM ON M.major_id = sm.major_id "
				+ "WHERE student_id = ? ";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("major_code"));
					list.add(rs.getString("major_name"));
				}
				return list;
			} catch(Exception e) {
				System.out.println("getCodeAndNameByStudentId() 쿼리 예외 발생: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getCodeAndNameByStudentId() 예외 발생: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
}
