package com.goodeelms.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import com.goodeelms.dao.EvalutationDAO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.LectureEvaluationDTO;

public class LectureEvaluationService {
	
	public ArrayList<LectureDTO> getLectureList(int studentId, String targetYear, int targetSemester) {
		EvalutationDAO evaluationDAO = EvalutationDAO.getInstance();
		return evaluationDAO.getLectureList(studentId, targetYear, targetSemester);
	}

	public int writeEvaluation(LectureEvaluationDTO evaDTO) {
		EvalutationDAO evaluationDAO = EvalutationDAO.getInstance();
		return evaluationDAO.writeEvaluation(evaDTO);
	}
	
	public boolean isAccessPeriod() {
	    LocalDate now = LocalDate.now();
	    int month = now.getMonthValue();
	    int day = now.getDayOfMonth();

	    // 예: 매년 1학기(2월), 2학기(8월) 한 달 동안만 접근 가능하다고 가정
	    // 혹은 특정 날짜 범위를 지정 (예: 2026-01-20 ~ 2026-02-28)
	    return (month == 2 || month == 8 || month == 1); // 테스트를 위해 현재 1월 포함
	}

	public String[] getEvaluationTargetTerm() {
		Calendar cal = Calendar.getInstance();
	    int currentYear = cal.get(Calendar.YEAR);
//	    int currentMonth = cal.get(Calendar.MONTH) + 1;
	    int currentMonth = 8; // 테스트용

	    String targetYear;
	    String targetSemester;

	    if (currentMonth == 8) {
	        targetYear = String.valueOf(currentYear);
	        targetSemester = "1"; // 8월엔 올해 1학기 평가
	    } else if (currentMonth == 2) {
	        targetYear = String.valueOf(currentYear - 1);
	        targetSemester = "2"; // 2월엔 작년 2학기 평가
	    } else {
	        return null; // 기간 외
	    }
	    return new String[] {targetYear, targetSemester};
	}
	
	
}
