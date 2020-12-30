package com.excentria_it.wamya.common.utils;

import java.util.List;
import java.util.Locale;

public class LocaleUtils {

	private LocaleUtils() {

	}

	public static final Locale defaultLocale = new Locale("en", "US");
	public static final List<Locale> supportedLocales = List.of(defaultLocale, new Locale("fr", "FR"));

	public static Locale getSupporedLocale(Locale locale) {
		for (Locale sl : supportedLocales) {
			if (sl.getLanguage().equals(locale.getLanguage()) && sl.getCountry().equals(locale.getCountry())) {
				return sl;
			}
		}

		return defaultLocale;
	}

}
