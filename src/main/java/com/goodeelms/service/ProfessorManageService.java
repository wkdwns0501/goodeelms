package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.ProfessorManageDAO;
import com.goodeelms.dto.ProfessorDTO;

public class ProfessorManageService {
	public ArrayList<ProfessorDTO> getProfessorList(String professorName, String majorName, String professorEmail) {
		ProfessorManageDAO dao = ProfessorManageDAO.getInstance();
		return dao.getProfessorList(professorName, majorName, professorEmail);
	}
	public int updateProfessorStatus(String professorId, String newProfessorStatus) {
		ProfessorManageDAO dao = ProfessorManageDAO.getInstance();
		return dao.updateProfessorStatus(professorId, newProfessorStatus);
	}
}
