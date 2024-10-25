import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeFormatter;
import org.springframework.samples.petclinic.owner.OwnerRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PetTypeFormatterTest {

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private PetTypeFormatter petTypeFormatter;

	@Test
	@DisplayName("Test parse method with valid pet type")
	void testParseWithValidPetType() throws Exception {
		List<PetType> petTypes = new ArrayList<>();
		PetType dog = new PetType();
		dog.setName("Dog");
		petTypes.add(dog);
		given(ownerRepository.findPetTypes()).willReturn(petTypes);

		PetType result = petTypeFormatter.parse("Dog", Locale.ENGLISH);

		assertThat(result.getName()).isEqualTo("Dog");
		then(ownerRepository).should().findPetTypes();
	}

	@Test
	@DisplayName("Test parse method with invalid pet type")
	void testParseWithInvalidPetType() throws Exception {
		List<PetType> petTypes = new ArrayList<>();
		PetType dog = new PetType();
		dog.setName("Dog");
		petTypes.add(dog);
		given(ownerRepository.findPetTypes()).willReturn(petTypes);

		assertThatThrownBy(() -> petTypeFormatter.parse("Dragon", Locale.ENGLISH)).isInstanceOf(ParseException.class)
			.hasMessage("type not found: Dragon");
		then(ownerRepository).should().findPetTypes();
	}

}
