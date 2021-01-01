package com.excentria_it.wamya.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.PeriodCriterion.PeriodValue;
import com.excentria_it.wamya.common.SortCriterion;

public class ParameterUtils {

	private ParameterUtils() {
	}

	public static SortCriterion parameterToSortCriterion(Optional<String> sort, String defaultSort) {
		if (sort == null || defaultSort == null)
			return null;
		String sortStr = sort.orElse(defaultSort);

		String field, direction;

		String[] sortFieldAndDirection = sortStr.split(",");

		if (sortFieldAndDirection.length == 2) {
			field = sortFieldAndDirection[0].trim();
			direction = sortFieldAndDirection[1].trim();

		} else {
			field = sortFieldAndDirection[0].trim();
			direction = "asc";
		}

		return new SortCriterion(field, direction);
	}

	public static PeriodCriterion parameterToPeriodCriterion(Optional<String> period, String defaultPeriod) {

		if (period == null || defaultPeriod == null)
			return null;

		String periodStr = period.orElse(defaultPeriod);

		PeriodValue pv = null;
		LocalDateTime higherEdge = LocalDateTime.now(ZoneOffset.UTC);

		try {

			pv = PeriodValue.valueOf(periodStr.toUpperCase());

		} catch (IllegalArgumentException e1) {
			return new PeriodCriterion(periodStr, null, null);
		}

		LocalDateTime lowerEdge = pv.calculateLowerEdge(higherEdge);

		return new PeriodCriterion(pv.name(), lowerEdge, higherEdge);

	}

	public static String kebabToCamelCase(String kebabStyleSting) {
		int idx;
		while ((idx = kebabStyleSting.indexOf("-")) != -1) {
			if (idx < kebabStyleSting.length() - 1) {
				String charToUpper = kebabStyleSting.substring(idx + 1, idx + 2).toUpperCase();
				kebabStyleSting = kebabStyleSting.substring(0, idx) + charToUpper + kebabStyleSting.substring(idx + 2);
			} else {
				kebabStyleSting = kebabStyleSting.substring(0, idx);
			}
		}
		return kebabStyleSting;
	}
}
