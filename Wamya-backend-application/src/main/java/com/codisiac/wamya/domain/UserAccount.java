package com.codisiac.wamya.domain;

import javax.validation.constraints.Pattern;

import com.codisiac.wamya.common.annotation.FieldMatch;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAccount {

	@Getter
	private final UserAccountId id;

	@Getter
	private final MobilePhoneNumber mobilePhoneNumber;

	@Getter
	private final UserPassword userPassword;

	@Getter
	private final ValidationCode validationCode;

	@Getter
	private final boolean validated;

	/**
	 * Creates an {@link UserAccount} entity without an ID. Use to create a new
	 * entity that is not yet persisted.
	 */
	public static UserAccount withoutId(MobilePhoneNumber mobilePhoneNumber, UserPassword userPassword,
			ValidationCode validationCode) {
		return new UserAccount(null, mobilePhoneNumber, userPassword, validationCode, false);
	}

	/**
	 * Creates an {@link UserAccount} entity with an ID. Use to re-constitute a
	 * persisted entity.
	 */
	public static UserAccount withId(UserAccountId userAccountId, MobilePhoneNumber mobilePhoneNumber,
			UserPassword userPassword, ValidationCode validationCode, boolean validated) {
		return new UserAccount(userAccountId, mobilePhoneNumber, userPassword, validationCode, validated);
	}

	@Value
	public static class MobilePhoneNumber {
		@Pattern(regexp = "\\A\\+[0-9]{2,3}\\z", message = "{com.codisiac.wamya.domain.MobilePhoneNumber.internationalCallingCode.message}")
		String internationalCallingCode;

		@Pattern(regexp = "\\A[0-9]{8,10}\\z", message = "{com.codisiac.wamya.domain.MobilePhoneNumber.mobileNumber.message}")
		String mobileNumber;
	}

	@Value
	@FieldMatch.List({
			@FieldMatch(first = "password", second = "passwordConfirmation", message = "{com.codisiac.wamya.domain.UserPasswordPair.message}") })
	public static class UserPasswordPair {
		// @Pattern(regexp = "((?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20})")
		@Pattern(regexp = "\\A(?=[^a-z]*[a-z])(?=[^A-Z]*[A-Z])(?=\\D*\\d)\\w{8,20}\\z")
		String password;

		String passwordConfirmation;
	}

	@Value
	public static class UserPassword {

		String encodedPassword;

	}

	@Value
	public static class UserAccountId {
		private Long value;
	}
}
