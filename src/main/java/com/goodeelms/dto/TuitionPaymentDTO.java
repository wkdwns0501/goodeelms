package com.goodeelms.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	
	public String getFormattedPaymentDate() {
	    if (this.paymentDate == null) return "";
	    return this.paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
