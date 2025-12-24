package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponseDto {

	private Long id;
	private String content;
	
	//작성자 정보
	private String username;
	
	private LocalDateTime cteatedAt;
}
