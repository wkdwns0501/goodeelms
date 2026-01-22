package com.goodeelms.dao;

public class StudentGradeDAO {
	private static final StudentGradeDAO instance = new StudentGradeDAO();
    private StudentGradeDAO() {}
    
    public static StudentGradeDAO getInstance() {
        return instance;
    }
}
