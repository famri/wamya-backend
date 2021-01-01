package com.excentria_it.wamya.test.data.common;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand.CreateJourneyRequestCommandBuilder;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand.LoadJourneyRequestsCommandBuilder;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand.SearchJourneyRequestsCommandBuilder;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.CreateJourneyRequestDtoBuilder;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria.SearchJourneyRequestsCriteriaBuilder;

public class JourneyRequestTestData {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//	private static LocalDateTime startDate = LocalDateTime
//			.parse(LocalDateTime.of(2020, 12, 10, 12, 0, 0, 0).format(DATE_TIME_FORMATTER), DATE_TIME_FORMATTER);
//	private static LocalDateTime endDate = LocalDateTime.parse(addDays(startDate, 1).format(DATE_TIME_FORMATTER),
//			DATE_TIME_FORMATTER);

	private static LocalDateTime startDate = LocalDateTime.of(2020, 12, 10, 12, 0, 0, 0);
	private static LocalDateTime endDate = startDate.minusDays(1);

	private static List<ClientJourneyRequestDto> clientJourneyRequestDtos = List.of(new ClientJourneyRequestDto() {

		@Override
		public Long getId() {

			return 1L;
		}

		@Override
		public PlaceDto getDeparturePlace() {

			return new PlaceDto("departurePlaceId", "departurePlaceRegionId", "departurePlaceName");
		}

		@Override
		public PlaceDto getArrivalPlace() {
			return new PlaceDto("arrivalPlaceId1", "arrivalPlaceRegionId1", "arrivalPlaceName1");
		}

		@Override
		public EngineTypeDto getEngineType() {
			return new EngineTypeDto(1L, "engineType1");
		}

		@Override
		public Double getDistance() {
			return 100D;
		}

		@Override
		public LocalDateTime getDateTime() {
			return startDate;
		}

		@Override
		public LocalDateTime getEndDateTime() {
			return endDate;
		}

		@Override
		public LocalDateTime getCreationDateTime() {
			return endDate.minusDays(1);
		}

		@Override
		public Integer getWorkers() {
			return 2;
		}

		@Override
		public String getDescription() {

			return "Journey description 1";
		}

	}, new ClientJourneyRequestDto() {

		@Override
		public Long getId() {

			return 2L;
		}

		@Override
		public PlaceDto getDeparturePlace() {

			return new PlaceDto("departurePlaceId", "departurePlaceRegionId", "departurePlaceName");
		}

		@Override
		public PlaceDto getArrivalPlace() {
			return new PlaceDto("arrivalPlaceId2", "arrivalPlaceRegionId2", "arrivalPlaceName2");
		}

		@Override
		public EngineTypeDto getEngineType() {
			return new EngineTypeDto(2L, "engineType2");
		}

		@Override
		public Double getDistance() {
			return 200D;
		}

		@Override
		public LocalDateTime getDateTime() {
			return startDate;
		}

		@Override
		public LocalDateTime getEndDateTime() {
			return endDate;
		}

		@Override
		public LocalDateTime getCreationDateTime() {
			return endDate.minusDays(1);
		}

		@Override
		public Integer getWorkers() {
			return 2;
		}

		@Override
		public String getDescription() {

			return "Journey description 1";
		}

	});
	private static List<JourneyRequestSearchDto> journeyRequestSearchDtos =

			List.of(new JourneyRequestSearchDto() {

				@Override
				public Long getId() {

					return 1L;
				}

				@Override
				public PlaceDto getDeparturePlace() {

					return new PlaceDto("departurePlaceId", "departurePlaceRegionId", "departurePlaceName");
				}

				@Override
				public PlaceDto getArrivalPlace() {

					return new PlaceDto("arrivalPlaceId1", "arrivalPlaceRegionId1", "arrivalPlaceName1");
				}

				@Override
				public EngineTypeDto getEngineType() {

					return new EngineTypeDto(1L, "engineType1");
				}

				@Override
				public Double getDistance() {

					return 100D;
				}

				@Override
				public LocalDateTime getDateTime() {
					return startDate;
				}

				@Override
				public LocalDateTime getEndDateTime() {
					return endDate;
				}

				@Override
				public Integer getWorkers() {

					return 2;
				}

				@Override
				public String getDescription() {

					return "Journey description 1";
				}

				@Override
				public ClientDto getClient() {

					return new ClientDto(1L, "ClientName1", "SOME PHOTO URL 1");
				}

				@Override
				public Integer getMinPrice() {

					return 250;
				}
			}, new JourneyRequestSearchDto() {

				@Override
				public Long getId() {

					return 1L;
				}

				@Override
				public PlaceDto getDeparturePlace() {

					return new PlaceDto("departurePlaceId", "departurePlaceRegionId", "departurePlaceName");
				}

				@Override
				public PlaceDto getArrivalPlace() {

					return new PlaceDto("arrivalPlaceId2", "arrivalPlaceRegionId2", "arrivalPlaceName2");
				}

				@Override
				public EngineTypeDto getEngineType() {

					return new EngineTypeDto(2L, "engineType2");
				}

				@Override
				public Double getDistance() {

					return 90D;
				}

				@Override
				public LocalDateTime getDateTime() {
					return startDate;
				}

				@Override
				public LocalDateTime getEndDateTime() {
					return endDate;
				}

				@Override
				public Integer getWorkers() {

					return 2;
				}

				@Override
				public String getDescription() {

					return "Journey description 2";
				}

				@Override
				public ClientDto getClient() {

					return new ClientDto(1L, "ClientName2", "SOME PHOTO URL 2");
				}

				@Override
				public Integer getMinPrice() {

					return 220;
				}
			});

	public static SearchJourneyRequestsCommandBuilder defaultSearchJourneyRequestsCommandBuilder() {
		LocalDateTime startDate = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER),
				DATE_TIME_FORMATTER);
		LocalDateTime endDate = LocalDateTime.parse(addDays(startDate, 1).format(DATE_TIME_FORMATTER),
				DATE_TIME_FORMATTER);
		return SearchJourneyRequestsCommand.builder().departurePlaceRegionId("departurePlaceRegionId")
				.arrivalPlaceRegionIds(Set.of("arrivalPlaceRegionId1", "arrivalPlaceRegionId2"))
				.startDateTime(startDate).endDateTime(endDate).engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortCriterion("min-price", "desc"));

	}

	public static SearchJourneyRequestsCriteriaBuilder defaultSearchJourneyRequestsCriteriaBuilder() {
		LocalDateTime startDate = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER),
				DATE_TIME_FORMATTER);
		LocalDateTime endDate = LocalDateTime.parse(addDays(startDate, 1).format(DATE_TIME_FORMATTER),
				DATE_TIME_FORMATTER);
		return SearchJourneyRequestsCriteria.builder().departurePlaceRegionId("departurePlaceRegionId")
				.arrivalPlaceRegionIds(Set.of("arrivalPlaceRegionId1", "arrivalPlaceRegionId2"))
				.startDateTime(startDate).endDateTime(endDate).engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortCriterion("min-price", "desc")).locale("en_US");

	}

	public static SearchJourneyRequestsCommandBuilder arrivalPlaceRegionAgnosticSearchJourneyRequestsCommandBuilder() {
		LocalDateTime startDate = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER),
				DATE_TIME_FORMATTER);
		LocalDateTime endDate = LocalDateTime.parse(addDays(startDate, 1).format(DATE_TIME_FORMATTER),
				DATE_TIME_FORMATTER);

		return SearchJourneyRequestsCommand.builder().departurePlaceRegionId("departurePlaceRegionId")
				.arrivalPlaceRegionIds(Set.of(SearchJourneyRequestsCommand.ANY_ARRIVAL_REGION)).startDateTime(startDate)
				.endDateTime(endDate).engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortCriterion("min-price", "desc"));

	}

	public static CreateJourneyRequestCommandBuilder defaultCreateJourneyRequestCommandBuilder() {

		return CreateJourneyRequestCommand.builder().departurePlaceId("departurePlaceId")
				.departurePlaceRegionId("departurePlaceRegionId").departurePlaceName("departurePlaceName")
				.arrivalPlaceId("arrivalPlaceId").arrivalPlaceRegionId("arrivalPlaceRegionId")
				.arrivalPlaceName("arrivalPlaceName").dateTime(startDate).endDateTime(endDate).engineTypeId(1L)
				.distance(270.8).workers(2).description("Need a transporter URGENT!!!");

	}

	public static CreateJourneyRequestDto defaultCreateJourneyRequestDto() {
		return CreateJourneyRequestDto.builder().id(1L)
				.departurePlace(new CreateJourneyRequestDto.PlaceDto("departurePlaceId", "departurePlaceRegionId",
						"departurePlaceName"))
				.arrivalPlace(new CreateJourneyRequestDto.PlaceDto("arrivalPlaceId", "arrivalPlaceRegionId",
						"arrivalPlaceName"))
				.dateTime(startDate).endDateTime(endDate)
				.engineType(new CreateJourneyRequestDto.EngineTypeDto(1L, "EngineType1")).distance(270.8).workers(2)
				.description("Need a transporter URGENT!!!").build();
	}

	public static CreateJourneyRequestDtoBuilder defaultCreateJourneyRequestDtoBuilder() {
		return CreateJourneyRequestDto.builder().id(1L)
				.departurePlace(new CreateJourneyRequestDto.PlaceDto("departurePlaceId", "departurePlaceRegionId",
						"departurePlaceName"))
				.arrivalPlace(new CreateJourneyRequestDto.PlaceDto("arrivalPlaceId", "arrivalPlaceRegionId",
						"arrivalPlaceName"))
				.dateTime(startDate).endDateTime(endDate)
				.engineType(new CreateJourneyRequestDto.EngineTypeDto(1L, "EngineType1")).distance(270.8).workers(2)
				.description("Need a transporter URGENT!!!");
	}

	private static LocalDateTime addDays(LocalDateTime dateTime, int days) {

		ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);

		LocalDateTime dateAfter = zonedDateTime.plusDays(days).toLocalDateTime();

		return dateAfter;
	}

	public static List<JourneyRequestSearchDto> defaultJourneyRequestSearchDtoList() {
		return journeyRequestSearchDtos;
	}

	public static JourneyRequestsSearchResult defaultJourneyRequestsSearchResult() {

		return new JourneyRequestsSearchResult(5, 10, 0, 2, true, journeyRequestSearchDtos);
	}

	public static LoadJourneyRequestsCommandBuilder defaultLoadJourneyRequestsCommandBuilder() {

		LocalDateTime ldt = LocalDateTime.now(ZoneOffset.UTC);
		return LoadJourneyRequestsCommand.builder().clientUsername(TestConstants.DEFAULT_EMAIL).pageNumber(0)
				.pageSize(25).sortingCriterion(new SortCriterion("creation-date-time", "desc"))
				.periodCriterion(new PeriodCriterion("Y1", ldt.minusYears(1), ldt));
	}

	public static ClientJourneyRequests defaultClientJourneyRequests() {
		return new ClientJourneyRequests(5, 10, 0, 2, true, clientJourneyRequestDtos);
	}
}
