package com.excentria_it.wamya.application.aspects;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.excentria_it.wamya.application.ApplicationTestConfiguration;
import com.excentria_it.wamya.application.port.out.CheckDocumentEntitlementsPort;
import com.excentria_it.wamya.application.port.out.LoadDocumentInfoPort;
import com.excentria_it.wamya.application.port.out.LoadFilePort;

@Configuration
@Import(ApplicationTestConfiguration.class)
public class DocumentAccessAspectTestsConfiguration {
	@Bean
	public CheckDocumentEntitlementsPort getCheckDocumentEntitlementsPort() {
		return Mockito.mock(CheckDocumentEntitlementsPort.class);
	}

	@Bean
	public LoadDocumentInfoPort getLoadDocumentInfoPort() {
		return Mockito.mock(LoadDocumentInfoPort.class);
	}

	@Bean
	public LoadFilePort getLoadFilePort() {
		return Mockito.mock(LoadFilePort.class);
	}

}
