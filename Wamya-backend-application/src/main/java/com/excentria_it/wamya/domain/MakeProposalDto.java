package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MakeProposalDto {
	private Long id;

	private Double price;

	private String status;
}
