package com.goodeelms.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	
	// 0120 임욱 / major 테이블 조인용 필드, 메서드 추가
	private String fromMajorName;
	private String toMajorName;
	
	public String getFormattedChangedMajorDate() {
	    if (this.changedAt == null) return "";
	    return this.changedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
