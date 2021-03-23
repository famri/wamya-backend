package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "message")
@NoArgsConstructor
@SequenceGenerator(name = MessageJpaEntity.MESSAGE_SEQ)
public class MessageJpaEntity {
	public static final String MESSAGE_SEQ = "message_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = MESSAGE_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private UserAccountJpaEntity author;

	private Boolean read;

	@Column(length = 2048)
	private String content;

	private Instant dateTime;

	public MessageJpaEntity(UserAccountJpaEntity author, Boolean read, String content, Instant dateTime) {
		super();
		this.author = author;
		this.read = read;
		this.content = content;
		this.dateTime = dateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserAccountJpaEntity getAuthor() {
		return author;
	}

	public void setAuthor(UserAccountJpaEntity author) {
		this.author = author;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Instant getDateTime() {
		return dateTime;
	}

	public void setDateTime(Instant dateTime) {
		this.dateTime = dateTime;
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
		MessageJpaEntity other = (MessageJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageJpaEntity [id=" + id + ", author=" + author + ", read=" + read + ", content=" + content
				+ ", dateTime=" + dateTime + "]";
	}

}
