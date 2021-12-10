package com.excentria_it.wamya.test.data.common;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand.CreateJourneyRequestCommandBuilder;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestCommand.LoadJourneyRequestCommandBuilder;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand.LoadJourneyRequestsCommandBuilder;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand.SearchJourneyRequestsCommandBuilder;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.PeriodCriterion.PeriodValue;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.ClientJourneyRequestsOutput;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.CreateJourneyRequestDtoBuilder;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.JourneyRequestInputOutputBuilder;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto.Client;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto.EngineType;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto.Place;
import com.excentria_it.wamya.domain.JourneyRequestSearchOutput;
import com.excentria_it.wamya.domain.JourneyRequestsSearchOutputResult;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria.LoadClientJourneyRequestsCriteriaBuilder;
import com.excentria_it.wamya.domain.PlaceType;
import com.excentria_it.wamya.domain.SearchJourneyRequestsInput;
import com.excentria_it.wamya.domain.SearchJourneyRequestsInput.SearchJourneyRequestsInputBuilder;

public class JourneyRequestTestData {

	private static ZonedDateTime startDate = ZonedDateTime.of(2020, 12, 10, 12, 0, 0, 0, ZoneOffset.UTC);
	private static ZonedDateTime endDate = startDate.minusDays(1);
	private static DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();
	static{
		documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
	}
	private static List<ClientJourneyRequestDtoOutput> clientJourneyRequestDtos = List
			.of(new ClientJourneyRequestDtoOutput() {

				@Override
				public Long getId() {

					return 1L;
				}

				@Override
				public PlaceDto getDeparturePlace() {

					return new PlaceDto(1L, "DEPARTMENT", "departurePlaceName", new BigDecimal(34.486523),
							new BigDecimal(10.486523), 1L);
				}

				@Override
				public PlaceDto getArrivalPlace() {
					return new PlaceDto(2L, "DEPARTMENT", "arrivalPlaceName1", new BigDecimal(36.486523),
							new BigDecimal(10.486523), 2L);
				}

				@Override
				public EngineTypeDto getEngineType() {
					return new EngineTypeDto(1L, "engineType1", "UTILITY");
				}

				@Override
				public Integer getDistance() {
					return 100000;
				}

				@Override
				public Instant getDateTime() {
					return startDate.toInstant();
				}

				@Override
				public Instant getCreationDateTime() {
					return endDate.minusDays(1).toInstant();
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

			}, new ClientJourneyRequestDtoOutput() {

				@Override
				public Long getId() {

					return 2L;
				}

				@Override
				public PlaceDto getDeparturePlace() {

					return new PlaceDto(3L, "DEPARTMENT", "departurePlaceName2", new BigDecimal(35.486523),
							new BigDecimal(11.486523), 3L);
				}

				@Override
				public PlaceDto getArrivalPlace() {
					return new PlaceDto(4L, "DEPARTMENT", "arrivalPlaceName2", new BigDecimal(37.486523),
							new BigDecimal(12.486523), 4L);
				}

				@Override
				public EngineTypeDto getEngineType() {
					return new EngineTypeDto(2L, "engineType2", "UTILITY");
				}

				@Override
				public Integer getDistance() {
					return 200000;
				}

				@Override
				public Instant getDateTime() {
					return startDate.toInstant();
				}

				@Override
				public Instant getCreationDateTime() {
					return endDate.minusDays(1).toInstant();
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
	private static List<JourneyRequestSearchOutput> journeyRequestSearchOutputList =

			List.of(new JourneyRequestSearchOutput() {

				@Override
				public Long getId() {

					return 1L;
				}

				@Override
				public PlaceDto getDeparturePlace() {

					return new PlaceDto(1L, PlaceType.LOCALITY.name(), "departurePlaceName1", new BigDecimal(36.456582),
							new BigDecimal(10.456582), 1L);
				}

				@Override
				public PlaceDto getArrivalPlace() {

					return new PlaceDto(2L, PlaceType.LOCALITY.name(), "arrivalPlaceName1", new BigDecimal(37.456582),
							new BigDecimal(11.456582), 2L);
				}

				@Override
				public EngineTypeDto getEngineType() {

					return new EngineTypeDto(1L, "engineType1", "UTILITY");
				}

				@Override
				public Integer getDistance() {

					return 100000;
				}

				@Override
				public Instant getDateTime() {
					return startDate.toInstant();
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

					return new ClientDto(1L, "ClientName1", 1L, "SOME_IMAGE_HASH_1");
				}

				@Override
				public Double getMinPrice() {

					return 250D;
				}

				@Override
				public Integer getHours() {

					return 2;
				}

				@Override
				public Integer getMinutes() {

					return 30;
				}
			}, new JourneyRequestSearchOutput() {

				@Override
				public Long getId() {

					return 1L;
				}

				@Override
				public PlaceDto getDeparturePlace() {

					return new PlaceDto(1L, PlaceType.LOCALITY.name(), "departurePlaceName1", new BigDecimal(36.456582),
							new BigDecimal(10.456582), 1L);
				}

				@Override
				public PlaceDto getArrivalPlace() {

					return new PlaceDto(3L, PlaceType.LOCALITY.name(), "arrivalPlaceName2", new BigDecimal(38.456582),
							new BigDecimal(12.456582), 3L);
				}

				@Override
				public EngineTypeDto getEngineType() {

					return new EngineTypeDto(2L, "engineType2", "UTILITY");
				}

				@Override
				public Integer getDistance() {

					return 90000;
				}

				@Override
				public Instant getDateTime() {
					return startDate.toInstant();
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

					return new ClientDto(1L, "ClientName2", 2L, "SOME_IMAGE_HASH_2");
				}

				@Override
				public Double getMinPrice() {

					return 220D;
				}

				@Override
				public Integer getHours() {

					return 3;
				}

				@Override
				public Integer getMinutes() {

					return 15;
				}
			});

	public static JourneyRequestsSearchResult defaultJourneyRequestsSearchResult(ZoneId userZoneId) {
		JourneyRequestsSearchOutputResult searchOutput = defaultJourneyRequestsSearchOutputResult();
		return new JourneyRequestsSearchResult(searchOutput.getTotalPages(), searchOutput.getTotalElements(),
				searchOutput.getPageNumber(), searchOutput.getPageSize(), searchOutput.isHasNext(),
				searchOutput.getContent().stream().map(j -> mapToJourneyRequestSearchDto(j, userZoneId))
						.collect(Collectors.toList()));
	}

	public static SearchJourneyRequestsCommandBuilder defaultSearchJourneyRequestsCommandBuilder() {
		ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime endDate = startDate.plusDays(1);
		return SearchJourneyRequestsCommand.builder().departurePlaceDepartmentId(1L)
				.arrivalPlaceDepartmentIds(Set.of(2L, 3L)).startDateTime(startDate.toLocalDateTime())
				.endDateTime(endDate.toLocalDateTime()).engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortCriterion("min-price", "desc"));

	}

	public static SearchJourneyRequestsInputBuilder defaultSearchJourneyRequestsInputBuilder() {
		ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime endDate = startDate.plusDays(1);
		return SearchJourneyRequestsInput.builder().departurePlaceDepartmentId(1L)
				.arrivalPlaceDepartmentIds(Set.of(2L, 3L)).startDateTime(startDate.toInstant())
				.endDateTime(endDate.toInstant()).engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortCriterion("min-price", "desc")).locale("en_US");

	}

	public static SearchJourneyRequestsCommandBuilder arrivalPlaceRegionAgnosticSearchJourneyRequestsCommandBuilder() {
		ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime endDate = startDate.plusDays(1);

		return SearchJourneyRequestsCommand.builder().departurePlaceDepartmentId(1L)
				.arrivalPlaceDepartmentIds(Set.of(SearchJourneyRequestsCommand.ANY_ARRIVAL_DEPARTMENT))
				.startDateTime(startDate.toLocalDateTime()).endDateTime(endDate.toLocalDateTime())
				.engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortCriterion("min-price", "desc"));

	}

	public static CreateJourneyRequestCommandBuilder defaultCreateJourneyRequestCommandBuilder() {

		return CreateJourneyRequestCommand.builder().departurePlaceId(1L).departurePlaceType(PlaceType.LOCALITY.name())

				.arrivalPlaceId(2L).arrivalPlaceType(PlaceType.LOCALITY.name()).dateTime(startDate.toLocalDateTime())
				.engineTypeId(1L).workers(2).description("Need a transporter URGENT!!!");

	}

	public static CreateJourneyRequestDto defaultCreateJourneyRequestDto() {
		return CreateJourneyRequestDto.builder().id(1L)
				.departurePlace(new CreateJourneyRequestDto.PlaceDto(1L, PlaceType.LOCALITY, new BigDecimal(36.8015),
						new BigDecimal(10.1788)))
				.arrivalPlace(new CreateJourneyRequestDto.PlaceDto(2L, PlaceType.LOCALITY, new BigDecimal(37.8015),
						new BigDecimal(11.1788)))
				.dateTime(startDate.toLocalDateTime())
				.engineType(new CreateJourneyRequestDto.EngineTypeDto(1L, "EngineType1")).workers(2)
				.description("Need a transporter URGENT!!!").build();
	}

	public static CreateJourneyRequestDtoBuilder defaultCreateJourneyRequestDtoBuilder() {
		return CreateJourneyRequestDto.builder().id(1L)
				.departurePlace(new CreateJourneyRequestDto.PlaceDto(1L, PlaceType.LOCALITY, new BigDecimal(36.8015),
						new BigDecimal(10.1788)))
				.arrivalPlace(new CreateJourneyRequestDto.PlaceDto(2L, PlaceType.LOCALITY, new BigDecimal(37.8015),
						new BigDecimal(11.1788)))
				.dateTime(startDate.toLocalDateTime())
				.engineType(new CreateJourneyRequestDto.EngineTypeDto(1L, "EngineType1")).workers(2)
				.description("Need a transporter URGENT!!!");
	}

	public static JourneyRequestInputOutputBuilder defaultJourneyRequestInputOutputBuilder() {
		return JourneyRequestInputOutput.builder().id(1L)
				.departurePlace(new JourneyRequestInputOutput.Place(1L, PlaceType.LOCALITY, new BigDecimal(36.8015),
						new BigDecimal(10.1788)))
				.arrivalPlace(new JourneyRequestInputOutput.Place(2L, PlaceType.LOCALITY, new BigDecimal(37.8015),
						new BigDecimal(11.1788)))
				.dateTime(startDate.toInstant()).engineType(new JourneyRequestInputOutput.EngineType(1L, "EngineType1"))
				.workers(2).description("Need a transporter URGENT!!!").hours(1).minutes(30).distance(150000);
	}

	public static LoadClientJourneyRequestsCriteriaBuilder defaultLoadClientJourneyRequestsCriteriaBuilder() {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		return LoadClientJourneyRequestsCriteria.builder().clientUsername(TestConstants.DEFAULT_EMAIL).locale("en_US")
				.pageNumber(0).pageSize(25).sortingCriterion(new SortCriterion("creation-date-time", "desc"))
				.periodCriterion(new PeriodCriterion("m1", PeriodValue.M1.calculateLowerEdge(now), now));

	}

	public static List<JourneyRequestSearchOutput> defaultJourneyRequestSearchOutputList() {
		return journeyRequestSearchOutputList;
	}

	public static JourneyRequestsSearchOutputResult defaultJourneyRequestsSearchOutputResult() {

		return new JourneyRequestsSearchOutputResult(5, 10, 0, 2, true, journeyRequestSearchOutputList);
	}

	public static LoadJourneyRequestsCommandBuilder defaultLoadJourneyRequestsCommandBuilder() {

		ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC);
		return LoadJourneyRequestsCommand.builder().clientUsername(TestConstants.DEFAULT_EMAIL).pageNumber(0)
				.pageSize(25).sortingCriterion(new SortCriterion("creation-date-time", "desc"))
				.periodCriterion(new PeriodCriterion("Y1", ldt.minusYears(1), ldt));
	}

	public static LoadJourneyRequestCommandBuilder defaultLoadJourneyRequestCommandBuilder() {

		return LoadJourneyRequestCommand.builder().clientUsername(TestConstants.DEFAULT_EMAIL).journeyRequestId(1L);
	}

	public static ClientJourneyRequests defaultClientJourneyRequests() {
		return new ClientJourneyRequests(5, 10, 0, 2, true, clientJourneyRequestDtos.stream()
				.map(e -> mapclientJourneyRequestDtoOutputToclientJourneyRequestDto(e)).collect(Collectors.toList()));
	}

	public static ClientJourneyRequestsOutput defaultClientJourneyRequestsOutput() {
		return new ClientJourneyRequestsOutput(5, 10, 0, 2, true, clientJourneyRequestDtos);
	}

	public static ClientJourneyRequestDtoOutput defaultClientJourneyRequestDtoOutput() {
		return clientJourneyRequestDtos.get(0);
	}

	public static ClientJourneyRequestDto defaultClientJourneyRequestDto() {

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		ZonedDateTime userDateTimeZoned = clientJourneyRequestDtos.get(0).getDateTime().atZone(userZoneId);

		return new ClientJourneyRequestDto(clientJourneyRequestDtos.get(0).getId(),
				new ClientJourneyRequestDto.PlaceDto(clientJourneyRequestDtos.get(0).getDeparturePlace().getId(),
						clientJourneyRequestDtos.get(0).getDeparturePlace().getType(),
						clientJourneyRequestDtos.get(0).getDeparturePlace().getName(),
						clientJourneyRequestDtos.get(0).getDeparturePlace().getLatitude(),
						clientJourneyRequestDtos.get(0).getDeparturePlace().getLongitude(),
						clientJourneyRequestDtos.get(0).getDeparturePlace().getDepartmentId()),
				new ClientJourneyRequestDto.PlaceDto(clientJourneyRequestDtos.get(0).getArrivalPlace().getId(),
						clientJourneyRequestDtos.get(0).getArrivalPlace().getType(),
						clientJourneyRequestDtos.get(0).getArrivalPlace().getName(),
						clientJourneyRequestDtos.get(0).getArrivalPlace().getLatitude(),
						clientJourneyRequestDtos.get(0).getArrivalPlace().getLongitude(),
						clientJourneyRequestDtos.get(0).getArrivalPlace().getDepartmentId()),
				new ClientJourneyRequestDto.EngineTypeDto(clientJourneyRequestDtos.get(0).getEngineType().getId(),
						clientJourneyRequestDtos.get(0).getEngineType().getName(),
						clientJourneyRequestDtos.get(0).getEngineType().getCode()),
				clientJourneyRequestDtos.get(0).getDistance(),

				userDateTimeZoned.toLocalDateTime(), clientJourneyRequestDtos.get(0).getCreationDateTime(),
				clientJourneyRequestDtos.get(0).getWorkers(), clientJourneyRequestDtos.get(0).getDescription(),
				clientJourneyRequestDtos.get(0).getProposalsCount()

		);

	}

	public static ClientJourneyRequestDtoOutput defaultClientJourneyRequestDtoWithNoProposals() {
		return new ClientJourneyRequestDtoOutput() {

			@Override
			public Long getId() {

				return 1L;
			}

			@Override
			public PlaceDto getDeparturePlace() {

				return new PlaceDto(1L, "DEPARTMENT", "departurePlaceName", new BigDecimal(34.486523),
						new BigDecimal(10.486523), 1L);
			}

			@Override
			public PlaceDto getArrivalPlace() {
				return new PlaceDto(2L, "DEPARTMENT", "arrivalPlaceName1", new BigDecimal(36.486523),
						new BigDecimal(10.486523), 2L);
			}

			@Override
			public EngineTypeDto getEngineType() {
				return new EngineTypeDto(1L, "engineType1", "UTILITY");
			}

			@Override
			public Integer getDistance() {
				return 100000;
			}

			@Override
			public Instant getDateTime() {
				return startDate.toInstant();
			}

			@Override
			public Instant getCreationDateTime() {
				return endDate.minusDays(1).toInstant();
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

				return 0;
			}

		};
	}

	public static List<ClientJourneyRequestDtoOutput> defaultClientJourneyRequestDtoList() {
		return clientJourneyRequestDtos;
	}

	private static JourneyRequestSearchDto mapToJourneyRequestSearchDto(JourneyRequestSearchOutput jrso,
			ZoneId userZoneId) {

		return JourneyRequestSearchDto.builder().id(jrso.getId())
				.departurePlace(new Place(jrso.getDeparturePlace().getId(), jrso.getDeparturePlace().getType(),
						jrso.getDeparturePlace().getName(), jrso.getDeparturePlace().getLatitude(),
						jrso.getDeparturePlace().getLongitude(), jrso.getDeparturePlace().getDepartmentId()))
				.arrivalPlace(new Place(jrso.getArrivalPlace().getId(), jrso.getArrivalPlace().getType(),
						jrso.getArrivalPlace().getName(), jrso.getArrivalPlace().getLatitude(),
						jrso.getArrivalPlace().getLongitude(), jrso.getArrivalPlace().getDepartmentId()))
				.engineType(new EngineType(jrso.getEngineType().getId(), jrso.getEngineType().getName()))
				.distance(jrso.getDistance()).hours(jrso.getHours()).minutes(jrso.getMinutes())
				.dateTime(jrso.getDateTime().atZone(userZoneId).toLocalDateTime()).workers(jrso.getWorkers())
				.description(jrso.getDescription())
				.client(new Client(jrso.getClient().getId(), jrso.getClient().getFirstname(),documentUrlResolver.resolveUrl(jrso.getClient().getImageId(), jrso.getClient().getImageHash())
						))
				.minPrice(jrso.getMinPrice()).build();

	}

	private static ClientJourneyRequestDto mapclientJourneyRequestDtoOutputToclientJourneyRequestDto(
			ClientJourneyRequestDtoOutput cjrdo) {
		return new ClientJourneyRequestDto(cjrdo.getId(),
				new ClientJourneyRequestDto.PlaceDto(cjrdo.getDeparturePlace().getId(),
						cjrdo.getDeparturePlace().getType(), cjrdo.getDeparturePlace().getName(),
						cjrdo.getDeparturePlace().getLatitude(), cjrdo.getDeparturePlace().getLongitude(),
						cjrdo.getDeparturePlace().getDepartmentId()),
				new ClientJourneyRequestDto.PlaceDto(cjrdo.getArrivalPlace().getId(), cjrdo.getArrivalPlace().getType(),
						cjrdo.getArrivalPlace().getName(), cjrdo.getArrivalPlace().getLatitude(),
						cjrdo.getArrivalPlace().getLongitude(), cjrdo.getArrivalPlace().getDepartmentId()),
				new ClientJourneyRequestDto.EngineTypeDto(cjrdo.getEngineType().getId(),
						cjrdo.getEngineType().getName(), cjrdo.getEngineType().getCode()),
				cjrdo.getDistance(), cjrdo.getDateTime().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(),
				cjrdo.getCreationDateTime(), cjrdo.getWorkers(), cjrdo.getDescription(), cjrdo.getProposalsCount());

	}
}
