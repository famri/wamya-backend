package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehicleJpaEntity;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.EngineTypeCode;
import com.excentria_it.wamya.domain.LoadTransporterVehiclesCriteria;
import com.excentria_it.wamya.domain.LoadTransporterVehiclesCriteria.LoadTransporterVehiclesCriteriaBuilder;
import com.excentria_it.wamya.domain.TransporterVehicleOutput;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class VehicleJpaEntityTestData {

    public static final VehicleJpaEntity VEHICLE_JPA_ENTITY_1 = VehicleJpaEntity.builder().id(1L)
            .type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L1H1)
                    .image(DocumentJpaTestData.defaultVanL1H1VehicleImageDocumentJpaEntity()).build())
            .model(ModelJpaEntity.builder().id(1L).name("PARTNER")
                    .constructor(ConstructorJpaEntity.builder().id(1L).name("PEUGEOT").build()).build())
            .circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220").build();

    public static final VehicleJpaEntity VEHICLE_JPA_ENTITY_2 = VehicleJpaEntity.builder().id(2L)
            .type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L2H2)
                    .image(DocumentJpaTestData.defaultVanL2H2VehicleImageDocumentJpaEntity()).build())
            .model(ModelJpaEntity.builder().id(2L).name("KANGOO")
                    .constructor(ConstructorJpaEntity.builder().id(2L).name("RENAULT").build()).build())
            .circulationDate(LocalDate.of(2020, 01, 01)).registration("2 TUN 220").build();

    public static final VehicleJpaEntity VEHICLE_JPA_ENTITY_3 = VehicleJpaEntity.builder().id(3L)
            .type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L3H3)
                    .image(DocumentJpaTestData.defaultVanL3H3VehicleImageDocumentJpaEntity()).build())
            .model(ModelJpaEntity.builder().id(3L).name("JUMPER")
                    .constructor(ConstructorJpaEntity.builder().id(3L).name("CITROEN").build()).build())
            .circulationDate(LocalDate.of(2020, 01, 01)).registration("3 TUN 220").build();

    public static final Set<VehicleJpaEntity> defaultVehicleJpaEntitySet() {

        return Set.of(VEHICLE_JPA_ENTITY_1, VEHICLE_JPA_ENTITY_2, VEHICLE_JPA_ENTITY_3);
    }

    public static final VehicleJpaEntity defaultVehicleJpaEntity() {

        return VEHICLE_JPA_ENTITY_1;
    }

    private static TransporterVehicleOutput defaultTransporterVehicleOutput = new TransporterVehicleOutput() {

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

    private static TransporterVehicleOutput transporterVehicleOutputWithTemporaryConstructorAndModelAndNoImage = new TransporterVehicleOutput() {

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

    private static List<TransporterVehicleOutput> defaultTransporterVehicleOutputList = List
            .of(defaultTransporterVehicleOutput, transporterVehicleOutputWithTemporaryConstructorAndModelAndNoImage);

    public static LoadTransporterVehiclesCriteriaBuilder defaultLoadTransporterVehiclesCriteriaBuilder() {
        return LoadTransporterVehiclesCriteria.builder().transporterUsername(TestConstants.DEFAULT_EMAIL)
                .sortingCriterion(new SortCriterion("id", "asc"));
    }

    public static TransporterVehicleOutput defaultTransporterVehicleOutput() {
        return defaultTransporterVehicleOutput;
    }

    public static TransporterVehicleOutput transporterVehicleOutputWithTemporaryConstructorAndModelAndNoImage() {
        return transporterVehicleOutputWithTemporaryConstructorAndModelAndNoImage;
    }

    public static final VehicleJpaEntity.VehicleJpaEntityBuilder defaultVehicleJpaEntityBuilder() {
        return VehicleJpaEntity.builder().id(1L)
                .type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L1H1).build())
                .model(ModelJpaEntity.builder().id(1L).name("PARTNER")
                        .constructor(ConstructorJpaEntity.builder().id(1L).name("PEUGEOT").build()).build())
                .circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220");
    }

    public static List<TransporterVehicleOutput> defaultTransporterVehicleOutputList() {
        return defaultTransporterVehicleOutputList;
    }

}
