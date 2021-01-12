package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity.EngineTypeCode;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;

public class EngineTypeTestData {

	private static final List<LoadEngineTypesDto> loadEngineTypesDtos = List.of(new LoadEngineTypesDto() {

		@Override
		public Long getId() {
			return 1L;
		}

		@Override
		public String getName() {

			return "utility vehicule";
		}

		@Override
		public String getDescription() {
			return "utility vehicule description";
		}

	}, new LoadEngineTypesDto() {

		@Override
		public Long getId() {
			return 2L;
		}

		@Override
		public String getName() {

			return "L1H1 vehicule";
		}

		@Override
		public String getDescription() {
			return "L1H1 vehicule description";
		}

	}, new LoadEngineTypesDto() {

		@Override
		public Long getId() {
			return 3L;
		}

		@Override
		public String getName() {

			return "L2H2 vehicule";
		}

		@Override
		public String getDescription() {
			return "L2H2 vehicule description";
		}

	});

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
