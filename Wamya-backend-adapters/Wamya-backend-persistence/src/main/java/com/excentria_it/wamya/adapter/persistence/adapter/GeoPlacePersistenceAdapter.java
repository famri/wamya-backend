package com.excentria_it.wamya.adapter.persistence.adapter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.GeoPlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.application.port.out.CreateFavoriteGeoPlacePort;
import com.excentria_it.wamya.application.port.out.LoadFavoriteGeoPlacePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.exception.DepartmentNotFoundException;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.GeoPlaceDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@PersistenceAdapter
@Slf4j
public class GeoPlacePersistenceAdapter implements LoadFavoriteGeoPlacePort, CreateFavoriteGeoPlacePort {

	private final GeoPlaceRepository geoPlaceRepository;

	private final DepartmentRepository departmentRepository;

	private final ClientRepository clientRepository;

	private final GeoPlaceMapper geoPlaceMapper;

	@Override
	public Set<GeoPlaceDto> loadFavoriteGeoPlaces(String username, String locale) {

		Set<GeoPlaceJpaEntity> geoPlaces;

		if (username.contains("@")) {
			geoPlaces = geoPlaceRepository.findByClient_Email(username);
		} else if (username.contains("_")) {

			String[] userMobilePhone = username.split("_");

			geoPlaces = geoPlaceRepository.findByClient_Icc_ValueAndClient_MobileNumber(userMobilePhone[0],
					userMobilePhone[1]);
		} else {
			log.error(String.format("Could not find client by email or mobile number: %s", username));
			return Collections.emptySet();
		}

		return geoPlaceMapper.mapToDomainEntities(geoPlaces, locale);
	}

	@Override
	public GeoPlaceDto createFavoriteGeoPlace(String name, BigDecimal latitude, BigDecimal longitude, Long departmentId,
			String username, String locale) {
		Optional<DepartmentJpaEntity> department = departmentRepository.findById(departmentId);
		if (department.isEmpty()) {
			throw new DepartmentNotFoundException(String.format("Unable to find department by ID: %d", departmentId));
		}

		Optional<ClientJpaEntity> client = Optional.empty();
		if (username.contains("@")) {
			client = clientRepository.findByEmail(username);
		} else if (username.contains("_")) {

			String[] userMobilePhone = username.split("_");

			client = clientRepository.findByIcc_ValueAndMobileNumber(userMobilePhone[0], userMobilePhone[1]);
		} else {
			throw new UserAccountNotFoundException(String.format("User account not found by username: %s", username));
		}

		if (client.isEmpty()) {
			throw new UserAccountNotFoundException(String.format("User account not found by username: %s", username));
		}

		GeoPlaceJpaEntity geoPlace = geoPlaceRepository
				.save(new GeoPlaceJpaEntity(name, latitude, longitude, department.get(), client.get()));

		return geoPlaceMapper.mapToDomainEntity(geoPlace, locale);
	}

}
