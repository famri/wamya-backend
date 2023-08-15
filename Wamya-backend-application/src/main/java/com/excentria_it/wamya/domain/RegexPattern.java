package com.excentria_it.wamya.domain;

import com.excentria_it.wamya.common.annotation.Generated;

@Generated
public class RegexPattern {

    public static final String USER_PASSWORD_PATTERN = "^[a-zA-Z0-9@#$%^&+=!?]{8,20}$";
    public static final String FRENCH_MOBILE_NUMBER_PATTERN = "\\A(07|06)[0-9]{8}\\z";
    public static final String ICC_PATTERN = "^\\+33$";
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    public static final String USERNAME_PATTERN = EMAIL_PATTERN;

}
