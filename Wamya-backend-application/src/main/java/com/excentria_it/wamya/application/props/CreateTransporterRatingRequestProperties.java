package com.excentria_it.wamya.application.props;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "app.rating.record")
@Validated
@Data
public class CreateTransporterRatingRequestProperties {
	@NotNull
	private Integer limit;

}
