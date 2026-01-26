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
	
	public List<AcademicCalendarDTO> getCalendarAtYear(){
		String sql = "SELECT * FROM academic_calendar";
    	try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){    		
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

	public int updateCalendar(AcademicCalendarDTO dto, int adminId) {
	    // INSERT 대신 확실하게 UPDATE 쿼리로 변경
	    String sql = "UPDATE academic_calendar "
	               + "SET academic_event_date = ?, admin_id = ? "
	               + "WHERE academic_event_name = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, dto.getAcademicEventDate()); // 변경할 날짜
	        pstmt.setInt(2, adminId);                      // 수정한 관리자 ID
	        pstmt.setString(3, dto.getAcademicEventName()); // 기준이 되는 일정 이름
	        
	        int result = pstmt.executeUpdate();
	        
	        // 만약 업데이트된 행이 0개라면 (데이터가 아예 없어서 실패한 경우) 로그 출력
	        if(result == 0) {
	            System.out.println("DEBUG: 업데이트 실패! 해당 이름의 일정이 DB에 없습니다: " + dto.getAcademicEventName());
	        }
	        
	        return result;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}

	public ArrayList<AcademicCalendarDTO> getAcademicCalendar() {
        ArrayList<AcademicCalendarDTO> list = new ArrayList<>();
        // 실제 DB 테이블명과 컬럼명(category_key, academic_event_date)을 확인해주세요!
        String sql = "SELECT academic_event_name, academic_event_date FROM academic_calendar";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                AcademicCalendarDTO dto = new AcademicCalendarDTO();
                // Name(Key)과 Date만 세팅
                dto.setAcademicEventName(rs.getString("academic_event_name"));
                dto.setAcademicEventDate(rs.getString("academic_event_date"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
