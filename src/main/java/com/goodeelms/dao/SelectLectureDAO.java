package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.listener.LMSScheduleListener;
import com.goodeelms.util.DBUtil;
import com.goodeelms.util.StaticUtils;

public class SelectLectureDAO {
	
	// 공통 sql 삽입 -> 개별 sql 조건문 삽입 -> 공통 동적 쿼리 반영 -> 개별 동적 쿼리 반영
	private int queryIndex = 1;
	
	public SelectLectureDAO() {
		
	}
	
	public List<LectureDTO> SelectLectures(String cat, String searchWord, int viewPage, int viewLen, Set<Integer> lectureCodeSet, Set<Integer> professorIdSet, int ...majorIds){
		String sql = "SELECT  lecture_id, lecture_code, lecture_name, lecture_description, lecture_room, lecture_credit, lecture_type, "
				+ "lecture_capacity, major_id, professor_id, lecture_current_people ";
		
		// 쿼리 조건 삽입
		sql += getQueryString(searchWord, cat, lectureCodeSet, professorIdSet);

		sql += "ORDER BY lecture_id DESC LIMIT ? OFFSET ?";
		
		try(PreparedStatement pstmt = getPrepareStatement(sql, searchWord, cat, lectureCodeSet, professorIdSet, majorIds)){
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
					dto.setMajorId(rs.getInt("major_id"));
					dto.setProfessorId(rs.getInt("professor_id"));
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
	
	public int getLectureListOnCartCount(String cat, String searchWord, Set<Integer> lectureCodeSet, Set<Integer> professorIdSet, int ...majorIds) {
		String sql = "SELECT COUNT(*) as cnt ";

		// 공통 쿼리 조건 삽입
		sql += getQueryString(searchWord, cat, lectureCodeSet, professorIdSet);
		
		try(PreparedStatement pstmt = getPrepareStatement(sql, searchWord, cat, lectureCodeSet, professorIdSet, majorIds)){

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
	
	public List<LectureDTO> getLecturesOfStudent(Set<Integer> lectureIdSet){
		String sql = "SELECT * FROM lecture WHERE lecture_id IN (" +
				lectureIdSet.stream().map(id -> "?").collect(Collectors.joining(",")) + ") " +
				"AND lecture_year = ? AND lecture_semester = ?"
				+ " ORDER BY lecture_name";
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			int index = 1;
			for(Integer id : lectureIdSet) {
				pstmt.setInt(index++, id);
			}
			LocalDateTime year = LocalDateTime.now();
			DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String dateString = year.format(fomatter);
			String[] splits = dateString.split("-");
			
			int semester = 1;
			try {
				if(Integer.parseInt(splits[1]) > 8) semester = 2;
			}
			catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			pstmt.setString(index++, splits[0]);
			pstmt.setInt(index++, semester);
			
			try(ResultSet rs = pstmt.executeQuery()){
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
					dto.setMajorId(rs.getInt("major_id"));
					dto.setProfessorId(rs.getInt("professor_id"));
					
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
	
	public String getQueryString(String searchWord, String cat, Set<Integer> lectureCodeSet, Set<Integer> professorIdSet) {
		// 공통 조건
		StringBuilder sb = new StringBuilder();
		sb.append("FROM lecture ");
		sb.append("WHERE lecture_semester = ? ");
		sb.append("AND lecture_year = ? ");
		
		if(!"all".equals(cat)) sb.append("AND lecture_type = ? ");
		if(!"liberal".equals(cat) && !"all".equals(cat)) sb.append("AND major_id = ? ");	// 학생 major_id를 어디서 받아야함
		if(searchWord != null && !searchWord.isBlank()) {
			sb.append("AND (lecture_name LIKE ? ");
			// 교수명으로 검색한 결과가 있으면
			if(professorIdSet.size() > 0) {
				sb.append("OR professor_id IN(" + professorIdSet.stream().map(id -> "?").collect(Collectors.joining(",")) + ")");
			}
			sb.append(")");
		}
		if(lectureCodeSet.size() > 0) {
			sb.append(" AND lecture_code NOT IN (" + lectureCodeSet.stream().map(item -> "?").collect(Collectors.joining(",")) + ")");
		}
		
		return sb.toString();
	}
	
	public PreparedStatement getPrepareStatement(String sql, String searchWord, String cat, Set<Integer> lectureCodeSet, Set<Integer> professorIdSet, int ...majorIds) throws SQLException{
		queryIndex = 1;
		// 날짜
		ZonedDateTime zoneTime = ZonedDateTime.now(LMSScheduleListener.getZONE_ID());
		
		// 학기 계산
		int semester = 0;
		if(StaticUtils.isBetweenTime(zoneTime,
			LMSScheduleListener.getEventTimeMap().get("student_first_lecture_cart_start"),
			LMSScheduleListener.getEventTimeMap().get("student_first_enrollment_end"))) semester = 1;
		else if(StaticUtils.isBetweenTime(zoneTime,
				LMSScheduleListener.getEventTimeMap().get("student_second_lecture_cart_start"),
				LMSScheduleListener.getEventTimeMap().get("student_second_enrollment_end"))) semester = 2;
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(queryIndex++, semester);
		pstmt.setString(queryIndex++, zoneTime.getYear()+"");
		if(!"all".equals(cat)) {
			if(!"liberal".equals(cat)) pstmt.setString(queryIndex, "전공");
			else pstmt.setString(queryIndex, "교양");
			queryIndex++;
		}
		if("major".equals(cat) || majorIds.length == 1) pstmt.setInt(queryIndex++, majorIds[0]);
		else if("minor".equals(cat)) pstmt.setInt(queryIndex++, majorIds[1]);
		if(searchWord != null && !searchWord.isBlank()) {
			pstmt.setString(queryIndex++, "%" + searchWord.trim() + "%");
			if(professorIdSet.size() > 0) {
				for(Integer id : professorIdSet) {
					pstmt.setInt(queryIndex++, id);
				}
			}
		}
		if(lectureCodeSet.size() > 0) {
			for(Integer id : lectureCodeSet) {
				pstmt.setInt(queryIndex++, id);
			}
		}
		
		return pstmt;
	}
}
