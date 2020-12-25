package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "engine_type")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = EngineTypeJpaEntity.ENGINE_TYPE_SEQ, initialValue = 1, allocationSize = 5)
public class EngineTypeJpaEntity {

	public static final String ENGINE_TYPE_SEQ = "engine_type_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ENGINE_TYPE_SEQ)
	private Long id;

	private String code;

	@OneToMany(mappedBy = "engineType", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedEngineTypeJpaEntity> localizations = new HashMap<>();

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, LocalizedEngineTypeJpaEntity> getLocalizations() {
		return localizations;
	}

	public String getName(String locale) {
		return localizations.get(locale).getName();
	}

	public String getDescription(String locale) {
		return localizations.get(locale).getDescription();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		EngineTypeJpaEntity other = (EngineTypeJpaEntity) obj;

		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EngineTypeJpaEntity [id=" + id + ", code=" + code + "]";
	}

}
