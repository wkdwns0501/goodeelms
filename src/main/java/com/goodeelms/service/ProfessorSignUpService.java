package com.goodeelms.service;

import com.goodeelms.dao.ProfessorDAO;
import com.goodeelms.dto.ProfessorDTO;

public class ProfessorSignUpService {
	ProfessorDAO dao = ProfessorDAO.getInstance();
	
	public int addProfessor(ProfessorDTO dto) {
		if(dao.isEmailExist(dto.getProfessorEmail())) {	// 입력한 이메일이 기존에 존재하는지 확인
			return -2;
		} 
		
		return dao.addProfessor(dto); 
	}
}
