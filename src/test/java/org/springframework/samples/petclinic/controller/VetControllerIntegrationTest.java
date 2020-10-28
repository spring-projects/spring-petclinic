package org.springframework.samples.petclinic.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.VetDTO;
import org.springframework.samples.petclinic.dto.VetsDTO;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class VetControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private VetService vetService;

	@Autowired
	private VetRepository vetRepository;


	@BeforeEach
	void beforeEach() {

	}


	@Test
	@Tag("showVetList")
	void testShowVetListHtml() throws Exception {
		Collection<Vet> vets = vetRepository.findAll();

		VetsDTO expected = new VetsDTO(vetService.entitiesToDTOS(new ArrayList<>(vets)));

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.VETS_HTML))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists(CommonAttribute.VETS))
			.andExpect(view().name(CommonView.VET_VETS_LIST))
			.andReturn();

		VetsDTO found = (VetsDTO) result.getModelAndView().getModel().get(CommonAttribute.VETS);

		assertThat(found).isEqualToComparingFieldByField(expected);
	}


}
