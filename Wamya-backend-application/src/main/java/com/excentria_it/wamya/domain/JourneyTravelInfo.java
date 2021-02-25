package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = JourneyTravelInfo.Builder.class)
public class JourneyTravelInfo {
	private Integer distance;
	private Integer hours;
	private Integer minutes;

	@JsonPOJOBuilder
	static class Builder {
		Integer distance;
		Integer hours;
		Integer minutes;

		Builder withDistance(Integer distance) {
			this.distance = distance;
			return this;
		}

		Builder withHours(Integer hours) {
			this.hours = hours;
			return this;
		}

		Builder withMinutes(Integer minutes) {
			this.minutes = minutes;
			return this;
		}

		public JourneyTravelInfo build() {
			return new JourneyTravelInfo(distance, hours, minutes);
		}
	}
}
