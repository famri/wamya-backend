package com.excentria_it.wamya.adapter.persistence.repository;

import static com.excentria_it.wamya.test.data.common.DelegationJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
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
import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class DelegationToDepartmentTravelInfoRepositoryTests {
	@Autowired
	private DelegationToDepartmentTravelInfoRepository dl2dpTravelInfoRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DelegationRepository delegationRepository;

	@BeforeEach
	void cleanDatabase() {
		dl2dpTravelInfoRepository.deleteAll();
		delegationRepository.deleteAll();
		departmentRepository.deleteAll();
	}

	@Test
	void testFindByDelegation_IdAndDepartment_Id() {
		// given
		DelegationJpaEntity delegation = givenDelegation();
		DepartmentJpaEntity department = givenDepartment();

		DelegationToDepartmentTravelInfoJpaEntity dl2dpTi = givenDelegationToDepartmentTravelInfoJpaEntity(delegation,
				department);
		// when
		Optional<JourneyTravelInfo> ti = dl2dpTravelInfoRepository
				.findByDelegation_IdAndDepartment_Id(delegation.getId(), department.getId());

		// then
		assertThat(ti).isNotEmpty();
		assertEquals(dl2dpTi.getDistance(), ti.get().getDistance());
		assertEquals(dl2dpTi.getHours(), ti.get().getHours());
		assertEquals(dl2dpTi.getMinutes(), ti.get().getMinutes());
	}

	private DelegationToDepartmentTravelInfoJpaEntity givenDelegationToDepartmentTravelInfoJpaEntity(
			DelegationJpaEntity delegation, DepartmentJpaEntity department) {

		DelegationToDepartmentTravelInfoJpaEntity dl2dpTi = new DelegationToDepartmentTravelInfoJpaEntity(delegation,
				department, 2, 30, 250000);
		return dl2dpTravelInfoRepository.saveAndFlush(dl2dpTi);
	}

	private DepartmentJpaEntity givenDepartment() {
		DepartmentJpaEntity d = defaultNewDepartmentJpaEntity();
		return departmentRepository.saveAndFlush(d);
	}

	private DelegationJpaEntity givenDelegation() {
		DelegationJpaEntity del = defaultNewDelegationJpaEntity();
		departmentRepository.saveAndFlush(del.getDepartment());
		return delegationRepository.saveAndFlush(del);
	}
}
