package com.goodeelms.service;

import com.goodeelms.dao.StudentGradeDAO;

public class StudentGradeService {
	private static final StudentGradeService instance = new StudentGradeService();
	private final StudentGradeDAO gradeDAO = StudentGradeDAO.getInstance();
	public StudentGradeService() {}	
	
	public static StudentGradeService getInstance() {
		return instance;
	}

}
