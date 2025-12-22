package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostEditDto {

	// 이 요소들으 최소단위기 때문에 분리ㄲ
	private Long id;
	private String title;
	private String content;
}
