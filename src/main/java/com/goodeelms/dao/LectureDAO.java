package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
	
	// (교수) 학과(major_id)별 강의 리스트 조회 (검색, 페이징, 상태필터 포함)
	public ArrayList<LectureDTO> findPageByMajor(int majorId, int page, int limit, String keyword, String statusFilter) {
	    int offset = (page - 1) * limit;

	    String sql =
	        "SELECT l.lecture_id, l.lecture_code, l.lecture_name, l.lecture_room, " +
	        "       l.lecture_credit, l.lecture_year, l.lecture_semester, l.lecture_section, " +
	        "       l.lecture_type, l.lecture_current_people, l.lecture_capacity, l.lecture_status, " +
	        "       l.lecture_description, l.professor_id, l.major_id, p.professor_name, " +
	        "       b.building_name " +
	        "FROM lecture l " +
	        "JOIN professor p ON l.professor_id = p.professor_id " +
	        "JOIN building b ON l.building_id = b.building_id " +
	        "WHERE l.major_id = ? ";

	    if ("ACTIVE".equals(statusFilter)) {
	        sql += "AND l.lecture_status IN ('예정', '개강') ";
	    }

	    if (keyword != null && !keyword.isBlank()) {
	        sql += "AND (l.lecture_name LIKE ? OR p.professor_name LIKE ? OR b.building_name LIKE ?) ";
	    }

	    sql += "ORDER BY l.lecture_id DESC LIMIT ? OFFSET ?";

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
	                lecture.setLectureStatus(rs.getString("lecture_status"));
	                lecture.setLectureDescription(rs.getString("lecture_description"));
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


	// (교수) 페이징용 학과별 총 강의 개수
	public int countByMajor(int majorId, String keyword, String statusFilter) {
	    String sql =
	        "SELECT COUNT(*) " +
	        "FROM lecture l " +
	        "JOIN professor p ON l.professor_id = p.professor_id " +
	        "JOIN building b ON l.building_id = b.building_id " +
	        "WHERE l.major_id = ? ";

	    if ("ACTIVE".equals(statusFilter)) {
	        sql += "AND l.lecture_status IN ('예정', '개강') ";
	    }

	    if (keyword != null && !keyword.isBlank()) {
	        sql += "AND (l.lecture_name LIKE ? OR p.professor_name LIKE ? OR b.building_name LIKE ?) ";
	    }

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
	
	// 수강신청 적용 시 count++
	public int updateLectureCurrentPeople(Connection conn, String lectureId) {
		String sql = "UPDATE lecture " +
	             "SET lecture_current_people = lecture_current_people + 1 " +
	             "WHERE lecture_id = ? AND lecture_current_people < lecture_capacity";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, lectureId);
			
			return pstmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// student_id로 수강 예정 강의 조회하기
	public List<LectureDTO> getLectureOfStudent(String student_id){
		String sql = "SELECT l.lecture_id as lecture_id, l.lecture_code, l.lecture_name, l.lecture_credit, " 
					+"l.lecture_type, l.professor_id, l.major_id " 
					+ "FROM lecture_history as lh "
					+ "JOIN lecture as l ON l.lecture_id = lh.lecture_id "
					+ "WHERE lh.student_id = ? AND l.lecture_status = '예정'";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setString(1, student_id);
			
			try(ResultSet rs = pstmt.executeQuery()){
				List<LectureDTO> list = new ArrayList<LectureDTO>();
				while(rs.next()) {
					LectureDTO dto = new LectureDTO();
					dto.setLectureId(rs.getInt("lecture_id"));
					dto.setLectureCode(rs.getInt("lecture_code"));
					dto.setLectureName(rs.getString("lecture_name"));
					dto.setLectureCredit(rs.getInt("lecture_credit"));
					dto.setLectureType(rs.getString("lecture_type"));
					dto.setProfessorId(rs.getInt("professor_id"));
					dto.setMajorId(rs.getInt("major_id"));
					
					list.add(dto);
				}
				return list;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// lecture_id로 lecture_code 가져오기
	public Set<Integer> getLectureCodeWithLectureId(Set<Integer> lectureIds){
		if(lectureIds.size() == 0) return null;
		
		String sql = "SELECT lecture_code FROM lecture WHERE lecture_id IN("
				+ lectureIds.stream().map(id -> "?").collect(Collectors.joining(",")) +")";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			int index = 1;
			for(Integer id : lectureIds) {
				pstmt.setInt(index++, id);
			}
			
			try(ResultSet rs = pstmt.executeQuery()){
				Set<Integer> set = new HashSet<Integer>();
				while(rs.next()) {
					set.add(rs.getInt("lecture_code"));
				}
//				System.out.println("getLectureCodeWithLectureId_SetSize: " + set.size());
				return set;
			}
			catch(SQLException e) {
				System.out.println("ResultSet Ex");
				e.printStackTrace();
			}
		}
		catch(SQLException e) {
			System.out.println("getLectureCodeWithLectureId Catch");
			e.printStackTrace();
		}
		return null;
	}
	
	// (학생) 전체 강의 리스트 조회 (검색, 페이징 포함)
	public ArrayList<LectureDTO> findPageAll(int page, int limit, String keyword) {
	    int offset = (page - 1) * limit;

	    String sql =
	        "SELECT l.lecture_id, l.lecture_code, l.lecture_name, l.lecture_room, " +
	        "       l.lecture_credit, l.lecture_year, l.lecture_semester, l.lecture_section, " +
	        "       l.lecture_type, l.lecture_current_people, l.lecture_capacity, " +
	        "       l.lecture_description, l.professor_id, l.major_id, p.professor_name, " +
	        "       b.building_name " +
	        "FROM lecture l " +
	        "JOIN professor p ON l.professor_id = p.professor_id " +
	        "JOIN building b ON l.building_id = b.building_id " +
	        "WHERE l.lecture_status IN ('예정', '개강') ";

	    if (keyword != null && !keyword.isBlank()) {
	        sql += "AND (l.lecture_name LIKE ? OR p.professor_name LIKE ? OR b.building_name LIKE ?) ";
	    }

	    sql += "ORDER BY l.lecture_id DESC LIMIT ? OFFSET ?";

	    ArrayList<LectureDTO> list = new ArrayList<>();
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        int i = 1;
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
	                lecture.setLectureDescription(rs.getString("lecture_description"));
	                lecture.setProfessorId(rs.getInt("professor_id"));
	                lecture.setMajorId(rs.getInt("major_id"));
	                lecture.setProfessorName(rs.getString("professor_name"));
	                lecture.setBuildingName(rs.getString("building_name"));
	                list.add(lecture);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("findPageAll 예외: " + e);
	    }
	    return list;
	}
	
	// (학생) 페이징용 전체 강의 개수
	public int countAll(String keyword) {
	    String sql =
	        "SELECT COUNT(*) " +
	        "FROM lecture l " +
	        "JOIN professor p ON l.professor_id = p.professor_id " +
	        "JOIN building b ON l.building_id = b.building_id " +
	        "WHERE l.lecture_status IN ('예정', '개강') ";

	    if (keyword != null && !keyword.isBlank()) {
	        sql += "AND (l.lecture_name LIKE ? OR p.professor_name LIKE ? OR b.building_name LIKE ?) ";
	    }

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        int i = 1;
	        if (keyword != null && !keyword.isBlank()) {
	            String k = "%" + keyword.trim() + "%";
	            pstmt.setString(i++, k);
	            pstmt.setString(i++, k);
	            pstmt.setString(i++, k);
	        }
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) return rs.getInt(1);
	        }
	    } catch (Exception e) {
	        System.out.println("countAll() 예외: " + e);
	    }
	    return 0;
	}
	
	// 강의 상태 예정 -> 개강 변경
	public int updateLectureStatusToNext(int year, int semester, String beforeStatus, String afterStatus) {
		String sql = "UPDATE lecture SET lecture_status = ? WHERE lecture_status = ? "
				+ "AND lecture_year = ? AND lecture_semester = ? ";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setString(1, afterStatus);
			pstmt.setString(2, beforeStatus);
			pstmt.setInt(3, year);
			pstmt.setInt(4, semester);
			
			return pstmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Set<Integer> getLectureIdsBeforeUpdate(Connection conn, int year, int semester, String status){
		String sql = "SELECT lecture_id FROM lecture WHERE lecture_year = ? "
				+ "AND lecture_semester = ? AND lecture_status = ?";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){

				pstmt.setInt(1, year);
				pstmt.setInt(2, semester);
				pstmt.setString(3, status);
				
				try(ResultSet rs = pstmt.executeQuery()){
					Set<Integer> list = new HashSet<Integer>();
					while(rs.next()) {
						int id = rs.getInt("lecture_id");
						
						list.add(id);
					}
					return list;
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	// 개강 업데이트 검증 확인 조회
	public boolean selectLecutreStatusBeforeSchedule(int year, int semester, String status) {
		String sql = "SELECT COUNT(*) as cnt FROM lecture WHERE lecture_status = ? "
				+ "AND lecture_year = ? AND lecture_semester = ? ";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){

			pstmt.setString(1, status);
			pstmt.setInt(2, year);
			pstmt.setInt(3, semester);
			
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					int result = rs.getInt("cnt");
					if(result > 0) return true;
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	// 강의실 중복 존재 여부 체크
	public boolean existsLectureRoom(int buildingId, String room, String year, int semester) {
	    String sql ="SELECT COUNT(*) FROM lecture WHERE building_id = ? " +
			        " AND lecture_room = ? AND lecture_year = ? AND lecture_semester = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, buildingId);
	        pstmt.setString(2, room);
	        pstmt.setString(3, year);
	        pstmt.setInt(4, semester);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// 점유된 강의실 목록 조회
	public List<String> findOccupiedRooms(int buildingId, String year, int semester) {
	    String sql ="SELECT lecture_room FROM lecture WHERE building_id = ? " +
	    			" AND lecture_year = ? AND lecture_semester = ?";

	    List<String> list = new ArrayList<>();
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, buildingId);
	        pstmt.setString(2, year);
	        pstmt.setInt(3, semester);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                list.add(rs.getString("lecture_room"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
}
