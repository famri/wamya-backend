package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.LoadDiscussionsOutput;

public interface CreateDiscussionPort {

    LoadDiscussionsOutput createDiscussion(String clientId, String transporterId);

}
