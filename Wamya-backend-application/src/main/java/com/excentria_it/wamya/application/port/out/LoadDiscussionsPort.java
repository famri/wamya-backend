package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;

public interface LoadDiscussionsPort {

	LoadDiscussionsOutputResult loadDiscussions(Long userAccountId, Boolean isTransporter, Integer pageNumber,
			Integer pageSize, FilterCriterion filter, SortCriterion sortingCriterion);

	LoadDiscussionsOutputResult loadDiscussions(Long userAccountId, Boolean isTransporter, Integer pageNumber,
			Integer pageSize, SortCriterion sortingCriterion);

	Optional<LoadDiscussionsOutput> loadDiscussionByClientIdAndTransporterId(Long clientOauthId,
			Long transporterOauthId);

	Optional<LoadDiscussionsOutput> loadDiscussionById(Long discussionId);

}
