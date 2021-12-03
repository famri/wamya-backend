package com.excentria_it.wamya.application.port.out;

import java.util.Set;

public interface UpdateTransporterRatingRequestRecordPort {
	void incrementRevivals(Set<Long> ids);
}
