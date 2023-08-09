package com.excentria_it.wamya.domain;

import com.excentria_it.wamya.common.annotation.Generated;

@Generated
public class RegexPattern {

	public static final String USER_PASSWORD_PATTERN = "^[0-9a-zA-Z@#$%^&+=(?=\\\\S+$)]{8,20}$";
	public static final String MOBILE_NUMBER_PATTERN = "\\A[0-9]{9}|[0-9]{10}\\z";
	public static final String ICC_PATTERN = "\\A\\+[0-9]{2,3}\\z";
	public static final String EMAIL_PATTERN = "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
	public static final String MOBILE_USERNAME_PATTERN = "\\A\\+[0-9]{3}_[0-9]{8}\\z|\\A\\+[0-9]{2}_[0-9]{10}\\z";
	public static final String USERNAME_PATTERN = EMAIL_PATTERN + "|" + MOBILE_USERNAME_PATTERN;

}
