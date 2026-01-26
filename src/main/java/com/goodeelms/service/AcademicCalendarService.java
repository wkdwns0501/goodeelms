package com.goodeelms.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodeelms.dao.AcademicDAO;
import com.goodeelms.dto.AcademicCalendarDTO;

public class AcademicCalendarService {
	private static final AcademicCalendarService instance = new AcademicCalendarService();
	
	public static AcademicCalendarService getInstance() {
		return instance;
	}
	
	AcademicDAO academicDAO = AcademicDAO.getInstance();
	
	public List<AcademicCalendarDTO> getCalendarAtYear(int year){
		return academicDAO.getCalendarAtYear(year);
	}
	
	
	
	public Map<String, Integer> getCurrentYearAndSemester() { // 0125 임욱(추가) 수강 중인 현재 학기를 위한 학기 설정 
	    LocalDate now = LocalDate.now();
	    int year = now.getYear();
	    
	    List<AcademicCalendarDTO> calendar = AcademicDAO.getInstance().getCalendarAtYear(year);
	    Map<String, LocalDate> events = new HashMap<>();
	    if (calendar != null) {
	        for (AcademicCalendarDTO dto : calendar) {
	            String name = dto.getAcademicEventName();
	            LocalDate date = LocalDate.parse(dto.getAcademicEventDate());
	            events.put(name, date);
	        }
	    }

	    int targetYear = year;
	    int targetSemester;

	    LocalDate firstCart = events.get("student_first_enrollment_start");   
	    LocalDate secondCart= events.get("student_second_enrollment_start"); 

	    if (firstCart != null && now.isBefore(firstCart)) { // 작년 ~ 1학기 수강 장바구니 이전( 이전 년도 2학기 장학금 선정 이후 ) 
	        targetYear = year - 1;
	        targetSemester = 2;
	    } else if (secondCart != null && now.isBefore(secondCart)) { // 2학기 수강 장바구니 이전( 1학기 장학금 선정 이후 )
	        targetSemester = 1;
	    } else { // 올해 2학기 
	        targetSemester = 2;
	    }

	    Map<String, Integer> result = new HashMap<>();
	    result.put("year", targetYear);
	    result.put("semester", targetSemester);
	    
	    return result;
	}
	
	
}
