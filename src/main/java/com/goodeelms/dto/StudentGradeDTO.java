package com.goodeelms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentGradeDTO {
	private int lectureId;
	private int lectureCode;
	private String lectureName;
	private String lectureSection;
	private String lectureYear;
	private int lectureSemester;
	private Double lectureScore;
	private String professorName;
	
	// 강의 코드 출력용
	public String getLectureCodeDisplay() {
	    return String.format("%07d", lectureCode);
	}
}
