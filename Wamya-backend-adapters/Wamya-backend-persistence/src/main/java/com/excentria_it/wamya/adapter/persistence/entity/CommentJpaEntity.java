package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Generated
@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = CommentJpaEntity.COMMENT_SEQ, initialValue = 1, allocationSize = 5)
public class CommentJpaEntity {

	public static final String COMMENT_SEQ = "comment_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = COMMENT_SEQ)
	private Long id;

	private LocalDateTime creationDateTime;

	@Column(length = 500)
	private String content;

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
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
		CommentJpaEntity other = (CommentJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CommentJpaEntity [id=" + id + ", creationDateTime=" + creationDateTime + ", content=" + content + "]";
	}

}
