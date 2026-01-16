package com.goodeelms.service;

import com.goodeelms.dao.ProfessorDAO;
import com.goodeelms.dto.ProfessorDTO;

public class ProfessorLoginService {
	private ProfessorDAO dao = ProfessorDAO.getInstance();
	
	public ProfessorDTO checkIdAndPass(String professor_email, String professor_password) {
		return dao.checkProfessor(professor_email, professor_password);
	}
}
