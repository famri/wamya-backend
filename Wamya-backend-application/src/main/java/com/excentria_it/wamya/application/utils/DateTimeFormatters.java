package com.excentria_it.wamya.application.utils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class DateTimeFormatters {

	private DateTimeFormatters() {

	}

	private static Map<String, DateTimeFormatter> dateTimeFormatters = Map.of("fr_FR",
			DateTimeFormatter.ofPattern("dd LLLL yyyy HH:mm").withLocale(new Locale("fr", "FR")), "en_US",
			DateTimeFormatter.ofPattern("LLLL dd yyyy HH:mm").withLocale(new Locale("en", "US")));

	private static Map<String, DateTimeFormatter> dateFormatters = Map.of("fr_FR",
			DateTimeFormatter.ofPattern("dd LLLL yyyy").withLocale(new Locale("fr", "FR")), "en_US",
			DateTimeFormatter.ofPattern("LLLL dd yyyy").withLocale(new Locale("en", "US")));

	public static DateTimeFormatter getDateTimeFormatter(String locale) {
		return dateTimeFormatters.get(locale) == null ? dateTimeFormatters.get("en_US")
				: dateTimeFormatters.get(locale);
	}

	public static DateTimeFormatter getDateFormatter(String locale) {
		return dateFormatters.get(locale) == null ? dateFormatters.get("en_US") : dateFormatters.get(locale);
	}
}
