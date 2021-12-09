package com.excentria_it.wamya.test.data.common;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity.VehiculeJpaEntityBuilder;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.EngineTypeCode;
import com.excentria_it.wamya.domain.LoadTransporterVehiculesCriteria;
import com.excentria_it.wamya.domain.TransporterVehiculeOutput;
import com.excentria_it.wamya.domain.LoadTransporterVehiculesCriteria.LoadTransporterVehiculesCriteriaBuilder;

public class VehiculeJpaEntityTestData {

	public static final VehiculeJpaEntity vehiculeJpaEntity1 = VehiculeJpaEntity.builder().id(1L)
			.type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L1H1)
					.image(DocumentJpaTestData.defaultVanL1H1VehiculeImageDocumentJpaEntity()).build())
			.model(ModelJpaEntity.builder().id(1L).name("PARTNER")
					.constructor(ConstructorJpaEntity.builder().id(1L).name("PEUGEOT").build()).build())
			.circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220").build();

	public static final VehiculeJpaEntity vehiculeJpaEntity2 = VehiculeJpaEntity.builder().id(2L)
			.type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L2H2)
					.image(DocumentJpaTestData.defaultVanL2H2VehiculeImageDocumentJpaEntity()).build())
			.model(ModelJpaEntity.builder().id(2L).name("KANGOO")
					.constructor(ConstructorJpaEntity.builder().id(2L).name("RENAULT").build()).build())
			.circulationDate(LocalDate.of(2020, 01, 01)).registration("2 TUN 220").build();

	public static final VehiculeJpaEntity vehiculeJpaEntity3 = VehiculeJpaEntity.builder().id(3L)
			.type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L3H3)
					.image(DocumentJpaTestData.defaultVanL3H3VehiculeImageDocumentJpaEntity()).build())
			.model(ModelJpaEntity.builder().id(3L).name("JUMPER")
					.constructor(ConstructorJpaEntity.builder().id(3L).name("CITROEN").build()).build())
			.circulationDate(LocalDate.of(2020, 01, 01)).registration("3 TUN 220").build();

	public static final Set<VehiculeJpaEntity> defaultVehiculeJpaEntitySet() {

		return Set.of(vehiculeJpaEntity1, vehiculeJpaEntity2, vehiculeJpaEntity3);
	}

	public static final VehiculeJpaEntity defaultVehiculeJpaEntity() {

		return vehiculeJpaEntity1;
	}

	private static TransporterVehiculeOutput defaultTransporterVehiculeOutput = new TransporterVehiculeOutput() {

		@Override
		public Long getId() {

			return 1L;
		}

		@Override
		public String getRegistrationNumber() {

			return "0001 TUN 2020";
		}

		@Override
		public ConstructorDto getConstructor() {

			return new ConstructorDto(1L, "PEUGEOT", null);
		}

		@Override
		public ModelDto getModel() {

			return new ModelDto(1L, "PARTNER", null);

		}

		@Override
		public EngineTypeDto getEngineType() {
			return new EngineTypeDto(1L, "Utility vehicule", 1L, "SOME_HASH");
		}

		@Override
		public LocalDate getCirculationDate() {

			return LocalDate.of(2020, 01, 01);
		}

		@Override
		public ImageDto getImage() {

			return new ImageDto(2L, "SOME_OTHER_HASH");
		}

	};
	
	public static LoadTransporterVehiculesCriteriaBuilder defaultLoadTransporterVehiculesCriteriaBuilder() {
		return LoadTransporterVehiculesCriteria.builder().transporterUsername(TestConstants.DEFAULT_EMAIL).pageNumber(1)
				.pageSize(10).sortingCriterion(new SortCriterion("id", "asc"));
	}
	private static TransporterVehiculeOutput transporterVehiculeOutputWithTemporaryConstructorAndModelAndNoImage = new TransporterVehiculeOutput() {

		@Override
		public Long getId() {

			return 2L;
		}

		@Override
		public String getRegistrationNumber() {

			return "0001 TUN 2020";
		}

		@Override
		public ConstructorDto getConstructor() {

			return new ConstructorDto(null, null, "PEUGEOT");
		}

		@Override
		public ModelDto getModel() {

			return new ModelDto(null, null, "PARTNER");

		}

		@Override
		public EngineTypeDto getEngineType() {
			return new EngineTypeDto(1L, "Utility vehicule", 1L, "SOME_HASH");
		}

		@Override
		public LocalDate getCirculationDate() {

			return LocalDate.of(2020, 01, 01);
		}

		@Override
		public ImageDto getImage() {

			return new ImageDto(null, null);
		}

	};

	public static TransporterVehiculeOutput defaultTransporterVehiculeOutput() {
		return defaultTransporterVehiculeOutput;
	}

	public static TransporterVehiculeOutput transporterVehiculeOutputWithTemporaryConstructorAndModelAndNoImage() {
		return transporterVehiculeOutputWithTemporaryConstructorAndModelAndNoImage;
	}

	public static final VehiculeJpaEntityBuilder defaultVehiculeJpaEntityBuilder() {
		return VehiculeJpaEntity.builder().id(1L)
				.type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L1H1).build())
				.model(ModelJpaEntity.builder().id(1L).name("PARTNER")
						.constructor(ConstructorJpaEntity.builder().id(1L).name("PEUGEOT").build()).build())
				.circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220");
	}

	public static List<TransporterVehiculeOutput> defaultTransporterVehiculeOutputList() {
		return defaultTransporterVehiculeOutputList;
	}

	private static List<TransporterVehiculeOutput> defaultTransporterVehiculeOutputList = List.of(
			defaultTransporterVehiculeOutput(), transporterVehiculeOutputWithTemporaryConstructorAndModelAndNoImage());

	private static Page<TransporterVehiculeOutput> defaultTransporterVehiculeOutputPage = new Page<TransporterVehiculeOutput>() {

		@Override
		public int getNumber() {

			return 1;
		}

		@Override
		public int getSize() {

			return 25;
		}

		@Override
		public int getNumberOfElements() {

			return 2;
		}

		@Override
		public List<TransporterVehiculeOutput> getContent() {

			return defaultTransporterVehiculeOutputList();
		}

		@Override
		public boolean hasContent() {

			return true;
		}

		@Override
		public Sort getSort() {

			return Sort.by(Direction.ASC, "id");
		}

		@Override
		public boolean isFirst() {

			return true;
		}

		@Override
		public boolean isLast() {

			return true;
		}

		@Override
		public boolean hasNext() {

			return false;
		}

		@Override
		public boolean hasPrevious() {

			return false;
		}

		@Override
		public Pageable nextPageable() {

			return null;
		}

		@Override
		public Pageable previousPageable() {

			return null;
		}

		@Override
		public Iterator<TransporterVehiculeOutput> iterator() {

			return defaultTransporterVehiculeOutputList().iterator();
		}

		@Override
		public int getTotalPages() {

			return 1;
		}

		@Override
		public long getTotalElements() {

			return 2;
		}

		@Override
		public <U> Page<U> map(Function<? super TransporterVehiculeOutput, ? extends U> converter) {

			return null;
		}

	};

	public static Page<TransporterVehiculeOutput> emptyTransporterVehiculeOutputPage() {
		return emptyTransporterVehiculeOutputPage;
	}

	private static Page<TransporterVehiculeOutput> emptyTransporterVehiculeOutputPage = new Page<TransporterVehiculeOutput>() {

		@Override
		public int getNumber() {

			return 1;
		}

		@Override
		public int getSize() {

			return 25;
		}

		@Override
		public int getNumberOfElements() {

			return 0;
		}

		@Override
		public List<TransporterVehiculeOutput> getContent() {

			return Collections.emptyList();
		}

		@Override
		public boolean hasContent() {

			return false;
		}

		@Override
		public Sort getSort() {

			return Sort.by(Direction.ASC, "id");
		}

		@Override
		public boolean isFirst() {

			return true;
		}

		@Override
		public boolean isLast() {

			return true;
		}

		@Override
		public boolean hasNext() {

			return false;
		}

		@Override
		public boolean hasPrevious() {

			return false;
		}

		@Override
		public Pageable nextPageable() {

			return null;
		}

		@Override
		public Pageable previousPageable() {

			return null;
		}

		@Override
		public Iterator<TransporterVehiculeOutput> iterator() {

			return Collections.<TransporterVehiculeOutput>emptyList().iterator();
		}

		@Override
		public int getTotalPages() {

			return 1;
		}

		@Override
		public long getTotalElements() {

			return 0;
		}

		@Override
		public <U> Page<U> map(Function<? super TransporterVehiculeOutput, ? extends U> converter) {

			return null;
		}

	};

	public static Page<TransporterVehiculeOutput> defaultTransporterVehiculeOutputPage() {

		return defaultTransporterVehiculeOutputPage;
	}
}
