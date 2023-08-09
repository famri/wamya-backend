package com.excentria_it.wamya.adapter.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.integralblue.log4jdbc.spring.Log4jdbcAutoConfiguration;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(PersistenceConfiguration.class)
@ImportAutoConfiguration(Log4jdbcAutoConfiguration.class)
public class PersistenceTestConfiguration {

	@Test
	void contextLoads() {

	}

}
