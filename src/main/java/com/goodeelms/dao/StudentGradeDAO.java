package com.goodeelms.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.StudentGradeDTO;
import com.goodeelms.util.DBUtil;

public class StudentGradeDAO {
	private static final StudentGradeDAO instance = new StudentGradeDAO();
    private StudentGradeDAO() {}
    
    public static StudentGradeDAO getInstance() {
        return instance;
    }
    
    // 평가 대상 강의(종강 + 해당학기 + lecture_history 존재) 중 미평가 강의 수
    // 직전 종강 학기(recent) 성적 조회 잠금 해제 조건
    public int countMissEval(int studentId, int year, int semester) {
        String sql =
            "SELECT COUNT(*) FROM lecture_history lh " +
            "JOIN lecture l ON l.lecture_id = lh.lecture_id " +
            "LEFT JOIN lecture_evaluation le " +
            "  ON le.lecture_id = lh.lecture_id AND le.student_id = lh.student_id " +
            "WHERE lh.student_id = ? " +
            "  AND l.lecture_status = '종강' " +
            "  AND l.lecture_year = ? " +
            "  AND l.lecture_semester = ? " +
            "  AND le.evaluation_id IS NULL";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, String.valueOf(year)); // lecture_year VARCHAR(4)
            ps.setInt(3, semester);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1); // 미평가 강의 수 반환
            }
            return 0; // -> 모든 강의 평가 완료
        } catch (SQLException e) {
            throw new RuntimeException("미평가 강의 수 조회 실패", e);
        }
    }

    // 직전 종강 학기(recent) 성적 리스트 (종강 + 해당학기 + lecture_history 존재)
    public List<StudentGradeDTO> listRecentGrades(int studentId, int year, int semester) {
        List<StudentGradeDTO> list = new ArrayList<>();

        String sql =
            "SELECT " +
            "  l.lecture_id, l.lecture_code, l.lecture_name, l.lecture_section, " +
            "  lh.lecture_score, l.lecture_year, l.lecture_semester, p.professor_name " +
            "FROM lecture_history lh " +
            "JOIN lecture l ON l.lecture_id = lh.lecture_id " +
            "JOIN professor p ON p.professor_id = l.professor_id " +
            "WHERE lh.student_id = ? " +
            "  AND l.lecture_status = '종강' " +
            "  AND l.lecture_year = ? " +
            "  AND l.lecture_semester = ? " +
            "ORDER BY l.lecture_name ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, String.valueOf(year));
            ps.setInt(3, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	StudentGradeDTO dto = new StudentGradeDTO();
                    dto.setLectureId(rs.getInt("lecture_id"));
                    dto.setLectureCode(rs.getInt("lecture_code"));
                    dto.setLectureName(rs.getString("lecture_name"));
                    dto.setLectureSection(rs.getString("lecture_section"));
                    dto.setLectureYear(rs.getString("lecture_year"));
                    dto.setLectureSemester(rs.getInt("lecture_semester"));
                    BigDecimal bd = rs.getBigDecimal("lecture_score"); // NULL이면 bd=null
                    dto.setLectureScore(bd == null ? null : bd.doubleValue());
                    dto.setProfessorName(rs.getString("professor_name"));
                    list.add(dto);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("직전학기 성적 리스트 조회 실패", e);
        }
    }

    // 직전 종강 학기(recent)를 제외한 전체 이력(all) 카운트 (검색 포함)
    public int countAllHistory(int studentId, int recentYear, int recentSemester, String keyword) {
        boolean hasKeyword = (keyword != null && !keyword.isBlank());

        String sql =
            "SELECT COUNT(*) FROM lecture_history lh " +
            "JOIN lecture l ON l.lecture_id = lh.lecture_id " +
            "JOIN professor p ON p.professor_id = l.professor_id " +
            "WHERE lh.student_id = ? " +
            "  AND l.lecture_status = '종강' " +
            "  AND NOT (l.lecture_year = ? AND l.lecture_semester = ?) " +
            (hasKeyword ? " AND (l.lecture_name LIKE ? OR p.professor_name LIKE ?) " : "");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            ps.setInt(idx++, studentId);
            ps.setString(idx++, String.valueOf(recentYear));
            ps.setInt(idx++, recentSemester);
            if (hasKeyword) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1); // 개수 반환
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("전체 성적 이력 카운트 조회 실패", e);
        }
    }

    // 직전 종강 학기(recent)를 제외한 전체 이력(all) 리스트 (검색, 페이징 포함)
    public List<StudentGradeDTO> listAllHistory(int studentId, int recentYear, int recentSemester,
            									String keyword, int offset, int pageSize) {
        List<StudentGradeDTO> list = new ArrayList<>();
        boolean hasKeyword = (keyword != null && !keyword.isBlank());
        
        String sql =
            "SELECT " +
            "  l.lecture_id, l.lecture_code, l.lecture_name, l.lecture_section, " +
            "  lh.lecture_score, p.professor_name, " +
            "  l.lecture_year, l.lecture_semester " +
            "FROM lecture_history lh " +
            "JOIN lecture l ON l.lecture_id = lh.lecture_id " +
            "JOIN professor p ON p.professor_id = l.professor_id " +
            "WHERE lh.student_id = ? " +
            "  AND l.lecture_status = '종강' " +
            "  AND NOT (l.lecture_year = ? AND l.lecture_semester = ?) " +
            (hasKeyword ? " AND (l.lecture_name LIKE ? OR p.professor_name LIKE ?) " : "") +
            "ORDER BY l.lecture_year DESC, l.lecture_semester DESC, l.lecture_name ASC " +
            "LIMIT ? OFFSET ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            ps.setInt(idx++, studentId);
            ps.setString(idx++, String.valueOf(recentYear));
            ps.setInt(idx++, recentSemester);
            if (hasKeyword) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            ps.setInt(idx++, pageSize);
            ps.setInt(idx++, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	 StudentGradeDTO dto = new StudentGradeDTO();
                     dto.setLectureId(rs.getInt("lecture_id"));
                     dto.setLectureCode(rs.getInt("lecture_code"));
                     dto.setLectureName(rs.getString("lecture_name"));
                     dto.setLectureSection(rs.getString("lecture_section"));
                     dto.setLectureYear(rs.getString("lecture_year"));
                     dto.setLectureSemester(rs.getInt("lecture_semester"));
                     BigDecimal bd = rs.getBigDecimal("lecture_score");
                     dto.setLectureScore(bd == null ? null : bd.doubleValue());
                     dto.setProfessorName(rs.getString("professor_name"));
                     list.add(dto);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("전체 성적 이력 리스트 조회 실패", e);
        }
    }

}
