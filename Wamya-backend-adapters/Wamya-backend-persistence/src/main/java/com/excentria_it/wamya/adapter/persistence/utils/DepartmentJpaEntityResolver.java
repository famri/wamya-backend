package com.excentria_it.wamya.adapter.persistence.utils;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.domain.PlaceType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DepartmentJpaEntityResolver {
	private final LocalityRepository localityRepository;
	private final DelegationRepository delegationRepository;
	private final DepartmentRepository departementRepository;
	private final GeoPlaceRepository geoPlaceRepository;

	public Optional<DepartmentJpaEntity> resolveDepartment(Long placeId, PlaceType placeType) {

		switch (placeType) {
		case DEPARTMENT:

			return departementRepository.findById(placeId);

		case DELEGATION:
			Optional<DelegationJpaEntity> delegation = delegationRepository.findById(placeId);
			if (delegation.isPresent()) {
				return Optional.of(delegation.get().getDepartment());
			}
			return Optional.empty();

		case LOCALITY:
			Optional<LocalityJpaEntity> locality = localityRepository.findById(placeId);
			if (locality.isPresent()) {
				DelegationJpaEntity localityDelegation = locality.get().getDelegation();
				if (localityDelegation != null) {
					return Optional.of(localityDelegation.getDepartment());
				}
				return Optional.empty();
			}

			return Optional.empty();
		case GEO_PLACE:
			Optional<GeoPlaceJpaEntity> geoPlace = geoPlaceRepository.findById(placeId);
			if (geoPlace.isPresent()) {
				return Optional.of(geoPlace.get().getDepartment());
			}
		default:
			return Optional.empty();
		}

	}
}
