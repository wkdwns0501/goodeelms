package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.util.DBUtil;

public class SelectLectureDAO {
	
	public SelectLectureDAO() {
		
	}
	
	public List<LectureDTO> SelectLectureOnCartDuration(String cat, int ...majorIds){
		String sql = "SELECT  lecture_id, lecture_code, lecture_name, lecture_description, lecture_room, lecture_credit, lecture_type, "
				+ "lecture_capacity, major.major_name, pro.professor_name, lecture_current_people "
				+ "FROM lecture AS lec "
				+ "JOIN major ON lec.major_id = major.major_id "
				+ "JOIN PROFESSOR AS pro ON lec.PROFESSOR_ID = pro.PROFESSOR_ID "
				+ "WHERE lecture_semester = ? "
				+ "AND YEAR(lecture_year) = ?";
		
		if(!"all".equals(cat)) sql += "AND lecture_type = ? ";
		if(!"liberal".equals(cat)) sql += "AND (lec.major_id = ? ";	// 학생 major_id를 어디서 받아야함
		if("all".equals(cat) && majorIds.length > 1) sql += "OR lec.major_id = ? ";
		
		if(!"liberal".equals(cat)) sql += ")";
		
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dateTime.format(formatter);
		String[] rawSemester = date.split("-");
		int semester = 0;
		if(Integer.parseInt(rawSemester[1]) < 9) semester += 1;
		else semester += 2;
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			int index = 1;
			pstmt.setInt(index++, semester);
			pstmt.setString(index++, rawSemester[0]);
			if(!"all".equals(cat)) {
				if(!"liberal".equals(cat)) pstmt.setString(index, "전공");
				else pstmt.setString(index, "교양");
				index++;
			}
			if("major".equals(cat) || "all".equals(cat)) pstmt.setInt(index++, majorIds[0]);
			else if("minor".equals(cat)) pstmt.setInt(index++, majorIds[1]);
			if("all".equals(cat) && majorIds.length > 1) pstmt.setInt(index++, majorIds[1]);
			
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
}
