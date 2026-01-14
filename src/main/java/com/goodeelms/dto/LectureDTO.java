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
	private int lectureSemester;
	private String lectureType;
	private int lectureCurrentPeople;
	private int lectureCapacity;
	private int majorId;
	private int professorId;
}
