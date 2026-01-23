package com.goodeelms.service;

import java.time.ZonedDateTime;
import java.util.List;

import com.goodeelms.dao.StudentGradeDAO;
import com.goodeelms.dto.StudentGradeDTO;

public class StudentGradeService {
	private static final StudentGradeService instance = new StudentGradeService();
	private final StudentGradeDAO gradeDAO = StudentGradeDAO.getInstance();
	public StudentGradeService() {}	
	
	public static StudentGradeService getInstance() {
		return instance;
	}
	
	// 평가기간(2/8월)일 때, 직전학기 강의 중 미평가 강의 개수 반환 (0이면 전부 평가 완료)
    public int countMissEval(int studentId, int year, int semester) {
        return gradeDAO.countMissEval(studentId, year, semester);
    }
	
	// 직전 종강 학기(recent) 성적 리스트 (평가 기간이 아닐 때 또는 평가 기간 + 평가 완료 상태일 때 호출)
    public List<StudentGradeDTO> listRecentGrades(int studentId, int year, int semester) {
        return gradeDAO.listRecentGrades(studentId, year, semester);
    }
	
	// 전체 이력(all) 카운트 (직전 학기(recent) 제외, 검색 포함)
    public int countAllHistory(int studentId, int recentYear, int recentSemester, String keyword) {
    	if (keyword != null && keyword.isBlank()) keyword = null;
        return gradeDAO.countAllHistory(studentId, recentYear, recentSemester, keyword);
    }
	
	// 전체 이력(all) 리스트 (직전 학기(recent) 제외, 검색 + 페이징 포함)
	public List<StudentGradeDTO> listAllHistory(Integer studentId, int recentYear, int recentSemester, 
												String keyword, int page, int pageSize) {
		if (keyword != null && keyword.isBlank()) keyword = null;
		int safePage = Math.max(page, 1);
        int safeSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safeSize;
		return gradeDAO.listAllHistory(studentId, recentYear, recentSemester,
                					   keyword, offset, safeSize);
	}
	
	// 강의 평가 기간인지 여부 (2월/8월)
	public boolean isEvaluationPeriod(ZonedDateTime now) {
        int month = now.getMonthValue();
        return (month == 2 || month == 8);
    }

}
