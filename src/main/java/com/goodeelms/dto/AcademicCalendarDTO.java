package com.goodeelms.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicCalendarDTO {
	// DB TABLE COLUMN
	private int academicCalendarId;
	private String academicEventName;
	private String academicEventDate;
	private String academicEventTime;
	private int adminId;
	
	private ZonedDateTime eventZoneDateTime;
}
