package com.excentria_it.wamya.common;

import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.ValidationCode;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;
import com.excentria_it.wamya.domain.UserAccount.UserAccountId;
import com.excentria_it.wamya.domain.UserAccount.UserPassword;
import com.excentria_it.wamya.domain.UserAccount.UserPasswordPair;

public class UserAccountTestData {
	public static final String DEFAULT_RAW_PASSWORD = "NeDD93816M7F1IlM4nZ3";
	public static final String DEFAULT_ENCODED_PASSWORD = "NeDD93816M7F1IlM4nZ3YNAqXX6xSxPHinRJ72qvNx1omoa30zLBFsxMQwYxk28uY0fa6C46XFx76rGEKK6BriWBE8aS7bfKP0";
	public static final String DEFAULT_VALIDATION_CODE = "1234";
	public static final String DEFAULT_INTERNATIONAL_CALLING_CODE = "+216";
	public static final String DEFAULT_MOBILE_NUMBER = "99999999";

	public static UserAccountBuilder notYetValidatedUserAccount() {
		return new UserAccountBuilder().withUserAccountId(new UserAccountId(1L))
				.withMobilePhoneNumber(defaultMobilePhoneNumber().build())
				.withUserPassword(defaultUserPasswordBuilder().build())
				.withValidationCode(new ValidationCode(DEFAULT_VALIDATION_CODE)).withValidated(false);
	}

	public static UserAccountBuilder alreadyValidatedUserAccount() {
		return new UserAccountBuilder().withUserAccountId(new UserAccountId(1L))
				.withMobilePhoneNumber(new MobilePhoneNumber(DEFAULT_INTERNATIONAL_CALLING_CODE, DEFAULT_MOBILE_NUMBER))
				.withUserPassword(new UserPassword(DEFAULT_ENCODED_PASSWORD))
				.withValidationCode(new ValidationCode(DEFAULT_VALIDATION_CODE)).withValidated(true);
	}

	public static MobilePhoneNumberBuilder defaultMobilePhoneNumber() {
		return new MobilePhoneNumberBuilder().withInternationalCallingCode(DEFAULT_INTERNATIONAL_CALLING_CODE)
				.withMobileNumber(DEFAULT_MOBILE_NUMBER);

	}

	public static UserPasswordBuilder defaultUserPasswordBuilder() {
		return new UserPasswordBuilder().withPassword(DEFAULT_ENCODED_PASSWORD);

	}

	public static UserPasswordPairBuilder defaultUserPasswordPairBuilder() {
		String password = DEFAULT_RAW_PASSWORD;
		return new UserPasswordPairBuilder().withPassword(password).withPasswordConfirmation(password);
	}

	public static class UserPasswordPairBuilder {
		private String password;
		private String passwordConfirmation;

		public UserPasswordPairBuilder withPassword(String password) {
			this.password = password;
			return this;
		}

		public UserPasswordPairBuilder withPasswordConfirmation(String passwordConfirmation) {
			this.passwordConfirmation = passwordConfirmation;
			return this;
		}

		public UserPasswordPair build() {
			return new UserPasswordPair(this.password, this.passwordConfirmation);
		}
	}

	public static class MobilePhoneNumberBuilder {

		private String internationalCallingCode;
		private String mobileNumber;

		public MobilePhoneNumberBuilder withInternationalCallingCode(String internationalCallingCode) {
			this.internationalCallingCode = internationalCallingCode;
			return this;
		}

		public MobilePhoneNumberBuilder withMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
			return this;
		}

		public MobilePhoneNumber build() {
			return new MobilePhoneNumber(this.internationalCallingCode, this.mobileNumber);
		}
	}

	public static class UserPasswordBuilder {

		private String password;

		public UserPasswordBuilder withPassword(String password) {
			this.password = password;
			return this;
		}

		public UserPassword build() {
			return new UserPassword(this.password);
		}
	}

	public static class UserAccountBuilder {

		private UserAccountId id;

		private MobilePhoneNumber mobilePhoneNumber;

		private UserPassword userPassword;

		private ValidationCode validationCode;

		private boolean validated;

		public UserAccountBuilder withUserAccountId(UserAccountId userAccountId) {
			this.id = userAccountId;
			return this;
		}

		public UserAccountBuilder withMobilePhoneNumber(MobilePhoneNumber mobilePhoneNumber) {
			this.mobilePhoneNumber = mobilePhoneNumber;
			return this;
		}

		public UserAccountBuilder withUserPassword(UserPassword userPassword) {
			this.userPassword = userPassword;
			return this;
		}

		public UserAccountBuilder withValidationCode(ValidationCode validationCode) {
			this.validationCode = validationCode;
			return this;
		}

		public UserAccountBuilder withValidated(boolean validated) {
			this.validated = validated;
			return this;
		}

		public UserAccount build() {
			return UserAccount.withId(this.id, this.mobilePhoneNumber, this.userPassword, this.validationCode,
					this.validated);
		}

	}

}
