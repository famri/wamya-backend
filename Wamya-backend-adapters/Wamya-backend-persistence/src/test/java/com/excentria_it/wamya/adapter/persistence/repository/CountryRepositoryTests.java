package com.excentria_it.wamya.adapter.persistence.repository;

import static com.excentria_it.wamya.test.data.common.CountryJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.CountryJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedCountryJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.domain.CountryDto;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class CountryRepositoryTests {
	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private TimeZoneRepository timeZoneRepository;

	@Autowired
	private DelegationRepository delegationRepository;

	@Autowired
	private InternationalCallingCodeRepository iccRepository;

	@BeforeEach
	void cleanDatabase() {
		countryRepository.deleteAll();

	}

	@Test
	void testFindByIcc_ValueAndMobileNumber() {
		// given
		List<List<DelegationJpaEntity>> delegations = givenDelegations();
		List<DepartmentJpaEntity> departments = givenDepartments(delegations);

		CountryJpaEntity country = givenCountry(departments);
		// when
		Optional<CountryDto> countryDtoOptional = countryRepository.findByCodeAndLocale(country.getCode(), "fr_FR");
		// then
		assertEquals(country.getId(), countryDtoOptional.get().getId());
		assertEquals(country.getName("fr_FR"), countryDtoOptional.get().getName());
	}

	@Test
	void testFindAllByLocale() {
		// given
		List<CountryJpaEntity> countries = defaultCountryJpaEntitiesWithoutDepartments();
		List<DepartmentJpaEntity> departments = defaultCountryDepartmentsJpaEntities();
		departments.forEach(d -> d.setId(null));
		

		for (CountryJpaEntity c : countries) {
			c.getTimeZones().forEach(t -> {
				t.setId(null);
				timeZoneRepository.save(t);
			});

			c.getIcc().setId(null);
			iccRepository.save(c.getIcc());

			c.getDepartments().forEach(d -> {
				d.setId(null);
			});

			c = countryRepository.saveAndFlush(c);

		}
		
		countries.get(0).addDepartment(departments.get(0));
		countries.get(0).addDepartment(departments.get(1));
		
		countries.get(1).addDepartment(departments.get(2));
		countries.get(1).addDepartment(departments.get(3));
		
		for (CountryJpaEntity c : countries) {
			c = countryRepository.save(c);
		}
		
		countries.sort((c1, c2) -> c1.getName("fr_FR").compareTo(c2.getName("fr_FR")));

		// when

		List<CountryJpaEntity> result = countryRepository.findAllByLocale("fr_FR");

		// then
		assertEquals(countries, result);
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

	private List<DepartmentJpaEntity> givenDepartments(List<List<DelegationJpaEntity>> delegations) {
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
