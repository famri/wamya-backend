package com.excentria_it.wamya.domain;

import java.time.ZoneId;

import lombok.Data;

@Data

public class TransporterNotificationInfo {
	private String token;
	private ZoneId zoneId;
	private String locale;

	public TransporterNotificationInfo(String token, String zoneName, String locale) {
		super();
		this.token = token;
		this.zoneId = ZoneId.of(zoneName);
		this.locale = locale;
	}

}
