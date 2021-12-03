package com.excentria_it.wamya.application.port.out;

import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;

public interface LoadTransporterRatingRequestRecordPort {

	Optional<TransporterRatingRequestRecordOutput> loadRecord(String hash, Long userId, String locale);

	Set<TransporterRatingRequestRecordOutput> loadUnfulfilledRecords(Integer maxRevives);
}
