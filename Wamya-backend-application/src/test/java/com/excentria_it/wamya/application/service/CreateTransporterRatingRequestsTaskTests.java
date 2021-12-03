package com.excentria_it.wamya.application.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.CreateTransporterRatingRequestPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.props.CreateTransporterRatingRequestProperties;
import com.excentria_it.wamya.application.service.helper.HashGenerator;
import com.excentria_it.wamya.common.HashAlgorithm;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;

@ExtendWith(MockitoExtension.class)
public class CreateTransporterRatingRequestsTaskTests {
	@Mock
	private LoadJourneyRequestPort loadJourneyRequestPort;
	@Mock
	private CreateTransporterRatingRequestPort createTransporterRatingRequestPort;
	@Mock
	private CreateTransporterRatingRequestProperties createTransporterRatingRequestProperties;
	@Mock
	private HashGenerator hashGenerator;

	@InjectMocks
	private CreateTransporterRatingRequestsTask createTransporterRatingRequestsTask;

	@Test
	void testCreateTransporterRatingRequestsTask() {

		// given
		given(createTransporterRatingRequestProperties.getLimit()).willReturn(10);

		given(loadJourneyRequestPort.loadJourneyRequestIdsByStatusCodeAndLimit(JourneyRequestStatusCode.FULFILLED, 10))
				.willReturn(Set.of(1L, 2L, 3L, 4L, 5L));

		given(hashGenerator.generateHashes(Set.of(1L, 2L, 3L, 4L, 5L), HashAlgorithm.SHA3_256))
				.willReturn(List.of("HASH1", "HASH2", "HASH3", "HASH4", "HASH5"));

		// when
		createTransporterRatingRequestsTask.createTransporterRatingRequests();

		// then
		then(createTransporterRatingRequestPort).should(times(1)).createTransporterRatingRequests(
				Set.of(1L, 2L, 3L, 4L, 5L), List.of("HASH1", "HASH2", "HASH3", "HASH4", "HASH5"));
	}

	@Test
	void givenEmptyFulfilledJourneyIds_whenCreateTransporterRatingRequestsTask_thenDoNotcreateTransporterRatingRequests() {

		// given
		given(createTransporterRatingRequestProperties.getLimit()).willReturn(10);

		given(loadJourneyRequestPort.loadJourneyRequestIdsByStatusCodeAndLimit(JourneyRequestStatusCode.FULFILLED, 10))
				.willReturn(Collections.emptySet());

		// when
		createTransporterRatingRequestsTask.createTransporterRatingRequests();

		// then
		then(createTransporterRatingRequestPort).should(never()).createTransporterRatingRequests(any(Set.class),
				any(List.class));
	}
}