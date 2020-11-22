package org.springframework.samples.petclinic.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.model.business.Owner;
import org.springframework.samples.petclinic.model.business.Pet;
import org.springframework.samples.petclinic.model.business.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.business.OwnerService;
import org.springframework.samples.petclinic.service.business.PetService;
import org.springframework.samples.petclinic.service.business.PetTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class PetServiceTest {

	private final static Integer OWNER_ID = 5;

	private final static Integer PET_ID = 14;

	private final static String PET_NAME = "bowser";

	private final static String PET_BIRTH_DATE = "2020-07-11";

	private final static Integer PET_TYPE_ID = 4;

	private final static String PET_TYPE_NAME = "dinausor";

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private PetTypeRepository petTypeRepository;

	@Autowired
	private VisitRepository visitRepository;

	private PetService petService;

	private static Owner owner;

	private static Pet pet;

	private static PetDTO petDTO;

	@BeforeEach
	void beforeEach() {
		this.petService = new PetService(petRepository, petTypeRepository, visitRepository);

		PetTypeService petTypeService = new PetTypeService(petTypeRepository);
		Collection<PetTypeDTO> petTypeDTOS = petService.findPetTypes();
		PetTypeDTO petTypeDTO = petTypeDTOS.stream().findFirst().get();
		PetType petType = petTypeService.dtoToEntity(petTypeDTO);
		pet = new Pet();
		pet.setId(PET_ID);
		pet.setName(PET_NAME);
		pet.setType(petType);
		pet.setBirthDate(LocalDate.parse(PET_BIRTH_DATE));
		petDTO = new PetDTO();
		petDTO.setId(PET_ID);
		petDTO.setName(PET_NAME);
		petDTO.setType(petTypeDTO);
		petDTO.setBirthDate(LocalDate.parse(PET_BIRTH_DATE));

		OwnerDTO ownerDTO = ownerService.findById(OWNER_ID);
		ownerDTO.addPet(petDTO);

		pet.setOwner(ownerService.dtoToEntity(ownerDTO));
		petDTO.setOwner(ownerDTO);
	}

	@Test
	@Tag("dtoToEntity")
	@DisplayName("Verify the convertion from DTO to Entity")
	void dtoToEntity() {
		Pet found = petService.dtoToEntity(petDTO);

		assertThat(found.getId()).isEqualTo(pet.getId());
		assertThat(found.getName()).isEqualTo(pet.getName());
		assertThat(found.getBirthDate()).isEqualTo(pet.getBirthDate());
		assertThat(found.getType()).isEqualTo(pet.getType());
		assertThat(found.getOwner().getId()).isEqualTo(pet.getOwner().getId());
		assertThat(found.getVisits()).isEqualTo(pet.getVisits());
	}

	@Test
	@Tag("entityToDTO")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entityToDTO() {
		PetDTO found = petService.entityToDTO(pet);

		assertThat(found.getId()).isEqualTo(petDTO.getId());
		assertThat(found.getName()).isEqualTo(petDTO.getName());
		assertThat(found.getBirthDate()).isEqualTo(petDTO.getBirthDate());
		assertThat(found.getType()).isEqualTo(petDTO.getType());
		assertThat(found.getOwner().getId()).isEqualTo(petDTO.getOwner().getId());
		assertThat(found.getVisits()).isEqualTo(petDTO.getVisits());
	}

	@Test
	@Tag("dtosToEntities")
	@DisplayName("Verify the convertion from DTOs list to Entities list")
	void dtosToEntities() {
		List<Pet> expected = petRepository.findAll();
		List<PetDTO> allDTO = petService.findAll();

		List<Pet> found = petService.dtosToEntities(allDTO);

		assertThat(found).hasSameSizeAs(expected).isEqualTo(expected);
	}

	@Test
	@Tag("entitiesToDTOS")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entitiesToDTOS() {
		List<Pet> allEntity = petRepository.findAll();
		List<PetDTO> expected = petService.findAll();

		List<PetDTO> found = petService.entitiesToDTOS(allEntity);

		assertThat(found).hasSameSizeAs(expected).isEqualTo(expected);
	}

	@Test
	@Tag("findById")
	@DisplayName("Verify that we get PetDTO by his ID")
	void findById() {
		List<PetDTO> allDTO = petService.findAll();
		PetDTO expected = allDTO.get(2);

		assertThat(petService.findById(expected.getId())).isEqualTo(expected);
	}

	@Test
	@Tag("findAll")
	@DisplayName("Verify that the PetDTO list contain all previous elements and the new saved one")
	void findAll() {
		List<PetDTO> expected = petService.findAll();

		assertThat(expected).doesNotContain(petDTO);
		petService.save(petDTO);

		List<PetDTO> found = petService.findAll();

		assertThat(found).hasSize(expected.size() + 1).contains(petDTO).containsAll(expected);

	}

	@Test
	@Tag("save")
	@DisplayName("Verify that all PetDTO list contain the new saved one")
	void save() {
		assertThat(petService.findAll()).doesNotContain(petDTO);

		PetDTO saved = petService.save(petDTO);

		assertThat(saved).isEqualToIgnoringGivenFields(petDTO, "id");
		assertThat(petService.findAll()).containsAnyOf(petDTO);
	}

}
