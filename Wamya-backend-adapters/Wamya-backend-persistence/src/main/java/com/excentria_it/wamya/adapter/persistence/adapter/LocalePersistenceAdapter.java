package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.mapper.LocaleMapper;
import com.excentria_it.wamya.adapter.persistence.repository.LocaleRepository;
import com.excentria_it.wamya.application.port.out.LoadLocalesPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.LoadLocalesDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class LocalePersistenceAdapter implements LoadLocalesPort {

	private final LocaleRepository localeRepository;
	private final LocaleMapper localeMapper;

	@Override
	public List<LoadLocalesDto> loadAllLocales() {
		return localeRepository.findAll(Sort.by(Direction.ASC, "name")).stream()
				.map(l -> localeMapper.mapToDomainEntity(l)).collect(Collectors.toList());

	}

}
