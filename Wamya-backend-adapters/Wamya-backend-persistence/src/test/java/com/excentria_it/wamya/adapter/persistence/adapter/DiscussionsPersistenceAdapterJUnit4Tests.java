package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.excentria_it.wamya.adapter.persistence.mapper.DiscussionMapper;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionsRepository;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.FilterField;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;

@RunWith(PowerMockRunner.class)
public class DiscussionsPersistenceAdapterJUnit4Tests {
	@Mock
	private DiscussionsRepository discussionsRepository;
	@Mock
	private DiscussionMapper discussionMapper;
	@InjectMocks
	private DiscussionsPersistenceAdapter discussionsPersistenceAdapter;

	@Test
	@PrepareForTest(FilterField.class)
	public void givenTransporterDiscussionWithBadFilterFieldEnum_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult()
			throws Exception {
		// given
		FilterField UNKNOWN_FILTER_FIELD = PowerMockito.mock(FilterField.class);
		PowerMockito.when(UNKNOWN_FILTER_FIELD.ordinal()).thenReturn(1);
		PowerMockito.when(UNKNOWN_FILTER_FIELD.name()).thenReturn("UNKNOWN_FILTER_FIELD");
		PowerMockito.mockStatic(FilterField.class);

		FilterField[] filterFields = new FilterField[] { FilterField.ACTIVE, UNKNOWN_FILTER_FIELD };
		PowerMockito.when(FilterField.values()).thenAnswer((Answer<FilterField[]>) invocation -> filterFields);

		// when
		LoadDiscussionsOutputResult result = discussionsPersistenceAdapter.loadDiscussions(1L, true, 0, 25,
				new FilterCriterion("unknown-filter-field", "someValue"), new SortCriterion("date-time", "desc"));
		// then
		assertEquals(0, result.getPageNumber());
		assertEquals(25, result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertThat(result.getContent()).isEmpty();
		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());
	}

	@Test
	@PrepareForTest(FilterField.class)
	public void givenClientDiscussionWithBadFilterFieldEnum_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult()
			throws Exception {
		// given
		FilterField UNKNOWN_FILTER_FIELD = PowerMockito.mock(FilterField.class);
		PowerMockito.when(UNKNOWN_FILTER_FIELD.ordinal()).thenReturn(1);
		PowerMockito.when(UNKNOWN_FILTER_FIELD.name()).thenReturn("UNKNOWN_FILTER_FIELD");
		PowerMockito.mockStatic(FilterField.class);

		FilterField[] filterFields = new FilterField[] { FilterField.ACTIVE, UNKNOWN_FILTER_FIELD };
		PowerMockito.when(FilterField.values()).thenAnswer((Answer<FilterField[]>) invocation -> filterFields);

		// when
		LoadDiscussionsOutputResult result = discussionsPersistenceAdapter.loadDiscussions(1L, false, 0, 25,
				new FilterCriterion("unknown-filter-field", "someValue"), new SortCriterion("date-time", "desc"));
		// then
		assertEquals(0, result.getPageNumber());
		assertEquals(25, result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertThat(result.getContent()).isEmpty();
		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());
	}

}
