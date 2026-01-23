package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodeelms.dto.PreEnrollmentDTO;
import com.goodeelms.util.DBUtil;

public class LectureCartDAO {
	private static final LectureCartDAO instance = new LectureCartDAO();

	public static LectureCartDAO getInstance() {
		return instance;
	}
	
	public int insertLectureOnCart(String lectureId, String studentId) {
		String sql = "INSERT INTO pre_enrollment(lecture_id, student_id, pre_enrollment_status) VALUES(?, ?, ?)";
		
		int index = 1;
		try(PreparedStatement pstmt = getPrepare(sql)){
			pstmt.setString(index++, lectureId);
			pstmt.setString(index++, studentId);
			pstmt.setString(index++, "progress");
			
			int result = pstmt.executeUpdate();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int deleteLectureOnCart(String lectureId, String studentId) {
		String sql = "DELETE FROM pre_enrollment WHERE lecture_id = ? AND student_id = ?";
		
		try(PreparedStatement pstmt = getPrepare(sql)){
			int index = 1;
			pstmt.setString(index++, lectureId);
			pstmt.setString(index++, studentId);
			
			int result = pstmt.executeUpdate();
			return result;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int simpleSearchCart(String lectureId, String studentId) {
		String sql = "SELECT COUNT(*) as cnt FROM pre_enrollment WHERE lecture_id = ? AND student_id = ?";
		
		int index = 1;
		try(PreparedStatement pstmt = getPrepare(sql)){
			pstmt.setString(index++, lectureId);
			pstmt.setString(index++, studentId);
			
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {;
					return Integer.parseInt(rs.getString("cnt"));
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public List<PreEnrollmentDTO> getPreEnrollment(int studentId, Set<String> conditions){
		String sql = "SELECT lecture_id, pre_enrollment_status FROM pre_enrollment WHERE student_id = ? ";
		if(conditions.size() > 0) {
			sql += "AND pre_enrollment_status IN ("
					+ conditions.stream().map(condition -> "?").collect(Collectors.joining(","))+ ")";
		}
		try(PreparedStatement pstmt = getPrepare(sql)){
			int index = 1;
			pstmt.setInt(index++, studentId);
			if(conditions.size() > 0) {
				for(String condition : conditions) {
					pstmt.setString(index++, condition);
				}
			}
			
			try(ResultSet rs = pstmt.executeQuery()){
				List<PreEnrollmentDTO> list = new ArrayList<PreEnrollmentDTO>();
				while(rs.next()) {
					PreEnrollmentDTO dto = new PreEnrollmentDTO();
					dto.setLectureId(Integer.parseInt(rs.getString("lecture_id")));
					dto.setPreEnrollmentStatus(rs.getString("pre_enrollment_status"));
					
					list.add(dto);
				}
				return list;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int clearCart(String studentId, Set<Integer> lectureIds) throws NullPointerException{
		String sql ="DELETE FROM pre_enrollment WHERE student_id = ? AND pre_enrollment_status = 'progress' ";
		if(lectureIds.size() > 0) {
			sql += "AND lecture_id IN(" + lectureIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";
		}
		else {
			throw new NullPointerException("이거 말 안된다.");
		}
		try(PreparedStatement pstmt = getPrepare(sql)){
			int index = 1;
			pstmt.setString(index++, studentId);
			for(int id : lectureIds) {
				pstmt.setInt(index++, id);
			}
			
			int result = pstmt.executeUpdate();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<PreEnrollmentDTO> checkProgressCart(){
		String sql = "SELECT l.lecture_id as lecture_id, lecture_capacity, count(p.lecture_id) as enroll_count "
				+ "FROM pre_enrollment as p "
				+ "JOIN lecture as l ON p.lecture_id = l.lecture_id "
				+ "WHERE pre_enrollment_status = 'progress' "
				+ "GROUP BY l.lecture_id, l.lecture_capacity";
		
		try(PreparedStatement pstmt = getPrepare(sql);){
			
			try(ResultSet rs = pstmt.executeQuery();){
				List<PreEnrollmentDTO> list = new ArrayList<PreEnrollmentDTO>();
				while(rs.next()) {
					PreEnrollmentDTO dto = new PreEnrollmentDTO();
					dto.setLectureId(rs.getInt("lecture_id"));
					dto.setLectureCapacity(rs.getInt("lecture_capacity"));
					dto.setLectureEnrollCount(rs.getInt("enroll_count"));
					
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
	
	public int UpdatePreEnrollmentStatus(Connection conn , Set<Integer> updateSet, String updateStatus) {
		String sql = "UPDATE pre_enrollment SET pre_enrollment_status = ? WHERE lecture_id IN("
				+ updateSet.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";
		
		System.out.println(updateSet);
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			int index = 1;
			pstmt.setString(index++, updateStatus);
			for(Integer id : updateSet) {
				pstmt.setInt(index++, id);
			}
			
			int result = pstmt.executeUpdate();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updatePreEnrollmentStateOne(Connection conn, String status, String lectureId, String studentId) {
		String sql = "UPDATE pre_enrollment SET pre_enrollment_status = ? WHERE lecture_id = ? AND student_id = ?";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			int index = 1; 
			pstmt.setString(index++, status);
			pstmt.setString(index++, lectureId);
			pstmt.setString(index++, studentId);
			
			int result = pstmt.executeUpdate();
			return result;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int UpdateLectureCurrentPeople(Connection conn, Map<Integer, Integer> lecMap) {
		String sql = "UPDATE lecture SET lecture_current_people = ? WHERE lecture_id = ?";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			if(lecMap.size() == 0) return 0;
			
			
			for(Map.Entry<Integer, Integer> entry : lecMap.entrySet()) {
				pstmt.setInt(1, entry.getValue());
				pstmt.setInt(2, entry.getKey());
				
				pstmt.addBatch();
			}
			
			int[] results = pstmt.executeBatch();
			for(int i : results) {
				if(i == 0) return 0;
			}
			return 1;
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private PreparedStatement getPrepare(String sql) throws SQLException {
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		return pstmt;
	}
	
	
}
