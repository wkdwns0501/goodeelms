package com.goodeelms.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	
	public String getFormattedStatusAt() {
	    if (this.statusAt == null) return "";
	    return this.statusAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
}
