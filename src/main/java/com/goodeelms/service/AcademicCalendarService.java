package com.goodeelms.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
	
	public List<AcademicCalendarDTO> getCalendarAtYear(){
		return academicDAO.getCalendarAtYear();
	}

	public int updateCalendar(AcademicCalendarDTO dto, int adminId) {
		return academicDAO.updateCalendar(dto, adminId);
	}
	
	public Map<String, String> getCalendarMap() {
	    ArrayList<AcademicCalendarDTO> list = academicDAO.getAcademicCalendar();
	    Map<String, String> map = new HashMap<>();
	    
	    for (AcademicCalendarDTO dto : list) {
	        // "academic_event_name" -> "2026-01-26"
	        map.put(dto.getAcademicEventName(), dto.getAcademicEventDate());
	    }
	    return map;
	}
}
