package com.goodeelms.service;

import java.util.List;

import com.goodeelms.dao.MajorDAO;
import com.goodeelms.dto.MajorDTO;

public class MajorService {
	MajorDAO dao = MajorDAO.getInstance();
	
	// 0119 임욱(추가) / 모든 전공 목록 조회
	public List<MajorDTO> getAllMajor() {
	    return dao.findAll();
	}
	
	// 0120 임욱(추가) / 해당 학생의 전공 목록 조회
	public List<String> getMajorCodeAndMajorName(int studentId){
		return dao.getCodeAndNameByStudentId(studentId);
	}
	
}
