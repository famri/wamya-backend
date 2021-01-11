package com.excentria_it.wamya.adapter.persistence.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity.EngineTypeCode;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedEngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.domain.EngineTypeDto;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class EngineTypeRepositoryTests {

	@Autowired
	private EngineTypeRepository engineTypeRepository;

	@BeforeEach
	void cleanDatabase() {
		engineTypeRepository.deleteAll();
	}

	@Test
	void testFindByIdAndLocale() {

		// given
		List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes();

		// when
		Optional<EngineTypeDto> engineTypeDtoOptional = engineTypeRepository
				.findByIdAndLocale(engineTypes.get(0).get(0).getId(), "en_US");

		// then
		assertThat(engineTypeDtoOptional).isNotEmpty();

		assertEquals(engineTypes.get(0).get(0).getId(), engineTypeDtoOptional.get().getId());
		assertEquals(engineTypes.get(0).get(0).getName("en_US"), engineTypeDtoOptional.get().getName());
		assertEquals(engineTypes.get(0).get(0).getDescription("en_US"), engineTypeDtoOptional.get().getDescription());
		assertEquals(engineTypes.get(0).get(0).getCode().name(), engineTypeDtoOptional.get().getCode());

	}

	private List<List<EngineTypeJpaEntity>> givenEngineTypes() {
		// Engine type 11
		EngineTypeJpaEntity et11 = new EngineTypeJpaEntity();
		et11.setCode(EngineTypeCode.VAN_L1H1);

		LocalizedEngineTypeJpaEntity let11en = new LocalizedEngineTypeJpaEntity();
		let11en.setLocalizedId(new LocalizedId("en_US"));
		let11en.setEngineType(et11);
		let11en.setName("EngineType11");
		let11en.setDescription("EngineTypeDescription11");
		et11.getLocalizations().put("en_US", let11en);

		LocalizedEngineTypeJpaEntity let11fr = new LocalizedEngineTypeJpaEntity();
		let11fr.setLocalizedId(new LocalizedId("fr_FR"));
		let11fr.setEngineType(et11);
		let11fr.setName("TypeVehicule11");
		let11fr.setDescription("DescriptionTypeVehicule11");
		et11.getLocalizations().put("fr_FR", let11fr);

		// Engine type 12
		EngineTypeJpaEntity et12 = new EngineTypeJpaEntity();
		et12.setCode(EngineTypeCode.VAN_L2H2);

		LocalizedEngineTypeJpaEntity let12en = new LocalizedEngineTypeJpaEntity();
		let12en.setLocalizedId(new LocalizedId("en_US"));
		let12en.setEngineType(et12);
		let12en.setName("EngineType12");
		let12en.setDescription("EngineTypeDescription12");
		et12.getLocalizations().put("en_US", let12en);

		LocalizedEngineTypeJpaEntity let12fr = new LocalizedEngineTypeJpaEntity();
		let12fr.setLocalizedId(new LocalizedId("fr_FR"));
		let12fr.setEngineType(et12);
		let12fr.setName("TypeVehicule12");
		let12fr.setDescription("DescriptionTypeVehicule12");
		et12.getLocalizations().put("fr_FR", let12fr);

		// Engine type 13
		EngineTypeJpaEntity et13 = new EngineTypeJpaEntity();
		et13.setCode(EngineTypeCode.VAN_L3H3);

		LocalizedEngineTypeJpaEntity let13en = new LocalizedEngineTypeJpaEntity();
		let13en.setLocalizedId(new LocalizedId("en_US"));
		let13en.setEngineType(et13);
		let13en.setName("EngineType13");
		let13en.setDescription("EngineTypeDescription13");
		et13.getLocalizations().put("en_US", let13en);

		LocalizedEngineTypeJpaEntity let13fr = new LocalizedEngineTypeJpaEntity();
		let13fr.setLocalizedId(new LocalizedId("fr_FR"));
		let13fr.setEngineType(et13);
		let13fr.setName("TypeVehicule13");
		let13fr.setDescription("DescriptionTypeVehicule3");
		et13.getLocalizations().put("fr_FR", let13fr);

		// Engine type 21
		EngineTypeJpaEntity et21 = new EngineTypeJpaEntity();
		et21.setCode(EngineTypeCode.FLATBED_TRUCK);

		LocalizedEngineTypeJpaEntity let21en = new LocalizedEngineTypeJpaEntity();
		let21en.setLocalizedId(new LocalizedId("en_US"));
		let21en.setEngineType(et21);
		let21en.setName("EngineType21");
		let21en.setDescription("EngineTypeDescription21");
		et21.getLocalizations().put("en_US", let21en);

		LocalizedEngineTypeJpaEntity let21fr = new LocalizedEngineTypeJpaEntity();
		let21fr.setLocalizedId(new LocalizedId("fr_FR"));
		let21fr.setEngineType(et21);
		let21fr.setName("TypeVehicule21");
		let21fr.setDescription("DescriptionTypeVehicule21");
		et21.getLocalizations().put("fr_FR", let21fr);

		// Engine type 22
		EngineTypeJpaEntity et22 = new EngineTypeJpaEntity();
		et22.setCode(EngineTypeCode.UTILITY);

		LocalizedEngineTypeJpaEntity let22en = new LocalizedEngineTypeJpaEntity();
		let22en.setLocalizedId(new LocalizedId("en_US"));
		let22en.setEngineType(et22);
		let22en.setName("EngineType22");
		let22en.setDescription("EngineTypeDescription22");
		et22.getLocalizations().put("en_US", let22en);

		LocalizedEngineTypeJpaEntity let22fr = new LocalizedEngineTypeJpaEntity();
		let22fr.setLocalizedId(new LocalizedId("fr_FR"));
		let22fr.setEngineType(et22);
		let22fr.setName("TypeVehicule22");
		let22fr.setDescription("DescriptionTypeVehicule22");
		et22.getLocalizations().put("fr_FR", let22fr);

		// Engine type 23
		EngineTypeJpaEntity et23 = new EngineTypeJpaEntity();
		et23.setCode(EngineTypeCode.DUMP_TRUCK);
		;

		LocalizedEngineTypeJpaEntity let23en = new LocalizedEngineTypeJpaEntity();
		let23en.setLocalizedId(new LocalizedId("en_US"));
		let23en.setEngineType(et23);
		let23en.setName("EngineType23");
		let23en.setDescription("EngineTypeDescription23");
		et23.getLocalizations().put("en_US", let23en);

		LocalizedEngineTypeJpaEntity let23fr = new LocalizedEngineTypeJpaEntity();
		let23fr.setLocalizedId(new LocalizedId("fr_FR"));
		let23fr.setEngineType(et23);
		let23fr.setName("TypeVehicule23");
		let23fr.setDescription("DescriptionTypeVehicule23");
		et23.getLocalizations().put("fr_FR", let23fr);

		// Engine type 31
		EngineTypeJpaEntity et31 = new EngineTypeJpaEntity();
		et31.setCode(EngineTypeCode.BOX_TRUCK);

		LocalizedEngineTypeJpaEntity let31en = new LocalizedEngineTypeJpaEntity();
		let31en.setLocalizedId(new LocalizedId("en_US"));
		let31en.setEngineType(et31);
		let31en.setName("EngineType31");
		let31en.setDescription("EngineTypeDescription31");
		et31.getLocalizations().put("en_US", let31en);

		LocalizedEngineTypeJpaEntity let31fr = new LocalizedEngineTypeJpaEntity();
		let31fr.setLocalizedId(new LocalizedId("fr_FR"));
		let31fr.setEngineType(et31);
		let31fr.setName("TypeVehicule31");
		let31fr.setDescription("DescriptionTypeVehicule31");
		et31.getLocalizations().put("fr_FR", let31fr);

		// Engine type 32
		EngineTypeJpaEntity et32 = new EngineTypeJpaEntity();
		et32.setCode(EngineTypeCode.TANKER);

		LocalizedEngineTypeJpaEntity let32en = new LocalizedEngineTypeJpaEntity();
		let32en.setLocalizedId(new LocalizedId("en_US"));
		let32en.setEngineType(et32);
		let32en.setName("EngineType32");
		let32en.setDescription("EngineTypeDescription32");
		et32.getLocalizations().put("en_US", let32en);

		LocalizedEngineTypeJpaEntity let32fr = new LocalizedEngineTypeJpaEntity();
		let32fr.setLocalizedId(new LocalizedId("fr_FR"));
		let32fr.setEngineType(et32);
		let32fr.setName("TypeVehicule32");
		let32fr.setDescription("DescriptionTypeVehicule32");
		et32.getLocalizations().put("fr_FR", let32fr);

		// Engine type 33
		EngineTypeJpaEntity et33 = new EngineTypeJpaEntity();
		et33.setCode(EngineTypeCode.BUS);

		LocalizedEngineTypeJpaEntity let33en = new LocalizedEngineTypeJpaEntity();
		let33en.setLocalizedId(new LocalizedId("en_US"));
		let33en.setEngineType(et33);
		let33en.setName("EngineType33");
		let33en.setDescription("EngineTypeDescription33");
		et33.getLocalizations().put("en_US", let33en);

		LocalizedEngineTypeJpaEntity let33fr = new LocalizedEngineTypeJpaEntity();
		let33fr.setLocalizedId(new LocalizedId("fr_FR"));
		let33fr.setEngineType(et33);
		let33fr.setName("TypeVehicule33");
		let33fr.setDescription("DescriptionTypeVehicule3");
		et33.getLocalizations().put("fr_FR", let33fr);

		List<EngineTypeJpaEntity> engineTypes1 = List.of(et11, et12, et13);
		List<EngineTypeJpaEntity> engineTypes2 = List.of(et21, et22, et23);
		List<EngineTypeJpaEntity> engineTypes3 = List.of(et31, et32, et33);

		engineTypes1 = engineTypeRepository.saveAll(engineTypes1);
		engineTypes2 = engineTypeRepository.saveAll(engineTypes2);
		engineTypes3 = engineTypeRepository.saveAll(engineTypes3);

		return List.of(engineTypes1, engineTypes2, engineTypes3);
	}

}
