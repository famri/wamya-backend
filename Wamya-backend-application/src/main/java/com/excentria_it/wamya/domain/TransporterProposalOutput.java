package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransporterProposalOutput {

    private Long id;

    private Double price;

    private String status;

    private JourneyProposalStatusCode statusCode;

    private TransporterVehiculeOutput vehicule;

    private JourneyRequestOutput journey;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class TransporterVehiculeOutput {

        private Long id;

        private String registrationNumber;

        private LocalDate circulationDate;

        private String constructorName;

        private String modelName;

        private Long engineTypeId;

        private String engineTypeName;

        private Long imageId;

        private String imageHash;

    }

    @AllArgsConstructor
    @Data
    @Builder
    public static class JourneyRequestOutput {

        private Long id;

        private PlaceDto departurePlace;

        private PlaceDto arrivalPlace;

        private EngineTypeDto engineType;

        private Integer distance;

        private Integer hours;

        private Integer minutes;

        private Instant dateTime;

        private Integer workers;

        private String description;

        private ClientOutput client;

        @AllArgsConstructor
        @Data
        @Builder
        public static class EngineTypeDto {

            private Long id;

            private String name;

            private String code;

        }

        @AllArgsConstructor
        @Data
        @Builder
        public static class PlaceDto {

            private Long id;

            private String type;

            private String name;

            private BigDecimal latitude;

            private BigDecimal longitude;

            private Long departmentId;
        }

        @AllArgsConstructor
        @Data
        @Builder
        public static class ClientOutput {

            private String oauthId;

            private String firstname;

            private Long imageId;

            private String imageHash;
        }

    }

}
