package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "discussion")
@NoArgsConstructor
@SequenceGenerator(name = DiscussionJpaEntity.DISCUSSION_SEQ)
public class DiscussionJpaEntity {
	public static final String DISCUSSION_SEQ = "discussion_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DISCUSSION_SEQ)
	private Long id;

	@ManyToOne
	private ClientJpaEntity client;

	@ManyToOne
	private TransporterJpaEntity transporter;

	private Boolean active;

	private Instant dateTime;

	@OneToOne
	private MessageJpaEntity latestMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ClientJpaEntity getClient() {
		return client;
	}

	public void setClient(ClientJpaEntity client) {
		this.client = client;
	}

	public TransporterJpaEntity getTransporter() {
		return transporter;
	}

	public void setTransporter(TransporterJpaEntity transporter) {
		this.transporter = transporter;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Instant getDateTime() {
		return dateTime;
	}

	public void setDateTime(Instant dateTime) {
		this.dateTime = dateTime;
	}

	public MessageJpaEntity getLatestMessage() {
		return latestMessage;
	}

	public void setLatestMessage(MessageJpaEntity latestMessage) {
		this.latestMessage = latestMessage;
	}

	public DiscussionJpaEntity(ClientJpaEntity client, TransporterJpaEntity transporter, Boolean active,
			Instant dateTime, MessageJpaEntity latestMessage) {
		super();
		this.client = client;
		this.transporter = transporter;
		this.active = active;
		this.dateTime = dateTime;
		this.latestMessage = latestMessage;
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
		DiscussionJpaEntity other = (DiscussionJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DiscussionJpaEntity [id=" + id + ", client=" + client + ", transporter=" + transporter + ", active="
				+ active + ", dateTime=" + dateTime + ", latestMessage=" + latestMessage + "]";
	}

}
