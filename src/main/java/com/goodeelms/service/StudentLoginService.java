package com.goodeelms.service;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentDTO;

public class StudentLoginService {
    private StudentDAO dao = StudentDAO.getInstance();

    public StudentDTO checkIdAndPass(String student_no, String student_password) {
        return dao.checkStudent(student_no, student_password); 
    }
}