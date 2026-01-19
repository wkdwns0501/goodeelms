package com.goodeelms.service;

import com.goodeelms.dao.ProfessorDAO;
import com.goodeelms.dto.ProfessorDTO;

public class ProfessorSignUpService {
	ProfessorDAO dao = ProfessorDAO.getInstance();

	public int signup(ProfessorDTO dto) {
		if (dao.isEmailExist(dto.getProfessorEmail())) {
			return -1; 
		}

		int result = dao.addProfessor(dto);

		return result;
	}
}
