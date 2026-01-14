package com.goodeelms.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TuitionPaymentDTO {
	private int paymentId;
	private int paymentAmount;
	private LocalDateTime paymentDate;
	private String paymentStatus;
	private int student_id;
}
