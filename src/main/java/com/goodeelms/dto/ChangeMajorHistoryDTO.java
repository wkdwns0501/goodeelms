package com.goodeelms.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMajorHistoryDTO {
	private int changeMajorId;
	private LocalDateTime changedAt;
	private int studentId;
	private int fromMajorId;
	private int toMajorId;
}
