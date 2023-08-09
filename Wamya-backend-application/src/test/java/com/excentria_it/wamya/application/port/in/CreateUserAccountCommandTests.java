package com.excentria_it.wamya.application.port.in;

import com.excentria_it.wamya.domain.RegexPattern;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserAccountCommandTests {
    // Test password pattern
    @Test
    void testValidPasswordPattern() {
        Pattern pattern = Pattern.compile(RegexPattern.USER_PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher("1A9@#$%^&+=b");
        assertTrue(matcher.matches());
    }

    @Test
    void testInvalidPasswordPatternBecauseSpace() {
        Pattern pattern = Pattern.compile(RegexPattern.USER_PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher("Space Password");
        assertFalse(matcher.matches());
    }

    @Test
    void testInvalidPasswordPatternBecauseSpecialChar() {
        Pattern pattern = Pattern.compile(RegexPattern.USER_PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher("SpecialChar <");
        assertFalse(matcher.matches());
    }

    @Test
    void testInvalidPasswordPatternBecauseNotLongEnough() {
        Pattern pattern = Pattern.compile(RegexPattern.USER_PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher("Under8");
        assertFalse(matcher.matches());
    }

    @Test
    void testInvalidPasswordPatternBecauseTooLong() {
        Pattern pattern = Pattern.compile(RegexPattern.USER_PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher("Above20chars_Password");
        assertFalse(matcher.matches());
    }

    // Test mobile number pattern

    @Test
    void testValidMobileNumberFrance() {
        Pattern pattern = Pattern.compile(RegexPattern.MOBILE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher("0762072850");
        assertTrue(matcher.matches());
    }

    @Test
    void testValidMobileNumberTunisia() {
        Pattern pattern = Pattern.compile(RegexPattern.MOBILE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher("0711111111");
        assertTrue(matcher.matches());
    }

    @Test
    void testInvalidMobileNumberTunisia() {
        Pattern pattern = Pattern.compile(RegexPattern.MOBILE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher("2309341");
        assertFalse(matcher.matches());
    }

    // Test international calling code pattern

    @Test
    void testValidIccFrance() {
        Pattern pattern = Pattern.compile(RegexPattern.ICC_PATTERN);
        Matcher matcher = pattern.matcher("+33");
        assertTrue(matcher.matches());
    }

    @Test
    void testInvalidIccFrance() {
        Pattern pattern = Pattern.compile(RegexPattern.ICC_PATTERN);
        Matcher matcher = pattern.matcher("0033");
        assertFalse(matcher.matches());
    }

    @Test
    void testValidIccTunisia() {
        Pattern pattern = Pattern.compile(RegexPattern.ICC_PATTERN);
        Matcher matcher = pattern.matcher("+216");
        assertTrue(matcher.matches());
    }

    @Test
    void testInvalidIccTunisia() {
        Pattern pattern = Pattern.compile(RegexPattern.ICC_PATTERN);
        Matcher matcher = pattern.matcher("00216");
        assertFalse(matcher.matches());
    }

    // Test email pattern

    @Test
    void testValidEmail() {
        Pattern pattern = Pattern.compile(RegexPattern.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher("test.test@test.com");
        assertTrue(matcher.matches());
    }

    @Test
    void testInvalidEmailBecauseSpace() {
        Pattern pattern = Pattern.compile(RegexPattern.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher("test test@test.com");
        assertFalse(matcher.matches());
    }

    @Test
    void testInvalidEmailBecauseNotLongEnoughDomainExtension() {
        Pattern pattern = Pattern.compile(RegexPattern.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher("test_test@test.c");
        assertFalse(matcher.matches());
    }

    @Test
    void testInvalidEmailBecauseTooLongDomainExtension() {
        Pattern pattern = Pattern.compile(RegexPattern.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher("test_test@test.abcdefgh");
        assertFalse(matcher.matches());
    }

    @Test
    void testInvalidEmailBecauseDigitInDomainExtension() {
        Pattern pattern = Pattern.compile(RegexPattern.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher("test_test@test.123com");
        assertFalse(matcher.matches());
    }


}
