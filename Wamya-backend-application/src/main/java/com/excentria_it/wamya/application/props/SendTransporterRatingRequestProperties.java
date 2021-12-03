package com.excentria_it.wamya.application.props;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.NoArgsConstructor;


@ConfigurationProperties(prefix = "app.rating.request")
@Validated
@NoArgsConstructor
@Data
public class SendTransporterRatingRequestProperties {
	@NotNull
	private Integer revivals;


}
