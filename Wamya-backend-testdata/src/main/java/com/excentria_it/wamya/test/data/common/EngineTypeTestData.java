package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.EngineTypeCode;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;

public class EngineTypeTestData {

	private static final List<LoadEngineTypesDto> loadEngineTypesDtos = List.of(

			new LoadEngineTypesDto(1L, "utility vehicule", "UTILITY", "utility vehicule description"),
			new LoadEngineTypesDto(2L, "L1H1 vehicule", "VAN_L1H1", "L1H1 vehicule description"),
			new LoadEngineTypesDto(3L, "L2H2 vehicule", "VAN_L2H2", "L2H2 vehicule description"));

	public static EngineTypeDto defaultEngineTypeDto() {
		return new EngineTypeDto() {

			@Override
			public Long getId() {
				return 1L;
			}

			@Override
			public String getCode() {

				return EngineTypeCode.UTILITY.name();
			}

			@Override
			public String getName() {

				return "utility vehicule";
			}

			@Override
			public String getDescription() {

				return "utility vehicule description";
			}

		};
	}

	public static List<LoadEngineTypesDto> defaultLoadEngineTypesDtos() {
		return loadEngineTypesDtos;
	}
}
