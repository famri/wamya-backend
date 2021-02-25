package com.excentria_it.wamya.adapter.persistence.repository;

import static com.excentria_it.wamya.test.data.common.DelegationJpaTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class DelegationToDelegationTravelInfoRepositoryTests {

	@Autowired
	private DelegationToDelegationTravelInfoRepository dl2dlTravelInfoRepository;

	@Autowired
	private DelegationRepository delegationRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@BeforeEach
	void cleanDatabase() {
		dl2dlTravelInfoRepository.deleteAll();
		delegationRepository.deleteAll();

	}

	//@Test
	void testFindByDelegationOne_IdAndDelegationTwo_IdOrDelegationTwo_IdAndDelegationOne_Id() {
		// given
		DelegationJpaEntity delegation1 = givenDelegationOne();
		DelegationJpaEntity delegation2 = givenDelegationTwo();

		DelegationToDelegationTravelInfoJpaEntity dl2dlTi = givenDelegationToDelegationTravelInfoJpaEntity(delegation1,
				delegation2);
		// when
		Optional<JourneyTravelInfo> ti = dl2dlTravelInfoRepository
				.findByDelegationOne_IdAndDelegationTwo_Id(delegation1.getId(), delegation2.getId());

		// then
		assertThat(ti).isNotEmpty();
		assertEquals(dl2dlTi.getDistance(), ti.get().getDistance());
		assertEquals(dl2dlTi.getHours(), ti.get().getHours());
		assertEquals(dl2dlTi.getMinutes(), ti.get().getMinutes());
	}

	private DelegationToDelegationTravelInfoJpaEntity givenDelegationToDelegationTravelInfoJpaEntity(
			DelegationJpaEntity delegation1, DelegationJpaEntity delegation2) {

		DelegationToDelegationTravelInfoJpaEntity l2lTi = new DelegationToDelegationTravelInfoJpaEntity(delegation1,
				delegation2, 2, 30, 250000);

		return dl2dlTravelInfoRepository.saveAndFlush(l2lTi);
	}

	private DelegationJpaEntity givenDelegationOne() {
		DelegationJpaEntity dl = defaultNewDelegationJpaEntity();

		departmentRepository.saveAndFlush(dl.getDepartment());
		return delegationRepository.saveAndFlush(dl);
	}

	private DelegationJpaEntity givenDelegationTwo() {
		DelegationJpaEntity dl = defaultNewDelegationJpaEntity();

		departmentRepository.saveAndFlush(dl.getDepartment());
		return delegationRepository.saveAndFlush(dl);
	}
}
