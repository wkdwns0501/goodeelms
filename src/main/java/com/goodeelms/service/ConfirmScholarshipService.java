package com.goodeelms.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import com.goodeelms.dao.ScholarshipDAO;
import com.goodeelms.dto.LectureHistoryDTO;

public class ConfirmScholarshipService {
	
	// 조회할 수 있는 가장 최근 학기 장학 후보 목록 
	public ArrayList<LectureHistoryDTO> getScholarshipList(String year, int semester) {
		ScholarshipDAO scholarshipDAO = ScholarshipDAO.getInstance();
		return scholarshipDAO.getScholarshipList(year, semester);
	}

	public int writeScholarshipHistory(String[] confirmedStudentsId, int yearSemestertoInt) {
		System.out.println(yearSemestertoInt);
		ScholarshipDAO scholarshipDAO = ScholarshipDAO.getInstance();
		return scholarshipDAO.writeScholarshipHistory(confirmedStudentsId, yearSemestertoInt);
	}
	
	// 현재가 2 or 8월인지 체크
//	public boolean isAccessPeriod() {
//	    LocalDate now = LocalDate.now();
//	    int month = now.getMonthValue();
//	    int day = now.getDayOfMonth();
//	    // 예: 매년 1학기(2월), 2학기(8월) 한 달 동안만 접근 가능하다고 가정
//	    // 혹은 특정 날짜 범위를 지정 (예: 2026-01-20 ~ 2026-02-28)
//	    
//	    // 2월이나 8월이면 true를 반환
//	    return (month == 2 || month == 8 || month == 1); // 테스트를 위해 현재 1월 포함
//	}
	
	public String[] lastestSemester(ZonedDateTime now) {
		    int year = now.getYear(); // 2026
		    int month = now.getMonthValue(); // 1
		    String yearSemester = "";
		    if (month >= 7) {
		        yearSemester = year + "_1"; // 7월 넘으면 올해 1학기
		    } else if (month >= 1) {
		        yearSemester = (year - 1) + "_2"; // 2월 넘으면 작년 2학기
		    }
		    
		    return yearSemester.split("_");
		}

	public int cancelScholarship(String studentId, int yearSemesterInt) {
		ScholarshipDAO scholarshipDAO = ScholarshipDAO.getInstance();
		return scholarshipDAO.cancelScholarship(studentId, yearSemesterInt);
	}
		
}
