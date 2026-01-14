package com.goodeelms.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentStatusHistoryDTO {
	private int statusHistoryId;
	private String statusType;
	private String statusReason;
	private LocalDateTime statusAt;
	private int studentId;
	private int adminId;
}
