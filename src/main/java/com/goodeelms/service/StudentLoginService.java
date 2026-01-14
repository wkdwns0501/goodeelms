package com.goodeelms.service;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentDTO;

public class StudentLoginService {

    public StudentDTO checkIdAndPass(String student_no, String student_password) {
        StudentDAO dao = new StudentDAO();
        StudentDTO dto = dao.checkStudent(student_no, student_password);
        
        if (dto == null) return null; 
        if ( !(student_no.equals(dto.getStudentNo())) || !(student_password.equals(dto.getStudentPassword()))) {
        		return null;
        }
        
        return dto;
    }
}