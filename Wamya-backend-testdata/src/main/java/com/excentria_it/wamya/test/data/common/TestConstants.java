package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.common.domain.EmailTemplate;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Map;

public class TestConstants {
    public static final String DEFAULT_RAW_PASSWORD = "Abcd432$";
    public static final String DEFAULT_ENCODED_PASSWORD = "$2y$12$tbMLQdPhSlKJrVdscpDe9Oe7BTN3IsMyRHbZghQs84IkrIxJQSHF.";
    public static final String DEFAULT_VALIDATION_CODE = "1234";
    public static final String DEFAULT_VALIDATION_UUID = "cbbb422d-4b41-4697-aa16-5fba0c8a665c";
    public static final String DEFAULT_ACCESS_TOKEN = "SOME_ACCESS_TOKEN";
    public static final String DEFAULT_INTERNATIONAL_CALLING_CODE = "+33";
    public static final String DEFAULT_COUNTRY_NAME = "FRANCE";
    public static final String DEFAULT_MOBILE_NUMBER = "0711111111";
    public static final String DEFAULT_FLAG_PATH = "assets/images/icons/tunisia.png";
    public static final String DEFAULT_MOBILE_NUMBER_USERNAME = "+33_0711111111";
    public static final String DEFAULT_CALLABLE_MOBILE_NUMBER = "0033711111111";

    private static DateFormat DateFormatter = new SimpleDateFormat("yyyy/MM/dd");

    public static final String DEFAULT_FIRSTNAME = "User Firstname";

    public static final String DEFAULT_LASTNAME = "USER LASTNAME";

    public static final String DEFAULT_EMAIL = "user-email@gmail.com";
    public static final String DEFAULT_NEW_EMAIL = "user-email2@hotmail.com";

    public static final String DEFAULT_FROM_EMAIL = "wamya-team@wamya.com";

    public static final String DEFAULT_TEMPLATE_LANGUAGE = "fr";

    public static final Map<String, String> DEFAULT_TEMPLATE_PARAMS = Map.of("Param1Name", "Param1Value");

    public static Map<String, String> DEFAULT_TEMPLATE_ATTACHEMENTS;

    public static final EmailTemplate DEFAULT_EMAIL_TEMPLATE = EmailTemplate.EMAIL_VALIDATION;

    public static final String DEFAULT_EMAIL_SUBJECT = "EMAIL DEFAULT SUBJECT";

    public static final String DEFAULT_TEMPLATE_BODY = "<h1>This is an email template test</h1>";

    public static LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.of(1988, 10, 17);

    public static final String DEFAULT_MINIBIO = "This is a wonderful biography!";

    static {

        String attachementName = "dummy_email_attachement.txt";
        ClassLoader classLoader = EmailMessageTestData.class.getClassLoader();

        DEFAULT_TEMPLATE_ATTACHEMENTS = Map.of("File_1",
                new File(classLoader.getResource(attachementName).getFile()).getAbsolutePath());

    }

}
