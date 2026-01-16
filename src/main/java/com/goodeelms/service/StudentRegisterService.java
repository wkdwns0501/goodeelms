package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.StudentRegisterDAO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.MajorDTO;
import com.goodeelms.dto.StudentDTO;

public class StudentRegisterService {

	public int studentExistCheck(String studentNo) {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.studentExistCheck(studentNo);
	}

	public int studentRegister(StudentDTO studentDTO) {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.studentRegister(studentDTO);
	}

	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.getStudentList(studentName, majorName, studentNo);
	}

	public ArrayList<StudentDTO> getAllStudentList() {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.getAllStudentList();
	}

	public ArrayList<MajorDTO> getMajorList() {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.getMajorList();
	}

	public int getNewStudentId() {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.getNewStudentId();
	}

	public int writeStudentMajor(int newStudentId, int majorId) {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.writeStudentMajor(newStudentId, majorId);
	}

}
