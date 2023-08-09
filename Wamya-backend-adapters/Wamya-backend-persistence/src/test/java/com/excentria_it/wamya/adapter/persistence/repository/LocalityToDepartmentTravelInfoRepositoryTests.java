package com.excentria_it.wamya.adapter.persistence.repository;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
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

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class LocalityToDepartmentTravelInfoRepositoryTests {
	@Autowired
	private LocalityToDepartmentTravelInfoRepository l2dpTravelInfoRepository;
	@Autowired
	private LocalityRepository localityRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DelegationRepository delegationRepository;

	@BeforeEach
	void cleanDatabase() {
		l2dpTravelInfoRepository.deleteAll();
		departmentRepository.deleteAll();
		localityRepository.deleteAll();
	}

	@Test
	void testFindByLocality_IdAndDepartment_Id() {
		// given
		LocalityJpaEntity locality = givenLocality();
		DepartmentJpaEntity department = givenDepartment();

		LocalityToDepartmentTravelInfoJpaEntity l2dplTi = givenLocalityToDepartmentTravelInfoJpaEntity(locality,
				department);
		// when
		Optional<JourneyTravelInfo> ti = l2dpTravelInfoRepository.findByLocality_IdAndDepartment_Id(locality.getId(),
				department.getId());

		// then
		assertThat(ti).isNotEmpty();
		assertEquals(l2dplTi.getDistance(), ti.get().getDistance());
		assertEquals(l2dplTi.getHours(), ti.get().getHours());
		assertEquals(l2dplTi.getMinutes(), ti.get().getMinutes());
	}

	private LocalityToDepartmentTravelInfoJpaEntity givenLocalityToDepartmentTravelInfoJpaEntity(
			LocalityJpaEntity locality, DepartmentJpaEntity department) {
		LocalityToDepartmentTravelInfoJpaEntity l2dpTi = new LocalityToDepartmentTravelInfoJpaEntity(locality,
				department, 2, 30, 250000);

		return l2dpTravelInfoRepository.saveAndFlush(l2dpTi);
	}

	private LocalityJpaEntity givenLocality() {
		LocalityJpaEntity l = defaultNewLocalityJpaEntity();
		departmentRepository.saveAndFlush(l.getDelegation().getDepartment());
		delegationRepository.saveAndFlush(l.getDelegation());
		return localityRepository.saveAndFlush(l);
	}

	private DepartmentJpaEntity givenDepartment() {
		DepartmentJpaEntity dp = defaultNewDepartmentJpaEntity();
		return departmentRepository.saveAndFlush(dp);
	}
}
