package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.ConstructorDto;
import com.excentria_it.wamya.domain.ModelDto;

public class ConstructorTestData {

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
}
