package com.excentria_it.wamya.adapter.messaging;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({ MessagingConfiguration.class, WebSocketConfiguration.class, WebSocketSecurityConfiguration.class })
public class MessagingTestConfiguration {

}
