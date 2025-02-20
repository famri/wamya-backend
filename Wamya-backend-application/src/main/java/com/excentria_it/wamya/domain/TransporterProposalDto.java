package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransporterProposalDto {
    private Long id;
    private Double price;
    private String status;
    private JourneyProposalStatusCode statusCode;
    private JourneyRequestDto journey;
    private TransporterVehicleDto vehicle;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class JourneyRequestDto {
        private Long id;

        private Place departurePlace;

        private Place arrivalPlace;

        private EngineType engineType;

        private Integer distance;

        private Integer hours;

        private Integer minutes;

        private LocalDateTime dateTime;

        private Integer workers;

        private String description;

        private Client client;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @Builder
        public static class EngineType {

            private Long id;

            private String name;

            private String code;

        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @Builder
        public static class Place {

            private Long id;

            private String type;

            private String name;

            private BigDecimal latitude;

            private BigDecimal longitude;

            private Long departmentId;
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @Builder
        public static class Client {

            private String id;

            private String firstname;

            private String photoUrl;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class TransporterVehicleDto {
        private Long id;

        private String registrationNumber;

        private LocalDate circulationDate;

        private String constructorName;

        private String modelName;

        private Long engineTypeId;

        private String engineTypeName;

        private String photoUrl;

    }
}
