package com.excentria_it.wamya.common.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

import com.excentria_it.wamya.common.LoginType;
import com.excentria_it.wamya.common.annotation.ValidUserLogin;

public class UserLoginValidator implements ConstraintValidator<ValidUserLogin, Object> {

	private String loginTypeFieldName;
	private String loginValueFieldName;

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
	public static final Pattern VALID_PHONE_NUMBER_REGEX = Pattern
			.compile("\\A\\+([0-9]{2}_[0-9]{9}|[0-9]{3}_[0-9]{8})\\z");

	@Override
	public void initialize(final ValidUserLogin constraintAnnotation) {
		loginTypeFieldName = constraintAnnotation.loginTypeField();
		loginValueFieldName = constraintAnnotation.loginValueField();

	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		String loginTypeStr = null, loginValue = null;

		try {
			loginTypeStr = BeanUtils.getProperty(value, loginTypeFieldName);
			loginValue = BeanUtils.getProperty(value, loginValueFieldName);
		} catch (Exception e) {
			return false;
		}

		LoginType loginTypeEnum;
		try {
			loginTypeEnum = LoginType.valueOf(loginTypeStr);
			if (LoginType.EMAIL.equals(loginTypeEnum)) {

				Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(loginValue.toString());
				return emailMatcher.matches();

			} else if (LoginType.PHONE_NUMBER.equals(loginTypeEnum)) {

				Matcher phoneMatcher = VALID_PHONE_NUMBER_REGEX.matcher(loginValue.toString());
				return phoneMatcher.matches();

			}
		} catch (IllegalArgumentException e) {
			return false;
		}
		return false;

	}

}
