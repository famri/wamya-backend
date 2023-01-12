package com.excentria_it.wamya.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
public class UserAccount {

	private Long id;

	private String oauthId;

	private Boolean isTransporter;

	private Long genderId;

	private String firstname;

	private String lastname;

	private LocalDate dateOfBirth;

	private String email;

	private String emailValidationCode;

	private Boolean isValidatedEmail;

	private MobilePhoneNumber mobilePhoneNumber;

	private String mobileNumberValidationCode;

	private Boolean isValidatedMobileNumber;

	private Boolean receiveNewsletter;

	private ZonedDateTime creationDateTime;

	private String photoUrl;

	private Double globalRating;

	private Map<String, String> preferences;

	private String deviceRegistrationToken;
	
	private Boolean isWebSocketConnected;

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class MobilePhoneNumber {

		String internationalCallingCode;

		String mobileNumber;

		public String toCallable() {

			StringBuilder strBuilder = new StringBuilder();
			if (this.internationalCallingCode != null) {
				strBuilder.append(this.internationalCallingCode.trim().replace(" ", "").replace("+", "00"));
			}
			if (this.mobileNumber != null) {
				strBuilder.append(mobileNumber);
			}
			return strBuilder.toString();
		}

		@Override
		public String toString() {
			return this.internationalCallingCode + this.mobileNumber;
		}

	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class UserGender {

		Long id;

		String name;
	}

}
