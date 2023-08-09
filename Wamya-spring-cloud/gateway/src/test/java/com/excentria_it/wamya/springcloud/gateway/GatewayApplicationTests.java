package com.excentria_it.wamya.springcloud.gateway;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = { "eureka.client.enabled=false" })
public class GatewayApplicationTests {

	@Test
	public void contextLoads() {
	}

}
