package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadCountriesDto.Builder.class)
public class LoadCountriesDto {

	private Long id;

	private String name;

	private String code;

	private String flagPath;

	private IccDto icc;

	private List<TimeZoneDto> timeZones;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		String name;

		String code;

		String flagPath;

		IccDto icc;

		List<TimeZoneDto> timeZones;

		Builder withId(Long id) {
			this.id = id;
			return this;
		}

		Builder withName(String name) {
			this.name = name;
			return this;
		}

		Builder withCode(String code) {
			this.code = code;
			return this;
		}

		Builder withFlagPath(String flagPath) {
			this.flagPath = flagPath;
			return this;
		}

		Builder withIcc(IccDto icc) {
			this.icc = icc;
			return this;
		}

		Builder withTimeZones(List<TimeZoneDto> timeZones) {
			this.timeZones = timeZones;
			return this;
		}

		public LoadCountriesDto build() {
			return new LoadCountriesDto(id, name, code, flagPath, icc, timeZones);
		}
	}

	@Value
	@JsonDeserialize(builder = LoadCountriesDto.IccDto.Builder.class)
	public static class IccDto {
		private Long id;
		private String value;

		@JsonPOJOBuilder
		static class Builder {
			Long id;

			String value;

			Builder withId(Long id) {
				this.id = id;
				return this;
			}

			Builder withValue(String value) {
				this.value = value;
				return this;
			}

			public IccDto build() {
				return new IccDto(id, value);
			}
		}
	}

	@Value
	@JsonDeserialize(builder = LoadCountriesDto.TimeZoneDto.Builder.class)
	public static class TimeZoneDto {
		Long id;

		String name;

		String gmtOffset;

		@JsonPOJOBuilder
		static class Builder {
			private Long id;

			private String name;

			private String gmtOffset;

			Builder withId(Long id) {
				this.id = id;
				return this;
			}

			Builder withName(String name) {
				this.name = name;
				return this;
			}

			Builder withGmtOffset(String gmtOffset) {
				this.gmtOffset = gmtOffset;
				return this;
			}

			public TimeZoneDto build() {
				return new TimeZoneDto(id, name, gmtOffset);
			}
		}

	}
}