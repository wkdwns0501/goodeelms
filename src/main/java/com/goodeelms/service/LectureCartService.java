package com.goodeelms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodeelms.dao.LectureCartDAO;
import com.goodeelms.dao.LectureHistoryDAO;
import com.goodeelms.dto.PreEnrollmentDTO;
import com.goodeelms.listener.CloseCartListener;
import com.goodeelms.util.DBUtil;

public class LectureCartService {
	LectureCartDAO dao = LectureCartDAO.getInstance();
	LectureHistoryDAO historyDAO = LectureHistoryDAO.getInstance();
	
	public int insertLectureOnCart(String lectureId, String studentId) {
		
		return dao.insertLectureOnCart(lectureId, studentId);
	}
	
	public int deleteLectureOnCart(String lectureId, String studentId) {
		return dao.deleteLectureOnCart(lectureId, studentId);
	}
	
	public int clearCart(String studentId, List<PreEnrollmentDTO> cartList) {
		
		Set<Integer> set = cartList.stream().map(PreEnrollmentDTO::getLectureId).collect(Collectors.toSet());
		
		return dao.clearCart(studentId, set);
	}
	
	public int simpleSearchBeforeAdd(String lectureId, String studentId) {
		return dao.simpleSearchCart(lectureId, studentId);
	}
	
	public List<PreEnrollmentDTO> getCartDataOfStudent(int studentId, Set<String> conditions){
		return dao.getPreEnrollment(studentId, conditions);
	}
	
	public void closeCartStatus() {
		// progress가 없으면 실행할 필요 없지. 전부 반영 됐다고 본다.
		List<PreEnrollmentDTO> list = dao.checkProgressCart();
		if(list == null || list.size() == 0) return;
		
		// 자동 신청, 추후 신청 대상 분기 Map, Set
		// 자동 신청은 lecture 테이블에 값 넣어야함
		Map<Integer, Integer> autoEnrollMap = new HashMap<Integer, Integer>();
		Set<Integer> reEnrollSet = new HashSet<Integer>();
		
		for(PreEnrollmentDTO dto : list) {
			// 수용 인원 vs 신청 인원
			int capa = dto.getLectureCapacity();
			int enroll = dto.getLectureEnrollCount();
			
			if(capa >= enroll) {
				autoEnrollMap.put(dto.getLectureId(), dto.getLectureEnrollCount());
			}
			else {
				reEnrollSet.add(dto.getLectureId());
			}
		}
		
		// pre_enrollment_status UPDATE -> completed or re_apply
		Connection conn = DBUtil.getBatchConnection();
		try{
			conn.setAutoCommit(false);
			if(autoEnrollMap.size() > 0) {
				String setStatus = "completed";
				// 모든 DAO 메서드에 동일한 conn 객체를 전달해야 합니다.
		        // 예: dao.updateLectureCurrentPeople(conn, autoEnrollMap);
		        
		        // 1. 인원수 업데이트 실행
				int peopleResult = dao.UpdateLectureCurrentPeople(conn, autoEnrollMap);
				if(peopleResult < 1) throw new SQLException("lecture_current_people value UPDATE 실패");
				
				// 2. lecture_history에 저장
				int insertResult = historyDAO.insertLectureHistoryByNewLecture(conn, autoEnrollMap.keySet());
				if(insertResult < 1) throw new SQLException("lecture_history INSERT 실패");
				
				// 3. 상태 업데이트 실행
				int statResult = dao.UpdatePreEnrollmentStatus(conn, autoEnrollMap.keySet(), setStatus);
				if(statResult < 1) throw new SQLException("pre_enrollment_status completed UPDATE 실패");
			}	
			if(reEnrollSet.size() > 0) {
				// 2. 상태 업데이트 실행
				String setStatus = "re_apply";
				int result = dao.UpdatePreEnrollmentStatus(conn, reEnrollSet, setStatus);
				if(result < 1) throw new SQLException("pre_enrollment_status re_apply UPDATE 실패");
			}
			
			conn.commit();
			System.out.println("수강 신청 처리가 성공적으로 완료되었습니다.");
		}
		catch(SQLException e) {
			if(conn != null) {
				try {
					conn.rollback();
					System.err.println("트랜잭션 롤백됨: " + e.getMessage());
				}
				catch (SQLException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
