package com.excentria_it.wamya.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadEngineTypesResult {
	private Integer totalElements;
	private List<LoadEngineTypesDto> content;

}
