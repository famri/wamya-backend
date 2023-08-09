package com.excentria_it.wamya.adapter.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.domain.JourneyProposalStatusCode;
import com.excentria_it.wamya.domain.TransporterNotificationInfo;

public interface JourneyProposalRepository extends JpaRepository<JourneyProposalJpaEntity, Long> {

	List<JourneyProposalJpaEntity> findByJourneyRequest_Id(Long journeyRequestId, Sort sort);

	List<JourneyProposalJpaEntity> findByJourneyRequest_IdAndStatus_CodeIn(Long journeyRequestId,
			List<JourneyProposalStatusCode> statusCodes, Sort sort);

	Optional<JourneyProposalJpaEntity> findByIdAndJourneyRequest_Id(Long proposalId, Long journeyRequestId);

	@Query(value = "SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM JourneyProposalJpaEntity p WHERE p.id = ?1 AND p.journeyRequest.id = ?2 AND p.status.code = ?3")
	boolean existsByIdAndJourneyRequestIdAndStatusCode(Long id, Long journeyRequestId,
			JourneyProposalStatusCode statusCode);

	@Query(value = "SELECT new com.excentria_it.wamya.domain.TransporterNotificationInfo(t.deviceRegistrationToken, VALUE(pr1).value, VALUE(pr2).value) FROM JourneyProposalJpaEntity p JOIN p.transporter t JOIN t.preferences pr1 JOIN t.preferences pr2 WHERE p.journeyRequest.id = ?1 AND KEY(pr1) = 'timezone' AND KEY(pr2) = 'locale'")
	Set<TransporterNotificationInfo> loadTransportersNotificationInfo(Long journeyRequestId);

	@Query(value = "SELECT p FROM JourneyProposalJpaEntity p JOIN p.transporter t JOIN p.journeyRequest jr JOIN p.status s JOIN s.localizations sl WHERE t.email = :email AND jr.dateTime >= :startDate AND jr.dateTime <= :endDate AND s.code IN :statusCodes AND KEY(sl) = :locale")
	Page<JourneyProposalJpaEntity> findByTransporter_EmailAndJourneyDateTimeBetweenAndProposal_Status_Code(
			@Param(value = "startDate") Instant startDate, @Param(value = "endDate") Instant endDate,
			@Param(value = "email") String transporterUsername,
			@Param(value = "statusCodes") Set<JourneyProposalStatusCode> statusCodes,
			@Param(value = "locale") String locale, Pageable pagingSort);
}
