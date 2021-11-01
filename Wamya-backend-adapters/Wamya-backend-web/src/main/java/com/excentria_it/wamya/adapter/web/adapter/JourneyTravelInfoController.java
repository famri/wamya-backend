package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadJourneyTravelInfoUseCase;
import com.excentria_it.wamya.application.utils.PlaceUtils;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/travel-info", produces = MediaType.APPLICATION_JSON_VALUE)
public class JourneyTravelInfoController {

	private final LoadJourneyTravelInfoUseCase loadJourneyTravelInfoUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public JourneyTravelInfo loadJourneyTravelInfo(@NotNull @RequestParam(name = "departure-id") Long departurePlaceId,
			@NotEmpty @RequestParam(name = "departure-type") String departurePlaceType,
			@NotNull @RequestParam(name = "arrival-id") Long arrivalPlaceId,
			@NotEmpty @RequestParam(name = "arrival-type") String arrivalPlaceType) {

		PlaceType departureType = PlaceUtils.placeTypeStringToEnum(departurePlaceType);
		PlaceType arrivalType = PlaceUtils.placeTypeStringToEnum(arrivalPlaceType);

		Optional<JourneyTravelInfo> journeyTravelInfo = loadJourneyTravelInfoUseCase.loadTravelInfo(departurePlaceId,
				departureType, arrivalPlaceId, arrivalType);

		return journeyTravelInfo.orElse(null);
	}
}
