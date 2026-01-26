package com.goodeelms.service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodeelms.dao.ProfessorGradeDAO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.LectureHistoryDTO;
import com.goodeelms.listener.LMSScheduleListener;

public class ProfessorGradeService {
	private static final ProfessorGradeService instance = new ProfessorGradeService();
	private final ProfessorGradeDAO gradeDAO = ProfessorGradeDAO.getInstance();
	private ProfessorGradeService() {}
	
	public static ProfessorGradeService getInstance() {
		return instance;
	}
	
	// 허용 점수 - 서버 검증용(보안/우회 방지), 점수 기준 명확
    private static final Set<Double> ALLOWED_SCORES = new HashSet<>(
        Arrays.asList(4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.5, 0.0)
    );
	
    // 교수의 직전학기(종강) 강의 목록을 조회 (select 박스용)
    public List<LectureDTO> listCompletedLectures(int professorId, int year, int semester) {
        if (professorId <= 0) return List.of();
        return gradeDAO.listCompletedLectures(professorId, year, semester);
    }
    
    // 특정 강의의 수강생 수 조회 (검색 포함)
    public int countStudents(int lectureId, String keyword) {
        if (lectureId <= 0) return 0;
        return gradeDAO.countStudents(lectureId, keyword);
    }
    
    // 특정 강의의 수강생 목록 조회 (검색 + 페이징)
    public List<LectureHistoryDTO> listStudents(int lectureId, String keyword, int page, int pageSize) {
        if (lectureId <= 0) return List.of();
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        int offset = (page - 1) * pageSize;
        return gradeDAO.listStudents(lectureId, keyword, offset, pageSize);
    }

    /**
     * 한 페이지(10명 등) 성적 일괄 저장
     * - 성적기입기간 아니면 막음
     * - 교수 본인 강의인지 검증
     * - 허용 점수만 허용
     * - oldScore와 newScore 비교해서 변경된 것만 업데이트
     */
    // 성적 업데이트
    public int updateGrades(int professorId, int lectureId,
            String[] studentIdArr, String[] oldScoreArr, String[] newScoreArr) {

		if (professorId <= 0) throw new IllegalArgumentException("로그인이 필요합니다.");
		if (lectureId <= 0) throw new IllegalArgumentException("강의 정보가 올바르지 않습니다.");
		
		ZonedDateTime now = ZonedDateTime.now(LMSScheduleListener.getZONE_ID());
		
		// 성적 기입 기간 체크 + 어떤 기간인지 결과 받기
		int period = validateGradeInputPeriod(now); // 1 또는 2 반환
		
		int targetYear;
		int targetSemester;
		
		if (period == 1) {
			// 1월 성적 기입 = 전년도 2학기 성적 기입
			targetYear = now.getYear() - 1;
			targetSemester = 2;
		} else {
			// 7월 성적 기입 = 해당년도 1학기 성적 기입
			targetYear = now.getYear();
			targetSemester = 1;
		}
		
		// 직전학기 + 종강 + 교수 본인 강의인지 검증
		if (!gradeDAO.isGradeUpdatableLecture(lectureId, professorId, targetYear, targetSemester)) {
			throw new SecurityException("성적 기입이 허용되지 않은 강의입니다.");
		}
		
		// 배열 체크
		if (studentIdArr == null || newScoreArr == null || oldScoreArr == null) {
			return 0;
		}
		
		int n = Math.min(studentIdArr.length, Math.min(oldScoreArr.length, newScoreArr.length));
		if (n == 0) return 0;
		
		int updatedCount = 0;
		
		for (int i = 0; i < n; i++) {
			Integer studentId = parseIntOrNull(studentIdArr[i]);
			Double oldScore = parseScoreOrNull(oldScoreArr[i]);
			Double newScore = parseScoreOrNull(newScoreArr[i]);
			if (studentId == null) continue;
			if (equalsScore(oldScore, newScore)) continue;
			if (newScore == null) continue;
			
			if (!ALLOWED_SCORES.contains(newScore)) {
				throw new IllegalArgumentException("허용되지 않는 점수가 포함되어 있습니다.");
			}
			
			int updated = gradeDAO.updateGrade(lectureId, studentId, newScore);
			if (updated == 0) {
				throw new IllegalStateException("수강 이력 정보가 없거나 업데이트에 실패했습니다.");
			}
			updatedCount += updated;
		}
		return updatedCount;
	}

	 // 성적 기입 기간 판단
	 // - 기간 아니면 예외
	 // - 1월 기간이면 1 반환
	 // - 7월 기간이면 2 반환
	 public int validateGradeInputPeriod(ZonedDateTime now) {
	     Map<String, ZonedDateTime> map = LMSScheduleListener.getEventTimeMap();
	     if (map == null || map.isEmpty()) {
	         throw new IllegalStateException("학사 일정(성적 기입 기간) 설정이 누락되었습니다. 관리자에게 문의하세요.");
	     }
	
	     ZonedDateTime firstStart  = map.get("ac_first_grade_insert_start");
	     ZonedDateTime firstEnd    = map.get("ac_first_grade_insert_end");
	     ZonedDateTime secondStart = map.get("ac_second_grade_insert_start");
	     ZonedDateTime secondEnd   = map.get("ac_second_grade_insert_end");
	
	     if (firstStart == null || firstEnd == null || secondStart == null || secondEnd == null) {
	         throw new IllegalStateException("학사 일정(성적 기입 기간) 설정이 누락되었습니다. 관리자에게 문의하세요.");
	     }
	
	     boolean inFirst  = !now.isBefore(firstStart)  && !now.isAfter(firstEnd);
	     boolean inSecond = !now.isBefore(secondStart) && !now.isAfter(secondEnd);
	
	     if (inFirst) return 1;
	     if (inSecond) return 2;
	
	     throw new IllegalArgumentException("현재는 성적 기입 기간이 아닙니다.");
	 }
    
	 // 성적 기입 기간인지 여부
	 public boolean isGradeInputPeriod(ZonedDateTime now) {
	    try {
	        validateGradeInputPeriod(now);
	        return true;
	    } catch (IllegalArgumentException e) { // 기간 아님만 false
	        return false;
	    }
	 }
    
    // 문자열 점수 -> Double 변환 (빈값/미선택이면 null)
    private Double parseScoreOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    // int 변환 (빈값/미선택이면 null)
    private Integer parseIntOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    // Double 비교(둘 다 null이면 같음, 값이면 값 비교)
    private boolean equalsScore(Double a, Double b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return Double.compare(a, b) == 0;
    }
	
}
