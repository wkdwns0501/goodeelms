package com.goodeelms.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipDTO {
	private int scholarshipId;
	private int scholarshipSemester;
	private int scholarshipAmount;
}
