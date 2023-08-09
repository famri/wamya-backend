package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.adapter.persistence.repository.GenderRepository;
import com.excentria_it.wamya.domain.LoadGendersDto;
import com.excentria_it.wamya.test.data.common.GenderTestData;

@ExtendWith(MockitoExtension.class)
public class GendersPersistenceAdapterTests {
	@Mock
	private GenderRepository genderRepository;
	@InjectMocks
	private GendersPersistenceAdapter gendersPersistenceAdapter;

	@Test
	void testLoadAllGenders() {
		// given
		List<LoadGendersDto> genders = GenderTestData.defaultLoadGendersDtos();
		given(genderRepository.findAllByLocale(any(String.class), any(Sort.class))).willReturn(genders);

		// when
		List<LoadGendersDto> gendersResult = gendersPersistenceAdapter.loadAllGenders("en_US");

		// then
		then(genderRepository).should(times(1)).findAllByLocale(eq("en_US"),
				eq(Sort.by(new Order(Direction.ASC, "id"))));
		assertEquals(genders, gendersResult);

	}

}
