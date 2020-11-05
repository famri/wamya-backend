package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

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

	private UUID oauthUuid;
	
	private Boolean isTransporter;

	private Gender gender;

	private String firstName;

	private String lastName;

	private Date dateOfBirth;

	private String email;

	private String emailValidationCode;

	private Boolean isValidatedEmail;

	private MobilePhoneNumber mobilePhoneNumber;

	private String mobileNumberValidationCode;

	private Boolean isValidatedMobileNumber;

	private String userPassword;

	private Boolean receiveNewsletter;

	private LocalDateTime creationTimestamp;

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

}
