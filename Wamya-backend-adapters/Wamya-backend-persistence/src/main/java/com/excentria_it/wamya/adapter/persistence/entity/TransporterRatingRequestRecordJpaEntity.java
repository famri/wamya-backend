package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.TransporterRatingRequestStatus;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "transporter_rating_details")
@NoArgsConstructor

@SequenceGenerator(name = TransporterRatingRequestRecordJpaEntity.RATING_DETAILS_SEQ, initialValue = 1, allocationSize = 5)
public class TransporterRatingRequestRecordJpaEntity {

	public static final String RATING_DETAILS_SEQ = "rating_details_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = RATING_DETAILS_SEQ)
	private Long id;

	@OneToOne
	private JourneyRequestJpaEntity jourenyRequest;

	@ManyToOne
	private TransporterJpaEntity transporter;

	@ManyToOne
	private ClientJpaEntity client;

	private String hash;

	private Integer revivals;

	@Enumerated(EnumType.STRING)
	private TransporterRatingRequestStatus status;

	public TransporterRatingRequestRecordJpaEntity(Long id, JourneyRequestJpaEntity jourenyRequest,
			TransporterJpaEntity transporter, ClientJpaEntity client, String hash, Integer revivals,
			TransporterRatingRequestStatus status) {
		super();
		this.id = id;
		this.jourenyRequest = jourenyRequest;
		this.transporter = transporter;
		this.client = client;
		this.hash = hash;
		this.revivals = revivals;
		this.status = status;
	}

	public TransporterRatingRequestRecordJpaEntity(JourneyRequestJpaEntity jourenyRequest,
			TransporterJpaEntity transporter, ClientJpaEntity client, String hash, Integer revivals,
			TransporterRatingRequestStatus status) {
		super();

		this.jourenyRequest = jourenyRequest;
		this.transporter = transporter;
		this.client = client;
		this.hash = hash;
		this.revivals = revivals;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JourneyRequestJpaEntity getJourenyRequest() {
		return jourenyRequest;
	}

	public void setJourenyRequest(JourneyRequestJpaEntity jourenyRequest) {
		this.jourenyRequest = jourenyRequest;
	}

	public TransporterJpaEntity getTransporter() {
		return transporter;
	}

	public void setTransporter(TransporterJpaEntity transporter) {
		this.transporter = transporter;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public ClientJpaEntity getClient() {
		return client;
	}

	public void setClient(ClientJpaEntity client) {
		this.client = client;
	}

	public Integer getRevivals() {
		return revivals;
	}

	public void setRevivals(Integer revivals) {
		this.revivals = revivals;
	}

	public TransporterRatingRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TransporterRatingRequestStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TransporterRatingRequestJpaEntity [id=" + id + ", jourenyRequest=" + jourenyRequest + ", transporter="
				+ transporter + ", client=" + client + ", hash=" + hash + ", revivals=" + revivals + ", status="
				+ status + "]";
	}

}
