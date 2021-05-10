package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedGenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.domain.Gender;

public class GenderJpaTestData {
	public static GenderJpaEntity defaultGenderJpaEntity() {
		GenderJpaEntity gender = GenderJpaEntity.builder().id(1L).gender(Gender.MAN).build();

		LocalizedGenderJpaEntity localizedGenderEn = new LocalizedGenderJpaEntity();
		localizedGenderEn.setLocalizedId(new LocalizedId("en_US"));

		localizedGenderEn.setName("MAN");

		gender.getLocalizations().put("en_US", localizedGenderEn);
		localizedGenderEn.setGender(gender);

		LocalizedGenderJpaEntity localizedGenderFr = new LocalizedGenderJpaEntity();
		localizedGenderFr.setLocalizedId(new LocalizedId("fr_FR"));

		localizedGenderFr.setName("HOMME");

		gender.getLocalizations().put("fr_FR", localizedGenderFr);
		localizedGenderFr.setGender(gender);
		
		return gender;
	}
}
