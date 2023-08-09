package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.LocaleJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.LocaleTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.entity.LocaleJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.LocaleMapper;
import com.excentria_it.wamya.adapter.persistence.repository.LocaleRepository;
import com.excentria_it.wamya.domain.LoadLocalesDto;

@ExtendWith(MockitoExtension.class)
public class LocalePersistenceAdapterTests {
	@Mock
	private LocaleRepository localeRepository;
	@Mock
	private LocaleMapper localeMapper;

	@InjectMocks
	private LocalePersistenceAdapter localePersistenceAdapter;

	@Test
	void testLoadAllLocales() {
		// given
		List<LocaleJpaEntity> localesEntities = defaultLocaleJpaEntities();
		given(localeRepository.findAll(Sort.by(Direction.ASC, "name"))).willReturn(localesEntities);
		List<LoadLocalesDto> localeDtos = defaultLoadLocalesDtos();
		given(localeMapper.mapToDomainEntity(localesEntities.get(0))).willReturn(localeDtos.get(0));
		given(localeMapper.mapToDomainEntity(localesEntities.get(1))).willReturn(localeDtos.get(1));
		// when
		List<LoadLocalesDto> result = localePersistenceAdapter.loadAllLocales();
		// then
		assertEquals(localeDtos, result);
	}
}
