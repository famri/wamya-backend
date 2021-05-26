package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.DocumentType;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "document")
@NoArgsConstructor
@SequenceGenerator(name = DocumentJpaEntity.DOCUMENT_SEQ)
public class DocumentJpaEntity {
	public static final String DOCUMENT_SEQ = "document_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DOCUMENT_SEQ)
	private Long id;

	@ManyToOne
	private UserAccountJpaEntity owner;

	private String location;

	@Enumerated(EnumType.STRING)
	private DocumentType type;

	private Instant creationDateTime;

//	@OneToMany(cascade = CascadeType.ALL)
//	private Set<EntitlementJpaEntity> entitlements;

	private String hash;

	private Boolean isDefault;

	public DocumentJpaEntity(UserAccountJpaEntity owner, String location, DocumentType type, Instant creationDateTime,
			// Set<EntitlementJpaEntity> entitlements,
			String hash, Boolean isDefault) {
		super();
		this.owner = owner;
		this.location = location;
		this.type = type;
		this.creationDateTime = creationDateTime;
		// this.entitlements = entitlements;
		this.hash = hash;
		this.isDefault = isDefault;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserAccountJpaEntity getOwner() {
		return owner;
	}

	public void setOwner(UserAccountJpaEntity owner) {
		this.owner = owner;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

	public Instant getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Instant creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

//	public Set<EntitlementJpaEntity> getEntitlements() {
//		return entitlements;
//	}
//
//	public void setEntitlements(Set<EntitlementJpaEntity> entitlements) {
//		this.entitlements = entitlements;
//	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
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
		DocumentJpaEntity other = (DocumentJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DocumentJpaEntity [id=" + id + ", owner=" + owner + ", location=" + location + ", type=" + type
				+ ", creationDateTime=" + creationDateTime + ", hash=" + hash + ", isDefault=" + isDefault + "]";
	}

}
