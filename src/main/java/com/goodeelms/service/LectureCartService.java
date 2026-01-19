package com.goodeelms.service;

import java.util.List;

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
	
	public int simpleSearchBeforeAdd(String lectureId, String studentId) {
		return dao.simpleSearchCart(lectureId, studentId);
	}
	
	public List<PreEnrollmentDTO> getCartDataOfStudent(int studentId){
		return dao.getPreEnrollment(studentId);
	}
	
}
