package com.excentria_it.wamya.application.task;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.text.StrSubstitutor;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;

import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.port.out.UpdateTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.props.SendTransporterRatingRequestProperties;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.domain.EmailSender;
import com.excentria_it.wamya.domain.EmailSubject;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SendTransporterRatingRequestTask {

	private final LoadTransporterRatingRequestRecordPort loadTransporterRatingRequestRecordPort;
	private final UpdateTransporterRatingRequestRecordPort updateTransporterRatingRequestRecordPort;
	private final AsynchronousMessagingPort asynchronousMessagingPort;

	private final ServerUrlProperties serverUrlProperties;
	private final DateTimeHelper dateTimeHelper;
	private final MessageSource messageSource;
	private final SendTransporterRatingRequestProperties sendTransporterRatingRequestProperties;
	private static final String RATING_URL_TEMPLATE = "${protocol}://${host}:${port}/rating-details?h=${hash}&uid=${uid}";

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT,
			FormatStyle.SHORT);

	@Scheduled(cron = "${app.rating.request.sending-cron-expression}")
	public void sendTransporterRatingRequest() {

		Set<TransporterRatingRequestRecordOutput> records = loadTransporterRatingRequestRecordPort
				.loadUnfulfilledRecords(sendTransporterRatingRequestProperties.getRevivals());
		Set<Long> sentRecordsIds = new HashSet<>();

		records.forEach(r -> {
			String receiverLocale = r.getClient().getLocale();
			String[] receiverLanguageAndCountry = receiverLocale.split("_");
			Locale clientLocale = new Locale(receiverLanguageAndCountry[0], receiverLanguageAndCountry[1]);
			String ratingLink = patchURL(RATING_URL_TEMPLATE, serverUrlProperties.getProtocol(),
					serverUrlProperties.getHost(), serverUrlProperties.getPort(), r.getHash(), r.getClient().getId());

			Map<String, String> emailTemplateParams = Map
					.of(EmailTemplate.RATING_REQUEST.getTemplateParams().get(0), r.getClient().getFirstname(),
							EmailTemplate.RATING_REQUEST.getTemplateParams().get(1), ratingLink,
							EmailTemplate.RATING_REQUEST.getTemplateParams().get(2), r.getTransporter().getFirstname(),
							EmailTemplate.RATING_REQUEST.getTemplateParams().get(3), r.getTransporter().getPhotoUrl(),
							EmailTemplate.RATING_REQUEST.getTemplateParams().get(4),
							r.getJourneyRequest().getDeparturePlace().getName(),
							EmailTemplate.RATING_REQUEST.getTemplateParams().get(5),
							r.getJourneyRequest().getArrivalPlace().getName(),
							EmailTemplate.RATING_REQUEST.getTemplateParams().get(6),
							dateTimeHelper
									.systemToUserLocalDateTime(r.getJourneyRequest().getDateTime(),
											ZoneId.of(r.getClient().getTimeZone()))
									.format(formatter.withLocale(clientLocale)));

			EmailMessage emailMessage = EmailMessage.builder().from(EmailSender.FRETTO_TEAM)
					.to(r.getClient().getEmail())
					.subject(messageSource.getMessage(EmailSubject.RATING_REQUEST, null, clientLocale))
					.template(EmailTemplate.RATING_REQUEST).params(emailTemplateParams).language(receiverLocale)
					.build();

			asynchronousMessagingPort.sendEmailMessage(emailMessage);

			sentRecordsIds.add(r.getId());
		});

		if (!sentRecordsIds.isEmpty()) {
			updateTransporterRatingRequestRecordPort.incrementRevivals(sentRecordsIds);
		}

	}

	protected String patchURL(String url, String protocol, String host, String port, String hash, Long userId) {

		Map<String, String> data = new HashMap<>();
		data.put("protocol", protocol);
		data.put("host", host);
		data.put("port", port);
		data.put("uid", userId.toString());
		data.put("hash", hash);

		String ratingRequestUrl = StrSubstitutor.replace(url, data);

		return ratingRequestUrl;
	}
}