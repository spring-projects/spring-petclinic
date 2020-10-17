package org.springframework.samples.petclinic.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.dto.VetDTO;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VetServiceTest {

	private final static Integer VET_ID = 11;

	private final static String VET_FIRST_NAME = "Sam";

	private final static String VET_LAST_NAME = "Schultz";

	@Autowired
	private VetRepository vetRepository;
	@Autowired
	private SpecialtyRepository specialtyRepository;

	private VetService vetService;

	private static Vet vet;

	private static VetDTO vetDTO;

	@BeforeEach
	void beforeEach() {
		vetService = new VetService(vetRepository, specialtyRepository);
		vet = new Vet();
		vet.setId(VET_ID);
		vet.setFirstName(VET_FIRST_NAME);
		vet.setLastName(VET_LAST_NAME);
		vetDTO = new VetDTO();
		vetDTO.setId(VET_ID);
		vetDTO.setFirstName(VET_FIRST_NAME);
		vetDTO.setLastName(VET_LAST_NAME);

	}

	@Test
	@Tag("dtoToEntity")
	@DisplayName("Verify the convertion from DTO to Entity")
	void dtoToEntity() {
		Vet found = vetService.dtoToEntity(vetDTO);

		assertThat(found.getId()).isEqualTo(vet.getId());
		assertThat(found.getFirstName()).isEqualTo(vet.getFirstName());
		assertThat(found.getLastName()).isEqualTo(vet.getLastName());
	}

	@Test
	@Tag("entityToDTO")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entityToDTO() {
		VetDTO found = vetService.entityToDTO(vet);

		assertThat(found.getId()).isEqualTo(vetDTO.getId());
		assertThat(found.getFirstName()).isEqualTo(vetDTO.getFirstName());
		assertThat(found.getLastName()).isEqualTo(vetDTO.getLastName());
	}

	@Test
	@DisplayName("Verify the convertion from DTOs list to Entities list")
	@Tag("dtosToEntities")
	void dtosToEntities() {
		List<VetDTO> vetDTOS = vetService.findAll();
		List<Vet> expected = new ArrayList<>();
		vetDTOS.forEach(dto -> expected.add(vetService.dtoToEntity(dto)));

		Collection<Vet> found = vetService.dtosToEntities(vetDTOS);

		assertThat(found).hasSameSizeAs(expected).isEqualTo(expected);
	}

	@Test
	@Tag("entitiesToDTOS")
	@DisplayName("Verify the convertion from Entities list to DTOs list")
	void entitiesToDTOS() {
		List<VetDTO> expected = vetService.findAll();
		List<Vet> vets = new ArrayList<>();
		expected.forEach(dto -> vets.add(vetService.dtoToEntity(dto)));

		List<VetDTO> found = vetService.entitiesToDTOS(vets);

		assertThat(found).hasSameSizeAs(expected).isEqualTo(expected);
	}

	@Test
	@Tag("findById")
	@DisplayName("Verify that we get VetDTO by his ID")
	void findById() {
		List<VetDTO> allDTO = vetService.findAll();
		VetDTO expected = allDTO.get(2);

		assertThat(vetService.findById(expected.getId())).isEqualTo(expected);
	}

	@Test
	@Tag("findAll")
	@DisplayName("Verify that the VetDTO list contain all previous elements and the new saved one")
	void findAll() {
		List<VetDTO> expected = vetService.findAll();

		assertThat(expected).doesNotContain(vetDTO);
		vetService.save(vetDTO);

		List<VetDTO> found = vetService.findAll();

		assertThat(found).contains(vetDTO).containsAll(expected);
	}

	@Test
	@Tag("save")
	@DisplayName("Verify that all VetDTO list contain the new saved one")
	void save() {
		assertThat(vetService.findAll()).doesNotContain(vetDTO);

		vetService.save(vetDTO);

		assertThat(vetService.findAll()).contains(vetDTO);
	}

}
