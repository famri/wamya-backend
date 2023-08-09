package com.excentria_it.wamya;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = { "eureka.client.enabled=false" })
@ActiveProfiles(profiles = "local")
class WamyaApplicationTests {

	@Test
	void contextLoads() {

	}

}
