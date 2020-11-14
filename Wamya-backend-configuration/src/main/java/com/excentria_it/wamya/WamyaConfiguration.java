package com.excentria_it.wamya;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.excentria_it.wamya.adapter.b2b.rest.B2bRestConfiguration;
import com.excentria_it.wamya.adapter.messaging.MessagingConfiguration;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.application.ApplicationConfiguration;

@Configuration
@Import(value = { ApplicationConfiguration.class, WebConfiguration.class, B2bRestConfiguration.class,
		MessagingConfiguration.class, })
public class WamyaConfiguration {

}
