package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Map;

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
	
	public boolean insertPayment(int studentId, int amount, String status) { // 0122 임욱(추가) / 최초 납입시 추가
	    String sql = "INSERT INTO tuition_payment (student_id, payment_amount, payment_status) VALUES (?, ?, ?)";
	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, studentId);
	        pstmt.setInt(2, amount);
	        pstmt.setString(3, status);
	        return pstmt.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public int getTotalAmountByStudentId(int studentId) { // 0122 임욱(추가) / 누적 금액 조회
	    String sql = "SELECT SUM(payment_amount) FROM tuition_payment WHERE student_id = ?";
	    
	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, studentId);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1); 
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("getTotalAmountByStudentId 예외: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return 0; 
	}
	
	public TuitionPaymentDTO getTotalAmountAndData(int studentId) { // 0122 임욱(추가) / 날짜 포함 조회
	    String sql = "SELECT SUM(payment_amount) as total, MAX(payment_date) as latest " +
	                 "FROM tuition_payment WHERE student_id = ?";
	    
	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, studentId);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                TuitionPaymentDTO dto = new TuitionPaymentDTO();
	                dto.setStudent_id(studentId);
	                dto.setPaymentAmount(rs.getInt("total"));
	                
	                java.sql.Timestamp timestamp = rs.getTimestamp("latest");
	                if (timestamp != null) { 
	                    dto.setPaymentDate(timestamp.toLocalDateTime());
	                }
	                return dto;
	            }
	        }
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    }
	    return null;
	}
	
}