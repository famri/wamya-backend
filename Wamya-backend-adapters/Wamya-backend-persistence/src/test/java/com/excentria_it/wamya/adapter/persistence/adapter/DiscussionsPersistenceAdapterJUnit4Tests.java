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

import com.excentria_it.wamya.adapter.persistence.adapter.DiscussionsPersistenceAdapter.DiscussionFilterField;
import com.excentria_it.wamya.adapter.persistence.mapper.DiscussionMapper;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionRepository;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;

@RunWith(PowerMockRunner.class)
public class DiscussionsPersistenceAdapterJUnit4Tests {
	@Mock
	private DiscussionRepository discussionsRepository;
	@Mock
	private DiscussionMapper discussionMapper;
	@InjectMocks
	private DiscussionsPersistenceAdapter discussionsPersistenceAdapter;

	@Test
	@PrepareForTest(DiscussionFilterField.class)
	public void givenTransporterDiscussionWithBadFilterFieldEnum_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult()
			throws Exception {
		// given
		DiscussionFilterField UNKNOWN_FILTER_FIELD = PowerMockito.mock(DiscussionFilterField.class);
		PowerMockito.when(UNKNOWN_FILTER_FIELD.ordinal()).thenReturn(1);
		PowerMockito.when(UNKNOWN_FILTER_FIELD.name()).thenReturn("UNKNOWN_FILTER_FIELD");
		PowerMockito.mockStatic(DiscussionFilterField.class);

		DiscussionFilterField[] filterFields = new DiscussionFilterField[] { DiscussionFilterField.ACTIVE, UNKNOWN_FILTER_FIELD };
		PowerMockito.when(DiscussionFilterField.values()).thenAnswer((Answer<DiscussionFilterField[]>) invocation -> filterFields);

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
	@PrepareForTest(DiscussionFilterField.class)
	public void givenClientDiscussionWithBadFilterFieldEnum_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult()
			throws Exception {
		// given
		DiscussionFilterField UNKNOWN_FILTER_FIELD = PowerMockito.mock(DiscussionFilterField.class);
		PowerMockito.when(UNKNOWN_FILTER_FIELD.ordinal()).thenReturn(1);
		PowerMockito.when(UNKNOWN_FILTER_FIELD.name()).thenReturn("UNKNOWN_FILTER_FIELD");
		PowerMockito.mockStatic(DiscussionFilterField.class);

		DiscussionFilterField[] filterFields = new DiscussionFilterField[] { DiscussionFilterField.ACTIVE, UNKNOWN_FILTER_FIELD };
		PowerMockito.when(DiscussionFilterField.values()).thenAnswer((Answer<DiscussionFilterField[]>) invocation -> filterFields);

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
