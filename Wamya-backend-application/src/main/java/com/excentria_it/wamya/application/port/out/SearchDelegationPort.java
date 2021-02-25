package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteDelegationDto;

public interface SearchDelegationPort {
	List<AutoCompleteDelegationDto> searchDelegationByName(String delegationName, Long countryId, Integer limit,
			String locale);

	List<AutoCompleteDelegationDto> searchDelegationByNameAndDepartmentName(String delegationName,
			String departmentName, Long countryId, Integer limit, String locale);
}
