package org.springframework.samples.petclinic.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.business.OwnerService;
import org.springframework.samples.petclinic.service.business.PetService;
import org.springframework.samples.petclinic.service.common.UserDetailsServiceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link PetController}
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerIntegrationTest {

	private static final int TEST_OWNER_ID = 5;

	private static final int PET_ID = 6;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private PetService petService;

	@Autowired
	private PetRepository petRepository;

	private OwnerDTO ownerDTO;

	private PetDTO petDTO;

	@BeforeEach
	void beforeEach() {
		petDTO = petService.findById(PET_ID);
		ownerDTO = petDTO.getOwner();
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initCreationForm")
	@DisplayName("Verify that the view for new Pet is initialised with new PetDTO")
	void givenOwnerId_whenGetNewPet_thenReturnCreationViewWithNewPet() throws Exception {

		final MvcResult result = mockMvc
				.perform(get(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID)
						.flashAttr(CommonAttribute.OWNER, ownerDTO))
				.andExpect(status().is2xxSuccessful()).andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE))
				.andReturn();

		PetDTO petFound = (PetDTO) Objects.requireNonNull(result.getModelAndView()).getModel().get(CommonAttribute.PET);

		assertThat(petFound.getOwner()).isEqualTo(ownerDTO);
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that save new Pet and display the view")
	void givenNewPet_whenPostNewPet_thenSavePetAndRedirectToOwnerView() throws Exception {

		final MvcResult result = mockMvc
				.perform(post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID)
						.flashAttr(CommonAttribute.OWNER, ownerDTO).flashAttr(CommonAttribute.PET, petDTO))
				.andExpect(status().is3xxRedirection()).andExpect(view().name(CommonView.OWNER_OWNERS_ID_R))
				.andReturn();

		List<PetDTO> found = ownerService.findById(TEST_OWNER_ID).getPets();

		assertThat(found).contains(petDTO);
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initUpdateForm")
	@DisplayName("Verify that the view to update Pet is initialised with right Pet")
	void givenPetId_whenGetUpdatePet_thenReturnUpdateViewWithPet() throws Exception {
		PetDTO expected = petService.entityToDTO(petRepository.findById(PET_ID));

		final MvcResult result = mockMvc
				.perform(get(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_ID_EDIT, ownerDTO.getId(), PET_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists(CommonAttribute.PET))
				.andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE)).andReturn();

		PetDTO petFound = (PetDTO) Objects.requireNonNull(result.getModelAndView()).getModel().get(CommonAttribute.PET);

		assertThat(petFound).isEqualTo(expected);
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateForm")
	@DisplayName("Verify that Pet is updated and the right view is displayed")
	void givenUpdatePet_whenPostUpdatePet_thenUpdatePetAndRedirectToOwnerView() throws Exception {
		PetDTO petExpected = petService.entityToDTO(petRepository.findById(PET_ID));
		OwnerDTO ownerExpected = ownerService.findById(petExpected.getOwner().getId());
		petExpected.setName("Nabucho");
		petExpected.setBirthDate(LocalDate.now());

		final MvcResult result = mockMvc.perform(
				post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_ID_EDIT, ownerExpected.getId(), petExpected.getId())
						.flashAttr(CommonAttribute.OWNER, ownerExpected).flashAttr(CommonAttribute.PET, petExpected))
				.andExpect(view().name(CommonView.OWNER_OWNERS_ID_R)).andReturn();

		PetDTO petFound = petService.entityToDTO(petRepository.findById(PET_ID));

		assertThat(petFound).isEqualTo(petExpected);
	}

}
