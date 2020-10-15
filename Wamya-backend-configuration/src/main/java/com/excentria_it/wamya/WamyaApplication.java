package com.excentria_it.wamya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.excentria_it.wamya.application.props.CodeGeneratorProperties;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.common.annotation.Generated;

@SpringBootApplication
@EnableConfigurationProperties(value = { CodeGeneratorProperties.class, ServerUrlProperties.class })
@Generated
public class WamyaApplication {

	public static void main(String[] args) {

		SpringApplication.run(WamyaApplication.class, args);
	}

}
