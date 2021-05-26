package com.excentria_it.wamya.adapter.file.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.excentria_it.wamya.adapter.file.storage.props.DockerFileStorageProperties;
import com.excentria_it.wamya.adapter.file.storage.props.LocalFileStorageProperties;

@Configuration
@ComponentScan
public class FileStorageConfiguration {
	@Bean
	@Profile("file-storage-local")
	public LocalFileStorageProperties getLocalFileStorageProperties() {
		return new LocalFileStorageProperties();
	}

	@Bean
	@Profile("file-storage-docker")
	public DockerFileStorageProperties getDockerFileStorageProperties() {
		return new DockerFileStorageProperties();
	}
}
