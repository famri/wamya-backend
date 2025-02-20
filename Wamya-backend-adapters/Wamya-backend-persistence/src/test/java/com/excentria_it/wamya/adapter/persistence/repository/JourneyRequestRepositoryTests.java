package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.domain.*;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles(profiles = {"persistence-local"})
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
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ConstructorRepository constructorRepository;

    @Autowired
    private InternationalCallingCodeRepository internationalCallingCodeRepository;

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private JourneyRequestStatusRepository journeyRequestStatusRepository;

    @BeforeEach
    public void cleanDatabase() {

        transporterRepository.deleteAll();

        clientRepository.deleteAll();
        journeyRequestRepository.deleteAll();
        engineTypeRepository.deleteAll();
        placeRepository.deleteAll();
        vehicleRepository.deleteAll();
        modelRepository.deleteAll();
        constructorRepository.deleteAll();
        internationalCallingCodeRepository.deleteAll();
        departmentRepository.deleteAll();
        countryRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @Test
    public void testFindByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdInAndEngineType_IdInAndDateBetween_OrderByMinPriceDesc() {

        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));
        InternationalCallingCodeJpaEntity icc = givenIcc("+33");

        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();
        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();
        List<Integer> distances = givenDistances();
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();

        givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
                descriptions, clients, proposalsMap, jrStatuses);

        List<ZonedDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(endDates.get(0));

        // When

        Page<JourneyRequestSearchOutput> journeyRequests = journeyRequestRepository
                .findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdsInAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                        departurePlaces.get(0).getDepartment().getId(),
                        Set.of(arrivalPlaces.get(0).getDepartment().getId(),
                                arrivalPlaces.get(1).getDepartment().getId()),
                        Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()),
                        searchStartAndEndDates.get(0).toInstant(), searchStartAndEndDates.get(1).toInstant(),
                        Set.of(JourneyRequestStatusCode.OPENED), TestConstants.DEFAULT_EMAIL, "en_US",
                        PageRequest.of(0, 1000, Sort.by(List.of(new Order(Direction.DESC, "id")))));

        // Then
        assertNotNull(journeyRequests);

        assertEquals(2, journeyRequests.getNumberOfElements());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(0).getDeparturePlace().getDepartmentId());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(1).getDeparturePlace().getDepartmentId());

        assertTrue(Set.of(arrivalPlaces.get(0).getDepartment().getId(), arrivalPlaces.get(1).getDepartment().getId())
                .containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getDepartmentId())
                        .collect(Collectors.toSet())));

        assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
                .getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

    }

    @Test
    public void testFindByDeparturePlace_DepartmentIdAndEngineType_IdInAndDateBetween_OrderByMinPriceDesc() {

        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();
        List<Integer> distances = givenDistances();
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();

        givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
                descriptions, clients, proposalsMap, jrStatuses);

        List<ZonedDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(endDates.get(0));

        // When

        Page<JourneyRequestSearchOutput> journeyRequests = journeyRequestRepository
                .findByDeparturePlace_DepartmentIdAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                        departurePlaces.get(0).getDepartment().getId(),
                        Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()),
                        searchStartAndEndDates.get(0).toInstant(), searchStartAndEndDates.get(1).toInstant(),
                        Set.of(JourneyRequestStatusCode.OPENED), TestConstants.DEFAULT_EMAIL, "en_US",
                        PageRequest.of(0, 1000, Sort.by(List.of(new Order(Direction.DESC, "dateTime")))));

        // Then
        assertNotNull(journeyRequests);

        assertEquals(2, journeyRequests.getNumberOfElements());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(0).getDeparturePlace().getDepartmentId());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(1).getDeparturePlace().getDepartmentId());

        assertTrue(Set.of(arrivalPlaces.get(0).getDepartment().getId(), arrivalPlaces.get(1).getDepartment().getId())
                .containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getDepartmentId())
                        .collect(Collectors.toSet())));

        assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
                .getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

    }

    @Test
    public void testFindByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdInAndEngineType_IdInAndDateBetween_OrderByMinPriceAsc_WithPaging() {

        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");

        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();
        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();
        List<Integer> distances = givenDistances();
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();

        List<JourneyRequestJpaEntity> createdJourneyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces,
                engineTypes, distances, dates, endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap,
                jrStatuses);

        List<ZonedDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(endDates.get(0));

        // When

        Page<JourneyRequestSearchOutput> journeyRequests = journeyRequestRepository
                .findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdsInAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                        departurePlaces.get(0).getDepartment().getId(),
                        Set.of(arrivalPlaces.get(0).getDepartment().getId(),
                                arrivalPlaces.get(1).getDepartment().getId()),
                        Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()),
                        searchStartAndEndDates.get(0).toInstant(), searchStartAndEndDates.get(1).toInstant(),
                        Set.of(JourneyRequestStatusCode.OPENED), TestConstants.DEFAULT_EMAIL, "en_US",
                        PageRequest.of(1, 1,
                                Sort.by(List.of(new Order(Direction.DESC, "dateTime")))
                        ));

        // Then
        assertNotNull(journeyRequests);

        assertEquals(1, journeyRequests.getNumberOfElements());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(0).getDeparturePlace().getDepartmentId());

        assertTrue(Set.of(arrivalPlaces.get(0).getDepartment().getId(), arrivalPlaces.get(1).getDepartment().getId())
                .containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getDepartmentId())
                        .collect(Collectors.toSet())));

        assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
                .getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

    }

    @Test
    public void testFindByDeparturePlace_DepartmentIdAndEngineType_IdInAndDateBetween_OrderByMinPriceAsc_WithPaging() {

        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();
        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();
        List<Integer> distances = givenDistances();
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();

        givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
                descriptions, clients, proposalsMap, jrStatuses);

        List<ZonedDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(endDates.get(0));

        // When

        Page<JourneyRequestSearchOutput> journeyRequests = journeyRequestRepository
                .findByDeparturePlace_DepartmentIdAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                        departurePlaces.get(0).getDepartment().getId(),
                        Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()),
                        searchStartAndEndDates.get(0).toInstant(), searchStartAndEndDates.get(1).toInstant(),
                        Set.of(JourneyRequestStatusCode.OPENED), TestConstants.DEFAULT_EMAIL, "en_US",
                        PageRequest.of(1, 1, Sort.by(List.of(new Order(Direction.DESC, "dateTime")))));

        // Then
        assertNotNull(journeyRequests);

        assertEquals(1, journeyRequests.getNumberOfElements());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(0).getDeparturePlace().getDepartmentId());

        assertTrue(Set.of(arrivalPlaces.get(0).getDepartment().getId(), arrivalPlaces.get(1).getDepartment().getId())
                .containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getDepartmentId())
                        .collect(Collectors.toSet())));

        assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
                .getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

    }

    @Test
    public void testFindByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdInAndEngineType_IdInAndDateBetween_OrderByDateTimeDesc() {

        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");

        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));

        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();
        List<Integer> distances = givenDistances();
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();

        givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
                descriptions, clients, proposalsMap, jrStatuses);

        List<ZonedDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(endDates.get(0));

        // When

        Page<JourneyRequestSearchOutput> journeyRequests = journeyRequestRepository
                .findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdsInAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                        departurePlaces.get(0).getDepartment().getId(),
                        Set.of(arrivalPlaces.get(0).getDepartment().getId(),
                                arrivalPlaces.get(1).getDepartment().getId()),
                        Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()),
                        searchStartAndEndDates.get(0).toInstant(), searchStartAndEndDates.get(1).toInstant(),
                        Set.of(JourneyRequestStatusCode.OPENED), TestConstants.DEFAULT_EMAIL, "en_US",
                        PageRequest.of(0, 1000,

                                Sort.by(Direction.DESC, "dateTime")));

        // Then
        assertNotNull(journeyRequests);

        assertEquals(2, journeyRequests.getNumberOfElements());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(0).getDeparturePlace().getDepartmentId());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(1).getDeparturePlace().getDepartmentId());

        assertTrue(Set.of(arrivalPlaces.get(0).getDepartment().getId(), arrivalPlaces.get(1).getDepartment().getId())
                .containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getDepartmentId())
                        .collect(Collectors.toSet())));

        assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
                .getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .isAfter(journeyRequests.getContent().get(1).getDateTime()));

    }

    @Test
    public void testFindByDeparturePlace_DepartmentIdAndEngineType_IdInAndDateBetween_OrderByDateTimeDesc() {

        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();
        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<Integer> distances = givenDistances();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();

        givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, distances, dates, endDates, List.of(2, 2, 2),
                descriptions, clients, proposalsMap, jrStatuses);

        List<ZonedDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(endDates.get(0));

        // When

        Page<JourneyRequestSearchOutput> journeyRequests = journeyRequestRepository
                .findByDeparturePlace_DepartmentIdAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                        departurePlaces.get(0).getDepartment().getId(),
                        Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()),
                        searchStartAndEndDates.get(0).toInstant(), searchStartAndEndDates.get(1).toInstant(),
                        Set.of(JourneyRequestStatusCode.OPENED), TestConstants.DEFAULT_EMAIL, "en_US",
                        PageRequest.of(0, 1000,

                                Sort.by(Direction.DESC, "dateTime")));

        // Then
        assertNotNull(journeyRequests);

        assertEquals(2, journeyRequests.getNumberOfElements());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(0).getDeparturePlace().getDepartmentId());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(1).getDeparturePlace().getDepartmentId());

        assertTrue(Set.of(arrivalPlaces.get(0).getDepartment().getId(), arrivalPlaces.get(1).getDepartment().getId())
                .containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getDepartmentId())
                        .collect(Collectors.toSet())));

        assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
                .getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .isAfter(journeyRequests.getContent().get(1).getDateTime()));

    }

    @Test
    public void testFindByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdInAndEngineType_IdInAndDateBetween_OrderByDistanceDesc() {

        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();

        givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);

        List<ZonedDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(endDates.get(0));

        // When

        Page<JourneyRequestSearchOutput> journeyRequests = journeyRequestRepository
                .findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdsInAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                        departurePlaces.get(0).getDepartment().getId(),
                        Set.of(arrivalPlaces.get(0).getDepartment().getId(),
                                arrivalPlaces.get(1).getDepartment().getId()),
                        Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()),
                        searchStartAndEndDates.get(0).toInstant(), searchStartAndEndDates.get(1).toInstant(),
                        Set.of(JourneyRequestStatusCode.OPENED), "toto@tata.titi", "en_US",
                        PageRequest.of(0, 1000,

                                Sort.by(Direction.DESC, "distance")));

        List<JourneyRequestJpaEntity> jrs = journeyRequestRepository.findAll();
        // Then
        assertNotNull(journeyRequests);

        assertEquals(2, journeyRequests.getNumberOfElements());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(0).getDeparturePlace().getDepartmentId());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(1).getDeparturePlace().getDepartmentId());

        assertTrue(Set.of(arrivalPlaces.get(0).getDepartment().getId(), arrivalPlaces.get(1).getDepartment().getId())
                .containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getDepartmentId())
                        .collect(Collectors.toSet())));

        assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
                .getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(
                journeyRequests.getContent().get(0).getDistance() >= journeyRequests.getContent().get(1).getDistance());

    }

    private DocumentJpaEntity givenDefaultManProfileImage() {
        return documentRepository.save(DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity());

    }

    @Test
    public void testFindByDeparturePlace_DepartmentIdAndEngineType_IdInAndDateBetween_OrderByDistanceDesc() {

        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");

        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();

        givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);

        List<ZonedDateTime> searchStartAndEndDates = getDateBeforeAndDateAfter(endDates.get(0));

        // When

        Page<JourneyRequestSearchOutput> journeyRequests = journeyRequestRepository
                .findByDeparturePlace_DepartmentIdAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                        departurePlaces.get(0).getDepartment().getId(),
                        Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()),
                        searchStartAndEndDates.get(0).toInstant(), searchStartAndEndDates.get(1).toInstant(),
                        Set.of(JourneyRequestStatusCode.OPENED), TestConstants.DEFAULT_EMAIL, "en_US",
                        PageRequest.of(0, 1000,

                                Sort.by(Direction.DESC, "distance")));

        // Then
        assertNotNull(journeyRequests);

        assertEquals(2, journeyRequests.getNumberOfElements());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(0).getDeparturePlace().getDepartmentId());

        assertEquals(departurePlaces.get(0).getDepartment().getId(),
                journeyRequests.getContent().get(1).getDeparturePlace().getDepartmentId());

        assertTrue(Set.of(arrivalPlaces.get(0).getDepartment().getId(), arrivalPlaces.get(1).getDepartment().getId())
                .containsAll(journeyRequests.getContent().stream().map(jr -> jr.getArrivalPlace().getDepartmentId())
                        .collect(Collectors.toSet())));

        assertTrue(Set.of(engineTypes.get(0).getId(), engineTypes.get(1).getId()).containsAll(journeyRequests
                .getContent().stream().map(jr -> jr.getEngineType().getId()).collect(Collectors.toSet())));

        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(0).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(0).toInstant()) >= 0);
        assertTrue(journeyRequests.getContent().get(1).getDateTime()
                .compareTo(searchStartAndEndDates.get(1).toInstant()) <= 0);

        assertTrue(
                journeyRequests.getContent().get(0).getDistance() >= journeyRequests.getContent().get(1).getDistance());

    }

    @Test
    void testFindByCreationDateTimeBetweenAndClient_Email() {
        // Given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);

        // When

        ZonedDateTime higherEdge = dates.get(2);
        ZonedDateTime lowerEdge = dates.get(0);

        Page<ClientJourneyRequestDtoOutput> journeyRequests = journeyRequestRepository
                .findByCreationDateTimeBetweenAndClient_Email(lowerEdge.toInstant(), higherEdge.toInstant(),
                        clients.get(0).getEmail(), "en_US", PageRequest.of(0, 25,

                                Sort.by(Direction.DESC, "creationDateTime")));

        // Then
        assertNotNull(journeyRequests);
        assertEquals(1, journeyRequests.getNumberOfElements());
        assertEquals(3, journeyRequests.getContent().get(0).getProposalsCount());

        JourneyRequestJpaEntity clientJourneyRequest = journeyRequestRepository
                .findById(journeyRequests.getContent().get(0).getId()).get();

        assertEquals(clients.get(0).getEmail(), clientJourneyRequest.getClient().getEmail());
        assertTrue(journeyRequests.getContent().get(0).getCreationDateTime().compareTo(lowerEdge.toInstant()) >= 0);

        assertTrue(journeyRequests.getContent().get(0).getCreationDateTime().compareTo(higherEdge.toInstant()) <= 0);

    }

    @Test
    void givenNullClientSubject_WhenExistsAndNotExpiredByIdAndClientSubject_thenReturnFalse() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);

        // when
        boolean result = journeyRequestRepository.existsAndNotExpiredByIdAndClientSubject(journeyRequests.get(1).getId(), null);
        // then
        assertFalse(result);
    }

    @Test
    void givenNullJourneyRequestId_WhenExistsAndNotExpiredByIdAndClientSubject_thenReturnFalse() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);

        // when
        boolean result = journeyRequestRepository.existsAndNotExpiredByIdAndClientSubject(null, clients.get(1).getEmail());
        // then
        assertFalse(result);
    }

    @Test
    void testExistsAndNotExpiredByIdAndClientEmailSubject() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);

        // when
        boolean result = journeyRequestRepository.existsAndNotExpiredByIdAndClientSubject(journeyRequests.get(1).getId(), clients.get(1).getEmail());
        // then
        assertTrue(result);
    }


    @Test
    void testExistsAndNotExpiredByIdAndClientIccAndMobileNumberSubject() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);

        // when
        boolean result = journeyRequestRepository.existsAndNotExpiredByIdAndClientSubject(journeyRequests.get(1).getId(), clients.get(1).getIcc().getValue() + "_" + clients.get(1).getMobileNumber());
        // then
        assertTrue(result);
    }

    @Test
    void testExistsAndNotExpiredByIdAndClientOauthIdSubject() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);

        // when
        boolean result = journeyRequestRepository.existsAndNotExpiredByIdAndClientSubject(journeyRequests.get(1).getId(), clients.get(1).getOauthId());
        // then
        assertTrue(result);
    }

    @Test
    void givenNullClientSubject_WhenFindByIdAndClientSubject_ThenReturnEmpty() {
        // given
        // when
        Optional<ClientJourneyRequestDtoOutput> journeyRequestOptional = journeyRequestRepository.findByIdAndClientSubject(1L, null, "fr_FR");
        // then
        assertTrue(journeyRequestOptional.isEmpty());
    }

    @Test
    void givenEmailClientSubject_WhenFindByIdAndClientSubject_ThenReturnClientJourneyRequestDtoOutput() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);
        // when
        Optional<ClientJourneyRequestDtoOutput> journeyRequestOptional = journeyRequestRepository.findByIdAndClientSubject(journeyRequests.get(0).getId(), journeyRequests.get(0).getClient().getEmail(), "fr_FR");
        // then
        assertEquals(journeyRequestOptional.get().getId(), journeyRequests.get(0).getId());

    }

    @Test
    void givenMobileNumberClientSubject_WhenFindByIdAndClientSubject_ThenReturnClientJourneyRequestDtoOutput() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);
        // when
        Optional<ClientJourneyRequestDtoOutput> journeyRequestOptional = journeyRequestRepository.findByIdAndClientSubject(journeyRequests.get(0).getId(), journeyRequests.get(0).getClient().getEmail(), "fr_FR");
        // then
        assertEquals(journeyRequestOptional.get().getId(), journeyRequests.get(0).getId());

    }

    @Test
    void givenFrenchMobileNumberClientSubjectWithoutLeadingZero_WhenFindByIdAndClientSubject_ThenReturnClientJourneyRequestDtoOutput() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClientsWithoutLeadingZeroMobileNumber(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);
        // when
        Optional<ClientJourneyRequestDtoOutput> journeyRequestOptional = journeyRequestRepository.findByIdAndClientSubject(journeyRequests.get(0).getId(), journeyRequests.get(0).getClient().getIcc().getValue() + "_0" + journeyRequests.get(0).getClient().getMobileNumber(), "fr_FR");
        // then
        assertEquals(journeyRequestOptional.get().getId(), journeyRequests.get(0).getId());

    }

    @Test
    void givenOauthIdClientSubject_WhenFindByIdAndClientSubject_ThenReturnClientJourneyRequestDtoOutput() {
        // given
        List<DepartmentJpaEntity> departureDepartments = givenDepartureDepartments();
        List<DepartmentJpaEntity> arrivalDepartments = givenArrivalDepartments();
        List<PlaceJpaEntity> departurePlaces = givenDeparturePlaces(departureDepartments);
        List<PlaceJpaEntity> arrivalPlaces = givenArrivalPlaces(arrivalDepartments);

        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypesListList = givenEngineTypes(images);
        List<EngineTypeJpaEntity> engineTypes = List.of(engineTypesListList.get(0).get(0),
                engineTypesListList.get(1).get(0), engineTypesListList.get(2).get(0));

        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        DocumentJpaEntity defaultManProfileImage = givenDefaultManProfileImage();

        List<ClientJpaEntity> clients = givenClients(icc,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<ZonedDateTime> dates = givenLocalDateTimes();
        List<ZonedDateTime> endDates = givenEndDates();

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);

        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypesListList, models);
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles,
                List.of(defaultManProfileImage, defaultManProfileImage, defaultManProfileImage));
        List<String> descriptions = givenDescriptions();

        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = givenProposals(transporters, vehicles);

        List<JourneyRequestStatusJpaEntity> jrStatuses = givenStatuses();
        List<JourneyRequestJpaEntity> journeyRequests = givenJourneyRequests(departurePlaces, arrivalPlaces, engineTypes, List.of(112700, 205500, 308200), dates,
                endDates, List.of(2, 2, 2), descriptions, clients, proposalsMap, jrStatuses);
        // when
        Optional<ClientJourneyRequestDtoOutput> journeyRequestOptional = journeyRequestRepository.findByIdAndClientSubject(journeyRequests.get(0).getId(), journeyRequests.get(0).getClient().getOauthId(), "fr_FR");
        // then
        assertEquals(journeyRequestOptional.get().getId(), journeyRequests.get(0).getId());

    }

    private List<JourneyRequestStatusJpaEntity> givenStatuses() {
        JourneyRequestStatusJpaEntity jrs1 = JourneyRequestStatusJpaEntity.builder()
                .code(JourneyRequestStatusCode.OPENED).description("Opened journey request status").build();
        jrs1 = journeyRequestStatusRepository.save(jrs1);
        return List.of(jrs1, jrs1, jrs1);
    }

    private List<Integer> givenDistances() {

        return List.of(112700, 205500, 308200);
    }

    private List<ZonedDateTime> getDateBeforeAndDateAfter(ZonedDateTime date) {

        ZonedDateTime zonedDateTime = date.toLocalDate().atStartOfDay(ZoneOffset.UTC);

        ZonedDateTime dateBefore = zonedDateTime.minusDays(1);

        ZonedDateTime dateAfter = zonedDateTime.plusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59)
                .plusNanos(999999999);

        return List.of(dateBefore, dateAfter);
    }

    private List<String> givenDescriptions() {
        return List.of("Need a transporter URGENT Today!!!", "Need a transporter URGENT Tomorrow!!!",
                "Need a transporter URGENT Overmorrow!!!");

    }

    private List<ZonedDateTime> givenLocalDateTimes() {

        ZonedDateTime zdt = ZonedDateTime.of(2020, 12, 10, 12, 0, 0, 0, ZoneOffset.UTC);

        ZonedDateTime tomorrow12PM = zdt.plusDays(1);

        ZonedDateTime overmorrow12PM = tomorrow12PM.plusDays(1);

        return List.of(zdt, tomorrow12PM, overmorrow12PM);

    }

    private List<ZonedDateTime> givenEndDates() {

        ZonedDateTime zdt = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime tomorrow11PM59 = zdt.plusDays(1);

        ZonedDateTime overmorrowZoned11PM59 = tomorrow11PM59.plusDays(1);

        return List.of(zdt, tomorrow11PM59, overmorrowZoned11PM59);
    }

    private List<PlaceJpaEntity> givenDeparturePlaces(List<DepartmentJpaEntity> departments) {

        PlaceJpaEntity p1 = new PlaceJpaEntity(departments.get(0), new HashMap<String, LocalizedPlaceJpaEntity>(),
                departments.get(0).getLatitude(), departments.get(0).getLongitude());
        p1.setPlaceId(new PlaceId(1L, PlaceType.DEPARTMENT));

        LocalizedPlaceJpaEntity p1FR = new LocalizedPlaceJpaEntity();
        p1FR.setLocalizedPlaceId(new LocalizedPlaceId("fr_FR"));
        p1FR.setName("Sfax");

        LocalizedPlaceJpaEntity p1EN = new LocalizedPlaceJpaEntity();
        p1EN.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(1L, PlaceType.DEPARTMENT), "en_US"));
        p1EN.setName("Sfax");

        p1FR.setPlace(p1);
        p1EN.setPlace(p1);

        p1.getLocalizations().put("fr_FR", p1FR);
        p1.getLocalizations().put("en_US", p1EN);

        PlaceJpaEntity p3 = new PlaceJpaEntity(departments.get(2), new HashMap<String, LocalizedPlaceJpaEntity>(),
                departments.get(2).getLatitude(), departments.get(2).getLongitude());
        p3.setPlaceId(new PlaceId(3L, PlaceType.DEPARTMENT));

        LocalizedPlaceJpaEntity p3FR = new LocalizedPlaceJpaEntity();
        p3FR.setLocalizedPlaceId(new LocalizedPlaceId("fr_FR"));
        p3FR.setName("Tunis");

        LocalizedPlaceJpaEntity p3EN = new LocalizedPlaceJpaEntity();
        p3EN.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(3L, PlaceType.DEPARTMENT), "en_US"));
        p3EN.setName("Tunis");

        p3FR.setPlace(p3);
        p3EN.setPlace(p3);

        p3.getLocalizations().put("fr_FR", p3FR);
        p3.getLocalizations().put("en_US", p3EN);

        return placeRepository.saveAll(List.of(p1, p1, p3));
    }

    private List<PlaceJpaEntity> givenArrivalPlaces(List<DepartmentJpaEntity> departments) {

        PlaceJpaEntity p1 = new PlaceJpaEntity(departments.get(0), new HashMap<String, LocalizedPlaceJpaEntity>(),
                departments.get(0).getLatitude(), departments.get(0).getLongitude());
        p1.setPlaceId(new PlaceId(4L, PlaceType.DEPARTMENT));

        LocalizedPlaceJpaEntity p1FR = new LocalizedPlaceJpaEntity();
        p1FR.setLocalizedPlaceId(new LocalizedPlaceId("fr_FR"));
        p1FR.setName("Sousse");

        LocalizedPlaceJpaEntity p1EN = new LocalizedPlaceJpaEntity();
        p1EN.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(4L, PlaceType.DEPARTMENT), "en_US"));
        p1EN.setName("Sousse");

        p1FR.setPlace(p1);
        p1EN.setPlace(p1);

        p1.getLocalizations().put("fr_FR", p1FR);
        p1.getLocalizations().put("en_US", p1EN);

        PlaceJpaEntity p2 = new PlaceJpaEntity(departments.get(1), new HashMap<String, LocalizedPlaceJpaEntity>(),
                departments.get(1).getLatitude(), departments.get(1).getLongitude());
        p2.setPlaceId(new PlaceId(5L, PlaceType.DEPARTMENT));

        LocalizedPlaceJpaEntity p2FR = new LocalizedPlaceJpaEntity();
        p2FR.setLocalizedPlaceId(new LocalizedPlaceId("fr_FR"));
        p2FR.setName("Bizerte");

        LocalizedPlaceJpaEntity p2EN = new LocalizedPlaceJpaEntity();
        p2EN.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(5L, PlaceType.DEPARTMENT), "en_US"));
        p2EN.setName("Bizerte");

        p2FR.setPlace(p2);
        p2EN.setPlace(p2);

        p2.getLocalizations().put("fr_FR", p2FR);
        p2.getLocalizations().put("en_US", p2EN);

        PlaceJpaEntity p3 = new PlaceJpaEntity(departments.get(2), new HashMap<String, LocalizedPlaceJpaEntity>(),
                departments.get(2).getLatitude(), departments.get(2).getLongitude());
        p3.setPlaceId(new PlaceId(6L, PlaceType.DEPARTMENT));

        LocalizedPlaceJpaEntity p3FR = new LocalizedPlaceJpaEntity();
        p3FR.setLocalizedPlaceId(new LocalizedPlaceId("fr_FR"));
        p3FR.setName("B�ja");

        LocalizedPlaceJpaEntity p3EN = new LocalizedPlaceJpaEntity();
        p3EN.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(6L, PlaceType.DEPARTMENT), "en_US"));
        p3EN.setName("B�ja");

        p3FR.setPlace(p3);
        p3EN.setPlace(p3);

        p3.getLocalizations().put("fr_FR", p3FR);
        p3.getLocalizations().put("en_US", p3EN);

        return placeRepository.saveAll(List.of(p1, p2, p3));
    }

    private List<List<DocumentJpaEntity>> givenImages() {

        DocumentJpaEntity i11 = DocumentJpaTestData.defaultVanL1H1VehicleImageDocumentJpaEntity();
        i11.setId(null);
        DocumentJpaEntity i12 = DocumentJpaTestData.defaultVanL2H2VehicleImageDocumentJpaEntity();
        i12.setId(null);
        DocumentJpaEntity i13 = DocumentJpaTestData.defaultVanL3H3VehicleImageDocumentJpaEntity();
        i13.setId(null);

        DocumentJpaEntity i21 = DocumentJpaTestData.defaultFlatbedTruckVehicleImageDocumentJpaEntity();
        i21.setId(null);
        DocumentJpaEntity i22 = DocumentJpaTestData.defaultUtilityVehicleImageDocumentJpaEntity();
        i22.setId(null);
        DocumentJpaEntity i23 = DocumentJpaTestData.defaultDumpTruckVehicleImageDocumentJpaEntity();
        i23.setId(null);

        DocumentJpaEntity i31 = DocumentJpaTestData.defaultBoxTruckVehicleImageDocumentJpaEntity();
        i31.setId(null);
        DocumentJpaEntity i32 = DocumentJpaTestData.defaultTankerVehicleImageDocumentJpaEntity();
        i32.setId(null);
        DocumentJpaEntity i33 = DocumentJpaTestData.defaultBusVehicleImageDocumentJpaEntity();
        i33.setId(null);

        List<DocumentJpaEntity> images1 = documentRepository.saveAll(List.of(i11, i12, i13));
        List<DocumentJpaEntity> images2 = documentRepository.saveAll(List.of(i21, i22, i23));
        List<DocumentJpaEntity> images3 = documentRepository.saveAll(List.of(i31, i32, i33));

        return List.of(images1, images2, images3);

    }

    private List<List<EngineTypeJpaEntity>> givenEngineTypes(List<List<DocumentJpaEntity>> images) {
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
        let11fr.setName("TypeVehicle11");
        let11fr.setDescription("DescriptionTypeVehicle11");
        et11.getLocalizations().put("fr_FR", let11fr);

        et11.setImage(images.get(0).get(0));

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
        let12fr.setName("TypeVehicle12");
        let12fr.setDescription("DescriptionTypeVehicle12");
        et12.getLocalizations().put("fr_FR", let12fr);

        et12.setImage(images.get(0).get(1));

        // Engine type 13
        EngineTypeJpaEntity et13 = new EngineTypeJpaEntity();
        et13.setCode(EngineTypeCode.VAN_L3H3);

        LocalizedEngineTypeJpaEntity let13en = new LocalizedEngineTypeJpaEntity();
        let13en.setLocalizedId(new LocalizedId("en_US"));
        let13en.setEngineType(et13);
        let13en.setName("EngineType13");
        let13en.setDescription("EngineTypeDescription13");
        et13.getLocalizations().put("en_US", let13en);

        LocalizedEngineTypeJpaEntity let13fr = new LocalizedEngineTypeJpaEntity();
        let13fr.setLocalizedId(new LocalizedId("fr_FR"));
        let13fr.setEngineType(et13);
        let13fr.setName("TypeVehicle13");
        let13fr.setDescription("DescriptionTypeVehicle3");
        et13.getLocalizations().put("fr_FR", let13fr);

        et13.setImage(images.get(0).get(2));

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
        let21fr.setName("TypeVehicle21");
        let21fr.setDescription("DescriptionTypeVehicle21");
        et21.getLocalizations().put("fr_FR", let21fr);

        et21.setImage(images.get(1).get(0));

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
        let22fr.setName("TypeVehicle22");
        let22fr.setDescription("DescriptionTypeVehicle22");
        et22.getLocalizations().put("fr_FR", let22fr);

        et22.setImage(images.get(1).get(1));

        // Engine type 23
        EngineTypeJpaEntity et23 = new EngineTypeJpaEntity();
        et23.setCode(EngineTypeCode.DUMP_TRUCK);

        LocalizedEngineTypeJpaEntity let23en = new LocalizedEngineTypeJpaEntity();
        let23en.setLocalizedId(new LocalizedId("en_US"));
        let23en.setEngineType(et23);
        let23en.setName("EngineType23");
        let23en.setDescription("EngineTypeDescription23");
        et23.getLocalizations().put("en_US", let23en);

        LocalizedEngineTypeJpaEntity let23fr = new LocalizedEngineTypeJpaEntity();
        let23fr.setLocalizedId(new LocalizedId("fr_FR"));
        let23fr.setEngineType(et23);
        let23fr.setName("TypeVehicle23");
        let23fr.setDescription("DescriptionTypeVehicle23");
        et23.getLocalizations().put("fr_FR", let23fr);

        et23.setImage(images.get(1).get(2));

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
        let31fr.setName("TypeVehicle31");
        let31fr.setDescription("DescriptionTypeVehicle31");
        et31.getLocalizations().put("fr_FR", let31fr);

        et31.setImage(images.get(2).get(0));

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
        let32fr.setName("TypeVehicle32");
        let32fr.setDescription("DescriptionTypeVehicle32");
        et32.getLocalizations().put("fr_FR", let32fr);

        et32.setImage(images.get(2).get(1));

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
        let33fr.setName("TypeVehicle33");
        let33fr.setDescription("DescriptionTypeVehicle3");
        et33.getLocalizations().put("fr_FR", let33fr);

        et33.setImage(images.get(2).get(2));

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
                ModelJpaEntity.builder().name("Model11").constructor(constructors.get(0).get(0)).build(),
                ModelJpaEntity.builder().name("Model12").constructor(constructors.get(0).get(1)).build(),
                ModelJpaEntity.builder().name("Model13").constructor(constructors.get(0).get(2)).build());
        List<ModelJpaEntity> models2 = List.of(
                ModelJpaEntity.builder().name("Model21").constructor(constructors.get(1).get(0)).build(),
                ModelJpaEntity.builder().name("Model22").constructor(constructors.get(1).get(1)).build(),
                ModelJpaEntity.builder().name("Model23").constructor(constructors.get(1).get(2)).build());
        List<ModelJpaEntity> models3 = List.of(
                ModelJpaEntity.builder().name("Model31").constructor(constructors.get(2).get(0)).build(),
                ModelJpaEntity.builder().name("Model32").constructor(constructors.get(2).get(1)).build(),
                ModelJpaEntity.builder().name("Model33").constructor(constructors.get(2).get(2)).build());

        models1 = modelRepository.saveAll(models1);
        models2 = modelRepository.saveAll(models2);
        models3 = modelRepository.saveAll(models3);

        return List.of(models1, models2, models3);

    }

    private List<List<VehicleJpaEntity>> givenVehicles(List<List<EngineTypeJpaEntity>> engineTypes,
                                                       List<List<ModelJpaEntity>> models) {
        List<VehicleJpaEntity> vehicles1 = List.of(
                VehicleJpaEntity.builder().type(engineTypes.get(0).get(0)).model(models.get(0).get(0))
                        .circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(0).get(1)).model(models.get(0).get(1))
                        .circulationDate(LocalDate.of(2020, 01, 02)).registration("2 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(0).get(2)).model(models.get(0).get(2))
                        .circulationDate(LocalDate.of(2020, 01, 03)).registration("3 TUN 220").build());

        List<VehicleJpaEntity> vehicles2 = List.of(
                VehicleJpaEntity.builder().type(engineTypes.get(1).get(0)).model(models.get(1).get(0))
                        .circulationDate(LocalDate.of(2020, 01, 11)).registration("11 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(1).get(1)).model(models.get(1).get(1))
                        .circulationDate(LocalDate.of(2020, 01, 12)).registration("12 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(1).get(2)).model(models.get(1).get(2))
                        .circulationDate(LocalDate.of(2020, 01, 13)).registration("13 TUN 220").build());

        List<VehicleJpaEntity> vehicles3 = List.of(
                VehicleJpaEntity.builder().type(engineTypes.get(2).get(0)).model(models.get(2).get(0))
                        .circulationDate(LocalDate.of(2020, 01, 21)).registration("21 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(2).get(1)).model(models.get(2).get(1))
                        .circulationDate(LocalDate.of(2020, 01, 22)).registration("22 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(2).get(2)).model(models.get(2).get(2))
                        .circulationDate(LocalDate.of(2020, 01, 23)).registration("23 TUN 220").build());

        vehicles1 = vehicleRepository.saveAll(vehicles1);
        vehicles2 = vehicleRepository.saveAll(vehicles2);
        vehicles3 = vehicleRepository.saveAll(vehicles3);

        return List.of(vehicles1, vehicles2, vehicles3);
    }

    private List<ClientJpaEntity> givenClients(InternationalCallingCodeJpaEntity icc,
                                               List<DocumentJpaEntity> profileImages) {
        List<ClientJpaEntity> clients = List.of(
                new ClientJpaEntity(null, "client1-oauth-id", null, "Client1", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client1@gmail.com", null, null, icc, "22111111", null,
                        null, null, null, profileImages.get(0), null, null, null, null),
                new ClientJpaEntity(null, "client2-oauth-id", null, "Client2", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client2@gmail.com", null, null, icc, "22222222", null,
                        null, null, null, profileImages.get(1), null, null, null, null),
                new ClientJpaEntity(null, "client3-oauth-id", null, "Client3", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client3@gmail.com", null, null, icc, "22333333", null,
                        null, null, null, profileImages.get(2), null, null, null, null));

        return clientRepository.saveAll(clients);
    }

    private List<ClientJpaEntity> givenClientsWithoutLeadingZeroMobileNumber(InternationalCallingCodeJpaEntity icc,
                                                                             List<DocumentJpaEntity> profileImages) {
        List<ClientJpaEntity> clients = List.of(
                new ClientJpaEntity(null, "client1-oauth-id", null, "Client1", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client1@gmail.com", null, null, icc, "711111111", null,
                        null, null, null, profileImages.get(0), null, null, null, null),
                new ClientJpaEntity(null, "client2-oauth-id", null, "Client2", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client2@gmail.com", null, null, icc, "722222222", null,
                        null, null, null, profileImages.get(1), null, null, null, null),
                new ClientJpaEntity(null, "client3-oauth-id", null, "Client3", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client3@gmail.com", null, null, icc, "733333333", null,
                        null, null, null, profileImages.get(2), null, null, null, null));

        return clientRepository.saveAll(clients);
    }

    private List<TransporterJpaEntity> givenTransporters(List<List<VehicleJpaEntity>> vehicles,
                                                         List<DocumentJpaEntity> profileImages) {

        TransporterJpaEntity t1 = new TransporterJpaEntity(null, null, null, "Transporter1", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, null, null, null, null, null, null,
                null, null, null, profileImages.get(0), null, null, null, null);
        vehicles.get(0).forEach(v -> t1.addVehicle(v));

        TransporterJpaEntity t2 = new TransporterJpaEntity(null, null, null, "Transporter2", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, null, null, null, null, null, null,
                null, null, null, profileImages.get(1), null, null, null, null);
        vehicles.get(1).forEach(v -> t2.addVehicle(v));

        TransporterJpaEntity t3 = new TransporterJpaEntity(null, null, null, "Transporter3", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, null, null, null, null, null, null,
                null, null, null, profileImages.get(2), null, null, null, null);
        vehicles.get(2).forEach(v -> t1.addVehicle(v));

        List<TransporterJpaEntity> transporters = List.of(t1, t2, t3);

        return transporterRepository.saveAll(transporters);
    }

    private Map<Integer, Set<JourneyProposalJpaEntity>> givenProposals(List<TransporterJpaEntity> transporters,
                                                                       List<List<VehicleJpaEntity>> vehicles) {
        Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap = Map.of(0,
                Set.of(JourneyProposalJpaEntity.builder().price(150.0).transporter(transporters.get(0))
                                .vehicle(vehicles.get(0).get(0)).build(),
                        JourneyProposalJpaEntity.builder().price(160.0).transporter(transporters.get(1))
                                .vehicle(vehicles.get(0).get(1)).build(),
                        JourneyProposalJpaEntity.builder().price(140.0).transporter(transporters.get(2))
                                .vehicle(vehicles.get(0).get(2)).build()),
                1,
                Set.of(JourneyProposalJpaEntity.builder().price(240.0).transporter(transporters.get(0))
                                .vehicle(vehicles.get(1).get(0)).build(),
                        JourneyProposalJpaEntity.builder().price(260.0).transporter(transporters.get(1))
                                .vehicle(vehicles.get(1).get(1)).build(),
                        JourneyProposalJpaEntity.builder().price(250.0).transporter(transporters.get(2))
                                .vehicle(vehicles.get(1).get(2)).build()),
                2,
                Set.of(JourneyProposalJpaEntity.builder().price(340.0).transporter(transporters.get(0))
                                .vehicle(vehicles.get(2).get(0)).build(),
                        JourneyProposalJpaEntity.builder().price(350.0).transporter(transporters.get(1))
                                .vehicle(vehicles.get(2).get(1)).build(),
                        JourneyProposalJpaEntity.builder().price(360.0).transporter(transporters.get(2))
                                .vehicle(vehicles.get(2).get(2)).build()));

        proposalsMap.forEach((k, v) -> {
            Iterator<JourneyProposalJpaEntity> it = v.iterator();
            JourneyProposalJpaEntity jp = it.next();
            TransporterJpaEntity t = jp.getTransporter();
            t.addProposal(jp);
        });

        return proposalsMap;
    }

    private List<JourneyRequestJpaEntity> givenJourneyRequests(List<PlaceJpaEntity> departurePlaces,
                                                               List<PlaceJpaEntity> arrivalPlaces, List<EngineTypeJpaEntity> engineTypes, List<Integer> distances,
                                                               List<ZonedDateTime> dates, List<ZonedDateTime> endDates, List<Integer> workers, List<String> descriptions,
                                                               List<ClientJpaEntity> clients, Map<Integer, Set<JourneyProposalJpaEntity>> proposalsMap,
                                                               List<JourneyRequestStatusJpaEntity> jrStatuses) {

        JourneyRequestJpaEntity jrToday = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(0))
                .arrivalPlace(arrivalPlaces.get(0)).engineType(engineTypes.get(0)).distance(distances.get(0))
                .dateTime(endDates.get(0).toInstant()).workers(workers.get(0)).description(descriptions.get(0))
                .client(clients.get(0)).proposals(proposalsMap.get(0))
                .creationDateTime(dates.get(0).toInstant()).status(jrStatuses.get(0)).build();
        proposalsMap.get(0).forEach(p -> p.setJourneyRequest(jrToday));
        clients.get(0).addJourneyRequest(jrToday);

        JourneyRequestJpaEntity jrTomorrow = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(1))
                .arrivalPlace(arrivalPlaces.get(1)).engineType(engineTypes.get(1)).distance(distances.get(1))
                .dateTime(endDates.get(1).toInstant()).workers(workers.get(1)).description(descriptions.get(1))
                .client(clients.get(1)).proposals(proposalsMap.get(1))
                .creationDateTime(dates.get(1).toInstant()).status(jrStatuses.get(1)).build();
        proposalsMap.get(1).forEach(p -> p.setJourneyRequest(jrTomorrow));
        clients.get(1).addJourneyRequest(jrTomorrow);

        JourneyRequestJpaEntity jrOvermorrow = JourneyRequestJpaEntity.builder().departurePlace(departurePlaces.get(2))
                .arrivalPlace(arrivalPlaces.get(2)).engineType(engineTypes.get(2)).distance(distances.get(2))
                .dateTime(endDates.get(2).toInstant()).workers(workers.get(2)).description(descriptions.get(2))
                .client(clients.get(2)).proposals(proposalsMap.get(2))
                .creationDateTime(dates.get(2).toInstant()).status(jrStatuses.get(2)).build();

        proposalsMap.get(2).forEach(p -> p.setJourneyRequest(jrOvermorrow));
        clients.get(2).addJourneyRequest(jrOvermorrow);

        return journeyRequestRepository.saveAll(List.of(jrToday, jrTomorrow, jrOvermorrow));

    }

    private InternationalCallingCodeJpaEntity givenIcc(String code) {
        InternationalCallingCodeJpaEntity icc = InternationalCallingCodeJpaEntity.builder().value(code).enabled(true)
                .build();
        return internationalCallingCodeRepository.save(icc);
    }

    private List<DepartmentJpaEntity> givenArrivalDepartments() {
        CountryJpaEntity country = givenCountry();

        DepartmentJpaEntity d1 = new DepartmentJpaEntity("Sousse", new HashMap<String, LocalizedDepartmentJpaEntity>(),
                country, Collections.<DelegationJpaEntity>emptySet(), new BigDecimal(34.73910078895307),
                new BigDecimal(10.755186404578666));
        d1.setId(3L);

        DepartmentJpaEntity d2 = new DepartmentJpaEntity("Bizerte", new HashMap<String, LocalizedDepartmentJpaEntity>(),
                country, Collections.<DelegationJpaEntity>emptySet(), new BigDecimal(36.44898025387017),
                new BigDecimal(10.737296664741075));
        d2.setId(4L);

        DepartmentJpaEntity d3 = new DepartmentJpaEntity("B�ja", new HashMap<String, LocalizedDepartmentJpaEntity>(),
                country, Collections.<DelegationJpaEntity>emptySet(), new BigDecimal(36.80157399848794),
                new BigDecimal(10.178896922512495));
        d3.setId(5L);

        return departmentRepository.saveAll(List.of(d1, d2, d3));
    }

    private List<DepartmentJpaEntity> givenDepartureDepartments() {
        CountryJpaEntity country = givenCountry();
        DepartmentJpaEntity d1 = new DepartmentJpaEntity("Sfax", new HashMap<String, LocalizedDepartmentJpaEntity>(),
                country, Collections.<DelegationJpaEntity>emptySet(), new BigDecimal(34.73910078895307),
                new BigDecimal(10.755186404578666));
        d1.setId(1L);

        DepartmentJpaEntity d2 = new DepartmentJpaEntity("Nabeul", new HashMap<String, LocalizedDepartmentJpaEntity>(),
                country, Collections.<DelegationJpaEntity>emptySet(), new BigDecimal(36.44898025387017),
                new BigDecimal(10.737296664741075));
        d2.setId(2L);

        DepartmentJpaEntity d3 = new DepartmentJpaEntity("Tunis", new HashMap<String, LocalizedDepartmentJpaEntity>(),
                country, Collections.<DelegationJpaEntity>emptySet(), new BigDecimal(36.80157399848794),
                new BigDecimal(10.178896922512495));
        d3.setId(3L);

        return departmentRepository.saveAll(List.of(d1, d2, d3));

    }

    private CountryJpaEntity givenCountry() {
        Optional<CountryJpaEntity> countryFromDb = countryRepository.findByCode("TN");
        if (countryFromDb.isPresent()) {
            return countryFromDb.get();
        }

        InternationalCallingCodeJpaEntity icc = new InternationalCallingCodeJpaEntity();
        icc.setValue("+33");
        icc.setEnabled(true);
        icc = internationalCallingCodeRepository.save(icc);

        TimeZoneJpaEntity timeZone = new TimeZoneJpaEntity();
        timeZone.setName("Africa/Tunis");
        timeZone.setGmtOffset("GMT+01:00");
        timeZone = timeZoneRepository.save(timeZone);

        CountryJpaEntity country = new CountryJpaEntity("TN", "/path/to/country/flag", icc, List.of(timeZone),
                new HashMap<String, LocalizedCountryJpaEntity>(), Collections.<DepartmentJpaEntity>emptySet());

        return countryRepository.save(country);

    }

}
