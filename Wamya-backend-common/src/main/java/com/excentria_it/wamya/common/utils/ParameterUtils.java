package com.excentria_it.wamya.common.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.PeriodCriterion.PeriodValue;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.domain.StatusCode;

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
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

		try {

			pv = PeriodValue.valueOf(periodStr.toUpperCase());

		} catch (IllegalArgumentException e1) {
			return new PeriodCriterion(periodStr, null, null);
		}

		ZonedDateTime[] lowerAndHigherEdges = pv.calculateLowerAndHigherEdges(now);

		return new PeriodCriterion(pv.name(), lowerAndHigherEdges[0], lowerAndHigherEdges[1]);

	}

	public static String kebabToCamelCase(String kebabStyleString) {
		int idx;
		while ((idx = kebabStyleString.indexOf("-")) != -1) {
			if (idx < kebabStyleString.length() - 1) {
				String charToUpper = kebabStyleString.substring(idx + 1, idx + 2).toUpperCase();
				kebabStyleString = kebabStyleString.substring(0, idx) + charToUpper
						+ kebabStyleString.substring(idx + 2);
			} else {
				kebabStyleString = kebabStyleString.substring(0, idx);
			}
		}
		return kebabStyleString;
	}

	public static String kebabToSnakeCase(String kebabStyleString) {

		return kebabStyleString.replace("-", "_");
	}

	public static List<StatusCode> parseProposalStatusFilter(Optional<String> filter) {
		if (filter.isEmpty())
			return Collections.emptyList();
		String[] filterTokens = filter.get().split(":");
		if (filterTokens.length != 2) {
			return Collections.emptyList();
		}

		String[] filterValues = filterTokens[1].split(",");

		List<StatusCode> result = new ArrayList<>();
		for (int i = 0; i < filterValues.length; i++) {
			try {
				StatusCode statusCode = StatusCode.valueOf(filterValues[i].toUpperCase());
				result.add(statusCode);
			} catch (IllegalArgumentException e) {
				continue;
			}
		}
		return result.isEmpty() ? Collections.emptyList() : result;

	}

	public static FilterCriterion parameterToFilterCriterion(Optional<String> filter) {
		if (filter == null)
			return null;
		String filterStr = filter.orElse(null);
		if (filterStr == null) {
			return null;
		}
		String field, value;

		String[] filterFieldAndValue = filterStr.split(":");

		if (filterFieldAndValue.length == 2) {
			field = filterFieldAndValue[0].trim();
			value = filterFieldAndValue[1].trim();

		} else {
			return null;
		}

		return new FilterCriterion(field, value);
	}

}
