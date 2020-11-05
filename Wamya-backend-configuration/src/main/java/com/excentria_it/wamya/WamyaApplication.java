package com.excentria_it.wamya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.excentria_it.wamya.common.annotation.Generated;

@SpringBootApplication
@Generated
public class WamyaApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(WamyaApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
