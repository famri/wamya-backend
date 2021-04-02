package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadMessagesOutputResult;

public interface LoadMessagesPort {

	LoadMessagesOutputResult loadMessages(Long discussionId, Integer pageNumber, Integer pageSize,
			SortCriterion sortingCriterion);

}
