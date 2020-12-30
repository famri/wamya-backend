package com.excentria_it.wamya.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;

public class LocaleUtilsTests {

	@Test
	void testSupportedLocale() {
		Locale givenLocaleFR = new Locale("fr", "FR");
		Locale supportedLocaleFR = LocaleUtils.getSupporedLocale(givenLocaleFR);

		Locale givenLocaleEN = new Locale("en", "US");
		Locale supportedLocaleEN = LocaleUtils.getSupporedLocale(givenLocaleEN);

		Locale givenLocaleAR = new Locale("ar", "TN");
		Locale supportedLocaleAR = LocaleUtils.getSupporedLocale(givenLocaleAR);

		assertEquals(givenLocaleFR, supportedLocaleFR);
		assertEquals(LocaleUtils.defaultLocale, supportedLocaleEN);
		assertEquals(LocaleUtils.defaultLocale, supportedLocaleAR);

	}

	@Test
	void testUnsupportedLocale() {
		Locale givenLocaleUK = new Locale("en", "UK");
		Locale supportedLocaleUK = LocaleUtils.getSupporedLocale(givenLocaleUK);

		Locale givenLocaleDE = new Locale("de", "DE");
		Locale supportedLocaleDE = LocaleUtils.getSupporedLocale(givenLocaleDE);

		Locale givenLocaleFRByLang = new Locale("fr");
		Locale supportedLocaleFRByLang = LocaleUtils.getSupporedLocale(givenLocaleFRByLang);

		Locale givenLocaleFRBE = new Locale("fr", "BE");
		Locale supportedLocaleFRBE = LocaleUtils.getSupporedLocale(givenLocaleFRBE);

		assertEquals(LocaleUtils.defaultLocale, supportedLocaleUK);
		assertEquals(LocaleUtils.defaultLocale, supportedLocaleDE);
		assertEquals(LocaleUtils.defaultLocale, supportedLocaleFRByLang);
		assertEquals(LocaleUtils.defaultLocale, supportedLocaleFRBE);

	}
}
