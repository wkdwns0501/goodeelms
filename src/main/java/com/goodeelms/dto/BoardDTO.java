package com.goodeelms.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
	private int boardId;
	private String boardTitle;
	private String boardType;
	private String boardContent;
	private LocalDateTime boardRegAt;
	private int adminId;	
}
