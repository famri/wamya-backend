package com.excentria_it.wamya.adapter.persistence.repository;

import static com.excentria_it.wamya.test.data.common.LocalityJpaTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToLocalityTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class LocalityToLocalityTravelInfoRepositoryTests {
	@Autowired
	private LocalityToLocalityTravelInfoRepository l2lTravelInfoRepository;
	@Autowired
	private LocalityRepository localityRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DelegationRepository delegationRepository;

	@BeforeEach
	void cleanDatabase() {
		l2lTravelInfoRepository.deleteAll();
		localityRepository.deleteAll();
	}

	// @Test
	void testFindByLocalityOne_IdAndLocalityTwo_Id() {
		// given
		LocalityJpaEntity locality1 = givenLocalityOne();
		LocalityJpaEntity locality2 = givenLocalityTwo();

		LocalityToLocalityTravelInfoJpaEntity l2lTi = givenLocalityToLocalityTravelInfoJpaEntity(locality1, locality2);
		// when
		Optional<JourneyTravelInfo> ti = l2lTravelInfoRepository
				.findByLocalityOne_IdAndLocalityTwo_Id(locality1.getId(), locality2.getId());

		// then
		assertThat(ti).isNotEmpty();
		assertEquals(l2lTi.getDistance(), ti.get().getDistance());
		assertEquals(l2lTi.getHours(), ti.get().getHours());
		assertEquals(l2lTi.getMinutes(), ti.get().getMinutes());
	}

	private LocalityToLocalityTravelInfoJpaEntity givenLocalityToLocalityTravelInfoJpaEntity(
			LocalityJpaEntity locality1, LocalityJpaEntity locality2) {

		LocalityToLocalityTravelInfoJpaEntity l2lTi = new LocalityToLocalityTravelInfoJpaEntity(locality1, locality2, 2,
				30, 250000);

		return l2lTravelInfoRepository.saveAndFlush(l2lTi);
	}

	private LocalityJpaEntity givenLocalityTwo() {
		LocalityJpaEntity l = defaultNewLocalityJpaEntity();

		departmentRepository.saveAndFlush(l.getDelegation().getDepartment());
		delegationRepository.saveAndFlush(l.getDelegation());
		return localityRepository.saveAndFlush(l);
	}

	private LocalityJpaEntity givenLocalityOne() {
		LocalityJpaEntity l = defaultNewLocalityJpaEntity();

		departmentRepository.saveAndFlush(l.getDelegation().getDepartment());
		delegationRepository.saveAndFlush(l.getDelegation());
		return localityRepository.saveAndFlush(l);
	}
}
