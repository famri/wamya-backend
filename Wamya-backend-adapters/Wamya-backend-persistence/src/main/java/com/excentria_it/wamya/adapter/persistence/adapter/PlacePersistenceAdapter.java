package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.adapter.persistence.repository.PlaceRepository;
import com.excentria_it.wamya.application.port.out.LoadPlaceGeoCoordinatesPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceNamesPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.PlaceType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class PlacePersistenceAdapter implements LoadPlaceGeoCoordinatesPort, LoadPlaceNamesPort {
	private final LocalityRepository localityRepository;
	private final DelegationRepository delegationRepository;
	private final DepartmentRepository departmentRepository;
	private final GeoPlaceRepository geoPlaceRepository;
	private final PlaceRepository placeRepository;

	@Override
	public Optional<GeoCoordinates> loadPlaceGeoCoordinates(Long placeId, PlaceType placeType) {
		switch (placeType) {
		case LOCALITY:
			Optional<LocalityJpaEntity> optionalLocality = localityRepository.findById(placeId);
			if (optionalLocality.isPresent()) {
				return Optional.of(new GeoCoordinates(optionalLocality.get().getLatitude(),
						optionalLocality.get().getLongitude()));
			}
			return Optional.empty();
		case DELEGATION:
			Optional<DelegationJpaEntity> optionalDelegation = delegationRepository.findById(placeId);
			if (optionalDelegation.isPresent()) {
				return Optional.of(new GeoCoordinates(optionalDelegation.get().getLatitude(),
						optionalDelegation.get().getLongitude()));
			}
			return Optional.empty();
		case DEPARTMENT:
			Optional<DepartmentJpaEntity> optionalDepartment = departmentRepository.findById(placeId);
			if (optionalDepartment.isPresent()) {
				return Optional.of(new GeoCoordinates(optionalDepartment.get().getLatitude(),
						optionalDepartment.get().getLongitude()));
			}
			return Optional.empty();
		case GEO_PLACE:
			Optional<GeoPlaceJpaEntity> optionalPlace = geoPlaceRepository.findById(placeId);
			if (optionalPlace.isPresent()) {
				return Optional
						.of(new GeoCoordinates(optionalPlace.get().getLatitude(), optionalPlace.get().getLongitude()));
			}
			return Optional.empty();
		default:
			return Optional.empty();
		}

	}

	@Override
	public Map<String, String> loadPlaceNames(Long placeId, PlaceType placeType, Set<String> locales) {

		Map<String, String> placesNamesByLocale = new HashMap<>();
		for (String locale : locales) {

			String placeName = placeRepository.findNameByLocale(placeId, placeType, locale);
			placesNamesByLocale.put(locale, placeName);

		}
		return placesNamesByLocale;
	}

}
