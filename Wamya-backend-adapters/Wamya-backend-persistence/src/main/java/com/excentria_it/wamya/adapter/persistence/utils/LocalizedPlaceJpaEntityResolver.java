package com.excentria_it.wamya.adapter.persistence.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedLocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.domain.PlaceType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocalizedPlaceJpaEntityResolver {
	private final LocalityRepository localityRepository;
	private final DelegationRepository delegationRepository;
	private final DepartmentRepository departementRepository;
	private final GeoPlaceRepository geoPlaceRepository;

	public List<LocalizedPlaceJpaEntity> resolveLocalizedPlaces(Long placeId, PlaceType placeType) {

		List<LocalizedPlaceJpaEntity> result = new ArrayList<>();
		switch (placeType) {
		case DEPARTMENT:
			Optional<DepartmentJpaEntity> department = departementRepository.findById(placeId);
			if (department.isPresent()) {

				Map<String, LocalizedDepartmentJpaEntity> departmentLocalizations = department.get().getLocalizations();
				departmentLocalizations.forEach((k, v) -> {
					LocalizedPlaceJpaEntity lje = new LocalizedPlaceJpaEntity();
					lje.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(placeId, placeType), k));
					lje.setName(v.getName());
					result.add(lje);
				});
			}
			return result;

		case DELEGATION:
			Optional<DelegationJpaEntity> delegation = delegationRepository.findById(placeId);
			if (delegation.isPresent()) {

				Map<String, LocalizedDelegationJpaEntity> delegationLocalizations = delegation.get().getLocalizations();
				DepartmentJpaEntity delegationDepartment = delegation.get().getDepartment();
				if (delegationDepartment != null) {
					delegationLocalizations.forEach((k, v) -> {
						LocalizedPlaceJpaEntity lje = new LocalizedPlaceJpaEntity();
						lje.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(placeId, placeType), k));
						lje.setName(v.getName() + ", " + delegationDepartment.getName(k));
						result.add(lje);
					});
				}
			}
			return result;

		case LOCALITY:
			Optional<LocalityJpaEntity> locality = localityRepository.findById(placeId);
			if (locality.isPresent()) {

				Map<String, LocalizedLocalityJpaEntity> localityLocalizations = locality.get().getLocalizations();
				DelegationJpaEntity localityDelegation = locality.get().getDelegation();
				if (localityDelegation != null) {
					DepartmentJpaEntity localityDepartment = localityDelegation.getDepartment();
					if (localityDepartment != null) {
						localityLocalizations.forEach((k, v) -> {
							LocalizedPlaceJpaEntity lje = new LocalizedPlaceJpaEntity();
							lje.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(placeId, placeType), k));
							lje.setName(v.getName() + ", " + localityDelegation.getName(k) + ", "
									+ localityDepartment.getName(k));
							result.add(lje);
						});
					}
				}
			}
			return result;
		case GEO_PLACE:
			Optional<GeoPlaceJpaEntity> geoPlace = geoPlaceRepository.findById(placeId);

			if (geoPlace.isPresent()) {
				DepartmentJpaEntity geoPlaceDepartment = geoPlace.get().getDepartment();
				if (geoPlaceDepartment != null) {
					Map<String, LocalizedDepartmentJpaEntity> geoPlaceDepartmentLocalizations = geoPlaceDepartment
							.getLocalizations();
					geoPlaceDepartmentLocalizations.forEach((k, v) -> {
						LocalizedPlaceJpaEntity lje = new LocalizedPlaceJpaEntity();
						lje.setLocalizedPlaceId(new LocalizedPlaceId(new PlaceId(placeId, placeType), k));
						lje.setName(geoPlace.get().getName() + ", " + v.getName());
						result.add(lje);
					});
				}
			}
			return result;
		default:
			return result;
		}

	}
}
