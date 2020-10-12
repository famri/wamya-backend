package com.excentria_it.wamya.test.data.common;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.domain.EmailSubject;

public class TestConstants {
	public static final String DEFAULT_RAW_PASSWORD = "NeDD93816M7F1IlM4nZ3";
	public static final String DEFAULT_ENCODED_PASSWORD = "NeDD93816M7F1IlM4nZ3YNAqXX6xSxPHinRJ72qvNx1omoa30zLBFsxMQwYxk28uY0fa6C46XFx76rGEKK6BriWBE8aS7bfKP0";
	public static final String DEFAULT_VALIDATION_CODE = "1234";
	public static final String DEFAULT_INTERNATIONAL_CALLING_CODE = "+216";
	public static final String DEFAULT_MOBILE_NUMBER = "99999999";
	public static final String DEFAULT_CALLABLE_MOBILE_NUMBER = "0021699999999";
	private static DateFormat DateFormatter = new SimpleDateFormat("yyyy/MM/dd");

	public static final String DEFAULT_FIRSTNAME = "User Firstname";

	public static final String DEFAULT_LASTNAME = "USER LASTNAME";

	public static final String DEFAULT_EMAIL = "user-email@gmail.com";

	public static final String DEFAULT_FROM_EMAIL = "wamya-team@wamya.com";

	public static final String DEFAULT_TEMPLATE_LANGUAGE = "fr";

	public static final Map<String, String> DEFAULT_TEMPLATE_PARAMS = Map.of("Param1Name", "Param1Value");

	public static Map<String, String> DEFAULT_TEMPLATE_ATTACHEMENTS;

	public static final EmailTemplate DEFAULT_EMAIL_TEMPLATE = EmailTemplate.EMAIL_VALIDATION;

	public static final String DEFAULT_EMAIL_SUBJECT ="EMAIL DEFAULT SUBJECT" ;
	
	public static final String DEFAULT_TEMPLATE_BODY = "<h1>This is an email template test</h1>";

	public static Date DEFAULT_DATE_OF_BIRTH;

	static {

		try {
			DEFAULT_DATE_OF_BIRTH = DateFormatter.parse("1988/10/17");
		} catch (ParseException e) {
			DEFAULT_DATE_OF_BIRTH = new Date();
		}

		String attachementName = "dummy_email_attachement.txt";
		ClassLoader classLoader = EmailMessageTestData.class.getClassLoader();

		DEFAULT_TEMPLATE_ATTACHEMENTS = Map.of("File_1",
				new File(classLoader.getResource(attachementName).getFile()).getAbsolutePath());
		
		

	}

}
