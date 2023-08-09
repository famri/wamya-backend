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
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedCountryJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class DepartmentRepositoryTests {
	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DelegationRepository delegationRepository;

	@BeforeEach
	void cleanDatabase() {
		delegationRepository.deleteAll();
		departmentRepository.deleteAll();
		countryRepository.deleteAll();

	}

	@Test
	void testFindByCountry_IdAndNameLikeIgnoringCase() {
		// given
		List<List<DelegationJpaEntity>> delegations = givenDelegations();
		List<DepartmentJpaEntity> departments = givenDepartments(delegations);

		CountryJpaEntity country = givenCountry(departments);
		// when

		Pageable firstPageWithFiveElements = PageRequest.of(0, 5);
		List<AutoCompleteDepartmentDto> departmentsDtosResult = departmentRepository
				.findByCountry_IdAndNameLikeIgnoringCase(country.getId(),
						departments.get(0).getPossibleNames().substring(0, 4), "fr_FR", firstPageWithFiveElements);
		// then
		assertEquals(departments.get(0).getId(), departmentsDtosResult.get(0).getId());
		assertEquals(departments.get(0).getName("fr_FR"), departmentsDtosResult.get(0).getName());
		assertThat(departmentsDtosResult.size()).isLessThanOrEqualTo(5);
	}

	private List<List<DelegationJpaEntity>> givenDelegations() {

		DelegationJpaEntity dl11 = new DelegationJpaEntity();
		dl11.setPossibleNames("BenArous, Ben Arous, BinArous, Bin Arous");
		LocalizedDelegationJpaEntity ldje11 = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Ben arous",
				dl11);
		dl11.getLocalizations().put("fr_FR", ldje11);

		DelegationJpaEntity dl12 = new DelegationJpaEntity();
		dl12.setPossibleNames("Boumhel, Boumhal, Bomhel, Bomhal");
		LocalizedDelegationJpaEntity ldje12 = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Boumhel",
				dl12);
		dl12.getLocalizations().put("fr_FR", ldje12);

		DelegationJpaEntity dl21 = new DelegationJpaEntity();
		dl21.setPossibleNames(
				"Beni Khedech, BeniKhedech, Bini Khedech, BiniKhedech, BeniKhadech, Beni Khadech, BeniKhadach, Beni Khadach, BiniKhadach, Bini Khadach, BeniKdach, BeniKdach, BiniKdach, Bini Kdach, BeniKdech, BeniKdech, BiniKdech, BiniKdech");
		LocalizedDelegationJpaEntity ldje21 = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Beni Khedech",
				dl21);
		dl21.getLocalizations().put("fr_FR", ldje21);

		DelegationJpaEntity dl22 = new DelegationJpaEntity();
		dl22.setPossibleNames("Zarzis, Zerzis, Djerjis, Djarjis, Jarjis, Jerjis");
		LocalizedDelegationJpaEntity ldje22 = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Zarzis",
				dl22);
		dl22.getLocalizations().put("fr_FR", ldje22);

		dl11 = delegationRepository.save(dl11);
		dl12 = delegationRepository.save(dl12);
		dl21 = delegationRepository.save(dl21);
		dl22 = delegationRepository.save(dl22);

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
