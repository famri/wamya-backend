package com.excentria_it.wamya.test.data.common;

import java.time.Instant;
import java.time.ZoneId;

import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto.ClientDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto.JourneyRequestDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto.PlaceDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto.TransporterDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput.JourneyRequestOutput;

public class TransporterRatingRequestTestData {

	public static final TransporterRatingRequestRecordDto defaultTransporterRatingRequestRecordDto() {

		JourneyRequestDto jrDto = new JourneyRequestDto(new PlaceDto("Sfax"), new PlaceDto("Tunis"),
				Instant.now().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime());

		TransporterDto trDto = new TransporterDto("Transporter1", "http://some/photo/url", 4.5);
		ClientDto cDto = new ClientDto(1L, "Client1");
		return new TransporterRatingRequestRecordDto(jrDto, trDto, cDto, "SOME_HASH");

	}

	public static final TransporterRatingRequestRecordOutput defaultTransporterRatingRequestRecordOutput() {

		JourneyRequestOutput jro = new JourneyRequestOutput(new TransporterRatingRequestRecordOutput.PlaceDto("Sfax"),
				new TransporterRatingRequestRecordOutput.PlaceDto("Tunis"), Instant.now());

		TransporterRatingRequestRecordOutput.TransporterDto trDto = new TransporterRatingRequestRecordOutput.TransporterDto(
				1L, "Transporter1", "http://some/photo/url", 4.5);

		TransporterRatingRequestRecordOutput.ClientDto cDto = new TransporterRatingRequestRecordOutput.ClientDto(1L,
				"Client1", "client1@gmail.com", "fr_FR", "Africa/Tunis");

		return new TransporterRatingRequestRecordOutput(1L, jro, trDto, cDto, "SOME_HASH");

	}

}
