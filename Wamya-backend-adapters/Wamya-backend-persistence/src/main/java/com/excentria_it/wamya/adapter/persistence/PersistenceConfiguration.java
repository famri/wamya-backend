package com.excentria_it.wamya.adapter.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class PersistenceConfiguration {
//	public class TimestampToZonedDateTimeConverter implements Converter<Timestamp, Instant> {
//		@Override
//		public Instant convert(Timestamp timestatmp) {
//			return timestatmp.toInstant();
//
//		}
//	}

//	public class InstantToZonedDateTimeConverter implements Converter<Instant, ZonedDateTime> {
//		@Override
//		public ZonedDateTime convert(Instant instant) {
//			return instant.atZone(ZoneOffset.UTC);
//
//		}
//	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void config() {
//		DefaultConversionService conversionService = (DefaultConversionService) DefaultConversionService
//				.getSharedInstance();
//		conversionService.addConverter(new TimestampToZonedDateTimeConverter());
//	//	conversionService.addConverter(new InstantToZonedDateTimeConverter());
//	}
}
