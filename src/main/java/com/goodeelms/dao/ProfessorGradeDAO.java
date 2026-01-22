package com.goodeelms.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.LectureHistoryDTO;
import com.goodeelms.util.DBUtil;

public class ProfessorGradeDAO {
	private static final ProfessorGradeDAO instance = new ProfessorGradeDAO();
    private ProfessorGradeDAO() {}

    public static ProfessorGradeDAO getInstance() {
        return instance;
    }
    
    // 교수의 직전학기의 종강 강의 목록 조회 (select 박스용)
    public List<LectureDTO> listCompletedLectures(int professorId, int year, int semester) {
        List<LectureDTO> list = new ArrayList<>();

        String sql =
            "SELECT lecture_id, lecture_code, lecture_name, lecture_year, lecture_semester, lecture_section, lecture_status, professor_id " +
            "FROM lecture " +
            "WHERE professor_id = ? " +
            "  AND lecture_year = ? " +
            "  AND lecture_semester = ? " +
            "  AND lecture_status = '종강' " +
            "ORDER BY lecture_code ASC, lecture_section ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, professorId);
            ps.setString(2, String.valueOf(year)); // lecture_year가 VARCHAR(4)라서
            ps.setInt(3, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LectureDTO lec = new LectureDTO();
                    lec.setLectureId(rs.getInt("lecture_id"));
                    lec.setLectureCode(rs.getInt("lecture_code"));
                    lec.setLectureName(rs.getString("lecture_name"));
                    lec.setLectureYear(rs.getString("lecture_year"));
                    lec.setLectureSemester(rs.getInt("lecture_semester"));
                    lec.setLectureSection(rs.getString("lecture_section"));
                    lec.setLectureStatus(rs.getString("lecture_status"));
                    lec.setProfessorId(rs.getInt("professor_id"));
                    list.add(lec);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // 페이징을 위한 특정 강의의 수강생 수 조회 (검색 포함)
    public int countStudents(int lectureId, String keyword) {
        int count = 0;
        boolean hasKeyword = (keyword != null && !keyword.isBlank());

        String sql =
            "SELECT COUNT(*) AS cnt " +
            "FROM lecture_history lh " +
            "JOIN student s ON s.student_id = lh.student_id " +
            "WHERE lh.lecture_id = ? " +
            (hasKeyword ? " AND s.student_name LIKE ? " : "");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lectureId);
            if (hasKeyword) {
                ps.setString(2, "%" + keyword.trim() + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    // 특정 강의의 수강생 목록 페이지 조회 (학생명 검색 + 페이징)
    public List<LectureHistoryDTO> listStudents(int lectureId, String keyword, int page, int pageSize) {
        List<LectureHistoryDTO> list = new ArrayList<>();

        boolean hasKeyword = (keyword != null && !keyword.isBlank());
        int offset = (page - 1) * pageSize;

        String sql =
            "SELECT " +
            "  lh.student_id, lh.lecture_id, lh.lecture_score, " +
            "  s.student_no, s.student_name " +
            "FROM lecture_history lh " +
            "JOIN student s ON s.student_id = lh.student_id " +
            "JOIN lecture l ON l.lecture_id = lh.lecture_id " +
            "WHERE lh.lecture_id = ? " +
            (hasKeyword ? " AND s.student_name LIKE ? " : "") +
            "ORDER BY s.student_name ASC " +
            "LIMIT ? OFFSET ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            ps.setInt(idx++, lectureId);
            if (hasKeyword) {
                ps.setString(idx++, "%" + keyword.trim() + "%");
            }
            ps.setInt(idx++, pageSize);
            ps.setInt(idx++, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LectureHistoryDTO dto = new LectureHistoryDTO();
                    dto.setStudentId(rs.getInt("student_id"));
                    dto.setStudentNo(rs.getInt("student_no"));
                    dto.setLectureId(rs.getInt("lecture_id"));
                    BigDecimal bd = rs.getBigDecimal("lecture_score"); // NULL이면 bd도 null
                    dto.setLectureScore(bd == null ? null : bd.doubleValue());
                    dto.setStudentName(rs.getString("student_name"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // 한 학생의 성적 업데이트
    public int updateGrade(int lectureId, int studentId, Double newScore) {
        String sql =
            "UPDATE lecture_history " +
            "SET lecture_score = ? " +
            "WHERE lecture_id = ? AND student_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (newScore == null) {
                ps.setNull(1, Types.DECIMAL); // NULL 저장, 타입 명시
            } else {
                ps.setBigDecimal(1, BigDecimal.valueOf(newScore)); // BigDecimal 타입으로 저장
            }
            ps.setInt(2, lectureId);
            ps.setInt(3, studentId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // 성적 기입이 허용되는 강의인지 검증 (교수 본인 + 종강 + 직전학기)
    public boolean isGradeUpdatableLecture(int lectureId, int professorId,
                                           int year, int semester) {
        String sql = "SELECT COUNT(*) FROM lecture " +
        			 "WHERE lecture_id = ? AND professor_id = ? " +
        			 "  AND lecture_status = '종강' AND lecture_year = ? " +
        			 "  AND lecture_semester = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lectureId);
            ps.setInt(2, professorId);
            ps.setString(3, String.valueOf(year));
            ps.setInt(4, semester);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
