package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.goodeelms.dto.ScholarshipDTO;
import com.goodeelms.util.DBUtil;

public class ScholarshipDAO {
	private static final ScholarshipDAO instance = new ScholarshipDAO();
	private ScholarshipDAO() {}
	public static ScholarshipDAO getInstance() {
		return instance;
	}
	
	public List<ScholarshipDTO> getSemesterAndAmountByStudentId(int studentId){
		String sql = "SELECT scholarship_semester, scholarship_amount "
				+ "FROM scholarship_history SH "
				+ "JOIN student S ON S.student_id = SH.student_id "
				+ "WHERE sh.student_id = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			
			try (ResultSet rs = pstmt.executeQuery()) {  
				List<ScholarshipDTO> list = new ArrayList<ScholarshipDTO>();
				
				while (rs.next()) {
					ScholarshipDTO dto = new ScholarshipDTO();
					dto.setScholarshipSemester(rs.getInt("scholarship_semester"));
					dto.setScholarshipAmount(rs.getInt("scholarship_amount"));
					list.add(dto);
				}
				return list;
			} catch (Exception e) {
				System.out.println("getSemesterAndAmountByStudentId() 쿼리 실행 예외: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("getSemesterAndAmountByStudentId() DB 연결 예외: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
}
