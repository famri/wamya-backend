package com.excentria_it.wamya.domain;

import java.util.List;

import lombok.Value;

@Value

public class JourneyRequestsSearchResult {

	private int totalPages;

	private long totalElements;

	private int pageNumber;

	private int pageSize;

	private boolean hasNext;

	private List<JourneyRequestSearchDto> content;

}
