package com.goodeelms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {
	private int professorId;
	private String professorName;
	private String professorEmail;
	private String professorPassword;
	private String professorStatus;
	private int majorId;
	private String majorName;
}
