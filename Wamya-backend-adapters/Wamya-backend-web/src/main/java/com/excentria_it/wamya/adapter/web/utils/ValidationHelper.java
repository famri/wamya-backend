package com.excentria_it.wamya.adapter.web.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidationHelper {
	
	private final LocalValidatorFactoryBean localValidatorFactoryBean;

	public void validateInput(Object command) {
		Set<ConstraintViolation<Object>> errors = localValidatorFactoryBean.getValidator().validate(command);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
	}
}
