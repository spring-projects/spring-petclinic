package org.springframework.samples.petclinic.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerIntegrationTest {

	private static final int TEST_OWNER_ID = 5;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private PetRepository petRepository;

	private OwnerDTO ownerDTO;

	private PetDTO petDTO;

	@BeforeEach
	void beforeEach() {
		ownerDTO = ownerService.findById(TEST_OWNER_ID);
		petDTO = new PetDTO();
		PetType type = petRepository.findPetTypes().get(1);
		PetTypeDTO typeDTO = new PetTypeDTO();
		typeDTO.setId(type.getId());
		typeDTO.setName(type.getName());
		petDTO.setType(typeDTO);
		petDTO.setName("Max");
		petDTO.setBirthDate(LocalDate.now());
	}

	@Test
	@Tag("initCreationForm")
	@DisplayName("Verify that the view for new Pet is initialised with new PetDTO")
	void testInitCreationForm() throws Exception {

		final MvcResult result = mockMvc
				.perform(get(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID)
						.flashAttr(CommonAttribute.OWNER, ownerDTO))
				.andExpect(status().isOk()).andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE)).andReturn();

		PetDTO petFound = (PetDTO) Objects.requireNonNull(result.getModelAndView()).getModel().get(CommonAttribute.PET);

		assertThat(petFound.getOwner()).isEqualTo(ownerDTO);
	}

	@Test
	@Tag("processCreationForm")
	@DisplayName("Verify that save new Pet and display the view")
	void testProcessCreationFormSuccess() throws Exception {

		final MvcResult result = mockMvc
				.perform(post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID)
						.flashAttr(CommonAttribute.OWNER, ownerDTO).flashAttr(CommonAttribute.PET, petDTO))
				.andExpect(status().is3xxRedirection()).andExpect(view().name(CommonView.OWNER_OWNERS_ID_R))
				.andReturn();

		List<PetDTO> found = ownerService.findById(TEST_OWNER_ID).getPets();

		assertThat(found).contains(petDTO);
	}

}
