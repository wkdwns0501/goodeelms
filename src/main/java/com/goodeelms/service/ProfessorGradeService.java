package com.goodeelms.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.goodeelms.dao.ProfessorGradeDAO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.LectureHistoryDTO;

public class ProfessorGradeService {
	private static final ProfessorGradeService instance = new ProfessorGradeService();
	private final ProfessorGradeDAO gradeDAO = ProfessorGradeDAO.getInstance();
	private static final ZoneId KST = ZoneId.of("Asia/Seoul");
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
        return gradeDAO.listStudents(lectureId, keyword, page, pageSize);
    }

    /**
     * 한 페이지(10명 등) 성적 일괄 저장
     * - 성적기입기간(1월/7월) 아니면 막음
     * - 교수 본인 강의인지 검증
     * - 허용 점수만 허용
     * - oldScore와 newScore 비교해서 변경된 것만 업데이트
     */
    // 성적 업데이트
    public int updateGrades(int professorId, int lectureId,
                             String[] studentIdArr, String[] oldScoreArr, String[] newScoreArr) {

        if (professorId <= 0) throw new IllegalArgumentException("로그인이 필요합니다.");
        if (lectureId <= 0) throw new IllegalArgumentException("강의 정보가 올바르지 않습니다.");

        // 성적 입력 기간 체크
        if (!isGradeInputPeriod(ZonedDateTime.now(KST))) {
            throw new IllegalStateException("현재는 성적 기입 기간이 아닙니다.");
        }
        
        // 직전학기 계산
        ZonedDateTime now = ZonedDateTime.now(KST);
        int targetYear;
        int targetSemester;
        int month = now.getMonthValue();
        if (month <= 6) {
            targetYear = now.getYear() - 1;
            targetSemester = 2;
        } else {
            targetYear = now.getYear();
            targetSemester = 1;
        }

        // 직전학기 + 종강 + 교수 본인 강의인지 검증
        if (!gradeDAO.isGradeUpdatableLecture(lectureId, professorId, targetYear, targetSemester)) {
            throw new SecurityException("성적 기입이 허용되지 않은 강의입니다.");
        }

        // 배열 체크
        if (studentIdArr == null || newScoreArr == null || oldScoreArr == null) {
            // 페이지에서 아무도 안 보냈거나 폼이 깨진 케이스
            return 0;
        }
        
        // 세 배열이 모두 안전하게 접근 가능한 최대 인덱스 수
        int n = Math.min(studentIdArr.length, Math.min(oldScoreArr.length, newScoreArr.length));
        if (n == 0) return 0;
        
        // 수정된 개수
        int updatedCount = 0;

        // 변경된 것만 업데이트
        for (int i = 0; i < n; i++) {
            Integer studentId = parseIntOrNull(studentIdArr[i]);
            Double oldScore = parseScoreOrNull(oldScoreArr[i]);
            Double newScore = parseScoreOrNull(newScoreArr[i]);

            if (studentId == null) continue;
            
            // 값이 안 바뀌었으면 스킵
            if (equalsScore(oldScore, newScore)) continue;
            //  저장 시도 자체를 스킵(= DB 덮어쓰지 않음)
            if (newScore == null) continue;

            // 허용 점수인지 검증
            if (!ALLOWED_SCORES.contains(newScore)) {
                throw new IllegalArgumentException("허용되지 않는 점수가 포함되어 있습니다.");
            }

            // 업데이트 실행
            int updated = gradeDAO.updateGrade(lectureId, studentId, newScore);
            if (updated == 0) {
                throw new IllegalStateException("수강 이력 정보가 없거나 업데이트에 실패했습니다.");
            }
            updatedCount += updated; // 성공 시 +1
        }
        return updatedCount;
    }
    
    
    // 성적 기입 기간인지 여부 (1월/7월)
    private boolean isGradeInputPeriod(ZonedDateTime now) {
        int m = now.getMonthValue();
        return (m == 1 || m == 7);
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
