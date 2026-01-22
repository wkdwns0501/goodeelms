package com.goodeelms.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureEvaluationDTO {
	private int evaluationId;
	private int rating;
	private String comment;
	private LocalDateTime evaluatedAt;
	private int studentId;
	private int lectureId;
}
