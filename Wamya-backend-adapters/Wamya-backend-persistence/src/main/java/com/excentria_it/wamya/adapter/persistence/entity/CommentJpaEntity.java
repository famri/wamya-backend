package com.excentria_it.wamya.adapter.persistence.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = CommentJpaEntity.COMMENT_SEQ, initialValue = 1, allocationSize = 5)
public class CommentJpaEntity {

	public static final String COMMENT_SEQ = "comment_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = COMMENT_SEQ)
	private Long id;

	private Timestamp creationDate;

	@Column(length = 500)
	private String content;
}
