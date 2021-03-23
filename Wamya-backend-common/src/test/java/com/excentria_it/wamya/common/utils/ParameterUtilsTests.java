package com.excentria_it.wamya.common.utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.domain.StatusCode;

public class ParameterUtilsTests {

	@Test
	void testParameterToSortCriterionWithNullDefaultSort() {
		SortCriterion sc = ParameterUtils.parameterToSortCriterion(Optional.of("price,desc"), null);
		assertNull(sc);

	}

	@Test
	void testParameterToSortCriterionWithNullSort() {
		SortCriterion sc = ParameterUtils.parameterToSortCriterion(null, "date,asc");
		assertNull(sc);

	}

	@Test
	void testParameterToSortCriterionWithEmptySort() {
		SortCriterion sc = ParameterUtils.parameterToSortCriterion(Optional.empty(), "date,asc");
		assertEquals("date", sc.getField());
		assertEquals("asc", sc.getDirection());

	}

	@Test
	void testParameterToSortCriterion() {
		SortCriterion sc = ParameterUtils.parameterToSortCriterion(Optional.of("price,desc"), "date,asc");
		assertEquals("price", sc.getField());
		assertEquals("desc", sc.getDirection());

	}

	@Test
	void testParameterToSortCriterionWithNoDirection() {
		SortCriterion sc = ParameterUtils.parameterToSortCriterion(Optional.of("price"), "date,asc");
		assertEquals("price", sc.getField());
		assertEquals("asc", sc.getDirection());

	}

	@Test
	void testParameterToPeriodCriterionWithEmptyPeriod() {

		ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime expectedLowerEdge = ldt.minusYears(1);

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.empty(), "y1");

		assertEquals("Y1", pc.getValue());

		assertEquals(ldt.getYear(), pc.getHigherEdge().getYear());
		assertEquals(ldt.getMonth(), pc.getHigherEdge().getMonth());
		assertEquals(ldt.getDayOfMonth(), pc.getHigherEdge().getDayOfMonth());
		assertTrue(ldt.isBefore(pc.getHigherEdge()) || ldt.equals(pc.getHigherEdge()));

		assertEquals(expectedLowerEdge.getYear(), pc.getLowerEdge().getYear());
		assertEquals(expectedLowerEdge.getMonth(), pc.getLowerEdge().getMonth());
		assertEquals(expectedLowerEdge.getDayOfMonth(), pc.getLowerEdge().getDayOfMonth());
		assertTrue(expectedLowerEdge.isBefore(pc.getLowerEdge()) || expectedLowerEdge.equals(pc.getLowerEdge()));

	}

	@Test
	void testParameterToPeriodCriterionWithNullPeriod() {

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(null, "y1");

		assertNull(pc);

	}

	@Test
	void testParameterToPeriodCriterionWithNullDefaultPeriod() {

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.of("y1"), null);

		assertNull(pc);

	}

	@Test
	void testParameterToPeriodCriterionWithInvalidPeriodAndInvalidDefaultPeriod() {

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.of("this_is_invalid_period"),
				"this_is_invalid_default_period");

		assertEquals("this_is_invalid_period", pc.getValue());

		assertEquals(null, pc.getHigherEdge());

		assertEquals(null, pc.getLowerEdge());

	}

	@Test
	void testParameterToPeriodCriterionWithInvalidPeriodAndValidDefaultPeriod() {

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.of("this_is_invalid_period"), "y1");

		assertEquals("this_is_invalid_period", pc.getValue());

		assertEquals(null, pc.getHigherEdge());

		assertEquals(null, pc.getLowerEdge());

	}

	@Test
	void testParameterToPeriodCriterionWith1Year() {
		ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime expectedLowerEdge = ldt.minusYears(1);

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.of("y1"), "y1");

		assertEquals("Y1", pc.getValue());

		assertEquals(ldt.getYear(), pc.getHigherEdge().getYear());
		assertEquals(ldt.getMonth(), pc.getHigherEdge().getMonth());
		assertEquals(ldt.getDayOfMonth(), pc.getHigherEdge().getDayOfMonth());
		assertTrue(ldt.isBefore(pc.getHigherEdge()) || ldt.equals(pc.getHigherEdge()));

		assertEquals(expectedLowerEdge.getYear(), pc.getLowerEdge().getYear());
		assertEquals(expectedLowerEdge.getMonth(), pc.getLowerEdge().getMonth());
		assertEquals(expectedLowerEdge.getDayOfMonth(), pc.getLowerEdge().getDayOfMonth());
		assertTrue(expectedLowerEdge.isBefore(pc.getLowerEdge()) || expectedLowerEdge.equals(pc.getLowerEdge()));

	}

	@Test
	void testParameterToPeriodCriterionWith6Months() {
		ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC);

		ZonedDateTime expectedLowerEdge = ldt.minusMonths(6);

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.of("m6"), "m6");

		assertEquals("M6", pc.getValue());

		assertEquals(ldt.getYear(), pc.getHigherEdge().getYear());
		assertEquals(ldt.getMonth(), pc.getHigherEdge().getMonth());
		assertEquals(ldt.getDayOfMonth(), pc.getHigherEdge().getDayOfMonth());
		assertTrue(ldt.isBefore(pc.getHigherEdge()) || ldt.equals(pc.getHigherEdge()));

		assertEquals(expectedLowerEdge.getYear(), pc.getLowerEdge().getYear());
		assertEquals(expectedLowerEdge.getMonth(), pc.getLowerEdge().getMonth());
		assertEquals(expectedLowerEdge.getDayOfMonth(), pc.getLowerEdge().getDayOfMonth());
		assertTrue(expectedLowerEdge.isBefore(pc.getLowerEdge()) || expectedLowerEdge.equals(pc.getLowerEdge()));

	}

	@Test
	void testParameterToPeriodCriterionWith3Months() {

		ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC);

		ZonedDateTime expectedLowerEdge = ldt.minusMonths(3);

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.of("m3"), "m3");

		assertEquals("M3", pc.getValue());

		assertEquals(ldt.getYear(), pc.getHigherEdge().getYear());
		assertEquals(ldt.getMonth(), pc.getHigherEdge().getMonth());
		assertEquals(ldt.getDayOfMonth(), pc.getHigherEdge().getDayOfMonth());
		assertTrue(ldt.isBefore(pc.getHigherEdge()) || ldt.equals(pc.getHigherEdge()));

		assertEquals(expectedLowerEdge.getYear(), pc.getLowerEdge().getYear());
		assertEquals(expectedLowerEdge.getMonth(), pc.getLowerEdge().getMonth());
		assertEquals(expectedLowerEdge.getDayOfMonth(), pc.getLowerEdge().getDayOfMonth());
		assertTrue(expectedLowerEdge.isBefore(pc.getLowerEdge()) || expectedLowerEdge.equals(pc.getLowerEdge()));

	}

	@Test
	void testParameterToPeriodCriterionWith1Month() {
		ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC);

		ZonedDateTime expectedLowerEdge = ldt.minusMonths(1);

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.of("m1"), "m1");

		assertEquals("M1", pc.getValue());

		assertEquals(ldt.getYear(), pc.getHigherEdge().getYear());
		assertEquals(ldt.getMonth(), pc.getHigherEdge().getMonth());
		assertEquals(ldt.getDayOfMonth(), pc.getHigherEdge().getDayOfMonth());
		assertTrue(ldt.isBefore(pc.getHigherEdge()) || ldt.equals(pc.getHigherEdge()));

		assertEquals(expectedLowerEdge.getYear(), pc.getLowerEdge().getYear());
		assertEquals(expectedLowerEdge.getMonth(), pc.getLowerEdge().getMonth());
		assertEquals(expectedLowerEdge.getDayOfMonth(), pc.getLowerEdge().getDayOfMonth());
		assertTrue(expectedLowerEdge.isBefore(pc.getLowerEdge()) || expectedLowerEdge.equals(pc.getLowerEdge()));

	}

	@Test
	void testParameterToPeriodCriterionWith1Week() {
		ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC);

		ZonedDateTime expectedLowerEdge = ldt.minusWeeks(1);

		PeriodCriterion pc = ParameterUtils.parameterToPeriodCriterion(Optional.of("w1"), "w1");

		assertEquals("W1", pc.getValue());

		assertEquals(ldt.getYear(), pc.getHigherEdge().getYear());
		assertEquals(ldt.getMonth(), pc.getHigherEdge().getMonth());
		assertEquals(ldt.getDayOfMonth(), pc.getHigherEdge().getDayOfMonth());
		assertTrue(ldt.isBefore(pc.getHigherEdge()) || ldt.equals(pc.getHigherEdge()));

		assertEquals(expectedLowerEdge.getYear(), pc.getLowerEdge().getYear());
		assertEquals(expectedLowerEdge.getMonth(), pc.getLowerEdge().getMonth());
		assertEquals(expectedLowerEdge.getDayOfMonth(), pc.getLowerEdge().getDayOfMonth());
		assertTrue(expectedLowerEdge.isBefore(pc.getLowerEdge()) || expectedLowerEdge.equals(pc.getLowerEdge()));

	}

	@Test
	void testKebabToCamelCase() {
		String str1 = "-minprice";
		String res1 = ParameterUtils.kebabToCamelCase(str1);
		assertEquals("Minprice", res1);

		String str2 = "minprice-";
		String res2 = ParameterUtils.kebabToCamelCase(str2);
		assertEquals("minprice", res2);

		String str3 = "min-price";
		String res3 = ParameterUtils.kebabToCamelCase(str3);
		assertEquals("minPrice", res3);

		String str4 = "-min-p-ric-e-";
		String res4 = ParameterUtils.kebabToCamelCase(str4);
		assertEquals("MinPRicE", res4);

	}

	@Test
	void testParseProposalStatusFilter() {
		Optional<String> filter = Optional.of("status:accepted,submitted");
		List<StatusCode> statusCodes = ParameterUtils.parseProposalStatusFilter(filter);
		assertThat(
				List.of(StatusCode.ACCEPTED, StatusCode.SUBMITTED).containsAll(statusCodes) && statusCodes.size() == 2);
	}

	@Test
	void testParseProposalStatusFilterFromEmptyFilter() {
		Optional<String> filter = Optional.empty();
		List<StatusCode> statusCodes = ParameterUtils.parseProposalStatusFilter(filter);
		assertThat(statusCodes.isEmpty());
	}

	@Test
	void testParseProposalStatusFilterFromFilterWithNoColumn() {
		Optional<String> filter = Optional.of("status=accepted,submitted");
		List<StatusCode> statusCodes = ParameterUtils.parseProposalStatusFilter(filter);
		assertThat(statusCodes.isEmpty());
	}

	@Test
	void testParseProposalStatusFilterFromFilterWithOneBadStatusCodes() {
		Optional<String> filter = Optional.of("status:badcode,submitted");
		List<StatusCode> statusCodes = ParameterUtils.parseProposalStatusFilter(filter);
		assertThat(List.of(StatusCode.SUBMITTED).containsAll(statusCodes) && statusCodes.size() == 1);
	}

	@Test
	void testParseProposalStatusFilterFromFilterWithAllBadStatusCodes() {
		Optional<String> filter = Optional.of("status:badcode1,badcode2");
		List<StatusCode> statusCodes = ParameterUtils.parseProposalStatusFilter(filter);
		assertThat(statusCodes.isEmpty());
	}

	@Test
	void testParameterToFilterCriterion() {
		Optional<String> filter = Optional.of("field1:value1");
		FilterCriterion filterCriterion = ParameterUtils.parameterToFilterCriterion(filter);
		assertEquals("field1", filterCriterion.getField());
		assertEquals("value1", filterCriterion.getValue());

	}

	@Test
	void testParameterToFilterCriterionWithEmptyFilter() {
		Optional<String> filter = Optional.empty();
		FilterCriterion filterCriterion = ParameterUtils.parameterToFilterCriterion(filter);
		assertNull(filterCriterion);

	}

	@Test
	void testParameterToFilterCriterionWithNullFilter() {
		FilterCriterion filterCriterion = ParameterUtils.parameterToFilterCriterion(null);
		assertNull(filterCriterion);

	}

	@Test
	void testParameterToFilterCriterionWithMissiongColumn() {
		Optional<String> filter = Optional.of("field1=value1");
		FilterCriterion filterCriterion = ParameterUtils.parameterToFilterCriterion(filter);
		assertNull(filterCriterion);
	}

}
