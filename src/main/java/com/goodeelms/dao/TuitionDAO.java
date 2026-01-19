package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import com.goodeelms.dto.TuitionPaymentDTO;
import com.goodeelms.util.DBUtil;

public class TuitionDAO {
	private static final TuitionDAO instance = new TuitionDAO();

	private TuitionDAO() {
	}

	public static TuitionDAO getInstance() {
		return instance;
	}
	
	public TuitionPaymentDTO getTuitionByStudentId(int studentId) {
		String sql = "SELECT * FROM tuition_payment "
				+ "WHERE student_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, studentId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					TuitionPaymentDTO dto = new TuitionPaymentDTO();
					dto.setPaymentId(rs.getInt("payment_id"));
					dto.setPaymentAmount(rs.getInt("payment_amount"));
					dto.setPaymentStatus(rs.getString("payment_status"));
					dto.setPaymentDate(rs.getObject("payment_date", LocalDateTime.class));
					dto.setStudent_id(rs.getInt("student_id"));
					return dto;
				}
			} catch(Exception e) {
				System.out.println("getTuitionByStudentId() 쿼리 실행 예외 발생: " + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("getTuitionByStudentId() DB 연결 예외 발생: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public boolean updatePayment(int amount, String status, int studentId) {
		String sql = "UPDATE tuition_payment SET " + "payment_amount = payment_amount + ?, " + "payment_status = ?, "
				+ "payment_date = CURRENT_TIMESTAMP " + "WHERE student_id = ?";
		boolean result = false;
		
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, amount);
			pstmt.setString(2, status);
			pstmt.setInt(3, studentId);

			int queryResult = pstmt.executeUpdate();
			if (queryResult > 0) result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}