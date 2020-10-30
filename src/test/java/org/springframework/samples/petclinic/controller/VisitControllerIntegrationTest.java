package org.springframework.samples.petclinic.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.dto.VisitDTO;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class VisitControllerIntegrationTest {

	// An existing Pet ID in database
	private final static Integer PET_ID = 5;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private VisitRepository visitRepository;

	@Autowired
	private PetService petService;

	private PetDTO petDTO;

	private Visit visit;

	private VisitDTO visitDTO;

	@BeforeEach
	void beforeEach() {
		petDTO = petService.findById(PET_ID);

		visit = new Visit();
		visit.setPetId(PET_ID);
		visit.setDate(LocalDate.now());
		visit.setDescription("Visit Description");

		visitDTO = new VisitDTO();
		visitDTO.setPetId(PET_ID);
		visitDTO.setDate(LocalDate.now());
		visitDTO.setDescription("Visit Description");
	}

	@Test
	@Tag("initNewVisitForm")
	@DisplayName("Verify that return form for new Visit with right Pet")
	void givenPetId_whenGetNewVisit_thenReturnCreationViewWithNewVisit() throws Exception {
		Integer nbOfVisits = petDTO.getVisits().size();

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.VISITS_NEW, PET_ID))
				.andExpect(status().is2xxSuccessful()).andExpect(view().name(CommonView.VISIT_CREATE_OR_UPDATE))
				.andReturn();

		PetDTO found = (PetDTO) Objects.requireNonNull(result.getModelAndView()).getModel().get(CommonAttribute.PET);

		assertThat(found).isEqualToIgnoringGivenFields(petDTO, CommonAttribute.VISITS);
		assertThat(found.getVisits().size()).isEqualTo(nbOfVisits + 1);

		VisitDTO expected = new VisitDTO();
		expected.setDate(LocalDate.now());
		expected.setPetId(PET_ID);

		assertThat(found.getVisits()).contains(expected);
	}

	@Test
	@Tag("processNewVisitForm")
	@DisplayName("Verify that save Visit")
	void givenVisitAndOwnerIDAndPetId_whenSaveVisit_thenSaveVisit() throws Exception {

		mockMvc.perform(post(CommonEndPoint.VISITS_EDIT, petDTO.getOwner().getId(), PET_ID)
				.flashAttr(CommonAttribute.VISIT, visitDTO)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(CommonView.OWNER_OWNERS_ID_R)).andReturn();

		List<Visit> found = visitRepository.findByPetId(PET_ID);

		assertThat(found).contains(visit);
	}

}
