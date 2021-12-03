package com.excentria_it.wamya.application.port.out;

public interface SaveTransporterRatingPort {

	void saveRating(Long clientId, Integer rating, String comment, Long transporterId);

}
