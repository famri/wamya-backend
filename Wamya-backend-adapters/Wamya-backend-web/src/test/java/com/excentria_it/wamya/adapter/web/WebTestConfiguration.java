package com.excentria_it.wamya.adapter.web;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.JwtTestConf;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = { WebConfiguration.class, WebSecurityConfiguration.class
		//, JwtTestConf.class 
		})
public class WebTestConfiguration {

}
