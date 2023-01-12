package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;

import java.util.Optional;

public interface LoadDiscussionsPort {

    LoadDiscussionsOutputResult loadDiscussions(Long userAccountId, Boolean isTransporter, Integer pageNumber,
                                                Integer pageSize, FilterCriterion filter, SortCriterion sortingCriterion);

    LoadDiscussionsOutputResult loadDiscussions(Long userAccountId, Boolean isTransporter, Integer pageNumber,
                                                Integer pageSize, SortCriterion sortingCriterion);

    Optional<LoadDiscussionsOutput> loadDiscussionByClientIdAndTransporterId(String clientOauthId,
                                                                             String transporterOauthId);

    Optional<LoadDiscussionsOutput> loadDiscussionById(Long discussionId);

}
