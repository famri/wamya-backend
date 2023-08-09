package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.ConstructorDto;
import com.excentria_it.wamya.domain.LoadConstructorsDto;
import com.excentria_it.wamya.domain.ModelDto;

public class ConstructorTestData {
	private static final List<LoadConstructorsDto> loadConstructorsDtos = List.of(
			new LoadConstructorsDto(1L, "Constructor1"), new LoadConstructorsDto(2L, "Constructor2"),
			new LoadConstructorsDto(3L, "Constructor3"));

	public static ConstructorDto defaultConstructorDto() {
		return new ConstructorDto() {

			@Override
			public Long getId() {

				return 1L;
			}

			@Override
			public String getName() {

				return "PEUGEOT";
			}

			@Override
			public List<ModelDto> getModels() {

				return List.of(new ModelDto() {

					@Override
					public Long getId() {

						return 1L;
					}

					@Override
					public String getName() {
						return "PARTNER";
					}

				}, new ModelDto() {

					@Override
					public Long getId() {

						return 2L;
					}

					@Override
					public String getName() {
						return "EXPERT";
					}

				});
			}

		};
	}

	public static List<LoadConstructorsDto> defaultLoadConstructorsDtos() {
		return loadConstructorsDtos;
	}
}
