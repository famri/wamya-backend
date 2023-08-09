package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.adapter.persistence.repository.GenderRepository;
import com.excentria_it.wamya.application.port.out.LoadGendersPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.LoadGendersDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class GendersPersistenceAdapter implements LoadGendersPort {
	private final GenderRepository genderRepository;

	@Override
	public List<LoadGendersDto> loadAllGenders(String locale) {
		return genderRepository.findAllByLocale(locale, Sort.by(new Order(Direction.ASC, "id")));
	}

}
