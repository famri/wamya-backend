package com.excentria_it.wamya.adapter.persistence.repository;

import static com.excentria_it.wamya.test.data.common.DelegationJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.LocalityJpaTestData.*;
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
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class LocalityToDelegationTravelInfoRepositoryTests {
	@Autowired
	private LocalityToDelegationTravelInfoRepository l2dlTravelInfoRepository;
	@Autowired
	private LocalityRepository localityRepository;
	@Autowired
	private DelegationRepository delegationRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@BeforeEach
	void cleanDatabase() {
		l2dlTravelInfoRepository.deleteAll();
		delegationRepository.deleteAll();
		localityRepository.deleteAll();
	}
	
	@Test
	void testFindByLocality_IdAndDelegation_Id() {
		// given
		LocalityJpaEntity locality = givenLocality();
		DelegationJpaEntity delegation = givenDelegation();

		LocalityToDelegationTravelInfoJpaEntity l2dlTi = givenLocalityToDelegationTravelInfoJpaEntity(locality,
				delegation);
		// when
		Optional<JourneyTravelInfo> ti = l2dlTravelInfoRepository.findByLocality_IdAndDelegation_Id(locality.getId(),
				delegation.getId());

		// then
		assertThat(ti).isNotEmpty();
		assertEquals(l2dlTi.getDistance(), ti.get().getDistance());
		assertEquals(l2dlTi.getHours(), ti.get().getHours());
		assertEquals(l2dlTi.getMinutes(), ti.get().getMinutes());
	}

	private LocalityToDelegationTravelInfoJpaEntity givenLocalityToDelegationTravelInfoJpaEntity(
			LocalityJpaEntity locality, DelegationJpaEntity delegation) {
		LocalityToDelegationTravelInfoJpaEntity l2dlTi = new LocalityToDelegationTravelInfoJpaEntity(locality,
				delegation, 2, 30, 250000);

		return l2dlTravelInfoRepository.saveAndFlush(l2dlTi);
	}

	private LocalityJpaEntity givenLocality() {
		LocalityJpaEntity l = defaultNewLocalityJpaEntity();
		departmentRepository.saveAndFlush(l.getDelegation().getDepartment());
		delegationRepository.saveAndFlush(l.getDelegation());
		return localityRepository.saveAndFlush(l);
	}

	private DelegationJpaEntity givenDelegation() {
		DelegationJpaEntity del = defaultNewDelegationJpaEntity();
		departmentRepository.saveAndFlush(del.getDepartment());
		return delegationRepository.saveAndFlush(del);
	}
}
