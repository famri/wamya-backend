package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;

public interface SearchLocalityPort {

	List<AutoCompleteLocalityDto> searchLocalityByName(String localityName, Long countryId, Integer limit,
			String locale);

	List<AutoCompleteLocalityDto> searchLocalityByNameAndDelegationName(String localityName, String delegationName,
			Long countryId, Integer limit, String locale);

	List<AutoCompleteLocalityDto> searchLocalityByNameAndDepartmentName(String localityName, String departmentName,
			Long countryId, Integer limit, String locale);

	List<AutoCompleteLocalityDto> searchLocalityByNameAndDelegationNameAndDepartmentName(String localityName,
			String delegationName, String departmentName, Long countryId, Integer limit, String locale);
}
