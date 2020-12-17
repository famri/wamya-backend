package com.excentria_it.wamya.domain;

import java.util.List;

public class JourneyRequestsSearchResult extends PagedData<JourneyRequestSearchDto> {

	public JourneyRequestsSearchResult(int totalPages, long totalElements, int pageNumber, int pageSize,
			boolean hasNext, List<JourneyRequestSearchDto> content) {
		super(totalPages, totalElements, pageNumber, pageSize, hasNext, content);

	}

}
