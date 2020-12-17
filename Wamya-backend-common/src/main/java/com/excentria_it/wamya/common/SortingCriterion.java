package com.excentria_it.wamya.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortingCriterion {

	private String field;

	private String direction;

}
