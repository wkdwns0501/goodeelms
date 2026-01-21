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
	private String preEnrollmentStatus;
	private int lectureCapacity;	// 장바구니 마감 시 자동 신청 분기용
	private int lectureEnrollCount;	// 장바구니 마감 시 자동 신청 분기용
}
