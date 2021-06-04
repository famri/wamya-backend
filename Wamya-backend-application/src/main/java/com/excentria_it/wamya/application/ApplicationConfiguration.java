package com.excentria_it.wamya.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
public class ApplicationConfiguration {

}
