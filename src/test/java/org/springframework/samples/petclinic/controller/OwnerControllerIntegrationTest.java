package org.springframework.samples.petclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.samples.petclinic.dto.VisitDTO;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerIntegrationTest {

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OwnerRepository ownerRepository;

	static private OwnerDTO george;

	@BeforeAll
	static void beforeAll() {
		george = new OwnerDTO();
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setAddress("110 W. Liberty St.");
		george.setCity("Madison");
		george.setTelephone("6085551023");
		PetDTO max = new PetDTO();
		PetTypeDTO dog = new PetTypeDTO();
		dog.setName("dog");
		max.setType(dog);
		max.setName("Max");
		max.setBirthDate(LocalDate.now());
		// george.setPetsInternal(Collections.singleton(max));
		VisitDTO visit = new VisitDTO();
		visit.setDate(LocalDate.now());
	}

	@Test
	@Tag("initCreationForm")
	@DisplayName("Verify that the view for new Owner is initialised with new OwnerDTO")
	void testInitCreationForm() throws Exception {
		final MvcResult result = mockMvc.perform(get(CommonEndPoint.OWNERS_NEW)).andExpect(status().isOk())
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE)).andReturn();

		OwnerDTO found = (OwnerDTO) Objects.requireNonNull(result.getModelAndView()).getModel()
				.get(CommonAttribute.OWNER);

		assertThat(found).isEqualToComparingFieldByField(new OwnerDTO());
	}

	@Test
	@Tag("processCreationForm")
	void testProcessCreationFormSuccess() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(george);

		final MvcResult result = mockMvc
				.perform(post(CommonEndPoint.OWNERS_NEW).flashAttr(CommonAttribute.OWNER, george))
				.andExpect(status().is3xxRedirection()).andReturn();

		json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		OwnerDTO found = mapper.readValue(json, OwnerDTO.class);

		assertThat(found).isEqualTo(george);
	}

	@Test
	@Disabled
	@Tag("processFindForm")
	@DisplayName("Verify that we get the right view and all Owners")
	void testProcessFindFormSuccess() throws Exception {
		List<Owner> expected = ownerRepository.findAll();

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.OWNERS))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(view().name(CommonView.OWNER_OWNERS_LIST))
				.andReturn();

		Collection<OwnerDTO> founds = (Collection<OwnerDTO>) result.getModelAndView().getModel()
				.get(CommonAttribute.SELECTIONS);

		int[] position = new int[] { 0 };

		founds.forEach(ownerDTO -> {
			Owner owner = expected.get(position[0]++);

			assertThat(owner.getId()).isEqualTo(ownerDTO.getId());
			assertThat(owner.getFirstName()).isEqualTo(ownerDTO.getFirstName());
			assertThat(owner.getLastName()).isEqualTo(ownerDTO.getLastName());
			assertThat(owner.getTelephone()).isEqualTo(ownerDTO.getTelephone());
			assertThat(owner.getAddress()).isEqualTo(ownerDTO.getAddress());
			assertThat(owner.getCity()).isEqualTo(ownerDTO.getCity());
		});
	}

}
