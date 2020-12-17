package com.excentria_it.wamya.test.data.common;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand.SearchJourneyRequestsCommandBuilder;
import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.domain.ClientDto;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.PlaceDto;

public class SearchJourneyRequestsTestData {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	private static LocalDateTime startDate = LocalDateTime.now();
	private static LocalDateTime endDate = addDays(startDate, 1);

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
				public Integer getDistance() {

					return 100;
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
				public Integer getDistance() {

					return 90;
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
				.sortingCriterion(new SortingCriterion("min-price", "desc"));

	}

	public static SearchJourneyRequestsCommandBuilder arrivalPlaceRegionAgnosticSearchJourneyRequestsCommandBuilder() {
		LocalDateTime startDate = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER),
				DATE_TIME_FORMATTER);
		LocalDateTime endDate = LocalDateTime.parse(addDays(startDate, 1).format(DATE_TIME_FORMATTER),
				DATE_TIME_FORMATTER);

		return SearchJourneyRequestsCommand.builder().departurePlaceRegionId("departurePlaceRegionId")
				.arrivalPlaceRegionIds(Set.of(SearchJourneyRequestsCommand.ANY_ARRIVAL_REGION)).startDateTime(startDate)
				.endDateTime(endDate).engineTypes(Set.of(1L, 2L)).pageNumber(0).pageSize(2)
				.sortingCriterion(new SortingCriterion("min-price", "desc"));

	}

	private static LocalDateTime addDays(LocalDateTime dateTime, int days) {

		ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);

		LocalDateTime dateAfter = zonedDateTime.plusDays(days).toLocalDateTime();

		return dateAfter;
	}

	public static Page<JourneyRequestSearchDto> defaultJourneyRequestSearchDtoPage() {

		return new Page<JourneyRequestSearchDto>() {

			@Override
			public int getNumber() {

				return 0;
			}

			@Override
			public int getSize() {

				return 2;
			}

			@Override
			public int getNumberOfElements() {

				return 2;
			}

			@Override
			public List<JourneyRequestSearchDto> getContent() {

				return journeyRequestSearchDtos;
			}

			@Override
			public boolean hasContent() {

				return true;
			}

			@Override
			public Sort getSort() {

				return Sort.by(List.of(new Order(Direction.DESC, "min-price")));
			}

			@Override
			public boolean isFirst() {

				return true;
			}

			@Override
			public boolean isLast() {

				return false;
			}

			@Override
			public boolean hasNext() {

				return true;
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
			public Iterator<JourneyRequestSearchDto> iterator() {

				return journeyRequestSearchDtos.iterator();
			}

			@Override
			public int getTotalPages() {

				return 5;
			}

			@Override
			public long getTotalElements() {

				return 10;
			}

			@Override
			public <U> Page<U> map(Function<? super JourneyRequestSearchDto, ? extends U> converter) {

				return null;
			}
		};

	}

	public static JourneyRequestsSearchResult defaultJourneyRequestsSearchResult() {

		return new JourneyRequestsSearchResult(5, 10, 0, 2, true, journeyRequestSearchDtos);
	}

}
