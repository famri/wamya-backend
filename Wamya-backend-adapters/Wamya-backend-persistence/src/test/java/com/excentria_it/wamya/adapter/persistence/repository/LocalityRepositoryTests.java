package com.excentria_it.wamya.adapter.persistence.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.CountryJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedCountryJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedLocalityJpaEntity;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class LocalityRepositoryTests {
	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DelegationRepository delegationRepository;

	@Autowired
	private LocalityRepository localityRepository;

	@BeforeEach
	void cleanDatabase() {
		localityRepository.deleteAll();
		delegationRepository.deleteAll();
		departmentRepository.deleteAll();
		countryRepository.deleteAll();

	}

	@Test
	void testFindByCountry_IdAndNameLikeIgnoringCase() {
		// given
		List<List<LocalityJpaEntity>> localities = givenLocalities();
		List<List<DelegationJpaEntity>> delegations = givenDelegations(localities);
		List<DepartmentJpaEntity> departments = givenDepartments(delegations);

		CountryJpaEntity country = givenCountry(departments);
		// when

		Pageable firstPageWithFiveElements = PageRequest.of(0, 5);
		List<AutoCompleteLocalityDto> localitiesDtosResult = localityRepository.findByCountry_IdAndNameLikeIgnoringCase(
				country.getId(), localities.get(0).get(0).getPossibleNames().substring(0, 12), "fr_FR",
				firstPageWithFiveElements);
		// then
		assertEquals(localities.get(0).get(0).getId(), localitiesDtosResult.get(0).getId());
		assertEquals(localities.get(0).get(0).getName("fr_FR"), localitiesDtosResult.get(0).getName());
		assertThat(localitiesDtosResult.size()).isLessThanOrEqualTo(5);
	}

	@Test
	void testFindByCountry_IdAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase() {
		// given
		List<List<LocalityJpaEntity>> localities = givenLocalities();
		List<List<DelegationJpaEntity>> delegations = givenDelegations(localities);
		List<DepartmentJpaEntity> departments = givenDepartments(delegations);

		CountryJpaEntity country = givenCountry(departments);
		// when

		Pageable firstPageWithFiveElements = PageRequest.of(0, 5);
		List<AutoCompleteLocalityDto> localitiesDtosResult = localityRepository
				.findByCountry_IdAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(country.getId(),
						localities.get(0).get(0).getDelegation().getPossibleNames().substring(0, 9),
						localities.get(0).get(0).getPossibleNames().substring(0, 12), "fr_FR",
						firstPageWithFiveElements);
		// then
		assertEquals(localities.get(0).get(0).getId(), localitiesDtosResult.get(0).getId());
		assertEquals(localities.get(0).get(0).getName("fr_FR"), localitiesDtosResult.get(0).getName());
		assertThat(localitiesDtosResult.size()).isLessThanOrEqualTo(5);
	}

	@Test
	void testFindByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase() {
		// given
		List<List<LocalityJpaEntity>> localities = givenLocalities();
		List<List<DelegationJpaEntity>> delegations = givenDelegations(localities);
		List<DepartmentJpaEntity> departments = givenDepartments(delegations);

		CountryJpaEntity country = givenCountry(departments);
		// when

		Pageable firstPageWithFiveElements = PageRequest.of(0, 5);
		List<AutoCompleteLocalityDto> localitiesDtosResult = localityRepository
				.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(country.getId(),
						localities.get(0).get(0).getDelegation().getDepartment().getPossibleNames().substring(0, 12),
						localities.get(0).get(0).getPossibleNames().substring(0, 12), "fr_FR",
						firstPageWithFiveElements);
		// then
		assertEquals(localities.get(0).get(0).getId(), localitiesDtosResult.get(0).getId());
		assertEquals(localities.get(0).get(0).getName("fr_FR"), localitiesDtosResult.get(0).getName());
		assertThat(localitiesDtosResult.size()).isLessThanOrEqualTo(5);
	}
	
	@Test
	void testFindByCountry_IdAndDepartmentNameLikeIgnoringCaseAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase() {
		// given
		List<List<LocalityJpaEntity>> localities = givenLocalities();
		List<List<DelegationJpaEntity>> delegations = givenDelegations(localities);
		List<DepartmentJpaEntity> departments = givenDepartments(delegations);

		CountryJpaEntity country = givenCountry(departments);
		// when

		Pageable firstPageWithFiveElements = PageRequest.of(0, 5);
		List<AutoCompleteLocalityDto> localitiesDtosResult = localityRepository
				.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(country.getId(),
						localities.get(0).get(0).getDelegation().getDepartment().getPossibleNames().substring(0, 12),
						localities.get(0).get(0).getDelegation().getPossibleNames().substring(0,9),
						localities.get(0).get(0).getPossibleNames().substring(0, 12), "fr_FR",
						firstPageWithFiveElements);
		// then
		assertEquals(localities.get(0).get(0).getId(), localitiesDtosResult.get(0).getId());
		assertEquals(localities.get(0).get(0).getName("fr_FR"), localitiesDtosResult.get(0).getName());
		assertThat(localitiesDtosResult.size()).isLessThanOrEqualTo(5);
	}
	private List<List<LocalityJpaEntity>> givenLocalities() {
		LocalityJpaEntity l101 = new LocalityJpaEntity();
		l101.setPossibleNames("Ben Arous Sud");
		LocalizedLocalityJpaEntity ll101 = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"), "Ben Arous Sud",
				l101);
		l101.getLocalizations().put("fr_FR", ll101);

		LocalityJpaEntity l102 = new LocalityJpaEntity();
		l102.setPossibleNames("Ben Arous");
		LocalizedLocalityJpaEntity ll102 = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"), "Ben Arous", l102);
		l102.getLocalizations().put("fr_FR", ll102);

		LocalityJpaEntity l103 = new LocalityJpaEntity();
		l103.setPossibleNames("Cité El Wafa");
		LocalizedLocalityJpaEntity ll103 = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"), "Cité El Wafa",
				l103);
		l103.getLocalizations().put("fr_FR", ll103);

		LocalityJpaEntity l104 = new LocalityJpaEntity();
		l104.setPossibleNames("Residence Du Mediterrane");
		LocalizedLocalityJpaEntity ll104 = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"),
				"Residence Du Mediterrane", l104);
		l104.getLocalizations().put("fr_FR", ll104);

		LocalityJpaEntity l201 = new LocalityJpaEntity();
		l201.setPossibleNames("Chouamekh");
		LocalizedLocalityJpaEntity ll201 = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"), "Chouamekh", l201);
		l201.getLocalizations().put("fr_FR", ll201);

		LocalityJpaEntity l202 = new LocalityJpaEntity();
		l202.setPossibleNames("Ksar El Hallouf");
		LocalizedLocalityJpaEntity ll202 = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"), "Ksar El Hallouf",
				l202);
		l202.getLocalizations().put("fr_FR", ll202);

		LocalityJpaEntity l203 = new LocalityJpaEntity();
		l203.setPossibleNames("Ouled Abdennebi");
		LocalizedLocalityJpaEntity ll203 = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"), "Ouled Abdennebi",
				l203);
		l203.getLocalizations().put("fr_FR", ll203);

		LocalityJpaEntity l204 = new LocalityJpaEntity();
		l204.setPossibleNames("Hassi Jerbi");
		LocalizedLocalityJpaEntity ll204 = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"), "Hassi Jerbi",
				l204);
		l204.getLocalizations().put("fr_FR", ll204);

		l101 = localityRepository.save(l101);
		l102 = localityRepository.save(l102);
		l103 = localityRepository.save(l103);
		l104 = localityRepository.save(l104);

		l201 = localityRepository.save(l201);
		l202 = localityRepository.save(l202);
		l203 = localityRepository.save(l203);
		l204 = localityRepository.save(l204);
		return List.of(List.of(l101, l102), List.of(l103, l104), List.of(l201, l202), List.of(l203, l204));
	}

	private List<List<DelegationJpaEntity>> givenDelegations(List<List<LocalityJpaEntity>> localities) {

		final DelegationJpaEntity dl11 = new DelegationJpaEntity();
		dl11.setPossibleNames("Ben Arous, BenArous, BinArous, Bin Arous");
		LocalizedDelegationJpaEntity ldje11 = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Ben arous",
				dl11);
		dl11.getLocalizations().put("fr_FR", ldje11);
		localities.get(0).forEach(l -> dl11.addLocality(l));

		final DelegationJpaEntity dl12 = new DelegationJpaEntity();
		dl12.setPossibleNames("Boumhel, Boumhal, Bomhel, Bomhal");
		LocalizedDelegationJpaEntity ldje12 = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Boumhel",
				dl12);
		dl12.getLocalizations().put("fr_FR", ldje12);
		localities.get(1).forEach(l -> dl12.addLocality(l));

		final DelegationJpaEntity dl21 = new DelegationJpaEntity();
		dl21.setPossibleNames(
				"Beni Khedech, BeniKhedech, Bini Khedech, BiniKhedech, BeniKhadech, Beni Khadech, BeniKhadach, Beni Khadach, BiniKhadach, Bini Khadach, BeniKdach, BeniKdach, BiniKdach, Bini Kdach, BeniKdech, BeniKdech, BiniKdech, BiniKdech");
		LocalizedDelegationJpaEntity ldje21 = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Beni Khedech",
				dl21);
		dl21.getLocalizations().put("fr_FR", ldje21);
		localities.get(2).forEach(l -> dl21.addLocality(l));

		final DelegationJpaEntity dl22 = new DelegationJpaEntity();
		dl22.setPossibleNames("Zarzis, Zerzis, Djerjis, Djarjis, Jarjis, Jerjis");
		LocalizedDelegationJpaEntity ldje22 = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Zarzis",
				dl22);
		dl22.getLocalizations().put("fr_FR", ldje22);
		localities.get(3).forEach(l -> dl22.addLocality(l));

		delegationRepository.save(dl11);
		delegationRepository.save(dl12);
		delegationRepository.save(dl21);
		delegationRepository.save(dl22);

		return List.of(List.of(dl11, dl12), List.of(dl21, dl22));
	}

	private CountryJpaEntity givenCountry(List<DepartmentJpaEntity> departments) {

		CountryJpaEntity country = new CountryJpaEntity();
		country.setCode("TN");
		departments.forEach(d -> country.addDepartment(d));

		LocalizedCountryJpaEntity lcje = new LocalizedCountryJpaEntity(new LocalizedId("fr_FR"), "Tunisie", country);

		country.getLocalizations().put("fr_FR", lcje);
		return countryRepository.save(country);

	}

	List<DepartmentJpaEntity> givenDepartments(List<List<DelegationJpaEntity>> delegations) {
		DepartmentJpaEntity d1 = new DepartmentJpaEntity();
		d1.setPossibleNames("BenArous, Ben Arous, BinArous, Bin Arous");
		delegations.get(0).forEach(dl -> d1.addDelegation(dl));

		LocalizedDepartmentJpaEntity ldje1 = new LocalizedDepartmentJpaEntity(new LocalizedId("fr_FR"), d1,
				"Ben arous");
		delegations.get(0).forEach(d -> d.setDepartment(d1));
		d1.getLocalizations().put("fr_FR", ldje1);

		DepartmentJpaEntity d2 = new DepartmentJpaEntity();
		d2.setPossibleNames("Médenine, Médenin, Medenine, Medenin, Madanine, Madanin, Midnine, Midnin, Mednine");
		delegations.get(1).forEach(dl -> d2.addDelegation(dl));
		LocalizedDepartmentJpaEntity ldje2 = new LocalizedDepartmentJpaEntity(new LocalizedId("fr_FR"), d2, "Médenine");
		d2.getLocalizations().put("fr_FR", ldje2);

		return departmentRepository.saveAll(List.of(d1, d2));

	}

}
