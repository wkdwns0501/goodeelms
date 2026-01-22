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
	
	
	public String getFormattedSemester() {
	    String s = String.valueOf(this.scholarshipSemester); 
	    if (s.length() >= 5) {
	        return s.substring(0, 4) + "-" + s.substring(4);
	    }
	    return s;
	}
	
}
