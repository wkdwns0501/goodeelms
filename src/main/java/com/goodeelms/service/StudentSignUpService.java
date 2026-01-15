package com.goodeelms.service;

import java.sql.SQLException;

import com.goodeelms.dao.StudentDAO;
import com.goodeelms.dto.StudentDTO;

public class StudentSignUpService {

    public int signUpStudent(StudentDTO dto, String originPassword, int studentId) throws SQLException {
        StudentDAO dao = StudentDAO.getInstance();
        
        if (dto.getStudentPassword().equals(originPassword)) { 
            return -1; 
        }
        
        return dao.updateStudent(dto, studentId);
    }
}
