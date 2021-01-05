package com.excentria_it.wamya.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity.EngineTypeCode;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedEngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class JourneyRequestRepositoryTests {

	@Autowired
	private JourneyRequestRepository journeyRequestRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private TransporterRepository transporterRepository;

	@Autowired
	private EngineTypeRepository engineTypeRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private VehiculeRepository vehiculeRepository;

	@Autowired
	private ModelRepository modelRepository;

	@Autowired
	private ConstructorRepository constructorRepository;

	@Autowired
	private InternationalCallingCodeRepository internationalCallingCodeRepository;

	@BeforeEach
	public void cleanDatabase() {
		transporterRepository.deleteAll();
		clientRepository.deleteAll();
		journeyRequestRepository.deleteAll();
		engineTypeRepository.deleteAll();
		placeRepository.deleteAll();
		vehiculeRepository.deleteAll();
		modelRepository.deleteAll();
		constructorRepository.deleteAll();
		internationalCallingCodeRepository.deleteAll();
	}

	@Test
	public void testFindByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween_OrderByMinPriceDesc() {

		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));
		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();
		List<Double> distances = givenDistances();
		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
				descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaces.get(0).getRegionId(),
						Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId()),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), "en_US", PageRequest.of(0, 1000,
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

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();
		List<Double> distances = givenDistances();
		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
				descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaces.get(0).getRegionId(),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), "en_US", PageRequest.of(0, 1000,
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

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();
		List<Double> distances = givenDistances();
		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
				descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaces.get(0).getRegionId(),
						Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId()),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), "en_US", PageRequest.of(1, 1,
								// Sort.by(List.of(new Order(Direction.DESC, "(minPrice)")))
								JpaSort.unsafe(Direction.ASC, "(minPrice)")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(1, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

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

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();
		List<Double> distances = givenDistances();
		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
				descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaces.get(0).getRegionId(),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), "en_US", PageRequest.of(1, 1,
								// Sort.by(List.of(new Order(Direction.DESC, "(minPrice)")))
								JpaSort.unsafe(Direction.ASC, "(minPrice)")));

		// Then
		assertNotNull(journeyRequests);

		assertEquals(1, journeyRequests.getNumberOfElements());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

		assertEquals(departurePlaces.get(0).getRegionId(),
				journeyRequests.getContent().get(0).getDeparturePlace().getPlaceRegionId());

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

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();
		List<Double> distances = givenDistances();
		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
				descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaces.get(0).getRegionId(),
						Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId()),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), "en_US", PageRequest.of(0, 1000,

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

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<Double> distances = givenDistances();

		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
				descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaces.get(0).getRegionId(),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), "en_US", PageRequest.of(0, 1000,

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

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112.7D, 205.5, 308.2), dates,
				endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaces.get(0).getRegionId(),
						Set.of(arrivalPlaces.get(0).getRegionId(), arrivalPlaces.get(1).getRegionId()),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), "en_US", PageRequest.of(0, 1000,

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

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112.7D, 205.5, 308.2), dates,
				endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap);

		List<LocalDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(dates.get(0).toLocalDate());

		// When

		Page<JourneyRequestSearchDto> journeyRequests = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaces.get(0).getRegionId(),
						Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()), searchStartAndEndDates.get(0),
						searchStartAndEndDates.get(1), "en_US", PageRequest.of(0, 1000,

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
	void testFindByCreationDateTimeBetweenAndClient_Email() {
		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112.7D, 205.5, 308.2), dates,
				endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap);

		// When

		LocalDateTime higherEdge = LocalDateTime.now();
		LocalDateTime lowerEdge = endDates.get(0).minusDays(1);

		Page<ClientJourneyRequestDto> journeyRequests = journeyRequestRepository
				.findByCreationDateTimeBetweenAndClient_Email(lowerEdge, higherEdge, clients.get(0).getEmail(), "en_US",
						PageRequest.of(0, 25,

								Sort.by(Direction.DESC, "creationDateTime")));

		// Then
		assertNotNull(journeyRequests);
		assertEquals(1, journeyRequests.getNumberOfElements());
		assertEquals(3, journeyRequests.getContent().get(0).getProposalsCount());

		JourneyRequestJpaEntity clientJourneyRequest = journeyRequestRepository
				.findById(journeyRequests.getContent().get(0).getId()).get();

		assertEquals(clients.get(0).getEmail(), clientJourneyRequest.getClient().getEmail());
		assertTrue(journeyRequests.getContent().get(0).getCreationDateTime().isAfter(lowerEdge)
				|| journeyRequests.getContent().get(0).getCreationDateTime().isEqual(lowerEdge));

		assertTrue(journeyRequests.getContent().get(0).getCreationDateTime().isBefore(higherEdge)
				|| journeyRequests.getContent().get(0).getCreationDateTime().isEqual(higherEdge));

	}

	@Test
	void testFindByCreationDateTimeBetweenAndClient_MobileNumberAndClient_IccValue() {
		// Given
		List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces();
		List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces();

		List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes();
		List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
				engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		List<ClientJpaEntity> clients = givenClients(icc);
		List<LocalDateTime> dates = givenLocalDateTimes();
		List<LocalDateTime> endDates = givenEndDates();

		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);

		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypesListList, models);
		List<TransporterJpaEntity> transporters = givenTransporters(vehicules);
		List<String> descriptions = givenDescriptions();

		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicules);

		givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112.7D, 205.5, 308.2), dates,
				endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap);

		// When

		LocalDateTime higherEdge = LocalDateTime.now();
		LocalDateTime lowerEdge = endDates.get(0).minusDays(1);

		Page<ClientJourneyRequestDto> journeyRequests = journeyRequestRepository
				.findByCreationDateTimeBetweenAndClient_MobileNumberAndClient_IccValue(lowerEdge, higherEdge,
						clients.get(0).getMobileNumber(), clients.get(0).getIcc().getValue(), "en_US",
						PageRequest.of(0, 25,

								Sort.by(Direction.DESC, "creationDateTime")));

		// Then
		assertNotNull(journeyRequests);
		assertEquals(1, journeyRequests.getNumberOfElements());
		assertEquals(3, journeyRequests.getContent().get(0).getProposalsCount());

		JourneyRequestJpaEntity clientJourneyRequest = journeyRequestRepository
				.findById(journeyRequests.getContent().get(0).getId()).get();

		assertEquals(clients.get(0).getMobileNumber(), clientJourneyRequest.getClient().getMobileNumber());
		assertEquals(clients.get(0).getIcc().getValue(), clientJourneyRequest.getClient().getIcc().getValue());

		assertTrue(journeyRequests.getContent().get(0).getCreationDateTime().isAfter(lowerEdge)
				|| journeyRequests.getContent().get(0).getCreationDateTime().isEqual(lowerEdge));

		assertTrue(journeyRequests.getContent().get(0).getCreationDateTime().isBefore(higherEdge)
				|| journeyRequests.getContent().get(0).getCreationDateTime().isEqual(higherEdge));

	}

	private List<Double> givenDistances() {

		return List.of(112.7D, 205.5, 308.2);
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

		LocalDateTime tomorrow12PM = ldt.plusDays(1);

		LocalDateTime overmorrow12PM = tomorrow12PM.plusDays(1);

		return List.of(ldt, tomorrow12PM, overmorrow12PM);

	}

	private List<LocalDateTime> givenEndDates() {

		LocalDateTime ldt = LocalDateTime.of(2020, 12, 9, 23, 59, 59, 999999999);

		LocalDateTime tomorrow11PM59 = ldt.plusDays(1);

		LocalDateTime overmorrowZoned11PM59 = tomorrow11PM59.plusDays(1);

		return List.of(ldt, tomorrow11PM59, overmorrowZoned11PM59);
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

	private List<List<EngineTypeJpaEntity>> givenEngineTypes() {
		// Engine type 11
		EngineTypeJpaEntity et11 = new EngineTypeJpaEntity();
		et11.setCode(EngineTypeCode.VAN_L1H1);

		LocalizedEngineTypeJpaEntity let11en = new LocalizedEngineTypeJpaEntity();
		let11en.setLocalizedId(new LocalizedId("en_US"));
		let11en.setEngineType(et11);
		let11en.setName("EngineType11");
		let11en.setDescription("EngineTypeDescription11");
		et11.getLocalizations().put("en_US", let11en);

		LocalizedEngineTypeJpaEntity let11fr = new LocalizedEngineTypeJpaEntity();
		let11fr.setLocalizedId(new LocalizedId("fr_FR"));
		let11fr.setEngineType(et11);
		let11fr.setName("TypeVehicule11");
		let11fr.setDescription("DescriptionTypeVehicule11");
		et11.getLocalizations().put("fr_FR", let11fr);

		// Engine type 12
		EngineTypeJpaEntity et12 = new EngineTypeJpaEntity();
		et12.setCode(EngineTypeCode.VAN_L2H2);

		LocalizedEngineTypeJpaEntity let12en = new LocalizedEngineTypeJpaEntity();
		let12en.setLocalizedId(new LocalizedId("en_US"));
		let12en.setEngineType(et12);
		let12en.setName("EngineType12");
		let12en.setDescription("EngineTypeDescription12");
		et12.getLocalizations().put("en_US", let12en);

		LocalizedEngineTypeJpaEntity let12fr = new LocalizedEngineTypeJpaEntity();
		let12fr.setLocalizedId(new LocalizedId("fr_FR"));
		let12fr.setEngineType(et12);
		let12fr.setName("TypeVehicule12");
		let12fr.setDescription("DescriptionTypeVehicule12");
		et12.getLocalizations().put("fr_FR", let12fr);

		// Engine type 13
		EngineTypeJpaEntity et13 = new EngineTypeJpaEntity();
		et13.setCode(EngineTypeCode.VAN_L3H2);

		LocalizedEngineTypeJpaEntity let13en = new LocalizedEngineTypeJpaEntity();
		let13en.setLocalizedId(new LocalizedId("en_US"));
		let13en.setEngineType(et13);
		let13en.setName("EngineType13");
		let13en.setDescription("EngineTypeDescription13");
		et13.getLocalizations().put("en_US", let13en);

		LocalizedEngineTypeJpaEntity let13fr = new LocalizedEngineTypeJpaEntity();
		let13fr.setLocalizedId(new LocalizedId("fr_FR"));
		let13fr.setEngineType(et13);
		let13fr.setName("TypeVehicule13");
		let13fr.setDescription("DescriptionTypeVehicule3");
		et13.getLocalizations().put("fr_FR", let13fr);

		// Engine type 21
		EngineTypeJpaEntity et21 = new EngineTypeJpaEntity();
		et21.setCode(EngineTypeCode.FLATBED_TRUCK);

		LocalizedEngineTypeJpaEntity let21en = new LocalizedEngineTypeJpaEntity();
		let21en.setLocalizedId(new LocalizedId("en_US"));
		let21en.setEngineType(et21);
		let21en.setName("EngineType21");
		let21en.setDescription("EngineTypeDescription21");
		et21.getLocalizations().put("en_US", let21en);

		LocalizedEngineTypeJpaEntity let21fr = new LocalizedEngineTypeJpaEntity();
		let21fr.setLocalizedId(new LocalizedId("fr_FR"));
		let21fr.setEngineType(et21);
		let21fr.setName("TypeVehicule21");
		let21fr.setDescription("DescriptionTypeVehicule21");
		et21.getLocalizations().put("fr_FR", let21fr);

		// Engine type 22
		EngineTypeJpaEntity et22 = new EngineTypeJpaEntity();
		et22.setCode(EngineTypeCode.UTILITY);

		LocalizedEngineTypeJpaEntity let22en = new LocalizedEngineTypeJpaEntity();
		let22en.setLocalizedId(new LocalizedId("en_US"));
		let22en.setEngineType(et22);
		let22en.setName("EngineType22");
		let22en.setDescription("EngineTypeDescription22");
		et22.getLocalizations().put("en_US", let22en);

		LocalizedEngineTypeJpaEntity let22fr = new LocalizedEngineTypeJpaEntity();
		let22fr.setLocalizedId(new LocalizedId("fr_FR"));
		let22fr.setEngineType(et22);
		let22fr.setName("TypeVehicule22");
		let22fr.setDescription("DescriptionTypeVehicule22");
		et22.getLocalizations().put("fr_FR", let22fr);

		// Engine type 23
		EngineTypeJpaEntity et23 = new EngineTypeJpaEntity();
		et23.setCode(EngineTypeCode.DUMP_TRUCK);
		;

		LocalizedEngineTypeJpaEntity let23en = new LocalizedEngineTypeJpaEntity();
		let23en.setLocalizedId(new LocalizedId("en_US"));
		let23en.setEngineType(et23);
		let23en.setName("EngineType23");
		let23en.setDescription("EngineTypeDescription23");
		et23.getLocalizations().put("en_US", let23en);

		LocalizedEngineTypeJpaEntity let23fr = new LocalizedEngineTypeJpaEntity();
		let23fr.setLocalizedId(new LocalizedId("fr_FR"));
		let23fr.setEngineType(et23);
		let23fr.setName("TypeVehicule23");
		let23fr.setDescription("DescriptionTypeVehicule23");
		et23.getLocalizations().put("fr_FR", let23fr);

		// Engine type 31
		EngineTypeJpaEntity et31 = new EngineTypeJpaEntity();
		et31.setCode(EngineTypeCode.BOX_TRUCK);

		LocalizedEngineTypeJpaEntity let31en = new LocalizedEngineTypeJpaEntity();
		let31en.setLocalizedId(new LocalizedId("en_US"));
		let31en.setEngineType(et31);
		let31en.setName("EngineType31");
		let31en.setDescription("EngineTypeDescription31");
		et31.getLocalizations().put("en_US", let31en);

		LocalizedEngineTypeJpaEntity let31fr = new LocalizedEngineTypeJpaEntity();
		let31fr.setLocalizedId(new LocalizedId("fr_FR"));
		let31fr.setEngineType(et31);
		let31fr.setName("TypeVehicule31");
		let31fr.setDescription("DescriptionTypeVehicule31");
		et31.getLocalizations().put("fr_FR", let31fr);

		// Engine type 32
		EngineTypeJpaEntity et32 = new EngineTypeJpaEntity();
		et32.setCode(EngineTypeCode.TANKER);

		LocalizedEngineTypeJpaEntity let32en = new LocalizedEngineTypeJpaEntity();
		let32en.setLocalizedId(new LocalizedId("en_US"));
		let32en.setEngineType(et32);
		let32en.setName("EngineType32");
		let32en.setDescription("EngineTypeDescription32");
		et32.getLocalizations().put("en_US", let32en);

		LocalizedEngineTypeJpaEntity let32fr = new LocalizedEngineTypeJpaEntity();
		let32fr.setLocalizedId(new LocalizedId("fr_FR"));
		let32fr.setEngineType(et32);
		let32fr.setName("TypeVehicule32");
		let32fr.setDescription("DescriptionTypeVehicule32");
		et32.getLocalizations().put("fr_FR", let32fr);

		// Engine type 33
		EngineTypeJpaEntity et33 = new EngineTypeJpaEntity();
		et33.setCode(EngineTypeCode.BUS);

		LocalizedEngineTypeJpaEntity let33en = new LocalizedEngineTypeJpaEntity();
		let33en.setLocalizedId(new LocalizedId("en_US"));
		let33en.setEngineType(et33);
		let33en.setName("EngineType33");
		let33en.setDescription("EngineTypeDescription33");
		et33.getLocalizations().put("en_US", let33en);

		LocalizedEngineTypeJpaEntity let33fr = new LocalizedEngineTypeJpaEntity();
		let33fr.setLocalizedId(new LocalizedId("fr_FR"));
		let33fr.setEngineType(et33);
		let33fr.setName("TypeVehicule33");
		let33fr.setDescription("DescriptionTypeVehicule3");
		et33.getLocalizations().put("fr_FR", let33fr);

		List<EngineTypeJpaEntity> engineTypes1 = List.of(et11, et12, et13);
		List<EngineTypeJpaEntity> engineTypes2 = List.of(et21, et22, et23);
		List<EngineTypeJpaEntity> engineTypes3 = List.of(et31, et32, et33);

		engineTypes1 = engineTypeRepository.saveAll(engineTypes1);
		engineTypes2 = engineTypeRepository.saveAll(engineTypes2);
		engineTypes3 = engineTypeRepository.saveAll(engineTypes3);

		return List.of(engineTypes1, engineTypes2, engineTypes3);
	}

	private List<List<ConstructorJpaEntity>> givenConstructors() {
		List<ConstructorJpaEntity> constructors1 = List.of(ConstructorJpaEntity.builder().name("Constructor11").build(),
				ConstructorJpaEntity.builder().name("Constructor12").build(),
				ConstructorJpaEntity.builder().name("Constructor13").build());
		List<ConstructorJpaEntity> constructors2 = List.of(ConstructorJpaEntity.builder().name("Constructor21").build(),
				ConstructorJpaEntity.builder().name("Constructor22").build(),
				ConstructorJpaEntity.builder().name("Constructor23").build());
		List<ConstructorJpaEntity> constructors3 = List.of(ConstructorJpaEntity.builder().name("Constructor31").build(),
				ConstructorJpaEntity.builder().name("Constructor32").build(),
				ConstructorJpaEntity.builder().name("Constructor33").build());

		constructors1 = constructorRepository.saveAll(constructors1);
		constructors2 = constructorRepository.saveAll(constructors2);
		constructors3 = constructorRepository.saveAll(constructors3);

		return List.of(constructors1, constructors2, constructors3);
	}

	private List<List<ModelJpaEntity>> givenModels(List<List<ConstructorJpaEntity>> constructors) {

		List<ModelJpaEntity> models1 = List.of(
				ModelJpaEntity.builder().name("Model11").Constructor(constructors.get(0).get(0)).build(),
				ModelJpaEntity.builder().name("Model12").Constructor(constructors.get(0).get(1)).build(),
				ModelJpaEntity.builder().name("Model13").Constructor(constructors.get(0).get(2)).build());
		List<ModelJpaEntity> models2 = List.of(
				ModelJpaEntity.builder().name("Model21").Constructor(constructors.get(1).get(0)).build(),
				ModelJpaEntity.builder().name("Model22").Constructor(constructors.get(1).get(1)).build(),
				ModelJpaEntity.builder().name("Model23").Constructor(constructors.get(1).get(2)).build());
		List<ModelJpaEntity> models3 = List.of(
				ModelJpaEntity.builder().name("Model31").Constructor(constructors.get(2).get(0)).build(),
				ModelJpaEntity.builder().name("Model32").Constructor(constructors.get(2).get(1)).build(),
				ModelJpaEntity.builder().name("Model33").Constructor(constructors.get(2).get(2)).build());

		models1 = modelRepository.saveAll(models1);
		models2 = modelRepository.saveAll(models2);
		models3 = modelRepository.saveAll(models3);

		return List.of(models1, models2, models3);

	}

	private List<List<VehiculeJpaEntity>> givenVehicules(List<List<EngineTypeJpaEntity>> engineTypes,
			List<List<ModelJpaEntity>> models) {
		List<VehiculeJpaEntity> vehicules1 = List.of(
				VehiculeJpaEntity.builder().type(engineTypes.get(0).get(0)).model(models.get(0).get(0))
						.circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220")
						.photoUrl("https://path/to/vehicule11/photo").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(0).get(1)).model(models.get(0).get(1))
						.circulationDate(LocalDate.of(2020, 01, 02)).registration("2 TUN 220")
						.photoUrl("https://path/to/vehicule12/photo").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(0).get(2)).model(models.get(0).get(2))
						.circulationDate(LocalDate.of(2020, 01, 03)).registration("3 TUN 220")
						.photoUrl("https://path/to/vehicule13/photo").build());

		List<VehiculeJpaEntity> vehicules2 = List.of(
				VehiculeJpaEntity.builder().type(engineTypes.get(1).get(0)).model(models.get(1).get(0))
						.circulationDate(LocalDate.of(2020, 01, 11)).registration("11 TUN 220")
						.photoUrl("https://path/to/vehicule21/photo").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(1).get(1)).model(models.get(1).get(1))
						.circulationDate(LocalDate.of(2020, 01, 12)).registration("12 TUN 220")
						.photoUrl("https://path/to/vehicule22/photo").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(1).get(2)).model(models.get(1).get(2))
						.circulationDate(LocalDate.of(2020, 01, 13)).registration("13 TUN 220")
						.photoUrl("https://path/to/vehicule23/photo").build());

		List<VehiculeJpaEntity> vehicules3 = List.of(
				VehiculeJpaEntity.builder().type(engineTypes.get(2).get(0)).model(models.get(2).get(0))
						.circulationDate(LocalDate.of(2020, 01, 21)).registration("21 TUN 220")
						.photoUrl("https://path/to/vehicule31/photo").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(2).get(1)).model(models.get(2).get(1))
						.circulationDate(LocalDate.of(2020, 01, 22)).registration("22 TUN 220")
						.photoUrl("https://path/to/vehicule32/photo").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(2).get(2)).model(models.get(2).get(2))
						.circulationDate(LocalDate.of(2020, 01, 23)).registration("23 TUN 220")
						.photoUrl("https://path/to/vehicule33/photo").build());

		vehicules1 = vehiculeRepository.saveAll(vehicules1);
		vehicules2 = vehiculeRepository.saveAll(vehicules2);
		vehicules3 = vehiculeRepository.saveAll(vehicules3);

		return List.of(vehicules1, vehicules2, vehicules3);
	}

	private List<ClientJpaEntity> givenClients(InternationalCallingCodeJpaEntity icc) {
		List<ClientJpaEntity> clients = List.of(
				new ClientJpaEntity(null, null, null, "Client1", null, null, "client1@gmail.com", null, null, icc,
						"22111111", null, null, null, null, "https://path/to/client1/photo",
						new HashSet<JourneyRequestJpaEntity>()),
				new ClientJpaEntity(null, null, null, "Client2", null, null, "client2@gmail.com", null, null, icc,
						"22222222", null, null, null, null, "https://path/to/client2/photo",
						new HashSet<JourneyRequestJpaEntity>()),
				new ClientJpaEntity(null, null, null, "Client3", null, null, "client3@gmail.com", null, null, icc,
						"22333333", null, null, null, null, "https://path/to/client3/photo",
						new HashSet<JourneyRequestJpaEntity>()));

		return clientRepository.saveAll(clients);
	}

	private List<TransporterJpaEntity> givenTransporters(List<List<VehiculeJpaEntity>> vehicules) {
		List<TransporterJpaEntity> transporters = List.of(
				new TransporterJpaEntity(null, null, null, "Transporter1", null, null, null, null, null, null, null,
						null, null, null, null, "https://path/to/transporter1/photo", null,
						Set.copyOf(vehicules.get(0)), null, null, new HashSet<JourneyProposalJpaEntity>()),
				new TransporterJpaEntity(null, null, null, "Transporter2", null, null, null, null, null, null, null,
						null, null, null, null, "https://path/to/transporter2/photo", null,
						Set.copyOf(vehicules.get(1)), null, null, new HashSet<JourneyProposalJpaEntity>()),
				new TransporterJpaEntity(null, null, null, "Transporter3", null, null, null, null, null, null, null,
						null, null, null, null, "https://path/to/transporter3/photo", null,
						Set.copyOf(vehicules.get(2)), null, null, new HashSet<JourneyProposalJpaEntity>()));
		return transporterRepository.saveAll(transporters);
	}

	private Map<Integer, Set<JourneyProposalJpaEntity>> givenProposals(List<TransporterJpaEntity> transporters,
			List<List<VehiculeJpaEntity>> vehicules) {
		Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = Map.of(0,
				Set.of(JourneyProposalJpaEntity.builder().price(150.0).transporter(transporters.get(0))
						.vehicule(vehicules.get(0).get(0)).build(),
						JourneyProposalJpaEntity.builder().price(160.0).transporter(transporters.get(1))
								.vehicule(vehicules.get(0).get(1)).build(),
						JourneyProposalJpaEntity.builder().price(140.0).transporter(transporters.get(2))
								.vehicule(vehicules.get(0).get(2)).build()),
				1,
				Set.of(JourneyProposalJpaEntity.builder().price(240.0).transporter(transporters.get(0))
						.vehicule(vehicules.get(1).get(0)).build(),
						JourneyProposalJpaEntity.builder().price(260.0).transporter(transporters.get(1))
								.vehicule(vehicules.get(1).get(1)).build(),
						JourneyProposalJpaEntity.builder().price(250.0).transporter(transporters.get(2))
								.vehicule(vehicules.get(1).get(2)).build()),
				2,
				Set.of(JourneyProposalJpaEntity.builder().price(340.0).transporter(transporters.get(0))
						.vehicule(vehicules.get(2).get(0)).build(),
						JourneyProposalJpaEntity.builder().price(350.0).transporter(transporters.get(1))
								.vehicule(vehicules.get(2).get(1)).build(),
						JourneyProposalJpaEntity.builder().price(360.0).transporter(transporters.get(2))
								.vehicule(vehicules.get(2).get(2)).build()));

		proposalsMap.forEach((k, v) -> {
			Iterator<JourneyProposalJpaEntity> it = v.iterator();
			JourneyProposalJpaEntity jp = it.next();
			TransporterJpaEntity t = jp.getTransporter();
			t.addProposal(jp);
		});

		return proposalsMap;
	}

	private void givenJourneyRequests(List<PlaceJpaEntity> departurePlaces, List<PlaceJpaEntity> arrivalPlaces,
			List<EngineTypeJpaEntity> engineTypes, List<Double> distances, List<LocalDateTime> dates,
			List<LocalDateTime> endDates, List<Integer> workers, List<String> descriptions,
			List<ClientJpaEntity> clients, Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap) {

		JourneyRequestJpaEntity jrToday = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(0))
				.arrivalPlace(arrivalPlaces.get(0)).engineType(engineTypes.get(0)).distance(distances.get(0))
				.dateTime(dates.get(0)).endDateTime(endDates.get(0)).workers(workers.get(0))
				.description(descriptions.get(0)).client(clients.get(0)).proposals(proposalsMap.get(0))
				.creationDateTime(endDates.get(0).minusDays(1)).build();
		proposalsMap.get(0).forEach(p -> p.setJourneyRequest(jrToday));
		clients.get(0).addJourneyRequest(jrToday);

		JourneyRequestJpaEntity jrTomorrow = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(1))
				.arrivalPlace(arrivalPlaces.get(1)).engineType(engineTypes.get(1)).distance(distances.get(1))
				.dateTime(dates.get(1)).endDateTime(endDates.get(1)).workers(workers.get(1))
				.description(descriptions.get(1)).client(clients.get(1)).proposals(proposalsMap.get(1))
				.creationDateTime(endDates.get(1).minusDays(1)).build();
		proposalsMap.get(1).forEach(p -> p.setJourneyRequest(jrTomorrow));
		clients.get(1).addJourneyRequest(jrTomorrow);

		JourneyRequestJpaEntity jrOvermorrow = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(2))
				.arrivalPlace(arrivalPlaces.get(2)).engineType(engineTypes.get(2)).distance(distances.get(2))
				.dateTime(dates.get(2)).endDateTime(endDates.get(2)).workers(workers.get(2))
				.description(descriptions.get(2)).client(clients.get(2)).proposals(proposalsMap.get(2))
				.creationDateTime(endDates.get(2).minusDays(1)).build();
		proposalsMap.get(2).forEach(p -> p.setJourneyRequest(jrOvermorrow));
		clients.get(2).addJourneyRequest(jrOvermorrow);

		journeyRequestRepository.saveAll(List.of(jrToday, jrTomorrow, jrOvermorrow));

	}

	private InternationalCallingCodeJpaEntity givenIcc(String code) {
		InternationalCallingCodeJpaEntity icc = InternationalCallingCodeJpaEntity.builder().value(code)
				.countryName("Some country").flagPath("https://path/to/some/country/flag").enabled(true).build();
		return internationalCallingCodeRepository.save(icc);
	}

}
