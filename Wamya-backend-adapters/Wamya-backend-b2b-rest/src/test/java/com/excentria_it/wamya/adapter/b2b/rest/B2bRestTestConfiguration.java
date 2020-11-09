package com.excentria_it.wamya.adapter.b2b.rest;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.excentria_it.wamya.adapter.b2b.rest.props.B2bRestConfigurationProperties;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = { B2bRestConfiguration.class, B2bRestConfigurationProperties.class })
public class B2bRestTestConfiguration {

}
