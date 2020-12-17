package com.excentria_it.wamya.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
public class JourneyRequestRepositoryTests {

	@Autowired
	private JourneyRequestRepository journeyRequestRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private EngineTypeRepository engineTypeRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@BeforeEach
	public void cleanDatabase() {
		journeyRequestRepository.deleteAll();

	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween_OrderByMinPriceDesc() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();
		List<EngineTypeJpaEntity> engineTypes = givenEngineTypes();
		List<UserAccountJpaEntity> clients = givenClients();
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<UserAccountJpaEntity> transporters = givenTransporters();
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(100, 200, 300), dates, endDates,
				List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaces.get(0).getRegionId(),
						Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId()),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), PageRequest.of(0, 1000,
								// Sort.by(List.of(new Order(Direction.DESC, "(minPrice)")))
								JpaSort.unsafe(Direction.DESC, "(minPrice)")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(2, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(1).getDeparturePlace().getPlaceRegionId());

		assertTrue(Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId())
				.containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getPlaceRegionId())
						.collect(Collectors.toSet())));

		assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
				.getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

		assertTrue(journeyRequests.getContent().get(0).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(0).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertTrue(journeyRequests.getContent().get(1).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(1).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertTrue(
				journeyRequests.getContent().get(0).getMinPrice() >= journeyRequests.getContent().get(1).getMinPrice());

	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween_OrderByMinPriceDesc() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();
		List<EngineTypeJpaEntity> engineTypes = givenEngineTypes();
		List<UserAccountJpaEntity> clients = givenClients();
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<UserAccountJpaEntity> transporters = givenTransporters();
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(100, 200, 300), dates, endDates,
				List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaces.get(0).getRegionId(),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), PageRequest.of(0, 1000,
								// Sort.by(List.of(new Order(Direction.DESC, "(minPrice)")))
								JpaSort.unsafe(Direction.DESC, "(minPrice)")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(2, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(1).getDeparturePlace().getPlaceRegionId());

		assertTrue(Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId())
				.containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getPlaceRegionId())
						.collect(Collectors.toSet())));

		assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
				.getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

		assertTrue(journeyRequests.getContent().get(0).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(0).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertTrue(journeyRequests.getContent().get(1).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(1).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertTrue(
				journeyRequests.getContent().get(0).getMinPrice() >= journeyRequests.getContent().get(1).getMinPrice());

	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween_OrderByMinPriceAsc_WithPaging() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();
		List<EngineTypeJpaEntity> engineTypes = givenEngineTypes();
		List<UserAccountJpaEntity> clients = givenClients();
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<UserAccountJpaEntity> transporters = givenTransporters();
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(100, 200, 300), dates, endDates,
				List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaces.get(0).getRegionId(),
						Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId()),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), PageRequest.of(1, 1,
								// Sort.by(List.of(new Order(Direction.DESC, "(minPrice)")))
								JpaSort.unsafe(Direction.ASC, "(minPrice)")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(1, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(1).getDeparturePlace().getPlaceRegionId());

		assertTrue(Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId())
				.containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getPlaceRegionId())
						.collect(Collectors.toSet())));

		assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
				.getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

		assertTrue(journeyRequests.getContent().get(0).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(0).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertEquals(journeyRequests.getContent().get(0).getMinPrice(), 240);

	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween_OrderByMinPriceAsc_WithPaging() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();
		List<EngineTypeJpaEntity> engineTypes = givenEngineTypes();
		List<UserAccountJpaEntity> clients = givenClients();
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<UserAccountJpaEntity> transporters = givenTransporters();
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(100, 200, 300), dates, endDates,
				List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaces.get(0).getRegionId(),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), PageRequest.of(1, 1,
								// Sort.by(List.of(new Order(Direction.DESC, "(minPrice)")))
								JpaSort.unsafe(Direction.ASC, "(minPrice)")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(1, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(1).getDeparturePlace().getPlaceRegionId());

		assertTrue(Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId())
				.containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getPlaceRegionId())
						.collect(Collectors.toSet())));

		assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
				.getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

		assertTrue(journeyRequests.getContent().get(0).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(0).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertEquals(journeyRequests.getContent().get(0).getMinPrice(), 240);

	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween_OrderByDateTimeDesc() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();
		List<EngineTypeJpaEntity> engineTypes = givenEngineTypes();
		List<UserAccountJpaEntity> clients = givenClients();
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<UserAccountJpaEntity> transporters = givenTransporters();
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(100, 200, 300), dates, endDates,
				List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaces.get(0).getRegionId(),
						Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId()),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), PageRequest.of(0, 1000,

								Sort.by(Direction.DESC, "dateTime")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(2, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(1).getDeparturePlace().getPlaceRegionId());

		assertTrue(Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId())
				.containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getPlaceRegionId())
						.collect(Collectors.toSet())));

		assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
				.getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

		assertTrue(journeyRequests.getContent().get(0).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(0).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertTrue(journeyRequests.getContent().get(0).getDateTime()
				.isAfter(journeyRequests.getContent().get(1).getDateTime()));

	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween_OrderByDateTimeDesc() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();
		List<EngineTypeJpaEntity> engineTypes = givenEngineTypes();
		List<UserAccountJpaEntity> clients = givenClients();
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<UserAccountJpaEntity> transporters = givenTransporters();
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(100, 200, 300), dates, endDates,
				List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaces.get(0).getRegionId(),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), PageRequest.of(0, 1000,

								Sort.by(Direction.DESC, "dateTime")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(2, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(1).getDeparturePlace().getPlaceRegionId());

		assertTrue(Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId())
				.containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getPlaceRegionId())
						.collect(Collectors.toSet())));

		assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
				.getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

		assertTrue(journeyRequests.getContent().get(0).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(0).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertTrue(journeyRequests.getContent().get(0).getDateTime()
				.isAfter(journeyRequests.getContent().get(1).getDateTime()));

	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween_OrderByDistanceDesc() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();
		List<EngineTypeJpaEntity> engineTypes = givenEngineTypes();
		List<UserAccountJpaEntity> clients = givenClients();
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<UserAccountJpaEntity> transporters = givenTransporters();
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(100, 200, 300), dates, endDates,
				List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaces.get(0).getRegionId(),
						Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId()),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), PageRequest.of(0, 1000,

								Sort.by(Direction.DESC, "distance")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(2, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(1).getDeparturePlace().getPlaceRegionId());

		assertTrue(Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId())
				.containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getPlaceRegionId())
						.collect(Collectors.toSet())));

		assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
				.getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

		assertTrue(journeyRequests.getContent().get(0).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(0).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertTrue(
				journeyRequests.getContent().get(0).getDistance() >= journeyRequests.getContent().get(1).getDistance());

	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween_OrderByDistanceDesc() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();
		List<EngineTypeJpaEntity> engineTypes = givenEngineTypes();
		List<UserAccountJpaEntity> clients = givenClients();
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<UserAccountJpaEntity> transporters = givenTransporters();
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(100, 200, 300), dates, endDates,
				List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaces.get(0).getRegionId(),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), PageRequest.of(0, 1000,

								Sort.by(Direction.DESC, "distance")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(2, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(1).getDeparturePlace().getPlaceRegionId());

		assertTrue(Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId())
				.containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getPlaceRegionId())
						.collect(Collectors.toSet())));

		assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
				.getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

		assertTrue(journeyRequests.getContent().get(0).getDateTime().isAfter(searchStartAndEndDates.get(0)));
		assertTrue(journeyRequests.getContent().get(0).getDateTime().isBefore(searchStartAndEndDates.get(1)));

		assertTrue(
				journeyRequests.getContent().get(0).getDistance() >= journeyRequests.getContent().get(1).getDistance());

	}

	private List<LocalDateTime> getDateBeforeAndDateAfter(LocalDate date) {

		ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneOffset.UTC);

		LocalDateTime dateBefore = zonedDateTime.minusDays(1).toLocalDateTime();

		LocalDateTime dateAfter = zonedDateTime.plusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59)
				.plusNanos(999999999).toLocalDateTime();

		return List.of(dateBefore, dateAfter);
	}

	private List<String> givenDescriptions() {
		return List.of("Need a transporter URGENT Today!!!", "Need a transporter URGENT Tomorrow!!!",
				"Need a transporter URGENT Overmorrow!!!");

	}

	private List<LocalDateTime> givenLocalDateTimes() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 10, 12, 0, 0, 0);
		ZonedDateTime todayZoned12PM = ZonedDateTime.of(ldt, ZoneOffset.UTC);

		ZonedDateTime tomorrowZoned12PM = todayZoned12PM.plusDays(1);

		ZonedDateTime overmorrowZoned12PM = tomorrowZoned12PM.plusDays(1);

		return List.of(todayZoned12PM.toLocalDateTime(), tomorrowZoned12PM.toLocalDateTime(),
				overmorrowZoned12PM.toLocalDateTime());

	}

	private List<LocalDateTime> givenEndDates() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 10, 23, 59, 59, 999999999);
		ZonedDateTime todayZoned11PM59 = ZonedDateTime.of(ldt, ZoneOffset.UTC);

		ZonedDateTime tomorrowZoned11PM59 = todayZoned11PM59.plusDays(1);

		ZonedDateTime overmorrowZoned11PM59 = tomorrowZoned11PM59.plusDays(1);

		return List.of(todayZoned11PM59.toLocalDateTime(), tomorrowZoned11PM59.toLocalDateTime(),
				overmorrowZoned11PM59.toLocalDateTime());
	}

	private List<PlaceJpaEntity> givenDeparturePlaces() {
		List<PlaceJpaEntity> departurePlaces = List.of(
				new PlaceJpaEntity("DeparturePlace1", "DepartureRegion1", "Departure Place 1"),
				new PlaceJpaEntity("DeparturePlace2", "DepartureRegion1", "Departure Place 2"),
				new PlaceJpaEntity("DeparturePlace3", "DepartureRegion3", "Departure Place 3"));
		return placeRepository.saveAll(departurePlaces);
	}

	private List<PlaceJpaEntity> givenArrivalPlaces() {
		List<PlaceJpaEntity> arrivalPlaces = List.of(
				new PlaceJpaEntity("ArrivalPlace1", "ArrivalPlaceRegion1", "Arrival Place 1"),
				new PlaceJpaEntity("ArrivalPlace2", "ArrivalPlaceRegion2", "Arrival Place 2"),
				new PlaceJpaEntity("ArrivalPlace3", "ArrivalPlaceRegion3", "Arrival Place 3"));
		return placeRepository.saveAll(arrivalPlaces);
	}

	private List<EngineTypeJpaEntity> givenEngineTypes() {
		List<EngineTypeJpaEntity> engineTypes = List.of(new EngineTypeJpaEntity(null, "EngineType1"),
				new EngineTypeJpaEntity(null, "EngineType2"), new EngineTypeJpaEntity(null, "EngineType3"));
		return engineTypeRepository.saveAll(engineTypes);
	}

	private List<UserAccountJpaEntity> givenClients() {
		List<UserAccountJpaEntity> clients = List.of(
				UserAccountJpaEntity.builder().firstname("Client1").photoUrl("https://path/to/client1/photo").build(),
				UserAccountJpaEntity.builder().firstname("Client2").photoUrl("https://path/to/client2/photo").build(),
				UserAccountJpaEntity.builder().firstname("Client3").photoUrl("https://path/to/client3/photo").build());
		return userAccountRepository.saveAll(clients);
	}

	private List<UserAccountJpaEntity> givenTransporters() {
		List<UserAccountJpaEntity> transporters = List.of(
				UserAccountJpaEntity.builder().firstname("Transporter1").build(),
				UserAccountJpaEntity.builder().firstname("Transporter2").build(),
				UserAccountJpaEntity.builder().firstname("Transporter3").build());
		return userAccountRepository.saveAll(transporters);
	}

	private Map<Integer, Set<JourneyProposalJpaEntity>> givenProposals(List<UserAccountJpaEntity> transporters) {
		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = Map.of(0,
				Set.of(JourneyProposalJpaEntity.builder().price(150).transporter(transporters.get(0)).build(),
						JourneyProposalJpaEntity.builder().price(160).transporter(transporters.get(1)).build(),
						JourneyProposalJpaEntity.builder().price(140).transporter(transporters.get(2)).build()),
				1,
				Set.of(JourneyProposalJpaEntity.builder().price(240).transporter(transporters.get(0)).build(),
						JourneyProposalJpaEntity.builder().price(260).transporter(transporters.get(1)).build(),
						JourneyProposalJpaEntity.builder().price(250).transporter(transporters.get(2)).build()),
				2,
				Set.of(JourneyProposalJpaEntity.builder().price(340).transporter(transporters.get(0)).build(),
						JourneyProposalJpaEntity.builder().price(350).transporter(transporters.get(1)).build(),
						JourneyProposalJpaEntity.builder().price(360).transporter(transporters.get(2)).build()));

		return proposalsMap;
	}

	private void givenJourneyRequests(List<PlaceJpaEntity> departurePlaces, List<PlaceJpaEntity> arrivalPlaces,
			List<EngineTypeJpaEntity> engineTypes, List<Integer> distances, List<LocalDateTime> dates,
			List<LocalDateTime> endDates, List<Integer> workers, List<String> descriptions,
			List<UserAccountJpaEntity> clients, Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap) {

		JourneyRequestJpaEntity jrToday = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(0))
				.arrivalPlace(arrivalPlaces.get(0)).engineType(engineTypes.get(0)).distance(distances.get(0))
				.dateTime(dates.get(0)).endDateTime(endDates.get(0)).workers(workers.get(0))
				.description(descriptions.get(0)).client(clients.get(0)).proposals(proposalsMap.get(0)).build();

		JourneyRequestJpaEntity jrTomorrow = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(1))
				.arrivalPlace(arrivalPlaces.get(1)).engineType(engineTypes.get(1)).distance(distances.get(1))
				.dateTime(dates.get(1)).endDateTime(endDates.get(1)).workers(workers.get(1))
				.description(descriptions.get(1)).client(clients.get(1)).proposals(proposalsMap.get(1)).build();

		JourneyRequestJpaEntity jrOvermorrow = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(2))
				.arrivalPlace(arrivalPlaces.get(2)).engineType(engineTypes.get(2)).distance(distances.get(2))
				.dateTime(dates.get(2)).endDateTime(endDates.get(2)).workers(workers.get(2))
				.description(descriptions.get(2)).client(clients.get(2)).proposals(proposalsMap.get(2)).build();

		journeyRequestRepository.saveAll(List.of(jrToday, jrTomorrow, jrOvermorrow));

	}

}
