package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.LoadDiscussionsOutput;

public interface CreateDiscussionPort {

	LoadDiscussionsOutput createDiscussion(Long clientId, Long transporterId, boolean isTransporter);

}
