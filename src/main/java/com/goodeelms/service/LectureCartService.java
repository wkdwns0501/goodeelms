package com.goodeelms.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodeelms.dao.LectureCartDAO;
import com.goodeelms.dto.PreEnrollmentDTO;

public class LectureCartService {
	LectureCartDAO dao = LectureCartDAO.getInstance();
	
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
	
	public List<PreEnrollmentDTO> getCartDataOfStudent(int studentId){
		return dao.getPreEnrollment(studentId);
	}
	
}
