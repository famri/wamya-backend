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
		try {
			final Object loginType = BeanUtils.getProperty(value, loginTypeFieldName);
			final Object loginValue = BeanUtils.getProperty(value, loginValueFieldName);
			if (loginType instanceof LoginType) {
				if (LoginType.EMAIL.equals(loginType)) {
					if (loginValue instanceof String) {

						Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(loginValue.toString());
						return emailMatcher.matches();

					} else {
						return false;
					}
				} else if (LoginType.PHONE_NUMBER.equals(loginType)) {
					if (loginValue instanceof String) {

						Matcher phoneMatcher = VALID_PHONE_NUMBER_REGEX.matcher(loginValue.toString());
						return phoneMatcher.matches();

					} else {
						return false;
					}
				} else {
					return false;
				}

			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
