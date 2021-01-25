package com.excentria_it.wamya.test.data.common;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand.CreateJourneyRequestCommandBuilder;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand.LoadJourneyRequestsCommandBuilder;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand.SearchJourneyRequestsCommandBuilder;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.PeriodCriterion.PeriodValue;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.CreateJourneyRequestDtoBuilder;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria.LoadClientJourneyRequestsCriteriaBuilder;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria.SearchJourneyRequestsCriteriaBuilder;

public class JourneyRequestTestData {

	private static ZonedDateTime startDate = ZonedDateTime.of(2020, 12, 10, 12, 0, 0, 0, ZoneOffset.UTC);
	private static ZonedDateTime endDate = startDate.minusDays(1);

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
		public ZonedDateTime getDateTime() {
			return startDate;
		}

		@Override
		public ZonedDateTime getCreationDateTime() {
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

		@Override
		public Integer getProposalsCount() {

			return 3;
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
		public ZonedDateTime getDateTime() {
			return startDate;
		}

		@Override
		public ZonedDateTime getCreationDateTime() {
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

		@Override
		public Integer getProposalsCount() {

			return 3;
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
				public ZonedDateTime getDateTime() {
					return startDate;
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
				public Double getMinPrice() {

					return 250D;
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
				public ZonedDateTime getDateTime() {
					return startDate;
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
				public Double getMinPrice() {

					return 220D;
				}
			});

	public static SearchJourneyRequestsCommandBuilder defaultSearchJourneyRequestsCommandBuilder() {
		ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime endDate = startDate.plusDays(1);
		return SearchJourneyRequestsCommand.builder().departurePlaceRegionId("departurePlaceRegionId")
				.arrivalPlaceRegionIds(Set.of("arrivalPlaceRegionId1", "arrivalPlaceRegionId2"))
				.startDateTime(startDate).endDateTime(endDate).engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortCriterion("min-price", "desc"));

	}

	public static SearchJourneyRequestsCriteriaBuilder defaultSearchJourneyRequestsCriteriaBuilder() {
		ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime endDate = startDate.plusDays(1);
		return SearchJourneyRequestsCriteria.builder().departurePlaceRegionId("departurePlaceRegionId")
				.arrivalPlaceRegionIds(Set.of("arrivalPlaceRegionId1", "arrivalPlaceRegionId2"))
				.startDateTime(startDate.toInstant()).endDateTime(endDate.toInstant()).engineTypes(Set.of(1L, 2L))
				.pageNumber(0).pageSize(2).sortingCriterion(new SortCriterion("min-price", "desc")).locale("en_US");

	}

	public static SearchJourneyRequestsCommandBuilder arrivalPlaceRegionAgnosticSearchJourneyRequestsCommandBuilder() {
		ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime endDate = startDate.plusDays(1);

		return SearchJourneyRequestsCommand.builder().departurePlaceRegionId("departurePlaceRegionId")
				.arrivalPlaceRegionIds(Set.of(SearchJourneyRequestsCommand.ANY_ARRIVAL_REGION)).startDateTime(startDate)
				.endDateTime(endDate).engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortCriterion("min-price", "desc"));

	}

	public static CreateJourneyRequestCommandBuilder defaultCreateJourneyRequestCommandBuilder() {

		return CreateJourneyRequestCommand.builder().departurePlaceId("departurePlaceId")
				.departurePlaceRegionId("departurePlaceRegionId").departurePlaceName("departurePlaceName")
				.arrivalPlaceId("arrivalPlaceId").arrivalPlaceRegionId("arrivalPlaceRegionId")
				.arrivalPlaceName("arrivalPlaceName").dateTime(startDate).engineTypeId(1L).distance(270.8).workers(2)
				.description("Need a transporter URGENT!!!");

	}

	public static CreateJourneyRequestDto defaultCreateJourneyRequestDto() {
		return CreateJourneyRequestDto.builder().id(1L)
				.departurePlace(new CreateJourneyRequestDto.PlaceDto("departurePlaceId", "departurePlaceRegionId",
						"departurePlaceName"))
				.arrivalPlace(new CreateJourneyRequestDto.PlaceDto("arrivalPlaceId", "arrivalPlaceRegionId",
						"arrivalPlaceName"))
				.dateTime(startDate.toInstant())
				.engineType(new CreateJourneyRequestDto.EngineTypeDto(1L, "EngineType1")).distance(270.8).workers(2)
				.description("Need a transporter URGENT!!!").build();
	}

	public static CreateJourneyRequestDtoBuilder defaultCreateJourneyRequestDtoBuilder() {
		return CreateJourneyRequestDto.builder().id(1L)
				.departurePlace(new CreateJourneyRequestDto.PlaceDto("departurePlaceId", "departurePlaceRegionId",
						"departurePlaceName"))
				.arrivalPlace(new CreateJourneyRequestDto.PlaceDto("arrivalPlaceId", "arrivalPlaceRegionId",
						"arrivalPlaceName"))
				.dateTime(startDate.toInstant())
				.engineType(new CreateJourneyRequestDto.EngineTypeDto(1L, "EngineType1")).distance(270.8).workers(2)
				.description("Need a transporter URGENT!!!");
	}

	public static LoadClientJourneyRequestsCriteriaBuilder defaultLoadClientJourneyRequestsCriteriaBuilder() {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		return LoadClientJourneyRequestsCriteria.builder().clientUsername(TestConstants.DEFAULT_EMAIL).locale("en_US")
				.pageNumber(0).pageSize(25).sortingCriterion(new SortCriterion("creation-date-time", "desc"))
				.periodCriterion(new PeriodCriterion("m1", PeriodValue.M1.calculateLowerEdge(now), now));

	}

	public static List<JourneyRequestSearchDto> defaultJourneyRequestSearchDtoList() {
		return journeyRequestSearchDtos;
	}

	public static JourneyRequestsSearchResult defaultJourneyRequestsSearchResult() {

		return new JourneyRequestsSearchResult(5, 10, 0, 2, true, journeyRequestSearchDtos);
	}

	public static LoadJourneyRequestsCommandBuilder defaultLoadJourneyRequestsCommandBuilder() {

		ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC);
		return LoadJourneyRequestsCommand.builder().clientUsername(TestConstants.DEFAULT_EMAIL).pageNumber(0)
				.pageSize(25).sortingCriterion(new SortCriterion("creation-date-time", "desc"))
				.periodCriterion(new PeriodCriterion("Y1", ldt.minusYears(1), ldt));
	}

	public static ClientJourneyRequests defaultClientJourneyRequests() {
		return new ClientJourneyRequests(5, 10, 0, 2, true, clientJourneyRequestDtos);
	}

	public static ClientJourneyRequestDto defaultClientJourneyRequestDto() {
		return clientJourneyRequestDtos.get(0);
	}

	public static List<ClientJourneyRequestDto> defaultClientJourneyRequestDtoList() {
		return clientJourneyRequestDtos;
	}

}
