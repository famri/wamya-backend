package com.excentria_it.wamya.application.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.UpdateJourneyRequestStatusUseCase;
import com.excentria_it.wamya.application.port.out.CancelJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceNamesPort;
import com.excentria_it.wamya.application.port.out.LoadProposalsPort;
import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.utils.DateTimeFormatters;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.domain.PushMessage;
import com.excentria_it.wamya.common.domain.PushTemplate;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;
import com.excentria_it.wamya.domain.PlaceType;
import com.excentria_it.wamya.domain.TransporterNotificationInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateJourneyRequestStatusService implements UpdateJourneyRequestStatusUseCase {

	private final LoadJourneyRequestPort loadJourneyRequestPort;

	private final CancelJourneyRequestPort cancelJourneyRequestPort;

	private final LoadProposalsPort loadProposalsPort;

	private final AsynchronousMessagingPort messagingPort;

	private final LoadPlaceNamesPort loadPlaceNamesPort;

	private final DateTimeHelper dateTimeHelper;

	@Override
	public void updateStatus(Long journeyRequestId, String username, JourneyRequestStatusCode status, String locale) {
		ClientJourneyRequestDtoOutput clientJourneyRequestDto = loadJourneyRequestPort
				.loadJourneyRequestByIdAndClientEmail(journeyRequestId, username, locale)
				.orElseThrow(() -> new JourneyRequestNotFoundException(
						String.format("Journey request not found by ID: %d", journeyRequestId)));

		if (JourneyRequestStatusCode.CANCELED.equals(status)) {
			if (clientJourneyRequestDto.getProposalsCount() > 0) {
				Set<TransporterNotificationInfo> notifInfo = loadProposalsPort
						.loadTransportersNotificationInfo(journeyRequestId);
				Set<String> locales = notifInfo.stream().map(ni -> ni.getLocale()).collect(Collectors.toSet());

				Map<String, String> departurePlaceNames = loadPlaceNamesPort.loadPlaceNames(
						clientJourneyRequestDto.getDeparturePlace().getId(),
						PlaceType.valueOf(clientJourneyRequestDto.getDeparturePlace().getType()), locales);

				Map<String, String> arrivalPlaceNames = loadPlaceNamesPort.loadPlaceNames(
						clientJourneyRequestDto.getArrivalPlace().getId(),
						PlaceType.valueOf(clientJourneyRequestDto.getArrivalPlace().getType()), locales);

				requestSendingPushNotifications(notifInfo, departurePlaceNames, arrivalPlaceNames,
						clientJourneyRequestDto.getDateTime());

			}
			cancelJourneyRequestPort.cancelJourneyRequest(journeyRequestId);
		}

	}

	private boolean requestSendingPushNotifications(Set<TransporterNotificationInfo> notifInfo,
			Map<String, String> departurePlaceNames, Map<String, String> arrivalPlaceNames, Instant journeyDateTime) {

		for (TransporterNotificationInfo tni : notifInfo) {

			LocalDateTime localDateTime = dateTimeHelper.systemToUserLocalDateTime(journeyDateTime, tni.getZoneId());

			Map<String, String> pushTemplateParams = Map.of(PushTemplate.PROPOSAL_REJECTED.getTemplateParams().get(0),
					departurePlaceNames.get(tni.getLocale()),

					PushTemplate.PROPOSAL_REJECTED.getTemplateParams().get(1), arrivalPlaceNames.get(tni.getLocale()),

					PushTemplate.PROPOSAL_REJECTED.getTemplateParams().get(2),
					localDateTime.format(DateTimeFormatters.getDateTimeFormatter(tni.getLocale()))

			);

			PushMessage pushMessage = PushMessage.builder().to(tni.getToken()).template(PushTemplate.PROPOSAL_REJECTED)
					.params(pushTemplateParams).language(tni.getLocale().split("_")[0]).build();
			try {
				messagingPort.sendPushMessage(pushMessage);
			} catch (IllegalArgumentException e) {
				log.error("Exception sending PushMessage:", e);
				return false;
			}
		}

		return true;
	}

}
