package com.goodeelms.service;

import java.util.List;
import java.util.Set;

import com.goodeelms.dao.LectureDAO;
import com.goodeelms.dto.LectureDTO;

public class LectureService {
	private LectureDAO lectureDAO = LectureDAO.getInstance();
	
	private static final LectureService instance = new LectureService();
	
	private LectureService() {}
	
	public static LectureService getInstance() {
		return instance;
	}
	
    /**  강의 등록
     * - 교수는 세션에서 받은 professor_id 사용
     * - major_id는 professor에서 조회
     * - lecture_code는 (학과코드4 + 과목일련3) 자동 생성
     */
	public int insertLecture(LectureDTO lecture) {

	    validateForInsert(lecture);

	    // 교수 학과 조회
	    int majorId = lectureDAO.findMajorIdByProfessorId(lecture.getProfessorId());
	    if (majorId <= 0) {
	        throw new IllegalArgumentException("교수의 학과 정보를 찾을 수 없습니다.");
	    }
	    lecture.setMajorId(majorId);

	    // lecture_code 결정 (재사용 or 생성)
	    String name = lecture.getLectureName().trim();
	    String type = lecture.getLectureType().trim();

	    Integer existingCode = lectureDAO.findLectureCode(
	        majorId, name, lecture.getLectureCredit(), type
	    );

	    if (existingCode != null) { // 존재하면
	        lecture.setLectureCode(existingCode);
	    } else { // 없다면 (처음 생성이라면)
	        String majorCode = lectureDAO.findMajorCodeByMajorId(majorId);
	        Integer maxLectureCode = lectureDAO.findMaxLectureCodeByMajorId(majorId);
	        lecture.setLectureCode(generateLectureCode(majorCode, maxLectureCode));
	    }
	    
	    int opened = lectureDAO.countSections(
	    	    lecture.getProfessorId(),
	    	    lecture.getLectureCode(),
	    	    lecture.getLectureYear(),
	    	    lecture.getLectureSemester()
	    	);

    	if (opened == 0) {
    	    lecture.setLectureSection("01");
    	} else if (opened == 1) {
    	    lecture.setLectureSection("02");
    	} else {
    	    throw new IllegalArgumentException("분반은 최대 2개(01, 02)까지만 개설할 수 있습니다.");
    	}
	    
	    try {
	        return lectureDAO.insertLecture(lecture);
	    } catch (RuntimeException e) {
	        throw new IllegalArgumentException("이미 개설된 강의입니다. (중복 또는 제약조건 오류)");
	    }
	}

    private void validateForInsert(LectureDTO l) {
        if (l == null) {
            throw new IllegalArgumentException("강의 정보가 없습니다.");
        }
        if (l.getProfessorId() <= 0) {
            throw new IllegalArgumentException("로그인 정보(교수)가 없습니다.");
        }
        if (l.getLectureName() == null || l.getLectureName().trim().isEmpty()) {
            throw new IllegalArgumentException("강의명은 필수입니다.");
        }
        if (l.getLectureCredit() < 1 || l.getLectureCredit() > 6) {
            throw new IllegalArgumentException("학점은 1~6 사이여야 합니다.");
        }
        if (l.getLectureSemester() != 1 && l.getLectureSemester() != 2) {
            throw new IllegalArgumentException("학기는 1 또는 2만 가능합니다.");
        }
        if (l.getLectureYear() == null || !l.getLectureYear().matches("\\d{4}")) {
            throw new IllegalArgumentException("연도는 YYYY 형식이어야 합니다.");
        }
        if (l.getLectureCapacity() < 1 || l.getLectureCapacity() > 50) {
            throw new IllegalArgumentException("정원은 1~50명 사이여야 합니다.");
        }
        if (l.getLectureDescription() != null && l.getLectureDescription().length() > 1000) {
            throw new IllegalArgumentException("강의 설명은 최대 1000자까지 가능합니다.");
        }
        if (l.getLectureType() == null || l.getLectureType().trim().isEmpty()) {
            throw new IllegalArgumentException("강의 유형은 필수입니다.");
        }
        if (l.getLectureRoom() != null && l.getLectureRoom().trim().length() > 50) {
            throw new IllegalArgumentException("강의실은 최대 50자까지 가능합니다.");
        }
        if (l.getBuildingId() <= 0) {
            throw new IllegalArgumentException("건물을 선택하세요.");
        }
	}
    
	/**
     * lecture_code 규칙: 학과코드(4) + 과목일련(3)
     * - majorCode: "0100" 같은 4자리 문자열 (뒤 2자리는 00 고정)
     * - maxLectureCode: 해당 학과의 기존 최대 lecture_code (없으면 null)
     * - 반환: int lecture_code (DB 저장용)
     */
    private int generateLectureCode(String majorCode, Integer maxLectureCode) {
    	 // majorCode는 "0100" 같은 4자리 문자열
    	String trimmed = (majorCode == null) ? "" : majorCode.trim();

        // 4자리 숫자 + 뒤 2자리는 00
        if (!trimmed.matches("\\d{2}00")) {
            throw new IllegalArgumentException("학과 코드 형식이 올바르지 않습니다(예: 0100): " + majorCode);
        }
        // "0100" -> 100 (int는 맨 앞 0을 보존X)
        int majorCodeNum = Integer.parseInt(trimmed);
        // base: 학과코드(4) + 000 (예: 0100xxx)
        int base = majorCodeNum * 1000;

        // 다음 일련번호(001~999)
        int nextSeq;
        if (maxLectureCode == null) {
            nextSeq = 1;
        } else {
            int lastSeq = maxLectureCode % 1000;
            nextSeq = lastSeq + 1;
        }

        if (nextSeq > 999) {
            throw new IllegalArgumentException("해당 학과의 과목 일련번호가 가득 찼습니다(001~999).");
        }
        return base + nextSeq;
    }
    
    // 교수용 강의 리스트 (페이징, 검색 포함)
    public List<LectureDTO> getLecturePage(
            int professorId, int page, int limit, String keyword
    ) {
        if (professorId <= 0) {
            throw new IllegalArgumentException("교수 정보가 올바르지 않습니다.");
        }
        if (page < 1) page = 1;
        if (limit < 1) limit = 5;

        int majorId = lectureDAO.findMajorIdByProfessorId(professorId);
        if (majorId <= 0) {
            throw new IllegalArgumentException("교수의 학과 정보(major_id)를 찾을 수 없습니다.");
        }

        return lectureDAO.findPageByMajor(majorId, page, limit, keyword);
    }
    
    // 페이징을 위한 교수가 속한 학과의 강의 수
    public int getLectureTotalCount(int professorId, String keyword) {
        if (professorId <= 0) {
            throw new IllegalArgumentException("교수 정보가 올바르지 않습니다.");
        }
        int majorId = lectureDAO.findMajorIdByProfessorId(professorId);
        if (majorId <= 0) {
            throw new IllegalArgumentException("교수의 학과 정보(major_id)를 찾을 수 없습니다.");
        }
        return lectureDAO.countByMajor(majorId, keyword);
    }
    
    public LectureDTO getMajorIdAndByLectureId(int lectureId) {
    	return lectureDAO.fingdMajorIdAndTypeByLectureId(lectureId);
    }
    
    public List<LectureDTO> getLectureOfStudentId(String student_id){
    	return lectureDAO.getLectureOfStudent(student_id);
    }
    
    public Set<Integer> getLectureCodeWithLectureId(Set<Integer> lectureIds){
    	return lectureDAO.getLectureCodeWithLectureId(lectureIds);
    }
}
