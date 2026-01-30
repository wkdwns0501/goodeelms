package com.goodeelms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
	private int studentId;
	private String studentNo;
	private String studentPassword;
	private String studentName;
	private String studentPhone;
	private String studentIdentityNumber;
	private String studentGender;
	private String studentAddress;
	private String studentStatus;
	private String studentEmail;
	private String studentBank;
	private String majorName;
	private String photoFile;
	private String photoUUID;
}
