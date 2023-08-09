package com.excentria_it.wamya.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.domain.ConstructorDto;
import com.excentria_it.wamya.domain.LoadConstructorsDto;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class ConstructorRepositoryTests {
	@Autowired
	private ConstructorRepository constructorRepository;

	@BeforeEach
	void cleanDatabase() {
		constructorRepository.deleteAll();
	}

	@Test
	void testFindConstructorById() {

		// given
		List<List<ModelJpaEntity>> models = givenModels();
		List<ConstructorJpaEntity> constructors = givenConstructors(models);

		// when
		Optional<ConstructorDto> constructorDtoOptional = constructorRepository
				.findConstructorById(constructors.get(0).getId());
		ConstructorDto constructorDto = constructorDtoOptional.get();

		// then
		assertEquals(constructors.get(0).getId(), constructorDto.getId());
		assertEquals(constructors.get(0).getName(), constructorDto.getName());

		List<Long> modelDtosIds = constructorDto.getModels().stream().map(m -> m.getId()).collect(Collectors.toList());
		List<String> modelDtosNames = constructorDto.getModels().stream().map(m -> m.getName())
				.collect(Collectors.toList());

		List<Long> modelIds = models.get(0).stream().map(m -> m.getId()).collect(Collectors.toList());

		List<String> modelNames = models.get(0).stream().map(m -> m.getName()).collect(Collectors.toList());

		assertTrue(modelDtosIds.size() == modelIds.size() && modelDtosIds.containsAll(modelIds));

		assertTrue(modelDtosNames.size() == modelNames.size() && modelDtosNames.containsAll(modelNames));
	}

	@Test
	void testLoadAllConstructors() {

		// given
		List<List<ModelJpaEntity>> models = givenModels();
		List<ConstructorJpaEntity> constructors = givenConstructors(models);

		// when
		List<LoadConstructorsDto> constructorsListResult = constructorRepository
				.findAllBy(Sort.by(new Order(Direction.DESC, "name")));

		// then

		List<String> constructorsNames = constructors.stream().map(c -> c.getName())
				.sorted((n1, n2) -> n1.compareTo(n2)).collect(Collectors.toList());
		List<String> constructorsListResultNames = constructorsListResult.stream().map(c -> c.getName())
				.sorted((n1, n2) -> n1.compareTo(n2)).collect(Collectors.toList());
		assertTrue(constructorsListResultNames.size() == constructorsNames.size()
				&& constructorsNames.containsAll(constructorsListResultNames));
	}

	private List<ConstructorJpaEntity> givenConstructors(List<List<ModelJpaEntity>> models) {

		List<ConstructorJpaEntity> constructors = new ArrayList<>();
		Integer counter = 1;

		Iterator<List<ModelJpaEntity>> listIterator = models.iterator();
		while (listIterator.hasNext()) {

			List<ModelJpaEntity> modelsList = listIterator.next();

			ConstructorJpaEntity constructor = ConstructorJpaEntity.builder().name("Constructor" + counter).build();
			counter++;

			Iterator<ModelJpaEntity> it = modelsList.iterator();
			while (it.hasNext()) {
				ModelJpaEntity model = it.next();
				constructor.addModel(model);
			}

			constructor = constructorRepository.save(constructor);
			constructors.add(constructor);
		}

		return constructors;
	}

	private List<List<ModelJpaEntity>> givenModels() {

		List<ModelJpaEntity> models1 = List.of(ModelJpaEntity.builder().name("Model11").build(),
				ModelJpaEntity.builder().name("Model12").build(), ModelJpaEntity.builder().name("Model13").build());
		List<ModelJpaEntity> models2 = List.of(ModelJpaEntity.builder().name("Model21").build(),
				ModelJpaEntity.builder().name("Model22").build(), ModelJpaEntity.builder().name("Model23").build());
		List<ModelJpaEntity> models3 = List.of(ModelJpaEntity.builder().name("Model31").build(),
				ModelJpaEntity.builder().name("Model32").build(), ModelJpaEntity.builder().name("Model33").build());
		return List.of(models1, models2, models3);
	}
}
