package com.excentria_it.wamya.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileInfoDto {

	private GenderInfo gender;

	private NameInfo name;

	private Boolean isTransporter;

	private String photoUrl;

	private LocalDate dateOfBirth;

	private String miniBio;

	private EmailInfo email;

	private MobileInfo mobile;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class GenderInfo {

		private Long id;

		private String value;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class NameInfo {

		private String firstname;

		private String lastname;

		private ValidationInfo validationInfo;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class ValidationInfo {

		private String state;

		private Boolean isValidated;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class EmailInfo {

		private String value;

		private Boolean checked;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class MobileInfo {

		private String value;

		private IccInfo icc;

		private Boolean checked;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class IccInfo {

		private Long id;
		private String value;

	}
}