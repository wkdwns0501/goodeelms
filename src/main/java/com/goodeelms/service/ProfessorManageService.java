package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.ProfessorManageDAO;
import com.goodeelms.dto.ProfessorDTO;

public class ProfessorManageService {
	
	// 처음으로 페이지 진입시 전체 교수 목록 조회
	public ArrayList<ProfessorDTO> getAllProfessorList() {
		ProfessorManageDAO dao = ProfessorManageDAO.getInstance();
		return dao.getAllProfessorList();
	}
	
	// 검색 조건에 따른 교수 목록 조회
	public ArrayList<ProfessorDTO> getProfessorList(String professorName, String majorName, String professorEmail) {
		ProfessorManageDAO dao = ProfessorManageDAO.getInstance();
		return dao.getProfessorList(professorName, majorName, professorEmail);
	}
	
	// 교수 테이블 상태 update
	public int updateProfessorStatus(String professorId, String newProfessorStatus) {
		ProfessorManageDAO dao = ProfessorManageDAO.getInstance();
		return dao.updateProfessorStatus(professorId, newProfessorStatus);
	}
	
}
