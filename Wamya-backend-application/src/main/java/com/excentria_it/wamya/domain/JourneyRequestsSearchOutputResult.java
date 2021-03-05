package com.excentria_it.wamya.domain;

import java.util.List;

import lombok.Value;

@Value
public class JourneyRequestsSearchOutputResult {
	private int totalPages;

	private long totalElements;

	private int pageNumber;

	private int pageSize;

	private boolean hasNext;

	private List<JourneyRequestSearchOutput> content;
}
