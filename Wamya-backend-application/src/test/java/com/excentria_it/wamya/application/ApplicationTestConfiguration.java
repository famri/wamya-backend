package com.excentria_it.wamya.application;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = { ApplicationConfiguration.class })
public class ApplicationTestConfiguration {

}
