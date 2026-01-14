package com.goodeelms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreEnrollmentDTO {
	private int preEnrollmentId;
	private int studentId;
	private int lectureId;
}
