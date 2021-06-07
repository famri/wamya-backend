package com.excentria_it.wamya.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.excentria_it.wamya.common.annotation.Generated;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@Generated
public class ApplicationConfiguration {

}
