package com.goodeelms.service;

import com.goodeelms.dao.AdminDAO;
import com.goodeelms.dao.ProfessorDAO;
import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.AdminDTO;
import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.dto.StudentDTO;
import com.goodeelms.util.EncryptUtil;

public class CommonService {
	private StudentDAO studentDAO = StudentDAO.getInstance();
	private ProfessorDAO professorDAO = ProfessorDAO.getInstance();
	private AdminDAO adminDAO = AdminDAO.getInstance();

	public Object checkUserRoleByIdAndPassword(String id, String password) { // 1. 아이디와 일치하는 유저 조회 2. 비밀번호 비교 후 결과 반환
		Object obj = null;

		if (id.matches("\\d{9}")) { // 1-1. 아이디가 학번
			StudentDTO dto = studentDAO.getStudentByNo(id);
			
			System.out.println(EncryptUtil.encryptPassword(password));

			if (dto == null) return null;
			if (!EncryptUtil.isPasswordMatch(password, dto.getStudentPassword())) return null; // 2. 비밀번호 비교 
			
			
			obj = dto;
		} else if (id.contains("@")) { // 1-2. 아이디가 이메일 형식 = 교수
			ProfessorDTO dto = professorDAO.getProfessorByEmail(id);
			
			if (dto == null) return null;
			if (!EncryptUtil.isPasswordMatch(password, dto.getProfessorPassword())) return null; // 2. 비밀번호 비교 
				
			obj = dto;
		} else if (id.startsWith("admin")) { // 1-3. admin + 숫자 형식 = 관리자
			AdminDTO dto = adminDAO.getAdminBylogId(id);
			
			if (dto == null) return null;
			if (!EncryptUtil.isPasswordMatch(password, dto.getAdminPassword())) return null; // 2. 비밀번호 비교 	
				
			obj = dto;
		}
		
		
		return obj;
	}
	
}
