package com.gdsc.projectmiobackend.chat.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Greeting {

	private String content;

	public Greeting(String content) {
		this.content = content;
	}
}