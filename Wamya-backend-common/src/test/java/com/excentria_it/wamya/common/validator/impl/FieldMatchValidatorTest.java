package com.excentria_it.wamya.common.validator.impl;

import static org.assertj.core.api.Assertions.*;

import java.lang.annotation.Annotation;

import javax.validation.Payload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.annotation.FieldMatch;
import com.excentria_it.wamya.common.validator.impl.FieldMatchValidator;

public class FieldMatchValidatorTest {

	private FieldMatchValidator fieldMatchValidator1;

	private FieldMatchValidator fieldMatchValidator2;

	private FieldMatchValidator fieldMatchValidator3;

	private FieldMatchValidator fieldMatchValidator4;

	@BeforeEach
	void init() {

		fieldMatchValidator1 = new FieldMatchValidator();
		fieldMatchValidator2 = new FieldMatchValidator();
		fieldMatchValidator3 = new FieldMatchValidator();
		fieldMatchValidator4 = new FieldMatchValidator();

		fieldMatchValidator1.initialize(new FieldMatch() {

			@Override
			public Class<? extends Annotation> annotationType() {

				return null;
			}

			@Override
			public String message() {

				return null;
			}

			@Override
			public Class<?>[] groups() {

				return null;
			}

			@Override
			public Class<? extends Payload>[] payload() {

				return null;
			}

			@Override
			public String first() {

				return "field1";
			}

			@Override
			public String second() {

				return "field2";
			}

		});

		fieldMatchValidator2.initialize(new FieldMatch() {

			@Override
			public Class<? extends Annotation> annotationType() {

				return null;
			}

			@Override
			public String message() {

				return null;
			}

			@Override
			public Class<?>[] groups() {

				return null;
			}

			@Override
			public Class<? extends Payload>[] payload() {

				return null;
			}

			@Override
			public String first() {

				return "field3";
			}

			@Override
			public String second() {

				return "field4";
			}

		});

		fieldMatchValidator3.initialize(new FieldMatch() {

			@Override
			public Class<? extends Annotation> annotationType() {

				return null;
			}

			@Override
			public String message() {

				return null;
			}

			@Override
			public Class<?>[] groups() {

				return null;
			}

			@Override
			public Class<? extends Payload>[] payload() {

				return null;
			}

			@Override
			public String first() {

				return "field4";
			}

			@Override
			public String second() {

				return "field2";
			}

		});

		fieldMatchValidator4.initialize(new FieldMatch() {

			@Override
			public Class<? extends Annotation> annotationType() {

				return null;
			}

			@Override
			public String message() {

				return null;
			}

			@Override
			public Class<?>[] groups() {

				return null;
			}

			@Override
			public Class<? extends Payload>[] payload() {

				return null;
			}

			@Override
			public String first() {

				return "field1";
			}

			@Override
			public String second() {

				return "field4";
			}

		});
	}

	@Test
	void givenAnObjectWithSameFieldsValue_thenFieldMatchValidatorShouldBeValid() {

		assertThat(fieldMatchValidator1.isValid(new FieldsMatching("SOME_VALUE", "SOME_VALUE"), null)).isTrue();

	}

	@Test
	void givenAnObjectWithDifferentFieldsValues_thenFieldMatchValidatorShouldNotBeValid() {

		assertThat(fieldMatchValidator1.isValid(new FieldsMatching("SOME_VALUE", "ANOTHER_VALUE"), null)).isFalse();

	}

	@Test
	void givenAnObjectWithNullFirstField_thenFieldMatchValidatorShouldNotBeValid() {

		assertThat(fieldMatchValidator1.isValid(new FieldsMatching(null, "SOME_VALUE"), null)).isFalse();

	}

	@Test
	void givenAnObjectWithNullSeconfField_thenFieldMatchValidatorShouldNotBeValid() {

		assertThat(fieldMatchValidator1.isValid(new FieldsMatching("SOME_VALUE", null), null)).isFalse();

	}

	@Test
	void givenAnObjectWithNullFirstFieldAndNullSeconfField_thenFieldMatchValidatorShouldBeValid() {

		assertThat(fieldMatchValidator1.isValid(new FieldsMatching(null, null), null)).isTrue();

	}

	@Test
	void givenAnObjectWithIncorrectFieldNames_thenFieldMatchValidatorShouldNotBeValid() {

		assertThat(fieldMatchValidator2.isValid(new FieldsMatching("SOME_VALUE", "SOME_VALUE"), null)).isFalse();

	}

	@Test
	void givenAnObjectWithIncorrectFirstFieldName_thenFieldMatchValidatorShouldNotBeValid() {

		assertThat(fieldMatchValidator3.isValid(new FieldsMatching("SOME_VALUE", "SOME_VALUE"), null)).isFalse();

	}

	@Test
	void givenAnObjectWithIncorrectSecondFieldName_thenFieldMatchValidatorShouldNotBeValid() {

		assertThat(fieldMatchValidator4.isValid(new FieldsMatching("SOME_VALUE", "SOME_VALUE"), null)).isFalse();

	}

}
