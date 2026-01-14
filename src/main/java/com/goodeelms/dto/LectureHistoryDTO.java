package com.goodeelms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureHistoryDTO {
	private int studentId;
	private int lectureId;
	private String lectureStatus;
	private double lectureScore;
}
