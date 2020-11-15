package org.springframework.samples.petclinic.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.VetsDTO;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link VetController}
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class VetControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private VetService vetService;

	@Autowired
	private VetRepository vetRepository;

	private VetsDTO vetsDTO;

	@BeforeEach
	void beforeEach() {
		vetsDTO = new VetsDTO(vetService.findAll());
	}

	@Test
	@Tag("showVetList")
	@DisplayName("When asking vets get String containing Vets")
	void whenGetVets_thenReturnStringOfVets() throws Exception {

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.VETS_HTML)).andExpect(status().is2xxSuccessful())
				.andExpect(model().attributeExists(CommonAttribute.VETS))
				.andExpect(view().name(CommonView.VET_VETS_LIST)).andReturn();

		VetsDTO found = (VetsDTO) Objects.requireNonNull(result.getModelAndView()).getModel().get(CommonAttribute.VETS);

		assertThat(found).isEqualToComparingFieldByField(vetsDTO);
	}

	@Test
	@Tag("showResourcesVetList")
	@DisplayName("When asking vets get Vets DTO object containing Vets")
	void whenGetVets_thenReturnVetsDTO() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.VETS)).andExpect(status().is2xxSuccessful())
				.andReturn();

		String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		VetsDTO found = mapper.readValue(json, VetsDTO.class);

		assertThat(found).isEqualToComparingFieldByField(vetsDTO);
	}

}
