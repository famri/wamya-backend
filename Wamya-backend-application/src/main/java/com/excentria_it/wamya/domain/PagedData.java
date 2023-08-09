package com.excentria_it.wamya.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PagedData<T> {

	protected int totalPages;

	protected long totalElements;

	protected int pageNumber;
	
	protected int pageSize;
	
	protected boolean hasNext;
	
	protected List<T> content;

}
