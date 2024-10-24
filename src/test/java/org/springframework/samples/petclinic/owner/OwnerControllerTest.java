package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private BindingResult result;

	@Mock
	private RedirectAttributes redirectAttributes;

	@InjectMocks
	private OwnerController ownerController;

	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
	}

	@Test
	@DisplayName("Test processCreationForm with validation errors")
	void testProcessCreationFormWithValidationErrors() {
		Owner owner = new Owner();

		doReturn(true).when(result).hasErrors();

		String view = ownerController.processCreationForm(owner, result, redirectAttributes);

		assertThat(view).isEqualTo("owners/createOrUpdateOwnerForm");
		verify(result).hasErrors();
		verifyNoInteractions(ownerRepository);
	}

	@Test
	@DisplayName("Test processUpdateOwnerForm with validation errors")
	void testProcessUpdateOwnerFormWithValidationErrors() {
		Owner owner = new Owner();

		doReturn(true).when(result).hasErrors();

		String view = ownerController.processUpdateOwnerForm(owner, result, 1, redirectAttributes);

		assertThat(view).isEqualTo("owners/createOrUpdateOwnerForm");
		verify(result).hasErrors();
		verifyNoInteractions(ownerRepository);
	}

}
