package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.application.port.out.SearchDelegationPort;
import com.excentria_it.wamya.application.port.out.SearchDepartmentPort;
import com.excentria_it.wamya.application.port.out.SearchLocalityPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.AutoCompleteDelegationDto;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class PlaceSearchPersistenceAdapter implements SearchDepartmentPort, SearchDelegationPort, SearchLocalityPort {

	private final LocalityRepository localityRepository;

	private final DelegationRepository delegationRepository;

	private final DepartmentRepository departmentRepository;

	@Override
	public List<AutoCompleteLocalityDto> searchLocalityByName(String localityName, Long countryId, Integer limit,
			String locale) {

		Pageable page = PageRequest.of(0, limit);

		return localityRepository.findByCountry_IdAndNameLikeIgnoringCase(countryId, localityName, locale, page);

	}

	@Override
	public List<AutoCompleteLocalityDto> searchLocalityByNameAndDelegationName(String localityName,
			String delegationName, Long countryId, Integer limit, String locale) {
		Pageable page = PageRequest.of(0, limit);

		return localityRepository.findByCountry_IdAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(countryId,
				delegationName, localityName, locale, page);
	}

	@Override
	public List<AutoCompleteLocalityDto> searchLocalityByNameAndDepartmentName(String localityName,
			String departmentName, Long countryId, Integer limit, String locale) {
		Pageable page = PageRequest.of(0, limit);

		return localityRepository.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(countryId,
				departmentName, localityName, locale, page);
	}

	@Override
	public List<AutoCompleteLocalityDto> searchLocalityByNameAndDelegationNameAndDepartmentName(String localityName,
			String delegationName, String departmentName, Long countryId, Integer limit, String locale) {
		Pageable page = PageRequest.of(0, limit);

		return localityRepository
				.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(
						countryId, departmentName, delegationName, localityName, locale, page);
	}

	@Override
	public List<AutoCompleteDelegationDto> searchDelegationByName(String delegationName, Long countryId, Integer limit,
			String locale) {
		Pageable page = PageRequest.of(0, limit);
		return delegationRepository.findByCountry_IdAndNameLikeIgnoringCase(countryId, delegationName, locale, page);
	}

	@Override
	public List<AutoCompleteDelegationDto> searchDelegationByNameAndDepartmentName(String delegationName,
			String departmentName, Long countryId, Integer limit, String locale) {
		Pageable page = PageRequest.of(0, limit);
		return delegationRepository.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(countryId,
				departmentName, delegationName, locale, page);
	}

	@Override
	public List<AutoCompleteDepartmentDto> searchDepartmentByName(String departmentName, Long countryId, Integer limit,
			String locale) {
		Pageable page = PageRequest.of(0, limit);
		return departmentRepository.findByCountry_IdAndNameLikeIgnoringCase(countryId, departmentName, locale, page);
	}

}
