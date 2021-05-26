package com.excentria_it.wamya.adapter.file.storage.props;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "app.file-strorage.docker")
@Validated
@Data
public class DockerFileStorageProperties {
	@NotEmpty
	@NotNull
	private String rootFolder;
}
