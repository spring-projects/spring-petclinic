/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.Pet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.TestUtils.PetTypeTestUtil;
import org.springframework.samples.petclinic.owner.OwnerRepository;

/**
 * Test class for {@link PetTypeFormatter}
 *
 * @author Colin But
 */
@ExtendWith(MockitoExtension.class)
@DisabledInNativeImage
class PetTypeFormatterTests {

	@Mock
	private OwnerRepository pets;

	private PetTypeFormatter petTypeFormatter;

	@BeforeEach
	void setup() {
		this.petTypeFormatter = new PetTypeFormatter(pets);
	}

	@Test
	void testPrint() {
		var hamster = PetTypes.HAMSTER.getValue();
		var petType = PetTypeTestUtil.createPetType(hamster);
		String petTypeName = this.petTypeFormatter.print(petType, Locale.ENGLISH);
		assertThat(petTypeName).isEqualTo(hamster);
	}

	@Test
	void shouldParse() throws ParseException {
		mockFindPetTypes();
		var bird = PetTypes.BIRD.getValue();
		PetType petType = petTypeFormatter.parse(bird, Locale.ENGLISH);
		assertThat(petType.getName()).isEqualTo(bird);
	}

	@Test
	void shouldThrowParseException() throws ParseException {
		mockFindPetTypes();
		Assertions.assertThrows(ParseException.class, () -> {
			petTypeFormatter.parse("Fish", Locale.ENGLISH);
		});
	}

	private void mockFindPetTypes() {
		List<PetType> petTypes = PetTypeTestUtil.createPetTypes(PetTypes.BIRD, PetTypes.DOG);
		given(this.pets.findPetTypes()).willReturn(petTypes);
	}

}
