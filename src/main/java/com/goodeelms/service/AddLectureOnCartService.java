package com.goodeelms.service;

import java.util.List;

import com.goodeelms.dao.AddCartDAO;
import com.goodeelms.dto.PreEnrollmentDTO;

public class AddLectureOnCartService {
	AddCartDAO dao = AddCartDAO.getInstance();
	
	public int insertLectureOnCart(String lectureId, String studentId) {
		
		return dao.insertLectureOnCart(lectureId, studentId);
	}
	
	public int simpleSearchBeforeAdd(String lectureId, String studentId) {
		return dao.simpleSearchCart(lectureId, studentId);
	}
	
	public List<PreEnrollmentDTO> getCartDataOfStudent(String studentId){
		return dao.getPreEnrollment(studentId);
	}
}
