package com.excentria_it.wamya.test.data.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.excentria_it.wamya.adapter.persistence.entity.CountryJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedCountryJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.TimeZoneJpaEntity;

public class CountryJpaTestData {

	public static List<CountryJpaEntity> defaultCountryJpaEntities() {

		InternationalCallingCodeJpaEntity icc1 = new InternationalCallingCodeJpaEntity(1L, "+216", true);
		TimeZoneJpaEntity tz1 = new TimeZoneJpaEntity("Africa/Tunis", "GMT+01:00");
		tz1.setId(1L);
		List<TimeZoneJpaEntity> timeZones1 = List.of(tz1);

		DepartmentJpaEntity d1 = new DepartmentJpaEntity();
		d1.setId(1L);
		d1.setLatitude(new BigDecimal(34.8888));
		d1.setLongitude(new BigDecimal(10.8888));
		d1.setPossibleNames("Sfax");

		DepartmentJpaEntity d2 = new DepartmentJpaEntity();
		d2.setId(2L);
		d2.setLatitude(new BigDecimal(36.8888));
		d2.setLongitude(new BigDecimal(10.8888));
		d2.setPossibleNames("Tunis");

		CountryJpaEntity c1 = new CountryJpaEntity();
		c1.setId(1L);
		c1.setCode("TN");
		c1.setFlagPath("/content/icons/tunisia-flag-icon-32.png");
		c1.setIcc(icc1);
		c1.setTimeZones(timeZones1);
		c1.addDepartment(d1);
		c1.addDepartment(d2);

		LocalizedCountryJpaEntity l1 = new LocalizedCountryJpaEntity(new LocalizedId(1L, "fr_FR"), "Tunisie", c1);
		c1.getLocalizations().put("fr_FR", l1);

		InternationalCallingCodeJpaEntity icc2 = new InternationalCallingCodeJpaEntity(2L, "+33", false);
		TimeZoneJpaEntity tz2 = new TimeZoneJpaEntity("Europe/Paris", "GMT+01:00");
		tz2.setId(2L);
		List<TimeZoneJpaEntity> timeZones2 = List.of(tz2);

		DepartmentJpaEntity d3 = new DepartmentJpaEntity();
		d3.setId(3L);
		d3.setLatitude(new BigDecimal(35.8888));
		d3.setLongitude(new BigDecimal(11.8888));
		d3.setPossibleNames("Val d'Oise");

		DepartmentJpaEntity d4 = new DepartmentJpaEntity();
		d4.setId(4L);
		d4.setLatitude(new BigDecimal(37.8888));
		d4.setLongitude(new BigDecimal(12.8888));
		d4.setPossibleNames("Paris");

		CountryJpaEntity c2 = new CountryJpaEntity();
		c2.setId(2L);
		c2.setCode("FR");
		c2.setFlagPath("/content/icons/france-flag-icon-32.png");
		c2.setIcc(icc2);
		c2.setTimeZones(timeZones2);
		c2.addDepartment(d3);
		c2.addDepartment(d4);

		LocalizedCountryJpaEntity l2 = new LocalizedCountryJpaEntity(new LocalizedId(2L, "fr_FR"), "France", c2);
		c2.getLocalizations().put("fr_FR", l2);

		List<CountryJpaEntity> result = new ArrayList<>(2);
		result.add(c1);
		result.add(c2);

		return result;
	}

	public static List<DepartmentJpaEntity> defaultCountryDepartmentsJpaEntities() {
		DepartmentJpaEntity d1 = new DepartmentJpaEntity();
		d1.setId(1L);
		d1.setLatitude(new BigDecimal(34.8888));
		d1.setLongitude(new BigDecimal(10.8888));
		d1.setPossibleNames("Sfax");

		DepartmentJpaEntity d2 = new DepartmentJpaEntity();
		d2.setId(2L);
		d2.setLatitude(new BigDecimal(36.8888));
		d2.setLongitude(new BigDecimal(10.8888));
		d2.setPossibleNames("Tunis");

		DepartmentJpaEntity d3 = new DepartmentJpaEntity();
		d3.setId(3L);
		d3.setLatitude(new BigDecimal(35.8888));
		d3.setLongitude(new BigDecimal(11.8888));
		d3.setPossibleNames("Val d'Oise");

		DepartmentJpaEntity d4 = new DepartmentJpaEntity();
		d4.setId(4L);
		d4.setLatitude(new BigDecimal(37.8888));
		d4.setLongitude(new BigDecimal(12.8888));
		d4.setPossibleNames("Paris");

		return List.of(d1, d2, d3, d4);
	}

	public static List<CountryJpaEntity> defaultCountryJpaEntitiesWithoutDepartments() {

		InternationalCallingCodeJpaEntity icc1 = new InternationalCallingCodeJpaEntity(1L, "+216", true);
		TimeZoneJpaEntity tz1 = new TimeZoneJpaEntity("Africa/Tunis", "GMT+01:00");
		tz1.setId(1L);
		List<TimeZoneJpaEntity> timeZones1 = List.of(tz1);

		CountryJpaEntity c1 = new CountryJpaEntity();
		c1.setId(1L);
		c1.setCode("TN");
		c1.setFlagPath("/content/icons/tunisia-flag-icon-32.png");
		c1.setIcc(icc1);
		c1.setTimeZones(timeZones1);
	

		LocalizedCountryJpaEntity l1 = new LocalizedCountryJpaEntity(new LocalizedId(1L, "fr_FR"), "Tunisie", c1);
		c1.getLocalizations().put("fr_FR", l1);

		InternationalCallingCodeJpaEntity icc2 = new InternationalCallingCodeJpaEntity(2L, "+33", false);
		TimeZoneJpaEntity tz2 = new TimeZoneJpaEntity("Europe/Paris", "GMT+01:00");
		tz2.setId(2L);
		List<TimeZoneJpaEntity> timeZones2 = List.of(tz2);

		CountryJpaEntity c2 = new CountryJpaEntity();
		c2.setId(2L);
		c2.setCode("FR");
		c2.setFlagPath("/content/icons/france-flag-icon-32.png");
		c2.setIcc(icc2);
		c2.setTimeZones(timeZones2);
	

		LocalizedCountryJpaEntity l2 = new LocalizedCountryJpaEntity(new LocalizedId(2L, "fr_FR"), "France", c2);
		c2.getLocalizations().put("fr_FR", l2);

		List<CountryJpaEntity> result = new ArrayList<>(2);
		result.add(c1);
		result.add(c2);

		return result;
	}

	public static CountryJpaEntity defaultCountryJpaEntity() {

		InternationalCallingCodeJpaEntity icc1 = new InternationalCallingCodeJpaEntity(1L, "+216", true);
		TimeZoneJpaEntity tz1 = new TimeZoneJpaEntity("Africa/Tunis", "GMT+01:00");
		tz1.setId(1L);
		List<TimeZoneJpaEntity> timeZones1 = List.of(tz1);

		DepartmentJpaEntity d1 = new DepartmentJpaEntity();
		d1.setId(1L);
		d1.setLatitude(new BigDecimal(34.8888));
		d1.setLongitude(new BigDecimal(10.8888));
		d1.setPossibleNames("Sfax");

		DepartmentJpaEntity d2 = new DepartmentJpaEntity();
		d2.setId(2L);
		d2.setLatitude(new BigDecimal(36.8888));
		d2.setLongitude(new BigDecimal(10.8888));
		d2.setPossibleNames("Tunis");

		CountryJpaEntity c1 = new CountryJpaEntity();
		c1.setId(1L);
		c1.setCode("TN");
		c1.setFlagPath("/content/icons/tunisia-flag-icon-32.png");
		c1.setIcc(icc1);
		c1.setTimeZones(timeZones1);
		c1.addDepartment(d1);
		c1.addDepartment(d2);

		LocalizedCountryJpaEntity l1 = new LocalizedCountryJpaEntity(new LocalizedId(1L, "fr_FR"), "Tunisie", c1);
		c1.getLocalizations().put("fr_FR", l1);

		return c1;

	}
}
