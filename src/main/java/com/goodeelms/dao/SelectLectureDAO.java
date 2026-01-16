package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.util.DBUtil;

public class SelectLectureDAO {
	
	// 공통 sql 삽입 -> 개별 sql 조건문 삽입 -> 공통 동적 쿼리 반영 -> 개별 동적 쿼리 반영
	private int queryIndex = 1;
	
	public SelectLectureDAO() {
		
	}
	
	public List<LectureDTO> SelectLectureOnCartDuration(String cat, String searchWord, int viewPage, int viewLen, int ...majorIds){
		String sql = "SELECT  lecture_id, lecture_code, lecture_name, lecture_description, lecture_room, lecture_credit, lecture_type, "
				+ "lecture_capacity, major.major_name, pro.professor_name, lecture_current_people ";
		
		// 공통 쿼리 조건 삽입
		sql += getQueryString(sql, searchWord, cat, majorIds);

		sql += "ORDER BY lecture_id DESC LIMIT ? OFFSET ?";
		
		try(PreparedStatement pstmt = getPrepareStatement(sql, searchWord, cat, majorIds)){
			// Limit, Offset
			pstmt.setInt(queryIndex++, viewLen);
			pstmt.setInt(queryIndex++, (viewPage - 1) * viewLen);
			
			try(ResultSet rs = pstmt.executeQuery();){
				List<LectureDTO> list = new ArrayList<LectureDTO>();
				while(rs.next()) {
					LectureDTO dto = new LectureDTO();
					dto.setLectureId(Integer.parseInt(rs.getString("lecture_id")));
					dto.setLectureCode(Integer.parseInt(rs.getString("lecture_code")));
					dto.setLectureName(rs.getString("lecture_name"));
					dto.setLectureDescription(rs.getString("lecture_description"));
					dto.setLectureRoom(rs.getString("lecture_room"));
					dto.setLectureCredit(Integer.parseInt(rs.getString("lecture_credit")));
					dto.setLectureType(rs.getString("lecture_type"));
					dto.setLectureCurrentPeople(Integer.parseInt(rs.getString("lecture_current_people")));
					dto.setLectureCapacity(Integer.parseInt(rs.getString("lecture_capacity")));
					dto.setMajorName(rs.getString("major_name"));
					dto.setProfessorName(rs.getString("professor_name"));
					list.add(dto);
				}
				return list;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getLectureListOnCartCount(String cat, String searchWord, int ...majorIds) {
		String sql = "SELECT COUNT(*) as cnt ";

		// 공통 쿼리 조건 삽입
		sql += getQueryString(sql, searchWord, cat, majorIds);
		
		try(PreparedStatement pstmt = getPrepareStatement(sql, searchWord, cat, majorIds)){

			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					return rs.getInt("cnt");
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public String getQueryString(String sql, String searchWord, String cat, int ...majorIds) {
		// 공통 조건
		StringBuilder sb = new StringBuilder();
		sb.append("FROM lecture AS lec ");
		sb.append("JOIN major ON lec.major_id = major.major_id ");
		sb.append("JOIN PROFESSOR AS pro ON lec.PROFESSOR_ID = pro.PROFESSOR_ID ");
		sb.append("WHERE lecture_semester = ? ");
		sb.append("AND lecture_year = ? ");
		
		if(!"all".equals(cat)) sb.append("AND lecture_type = ? ");
		if(!"liberal".equals(cat) && !"all".equals(cat)) sb.append("AND lec.major_id = ? ");	// 학생 major_id를 어디서 받아야함
		// else if("all".equals(cat) && majorIds.length > 1) sb.append("AND (lec.major_id = ? OR lec.major_id = ?) ");
		if(searchWord != null && !searchWord.isBlank()) sb.append("AND (lec.lecture_name LIKE ? OR pro.professor_name LIKE ?)");
		
		
		return sb.toString();
	}
	
	public PreparedStatement getPrepareStatement(String sql, String searchWord, String cat, int ...majorIds) throws SQLException{
		queryIndex = 1;
		// 날짜
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dateTime.format(formatter);
		String[] rawSemester = date.split("-");
		// 학기
		int semester = 0;
		if(Integer.parseInt(rawSemester[1]) < 9) semester += 1;
		else semester += 2;
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(queryIndex++, semester);
		pstmt.setString(queryIndex++, rawSemester[0]);
		if(!"all".equals(cat)) {
			if(!"liberal".equals(cat)) pstmt.setString(queryIndex, "전공");
			else pstmt.setString(queryIndex, "교양");
			queryIndex++;
		}
		if("major".equals(cat) || majorIds.length == 1) pstmt.setInt(queryIndex++, majorIds[0]);
		else if("minor".equals(cat)) pstmt.setInt(queryIndex++, majorIds[1]);
		// if("all".equals(cat) && majorIds.length > 1) pstmt.setInt(queryIndex++, majorIds[1]);
		if(searchWord != null && !searchWord.isBlank()) {
			pstmt.setString(queryIndex++, "%" + searchWord.trim() + "%");
			pstmt.setString(queryIndex++, "%" + searchWord.trim() + "%");
		}
		
		return pstmt;
	}
}
