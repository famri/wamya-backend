package com.excentria_it.wamya.common.validator.impl;

import com.excentria_it.wamya.common.annotation.Among;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class AmongValidator implements ConstraintValidator<Among, String> {

    private Set<String> validValues;

    @Override
    public void initialize(Among amongAnnotation) {
        validValues = Set.of(amongAnnotation.value());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return value != null && validValues.contains(value);
    }

}
