package com.goodeelms.service;

import com.goodeelms.dao.AddCartDAO;

public class AddLectureOnCartService {
	AddCartDAO dao = AddCartDAO.getInstance();
	
	public int insertLectureOnCart(String lectureId, String studentId) {
		
		return dao.insertLectureOnCart(lectureId, studentId);
	}
	
	public int simpleSearchBeforeAdd(String lectureId, String studentId) {
		return dao.simpleSearchCart(lectureId, studentId);
	}
}
