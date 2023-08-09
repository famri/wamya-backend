package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.LoadLocalesDto;

public class LocaleTestData {
	public static List<LoadLocalesDto> defaultLoadLocalesDtos() {
		return List.of(new LoadLocalesDto(1L, "English", "US", "en"), new LoadLocalesDto(2L, "Français", "FR", "fr"));
	}
}
