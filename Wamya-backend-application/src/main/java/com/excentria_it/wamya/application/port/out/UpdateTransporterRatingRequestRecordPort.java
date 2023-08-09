package com.excentria_it.wamya.application.port.out;

import java.util.Set;

import com.excentria_it.wamya.domain.TransporterRatingRequestStatus;

public interface UpdateTransporterRatingRequestRecordPort {
	void incrementRevivals(Set<Long> ids);

	void updateRequestStatus(Long ratingRequestId, TransporterRatingRequestStatus status);
}
