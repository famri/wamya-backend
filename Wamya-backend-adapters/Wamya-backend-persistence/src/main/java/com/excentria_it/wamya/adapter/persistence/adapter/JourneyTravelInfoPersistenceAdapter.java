package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToGeoPlaceTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToLocalityTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToLocalityTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationToDelegationTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationToDepartmentTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentToDepartmentTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceToDelegationTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceToDepartmentTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceToGeoPlaceTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceToLocalityTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityToDelegationTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityToDepartmentTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityToLocalityTravelInfoRepository;
import com.excentria_it.wamya.application.port.out.CreateTravelInfoPort;
import com.excentria_it.wamya.application.port.out.LoadTravelInfoPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class JourneyTravelInfoPersistenceAdapter implements LoadTravelInfoPort, CreateTravelInfoPort {

	private final LocalityToLocalityTravelInfoRepository localityToLocalityTravelInfoRepository;
	private final LocalityToDelegationTravelInfoRepository localityToDelegationTravelInfoRepository;
	private final LocalityToDepartmentTravelInfoRepository localityToDepartmentTravelInfoRepository;

	private final DelegationToDelegationTravelInfoRepository delegationToDelegationTravelInfoRepository;
	private final DelegationToDepartmentTravelInfoRepository delegationToDepartmentTravelInfoRepository;

	private final DepartmentToDepartmentTravelInfoRepository departmentToDepartmentTravelInfoRepository;

	private final GeoPlaceToGeoPlaceTravelInfoRepository geoPlaceToGeoPlaceTravelInfoRepository;

	private final GeoPlaceToDepartmentTravelInfoRepository geoPlaceToDepartmentTravelInfoRepository;

	private final GeoPlaceToDelegationTravelInfoRepository geoPlaceToDelegationTravelInfoRepository;

	private final GeoPlaceToLocalityTravelInfoRepository geoPlaceToLocalityTravelInfoRepository;

	private final LocalityRepository localityRepository;
	private final DelegationRepository delegationRepository;
	private final DepartmentRepository departmentRepository;
	private final GeoPlaceRepository geoPlaceRepository;

	@Override
	public Optional<JourneyTravelInfo> loadTravelInfo(Long departurePlaceId, PlaceType departureType,
			Long arrivalPlaceId, PlaceType arrivalType) {

		switch (departureType) {
		case LOCALITY:
			switch (arrivalType) {
			case LOCALITY:
				return localityToLocalityTravelInfoRepository.findByLocalityOne_IdAndLocalityTwo_Id(departurePlaceId,
						arrivalPlaceId);
			case DELEGATION:
				return localityToDelegationTravelInfoRepository.findByLocality_IdAndDelegation_Id(departurePlaceId,
						arrivalPlaceId);
			case DEPARTMENT:
				return localityToDepartmentTravelInfoRepository.findByLocality_IdAndDepartment_Id(departurePlaceId,
						arrivalPlaceId);
			case GEO_PLACE:
				return geoPlaceToLocalityTravelInfoRepository.findByGeoPlace_IdAndLocality_Id(arrivalPlaceId,
						departurePlaceId);
			default:
				return Optional.empty();
			}

		case DELEGATION:
			switch (arrivalType) {
			case LOCALITY:
				return localityToDelegationTravelInfoRepository.findByLocality_IdAndDelegation_Id(arrivalPlaceId,
						departurePlaceId);
			case DELEGATION:
				return delegationToDelegationTravelInfoRepository
						.findByDelegationOne_IdAndDelegationTwo_Id(departurePlaceId, arrivalPlaceId);
			case DEPARTMENT:
				return delegationToDepartmentTravelInfoRepository.findByDelegation_IdAndDepartment_Id(departurePlaceId,
						arrivalPlaceId);
			case GEO_PLACE:
				return geoPlaceToDelegationTravelInfoRepository.findByGeoPlace_IdAndDelegation_Id(arrivalPlaceId,
						departurePlaceId);
			default:
				return Optional.empty();
			}

		case DEPARTMENT:
			switch (arrivalType) {
			case LOCALITY:
				return localityToDepartmentTravelInfoRepository.findByLocality_IdAndDepartment_Id(arrivalPlaceId,
						departurePlaceId);
			case DELEGATION:
				return delegationToDepartmentTravelInfoRepository.findByDelegation_IdAndDepartment_Id(arrivalPlaceId,
						departurePlaceId);
			case DEPARTMENT:
				return departmentToDepartmentTravelInfoRepository
						.findByDepartmentOne_IdAndDepartmentTwo_Id(departurePlaceId, arrivalPlaceId);
			case GEO_PLACE:
				return geoPlaceToDepartmentTravelInfoRepository.findByGeoPlace_IdAndDepartment_Id(arrivalPlaceId,
						departurePlaceId);
			default:
				return Optional.empty();
			}
		case GEO_PLACE:
			switch (arrivalType) {
			case LOCALITY:
				return geoPlaceToLocalityTravelInfoRepository.findByGeoPlace_IdAndLocality_Id(departurePlaceId,
						arrivalPlaceId);
			case DELEGATION:
				return geoPlaceToDelegationTravelInfoRepository.findByGeoPlace_IdAndDelegation_Id(departurePlaceId,
						arrivalPlaceId);
			case DEPARTMENT:
				return geoPlaceToDepartmentTravelInfoRepository.findByGeoPlace_IdAndDepartment_Id(departurePlaceId,
						arrivalPlaceId);
			case GEO_PLACE:
				return geoPlaceToGeoPlaceTravelInfoRepository.findByGeoPlaceOne_IdAndGeoPlaceTwo_Id(departurePlaceId,
						arrivalPlaceId);
			default:
				return Optional.empty();
			}
		default:
			return Optional.empty();
		}

	}

	@Override
	public void createTravelInfo(Long departurePlaceId, PlaceType departureType, Long arrivalPlaceId,
			PlaceType arrivalType, JourneyTravelInfo journeyTravelInfo) {
		switch (departureType) {
		case LOCALITY:
			Optional<LocalityJpaEntity> localityOptional1 = localityRepository.findById(departurePlaceId);

			switch (arrivalType) {
			case LOCALITY:

				Optional<LocalityJpaEntity> localityOptional2;
				LocalityToLocalityTravelInfoJpaEntity l2lti;
				if (departurePlaceId == arrivalPlaceId) {

					localityOptional2 = localityOptional1;

				} else {

					localityOptional2 = localityRepository.findById(arrivalPlaceId);

				}

				l2lti = new LocalityToLocalityTravelInfoJpaEntity(localityOptional1.get(), localityOptional2.get(),
						journeyTravelInfo.getHours(), journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				localityToLocalityTravelInfoRepository.save(l2lti);
				break;
			case DELEGATION:

				Optional<DelegationJpaEntity> delegationOptional2 = delegationRepository.findById(arrivalPlaceId);

				LocalityToDelegationTravelInfoJpaEntity l2dlti = new LocalityToDelegationTravelInfoJpaEntity(
						localityOptional1.get(), delegationOptional2.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				localityToDelegationTravelInfoRepository.save(l2dlti);
				break;
			case DEPARTMENT:

				Optional<DepartmentJpaEntity> departmentOptional2 = departmentRepository.findById(arrivalPlaceId);

				LocalityToDepartmentTravelInfoJpaEntity l2dpti = new LocalityToDepartmentTravelInfoJpaEntity(
						localityOptional1.get(), departmentOptional2.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				localityToDepartmentTravelInfoRepository.save(l2dpti);
				break;
			case GEO_PLACE:
				Optional<GeoPlaceJpaEntity> geoPlaceOptional2 = geoPlaceRepository.findById(arrivalPlaceId);

				GeoPlaceToLocalityTravelInfoJpaEntity geo2l = new GeoPlaceToLocalityTravelInfoJpaEntity(
						geoPlaceOptional2.get(), localityOptional1.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				geoPlaceToLocalityTravelInfoRepository.save(geo2l);

				break;
			default:
				return;
			}
			break;
		case DELEGATION:

			Optional<DelegationJpaEntity> delegationOptional1 = delegationRepository.findById(departurePlaceId);

			switch (arrivalType) {
			case LOCALITY:

				Optional<LocalityJpaEntity> localityOptional2 = localityRepository.findById(arrivalPlaceId);

				LocalityToDelegationTravelInfoJpaEntity dl2lti = new LocalityToDelegationTravelInfoJpaEntity(
						localityOptional2.get(), delegationOptional1.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				localityToDelegationTravelInfoRepository.save(dl2lti);
				break;
			case DELEGATION:

				Optional<DelegationJpaEntity> delegationOptional2;

				DelegationToDelegationTravelInfoJpaEntity dl2dlti;

				if (departurePlaceId == arrivalPlaceId) {

					delegationOptional2 = delegationOptional1;

				} else {

					delegationOptional2 = delegationRepository.findById(arrivalPlaceId);

				}

				dl2dlti = new DelegationToDelegationTravelInfoJpaEntity(delegationOptional1.get(),
						delegationOptional2.get(), journeyTravelInfo.getHours(), journeyTravelInfo.getMinutes(),
						journeyTravelInfo.getDistance());

				delegationToDelegationTravelInfoRepository.save(dl2dlti);
				break;
			case DEPARTMENT:
				Optional<DepartmentJpaEntity> departmentOptional2 = departmentRepository.findById(arrivalPlaceId);

				DelegationToDepartmentTravelInfoJpaEntity dl2dpti = new DelegationToDepartmentTravelInfoJpaEntity(
						delegationOptional1.get(), departmentOptional2.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				delegationToDepartmentTravelInfoRepository.save(dl2dpti);
				break;
			case GEO_PLACE:
				Optional<GeoPlaceJpaEntity> geoPlaceOptional2 = geoPlaceRepository.findById(arrivalPlaceId);

				GeoPlaceToDelegationTravelInfoJpaEntity geo2dl = new GeoPlaceToDelegationTravelInfoJpaEntity(
						geoPlaceOptional2.get(), delegationOptional1.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				geoPlaceToDelegationTravelInfoRepository.save(geo2dl);

				break;
			default:
				return;

			}
			break;

		case DEPARTMENT:

			Optional<DepartmentJpaEntity> departmentOptional1 = departmentRepository.findById(departurePlaceId);

			switch (arrivalType) {
			case LOCALITY:
				Optional<LocalityJpaEntity> localityOptional2 = localityRepository.findById(arrivalPlaceId);

				LocalityToDepartmentTravelInfoJpaEntity dp2lti = new LocalityToDepartmentTravelInfoJpaEntity(
						localityOptional2.get(), departmentOptional1.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				localityToDepartmentTravelInfoRepository.save(dp2lti);
				break;
			case DELEGATION:

				Optional<DelegationJpaEntity> delegationOptional2 = delegationRepository.findById(arrivalPlaceId);

				DelegationToDepartmentTravelInfoJpaEntity dp2dlti = new DelegationToDepartmentTravelInfoJpaEntity(
						delegationOptional2.get(), departmentOptional1.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				delegationToDepartmentTravelInfoRepository.save(dp2dlti);
				break;
			case DEPARTMENT:
				Optional<DepartmentJpaEntity> departmentOptional2;

				DepartmentToDepartmentTravelInfoJpaEntity dp2dpti;

				if (departurePlaceId == arrivalPlaceId) {

					departmentOptional2 = departmentOptional1;

				} else {

					departmentOptional2 = departmentRepository.findById(arrivalPlaceId);

				}

				dp2dpti = new DepartmentToDepartmentTravelInfoJpaEntity(departmentOptional1.get(),
						departmentOptional2.get(), journeyTravelInfo.getHours(), journeyTravelInfo.getMinutes(),
						journeyTravelInfo.getDistance());

				departmentToDepartmentTravelInfoRepository.save(dp2dpti);
				break;
			case GEO_PLACE:
				Optional<GeoPlaceJpaEntity> geoPlaceOptional2 = geoPlaceRepository.findById(arrivalPlaceId);

				GeoPlaceToDepartmentTravelInfoJpaEntity geo2dp = new GeoPlaceToDepartmentTravelInfoJpaEntity(
						geoPlaceOptional2.get(), departmentOptional1.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				geoPlaceToDepartmentTravelInfoRepository.save(geo2dp);

				break;
			default:
				return;
			}
			break;
		case GEO_PLACE:
			Optional<GeoPlaceJpaEntity> geoPlaceOptional1 = geoPlaceRepository.findById(departurePlaceId);
			switch (arrivalType) {
			case LOCALITY:
				Optional<LocalityJpaEntity> localityOptional2 = localityRepository.findById(arrivalPlaceId);

				GeoPlaceToLocalityTravelInfoJpaEntity geo2lti = new GeoPlaceToLocalityTravelInfoJpaEntity(
						geoPlaceOptional1.get(), localityOptional2.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				geoPlaceToLocalityTravelInfoRepository.save(geo2lti);
				break;
			case DELEGATION:

				Optional<DelegationJpaEntity> delegationOptional2 = delegationRepository.findById(arrivalPlaceId);

				GeoPlaceToDelegationTravelInfoJpaEntity geo2dlti = new GeoPlaceToDelegationTravelInfoJpaEntity(
						geoPlaceOptional1.get(), delegationOptional2.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				geoPlaceToDelegationTravelInfoRepository.save(geo2dlti);
				break;
			case DEPARTMENT:
				Optional<DepartmentJpaEntity> departmentOptional2 = departmentRepository.findById(arrivalPlaceId);

				GeoPlaceToDepartmentTravelInfoJpaEntity geo2dpti = new GeoPlaceToDepartmentTravelInfoJpaEntity(
						geoPlaceOptional1.get(), departmentOptional2.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				geoPlaceToDepartmentTravelInfoRepository.save(geo2dpti);
				break;
			case GEO_PLACE:
				Optional<GeoPlaceJpaEntity> geoPlaceOptional2 = geoPlaceRepository.findById(arrivalPlaceId);

				GeoPlaceToGeoPlaceTravelInfoJpaEntity geo2geo = new GeoPlaceToGeoPlaceTravelInfoJpaEntity(
						geoPlaceOptional2.get(), geoPlaceOptional1.get(), journeyTravelInfo.getHours(),
						journeyTravelInfo.getMinutes(), journeyTravelInfo.getDistance());

				geoPlaceToGeoPlaceTravelInfoRepository.save(geo2geo);
				break;
			default:
				return;
			}
			break;
		default:
			return;
		}

	}

}
