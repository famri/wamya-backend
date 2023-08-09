package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class DateTimeTests {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private ZonedDateTime dateTime;

	@Test
	void test() {
		LocalDateTime ldt = LocalDateTime.parse("2021-05-03T19:53:00.000");
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("Africa/Tunis"));
		System.out.println(ldt);
		System.out.println(zdt);
	}
}
