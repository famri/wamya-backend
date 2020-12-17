package com.excentria_it.wamya.adapter.web;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = { WebConfiguration.class, TestSecurityConfig.class })
public class WebTestConfiguration {

}
