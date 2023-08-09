package com.excentria_it.wamya.domain;

import java.util.List;

import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoadMessagesOutputResult {

	private int totalPages;

	private long totalElements;

	private int pageNumber;

	private int pageSize;

	private boolean hasNext;

	private List<MessageOutput> content;

}