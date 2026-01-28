package com.goodeelms.service;

import com.goodeelms.dao.ProfessorDAO;
import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.util.EncryptUtil;

public class ProfessorSignUpService {
	ProfessorDAO dao = ProfessorDAO.getInstance();

	public int signup(ProfessorDTO dto) {
		if (dao.isEmailExist(dto.getProfessorEmail())) {
			return -1; 
		}

		int result = dao.addProfessor(dto);

		return result;
	}
	
	public String resetPassowordByEmailAndName(String email, String newPassword) {
		String msg = "";
		ProfessorDTO dto = dao.getProfessorByEmail(email); // 
		
		if(dto == null) return msg= "일치하는 유저가 없습니다.";
		if(dto.getProfessorEmail().equals(email)) {
			if(dao.updateProfessorPassword(email, EncryptUtil.encryptPassword(newPassword))){
				msg = "비밀번호 재설정에 성공했습니다.";
			}
		}
		
		return msg;
	}
	
}
