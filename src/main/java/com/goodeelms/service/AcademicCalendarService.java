package com.goodeelms.service;

import java.util.List;

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
}
