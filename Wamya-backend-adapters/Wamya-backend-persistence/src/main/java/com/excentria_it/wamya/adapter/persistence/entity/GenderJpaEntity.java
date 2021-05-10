package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "gender")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = GenderJpaEntity.GENDER_SEQ, initialValue = 1, allocationSize = 5)
public class GenderJpaEntity {

	public static final String GENDER_SEQ = "gender_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENDER_SEQ)
	private Long id;

	@OneToMany(mappedBy = "gender", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	@Builder.Default
	private Map<String, LocalizedGenderJpaEntity> localizations = new HashMap<>();

	@Enumerated(EnumType.STRING)
	private Gender gender;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Map<String, LocalizedGenderJpaEntity> getLocalizations() {
		return localizations;
	}

	public String getName(String locale) {
		if (localizations.containsKey(locale)) {
			return localizations.get(locale).getName();
		}
		return null;
	}

}
