package com.excentria_it.wamya.application.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DateTimeFormattersTests {

	@Test
	void testGetDateTimeFormatter_fr_FR() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 10, 12, 0);
		assertEquals("10 d\u00e9cembre 2020 12:00", ldt.format(DateTimeFormatters.getDateTimeFormatter("fr_FR")));
	}

	@Test
	void testGetDateTimeFormatter_en_US() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 10, 12, 0);
		assertEquals("December 10 2020 12:00", ldt.format(DateTimeFormatters.getDateTimeFormatter("en_US")));
	}

	@Test
	void testGetDateTimeFormatter_de_DE() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 10, 12, 0);
		assertEquals("December 10 2020 12:00", ldt.format(DateTimeFormatters.getDateTimeFormatter("de_DE")));
	}

	@Test
	void testGetDateFormatter_fr_FR() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 10, 12, 0);
		assertEquals("10 d\u00e9cembre 2020", ldt.format(DateTimeFormatters.getDateFormatter("fr_FR")));
	}

	@Test
	void testGetDateFormatter_en_US() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 10, 12, 0);
		assertEquals("December 10 2020", ldt.format(DateTimeFormatters.getDateFormatter("en_US")));
	}

	@Test
	void testGetDateFormatter_de_DE() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 10, 12, 0);
		assertEquals("December 10 2020", ldt.format(DateTimeFormatters.getDateFormatter("de_DE")));
	}
}
