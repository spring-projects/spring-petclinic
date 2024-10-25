package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PetTypeFormatterTest {

	@Mock
	private PetType petType;

	@InjectMocks
	private PetTypeFormatter petTypeFormatter;

	@BeforeEach
	void setUp() {
		given(petType.getName()).willReturn("Dog");
	}

	@Test
	@DisplayName("Test print method")
	void testPrint() {
		String result = petTypeFormatter.print(petType, Locale.ENGLISH);
		assertThat(result).isEqualTo("Dog");
		verify(petType).getName();
	}

}
