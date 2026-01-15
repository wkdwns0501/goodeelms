package com.goodeelms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDTO {
	private int lectureId;
	private int lectureCode;
	private String lectureName;
	private String lectureDescription;
	private String lectureRoom;
	private int lectureCredit;
	private String lectureYear;
	private int lectureSemester;
	private String lectureSection;
	private String lectureType;
	private int lectureCurrentPeople;
	private int lectureCapacity;
	private int professorId;
	private String majorName;
	private int majorId;
	private String professorName;
	
	// 강의 코드 출력용
	public String getLectureCodeDisplay() {
	    return String.format("%07d", lectureCode);
	}
}
