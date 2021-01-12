package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.LoadModelsDto;

public class ModelTestData {

	public static final List<LoadModelsDto> loadModelsDtos = List.of(new LoadModelsDto() {

		@Override
		public Long getId() {

			return 1L;
		}

		@Override
		public String getName() {

			return "Model1";
		}

		@Override
		public Double getLength() {

			return 4.35;
		}

		@Override
		public Double getWidth() {

			return 1.83;
		}

		@Override
		public Double getHeight() {

			return 1.8;
		}

	}, new LoadModelsDto() {

		@Override
		public Long getId() {

			return 2L;
		}

		@Override
		public String getName() {

			return "Model2";
		}

		@Override
		public Double getLength() {

			return 4.35;
		}

		@Override
		public Double getWidth() {

			return 1.83;
		}

		@Override
		public Double getHeight() {

			return 1.8;
		}

	}, new LoadModelsDto() {

		@Override
		public Long getId() {

			return 3L;
		}

		@Override
		public String getName() {

			return "Model3";
		}

		@Override
		public Double getLength() {

			return 4.35;
		}

		@Override
		public Double getWidth() {

			return 1.83;
		}

		@Override
		public Double getHeight() {

			return 1.8;
		}

	});

	public static List<LoadModelsDto> defaultLoadModelsDtos() {
		return loadModelsDtos;
	}
}
