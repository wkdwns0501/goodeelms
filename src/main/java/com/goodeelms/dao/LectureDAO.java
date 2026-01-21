package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.LectureDTO;
import com.goodeelms.util.DBUtil;

public class LectureDAO {
	private static final LectureDAO instance = new LectureDAO(); 
	
	private LectureDAO() {}
	
	public static LectureDAO getInstance() {
		return instance;
	}
	
	// 강의 등록
	public int insertLecture(LectureDTO lecture) {
		String sql = "INSERT INTO lecture ("
					+ "lecture_code, lecture_name, lecture_description, "
					+ "lecture_room, lecture_credit, lecture_year, "
					+ "lecture_semester, lecture_section, lecture_type, "
					+ "lecture_capacity, professor_id, major_id, building_id"
					+ ") "
			        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, lecture.getLectureCode());
			pstmt.setString(2, lecture.getLectureName());
			pstmt.setString(3, lecture.getLectureDescription());
			pstmt.setString(4, lecture.getLectureRoom());
			pstmt.setInt(5, lecture.getLectureCredit());
			pstmt.setString(6, lecture.getLectureYear());
			pstmt.setInt(7, lecture.getLectureSemester());
			pstmt.setString(8, lecture.getLectureSection());
			pstmt.setString(9, lecture.getLectureType());
			pstmt.setInt(10, lecture.getLectureCapacity());
			pstmt.setInt(11, lecture.getProfessorId());
			pstmt.setInt(12, lecture.getMajorId());
			pstmt.setInt(13, lecture.getBuildingId());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("insertLecture 실패", e);
		}
	}
	
	// 분반 카운트
	public int countSections(int professorId, int lectureCode, String lectureYear, int lectureSemester) {
	    String sql = "SELECT COUNT(*) AS cnt "
	               + "FROM lecture "
	               + "WHERE professor_id = ? "
	               + "  AND lecture_code = ? "
	               + "  AND lecture_year = ? "
	               + "  AND lecture_semester = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, professorId);
	        pstmt.setInt(2, lectureCode);
	        pstmt.setString(3, lectureYear);
	        pstmt.setInt(4, lectureSemester);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("cnt");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("countSections 예외: " + e);
	    }
	    return 0;
	}


	// professor_id로 major_id 찾기
	public int findMajorIdByProfessorId(int professorId) {
		String sql = "SELECT major_id FROM professor WHERE professor_id = ?";
		
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, professorId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("major_id");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("findMajorIdByProfessorId() 예외: " + e);
	    }
	    return 0;
	}
	
	// lecture_id로 major_id 찾기
	public LectureDTO fingdMajorIdAndTypeByLectureId(int lectureId){
		String sql = "SELECT major_id, lecture_type FROM lecture WHERE lecture_id = ?";
		
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, lectureId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	            	LectureDTO dto = new LectureDTO();
	            	dto.setMajorId(rs.getInt("major_id"));
	            	dto.setLectureType(rs.getString("lecture_type"));
	            	return dto;
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("findMajorIdByProfessorId() 예외: " + e);
	    }
	    return null;
	}

	// major_id로 major_code찾기
	public String findMajorCodeByMajorId(int majorId) {
		String sql = "SELECT major_code FROM major WHERE major_id = ?";
		
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, majorId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("major_code"); // 예: "0100"
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("findMajorCodeByMajorId() 예외: " + e);
	    }
	    return null;
	}
	
	// 학과(major_id)별 현재 최대 lecture_code 찾기
	public Integer findMaxLectureCodeByMajorId(int majorId) {
		String sql = "SELECT MAX(lecture_code) AS max_code FROM lecture WHERE major_id = ?";
		
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, majorId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                int max = rs.getInt("max_code");
	                if (rs.wasNull()) return null;  // 아직 강의가 하나도 없으면 null
	                return max;
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("findMaxLectureCodeByMajorId() 예외: " + e);
	    }
	    return null;
	}
	
	// 교수가 속한 학과의 강의 리스트 조회 (검색, 페이징 포함)
	public ArrayList<LectureDTO> findPageByMajor(
	        int majorId, int page, int limit, String keyword
	) {
	    int offset = (page - 1) * limit;

	    String sql =
	    	    "SELECT l.lecture_id, l.lecture_code, l.lecture_name, l.lecture_room, " +
	    	    "       l.lecture_credit, l.lecture_year, l.lecture_semester, l.lecture_section, " +
	    	    "       l.lecture_type, l.lecture_current_people, l.lecture_capacity, " +
	    	    "       l.professor_id, l.major_id, p.professor_name, " +
	    	    "       b.building_name " +
	    	    "FROM lecture l " +
	    	    "JOIN professor p ON l.professor_id = p.professor_id " +
	    	    "JOIN building b ON l.building_id = b.building_id " +
	    	    "WHERE l.major_id = ? ";

	    if (keyword != null && !keyword.isBlank()) {
	        sql += "AND (l.lecture_name LIKE ? OR p.professor_name LIKE ? OR b.building_name LIKE ?) ";
	    }

	    sql += "ORDER BY l.lecture_year DESC, l.lecture_semester DESC, l.lecture_code ASC, l.lecture_section ASC " +
	           "LIMIT ? OFFSET ?";

	    ArrayList<LectureDTO> list = new ArrayList<>();
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        int i = 1;
	        pstmt.setInt(i++, majorId);
	        if (keyword != null && !keyword.isBlank()) {
	            String k = "%" + keyword.trim() + "%";
	            pstmt.setString(i++, k);
	            pstmt.setString(i++, k);
	            pstmt.setString(i++, k);
	        }
	        pstmt.setInt(i++, limit);
	        pstmt.setInt(i, offset);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                LectureDTO lecture = new LectureDTO();
	                lecture.setLectureId(rs.getInt("lecture_id"));
	                lecture.setLectureCode(rs.getInt("lecture_code"));
	                lecture.setLectureName(rs.getString("lecture_name"));
	                lecture.setLectureRoom(rs.getString("lecture_room"));
	                lecture.setLectureCredit(rs.getInt("lecture_credit"));
	                lecture.setLectureYear(rs.getString("lecture_year"));
	                lecture.setLectureSemester(rs.getInt("lecture_semester"));
	                lecture.setLectureSection(rs.getString("lecture_section"));
	                lecture.setLectureType(rs.getString("lecture_type"));
	                lecture.setLectureCurrentPeople(rs.getInt("lecture_current_people"));
	                lecture.setLectureCapacity(rs.getInt("lecture_capacity"));
	                lecture.setProfessorId(rs.getInt("professor_id"));
	                lecture.setMajorId(rs.getInt("major_id"));
	                lecture.setProfessorName(rs.getString("professor_name"));
	                lecture.setBuildingName(rs.getString("building_name"));
	                list.add(lecture);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("findPageByMajor 예외: " + e);
	    }
	    return list;
	}
	
	// 페이징용 총 강의 개수
	public int countByMajor(int majorId, String keyword) {
	    String sql =
	        "SELECT COUNT(*) FROM lecture l " +
	        "JOIN professor p ON l.professor_id = p.professor_id " +
	        "WHERE l.major_id = ? ";
	    if (keyword != null && !keyword.isBlank()) {
	        sql += "AND (l.lecture_name LIKE ? OR p.professor_name LIKE ?) ";
	    }

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        int i = 1;
	        pstmt.setInt(i++, majorId);
	        if (keyword != null && !keyword.isBlank()) {
	            String k = "%" + keyword.trim() + "%";
	            pstmt.setString(i++, k);
	            pstmt.setString(i++, k);
	        }
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) return rs.getInt(1);
	        }
	    } catch (Exception e) {
	        System.out.println("countByMajor() 예외: " + e);
	    }
	    return 0;
	}
	
	// 강의 코드가 존재하는지 판별하기 위한 강의 조회
	public Integer findLectureCode(int majorId, String lectureName, int lectureCredit, String lectureType) {
	    String sql =
	        "SELECT lecture_code FROM lecture " +
	        "WHERE major_id = ? " +
	        "  AND lecture_name = ? " +
	        "  AND lecture_credit = ? " +
	        "  AND lecture_type = ? " +
	        "ORDER BY lecture_code " +
	        "LIMIT 1";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, majorId);
	        pstmt.setString(2, lectureName);
	        pstmt.setInt(3, lectureCredit);
	        pstmt.setString(4, lectureType);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("lecture_code");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("findLectureCode() 예외: " + e);
	    }
	    return null;
	}
	
}
