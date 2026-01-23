package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.AcademicCalendarDTO;
import com.goodeelms.listener.LMSScheduleListener;
import com.goodeelms.util.DBUtil;

public class AcademicDAO {
	private static final AcademicDAO instance = new AcademicDAO();
	
	public static AcademicDAO getInstance() {
		return instance;
	}
	
	public List<AcademicCalendarDTO> getCalendarAtYear(int year){
		String sql = "SELECT * FROM academic_calendar WHERE academic_event_date LIKE ?";
    	try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
    		pstmt.setString(1, year + "%");
    		
    		try(ResultSet rs = pstmt.executeQuery()){
    			List<AcademicCalendarDTO> list = new ArrayList<AcademicCalendarDTO>();
    			while(rs.next()) {
    				AcademicCalendarDTO dto = new AcademicCalendarDTO();
    				dto.setAcademicCalendarId(rs.getInt("academic_calendar_id"));
    				dto.setAcademicEventName(rs.getString("academic_event_name"));
    				dto.setAcademicEventDate(rs.getString("academic_event_date"));
    				dto.setAcademicEventTime(rs.getString("academic_event_time"));
    				dto.setAdminId(rs.getInt("admin_id"));
    				
    				String date = rs.getString("academic_event_date");
    				String time = rs.getString("academic_event_time");
    				String[] dates = date.split("-");
    				String[] times = time.split(":");
    				ZonedDateTime zoneTime = ZonedDateTime.of(LocalDateTime.of(Integer.parseInt(dates[0]), 
    										Integer.parseInt(dates[1]), Integer.parseInt(dates[2]),
											Integer.parseInt(times[0]), Integer.parseInt(times[1])),
    										LMSScheduleListener.getZONE_ID());    				
    				dto.setEventZoneDateTime(zoneTime);
    				
    				list.add(dto);
    			}
    			return list;
    		}
    	}
    	catch(SQLException e) {
    		System.out.println("DB 조회 실패: 스케줄러 미등록 종료");
    	}
    	return null;
	}
}
