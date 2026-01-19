package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.StudentRegisterDAO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.MajorDTO;
import com.goodeelms.dto.StudentDTO;

public class StudentRegisterService {
	
	// 전체 학생 목록 조회 
	public ArrayList<StudentDTO> getAllStudentList() {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.getAllStudentList();
	}
	
	// 이미 사용중인 학번인지 검사
	public int studentExistCheck(String studentNo) {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.studentExistCheck(studentNo);
	}
	
	// student 테이블에 새 학생 등록
	public int studentRegister(StudentDTO studentDTO) {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.studentRegister(studentDTO);
	}
	
	// 검색 조건에 따른 학생 목록 조회
	public ArrayList<StudentDTO> getStudentList(String studentName, String majorName, String studentNo) {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.getStudentList(studentName, majorName, studentNo);
	}

	
	// 학과 목록 조회
	public ArrayList<MajorDTO> getMajorList() {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.getMajorList();
	}
	
	// 새로 등록할 학생의 student_id 정보 가져오기
	public int getNewStudentId() {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.getNewStudentId();
	}
	
	// student_major 테이블 작성
	public int writeStudentMajor(int newStudentId, int majorId) {
		StudentRegisterDAO dao = StudentRegisterDAO.getInstance();
		return dao.writeStudentMajor(newStudentId, majorId);
	}

}
