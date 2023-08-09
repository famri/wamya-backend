package com.excentria_it.wamya.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class TransporterProposalsOutput {

	private int totalPages;

	private long totalElements;

	private int pageNumber;

	private int pageSize;

	private boolean hasNext;

	private List<TransporterProposalOutput> content;
}
