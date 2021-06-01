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
import javax.persistence.OneToOne;
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

	@Enumerated(EnumType.STRING)
	private EngineTypeCode code;

	@OneToOne
	protected DocumentJpaEntity image;

	private Integer rank;

	@Builder.Default
	@OneToMany(mappedBy = "engineType", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedEngineTypeJpaEntity> localizations = new HashMap<>();

	public Long getId() {
		return id;
	}

	public EngineTypeCode getCode() {
		return code;
	}

	public void setCode(EngineTypeCode code) {
		this.code = code;
	}

	public Map<String, LocalizedEngineTypeJpaEntity> getLocalizations() {
		return localizations;
	}

	public String getName(String locale) {
		if (localizations.containsKey(locale)) {
			return localizations.get(locale).getName();
		}
		return null;
	}

	public String getDescription(String locale) {
		return localizations.get(locale).getDescription();
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public DocumentJpaEntity getImage() {
		return image;
	}

	public void setImage(DocumentJpaEntity image) {
		this.image = image;
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

	public enum EngineTypeCode {
		UTILITY, PICKUP, BUS, MINIBUS, VAN_L1H1, VAN_L2H2, VAN_L3H3, VAN_L4H3, FLATBED_TRUCK, BOX_TRUCK,
		REFRIGERATED_TRUCK, TANKER, DUMP_TRUCK, HOOK_LIFT_TRUCK, TANK_TRANSPORTER;
	}

}
