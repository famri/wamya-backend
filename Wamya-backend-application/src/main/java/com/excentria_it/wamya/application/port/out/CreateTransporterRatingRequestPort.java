package com.excentria_it.wamya.application.port.out;

import java.util.List;
import java.util.Set;

public interface CreateTransporterRatingRequestPort {

	void createTransporterRatingRequests(Set<Long> fulfilledJourneysIds, List<String> hashList);

}
