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
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.dto.VisitDTO;
import org.springframework.samples.petclinic.service.business.OwnerService;
import org.springframework.samples.petclinic.service.common.UserDetailsServiceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link OwnerController}
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerIntegrationTest {

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private OwnerService ownerService;

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
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initCreationForm")
	@DisplayName("Verify that the view for new Owner is initialised with new OwnerDTO")
	void whenGetNewOwner_thenReturnCreationViewWithNewOwner() throws Exception {

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.OWNERS_NEW)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE)).andReturn();

		OwnerDTO found = (OwnerDTO) Objects.requireNonNull(result.getModelAndView()).getModel()
				.get(CommonAttribute.OWNER);

		assertThat(found).isEqualToComparingFieldByField(new OwnerDTO());
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	void givenNewOwner_whenPostNewOwner_thenSaveOwnerAndRedirectToOwnerView() throws Exception {

		final MvcResult result = mockMvc
				.perform(post(CommonEndPoint.OWNERS_NEW).flashAttr(CommonAttribute.OWNER, george))
				.andExpect(status().is3xxRedirection()).andReturn();

		String path = Objects.requireNonNull(result.getModelAndView()).getViewName();
		int ownerId = Integer.parseInt(Objects.requireNonNull(path).split("/")[2]);
		OwnerDTO found = ownerService.findById(ownerId);

		assertThat(found).isEqualToIgnoringGivenFields(george, CommonAttribute.ID);
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processFindForm")
	@DisplayName("Verify that we get the right view and all Owners")
	void whenGetFindOwner_thenReturnFindViewWithAllOwners() throws Exception {
		List<OwnerDTO> expected = ownerService.findAll();

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.OWNERS))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(view().name(CommonView.OWNER_OWNERS_LIST))
				.andReturn();

		Map<String, Object> model = Objects.requireNonNull(result.getModelAndView()).getModel();
		Collection<OwnerDTO> founds = (Collection<OwnerDTO>) model.get(CommonAttribute.SELECTIONS);

		int[] position = new int[] { 0 };

		founds.forEach(found -> {
			OwnerDTO owner = expected.get(position[0]++);
			assertThat(owner).isEqualToComparingFieldByField(found);
		});
	}

}
