package com.excentria_it.wamya.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JourneyRequestProposals {

	private long totalElements;

	private List<JourneyProposalDto> content;
}
