package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.StudentDTO;
import com.goodeelms.dto.StudentMajorDTO;
import com.goodeelms.util.DBUtil;

public class StudentDAO {
	private static final StudentDAO instance = new StudentDAO();
	private StudentDAO() {}
	public static StudentDAO getInstance() {
		return instance;
	}

	public boolean createStudent(StudentDTO dto) {
		boolean result = false;

		String sql = "INSERT INTO student (student_name, student_password, student_identity_number, "
				+ "student_phone, student_gender, student_address, student_status, student_email, student_bank) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, dto.getStudentName());
	        pstmt.setString(2, dto.getStudentPassword()); 
	        pstmt.setString(3, dto.getStudentIdentityNumber()); 
	        pstmt.setString(4, dto.getStudentPhone());
	        pstmt.setString(5, dto.getStudentGender()); 
	        pstmt.setString(6, dto.getStudentAddress());
	        pstmt.setString(7, dto.getStudentStatus());
	        pstmt.setString(8, dto.getStudentEmail());
	        pstmt.setString(9, dto.getStudentBank());

			int resultQuerry = pstmt.executeUpdate();
			if (resultQuerry > 0)
				result = true;
		} catch (Exception e) {
			System.out.println("createStudent() 쿼리 실행 예외: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public StudentDTO getStudentByNo(String no) {
		String sql = "SELECT * FROM student WHERE student_no  = ?";
		StudentDTO dto = null;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, no);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					dto = new StudentDTO();
					// === not null ===
					dto.setStudentId(rs.getInt("student_id"));
					dto.setStudentNo(rs.getString("student_no"));
					dto.setStudentName(rs.getString("student_name"));
					dto.setStudentPassword(rs.getString("student_password"));
					dto.setStudentIdentityNumber(rs.getString("student_identity_number"));
					dto.setStudentPhone(rs.getString("student_phone"));
					// === null ===
					dto.setStudentGender(rs.getString("student_gender"));
					dto.setStudentAddress(rs.getString("student_address"));
					dto.setStudentStatus(rs.getString("student_status"));
					dto.setStudentEmail(rs.getString("student_email"));
					dto.setStudentBank(rs.getString("student_bank"));
				}
			} catch (Exception e) {
				System.out.println("getStudentByNoAndPassword() 쿼리 실행 예외: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getStudentByNoAndPassword() DB 연결 예외: " + e.getMessage());
			e.printStackTrace();
		}
		return dto;
	}

	public boolean updateStudent(StudentDTO dto) {
	    String sql = "UPDATE student SET student_name = ?, student_phone = ?, "
	               + "student_address = ?, student_status = ?, student_gender = ?, "
	               + "student_email = ?, student_bank = ?, student_password = ? "
	               + "WHERE student_id = ?";
	    boolean result = false;

	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, dto.getStudentName());
	        pstmt.setString(2, dto.getStudentPhone());
	        pstmt.setString(3, dto.getStudentAddress());
	        pstmt.setString(4, dto.getStudentStatus());
	        pstmt.setString(5, dto.getStudentGender());
	        pstmt.setString(6, dto.getStudentEmail());
	        pstmt.setString(7, dto.getStudentBank());
	        pstmt.setString(8, dto.getStudentPassword()); 
	        pstmt.setInt(9, dto.getStudentId());

	        int queryResult = pstmt.executeUpdate();
	        if(queryResult > 0) result = true;
	    } catch (Exception e) {
	        System.out.println("updateStudent() 예외: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return result;
	}

	// UNIQUE 속성의 컬럼이 존재하는지 확인 (student_email, student_identity_number, student_phone)
	public boolean existsStudentUniqueColumn(StudentDTO dto) {
		String sql = "SELECT COUNT(*) as cnt FROM student " 
	               + "WHERE (student_identity_number = ? "
	               + "OR student_email = ? " 
	               + "OR student_phone = ?) "
	               + "AND student_id != ?";

		boolean result = false;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, dto.getStudentIdentityNumber());
			pstmt.setString(2, dto.getStudentEmail());
			pstmt.setString(3, dto.getStudentPhone());
			pstmt.setInt(4, dto.getStudentId());

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("cnt");
					if (count > 0) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("existsStudentUniqueColumn() 예외: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public StudentDTO getIdentityNumAndNo (int studentId) { // 0122 임욱 추가
		String sql = "SELECT student_identity_number, student_no FROM student WHERE student_id = ? ";
		StudentDTO dto = null;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					dto = new StudentDTO();
					dto.setStudentIdentityNumber(rs.getString("student_identity_number"));
					dto.setStudentNo(rs.getString("student_no"));
				}
			} catch(Exception e) {
				System.out.println("getIdentityNumAndNo() 예외: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getIdentityNumAndNo() 예외: " + e.getMessage());
			e.printStackTrace();
		}
		return dto;
	}
	
	
// =============== 01.17 (토) 이전 작성 =================== 
	public List<StudentMajorDTO> getMajors(String studentId) {
		String sql = "SELECT * FROM student_major WHERE student_id = ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, studentId);
			try(ResultSet rs = pstmt.executeQuery()){
				List<StudentMajorDTO> list = new ArrayList<StudentMajorDTO>();
				
				while(rs.next()) {
					StudentMajorDTO dto = new StudentMajorDTO();
					dto.setStudentId(Integer.parseInt(rs.getString("student_id")));
					dto.setMajorId(Integer.parseInt(rs.getString("major_id")));
					list.add(dto);
				}
				
				return list;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getStudentId(String studentId) {
		String sql = "SELECT student_id FROM student WHERE student_id = ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, studentId);
			try(ResultSet rs = pstmt.executeQuery()){
				if(!rs.next()) return null;
				return rs.getString("student_id");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 학생 정보 수정을 위한 조회
	public StudentDTO selectById(Connection conn, int studentId) throws SQLException {
        String sql =
            "SELECT student_id, student_no, student_name, student_phone, " +
            "       student_gender, student_address, student_status, student_email, student_bank, " +
            "FROM student WHERE student_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, studentId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if(!rs.next()) return null;
				StudentDTO student = new StudentDTO();
				student.setStudentId(rs.getInt("student_id"));
				student.setStudentNo(rs.getString("student_no"));
				student.setStudentName(rs.getString("student_name"));
				student.setStudentPhone(rs.getString("student_phone"));
				student.setStudentGender(rs.getString("student_gender"));
				student.setStudentAddress(rs.getString("student_address"));
				student.setStudentStatus(rs.getString("student_status"));
				student.setStudentEmail(rs.getString("student_email"));
				student.setStudentBank(rs.getString("student_bank"));
				return student;
			}
		}
    }
	
	// 학생 일반 정보 수정
	public void updateProfile(Connection conn, int studentId, String phone, 
							  String email, String address, String studentBank) throws SQLException {
		String sql = "UPDATE student " +
			         "SET student_phone = ?, student_email = ?, " +
			         "    student_address = ?, student_bank = ? " +
			         "WHERE student_id = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, phone);
	        pstmt.setString(2, email);
	        pstmt.setString(3, address);
	        pstmt.setString(4, studentBank);
	        pstmt.setInt(5, studentId);
	        pstmt.executeUpdate();
	    }
	}
	
	// 학생 비밀번호 수정을 위한 조회
	public String selectPasswordById(Connection conn, int studentId) throws SQLException {
	    String sql = "SELECT student_password FROM student WHERE student_id = ?";

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, studentId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (!rs.next()) return null;
	            return rs.getString("student_password");
	        }
	    }
	}
	
	// 학생 비밀번호 수정
	public int updatePassword(Connection conn, int studentId, String newPw) throws SQLException {
	    String sql = "UPDATE student SET student_password = ? WHERE student_id = ?";

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, newPw);
	        pstmt.setInt(2, studentId);
	        return pstmt.executeUpdate(); // 1이면 성공
	    }
	}

}
