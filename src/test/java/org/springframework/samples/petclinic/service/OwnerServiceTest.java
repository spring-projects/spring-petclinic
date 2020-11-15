package org.springframework.samples.petclinic.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.model.business.Owner;
import org.springframework.samples.petclinic.model.business.Pet;
import org.springframework.samples.petclinic.model.business.PetType;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class OwnerServiceTest {

	private final static Integer OWNER_ID = 11;

	private final static String OWNER_FIRST_NAME = "Sam";

	private final static String OWNER_LAST_NAME = "Schultz";

	private final static String OWNER_ADDRESS = "4, Evans Street";

	private final static String OWNER_CITY = "Wollongong";

	private final static String OWNER_PHONE = "1234567890";

	private final static Integer PET_ID = 11;

	private final static String PET_NAME = "bowser";

	private final static String PET_BIRTH_DATE = "2020-07-11";

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private PetTypeRepository petTypeRepository;

	@Autowired
	private VisitRepository visitRepository;

	private PetService petService;

	private OwnerService ownerService;

	private static Owner owner;

	private static OwnerDTO ownerDTO;

	private static Pet pet;

	private static PetDTO petDTO;

	@BeforeEach
	void beforeEach() {
		petService = new PetService(petRepository, petTypeRepository, visitRepository);
		ownerService = new OwnerService(ownerRepository, petRepository, petTypeRepository, visitRepository);
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

		owner = new Owner();
		owner.setId(OWNER_ID);
		owner.setFirstName(OWNER_FIRST_NAME);
		owner.setLastName(OWNER_LAST_NAME);
		owner.setAddress(OWNER_ADDRESS);
		owner.setCity(OWNER_CITY);
		owner.setTelephone(OWNER_PHONE);
		owner.addPet(pet);
		ownerDTO = new OwnerDTO();
		ownerDTO.setId(OWNER_ID);
		ownerDTO.setFirstName(OWNER_FIRST_NAME);
		ownerDTO.setLastName(OWNER_LAST_NAME);
		ownerDTO.setAddress(OWNER_ADDRESS);
		ownerDTO.setCity(OWNER_CITY);
		ownerDTO.setTelephone(OWNER_PHONE);
		ownerDTO.addPet(petDTO);
	}

	@Test
	@Tag("dtoToEntity")
	@DisplayName("Verify the convertion from DTO to Entity")
	void dtoToEntity() {
		Owner found = ownerService.dtoToEntity(ownerDTO);

		assertThat(found.getId()).isEqualTo(owner.getId());
		assertThat(found.getFirstName()).isEqualTo(owner.getFirstName());
		assertThat(found.getLastName()).isEqualTo(owner.getLastName());
		assertThat(found.getAddress()).isEqualTo(owner.getAddress());
		assertThat(found.getCity()).isEqualTo(owner.getCity());
		assertThat(found.getTelephone()).isEqualTo(owner.getTelephone());

		assertThat(found.getPets().size()).isEqualTo(owner.getPets().size());

		for (Pet pet : found.getPets()) {
			assertThat(owner.getPets()).extracting("id").contains(pet.getId());
		}

	}

	@Test
	@Tag("entityToDTO")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entityToDTO() {
		OwnerDTO found = ownerService.entityToDTO(owner);

		assertThat(found.getId()).isEqualTo(ownerDTO.getId());
		assertThat(found.getFirstName()).isEqualTo(ownerDTO.getFirstName());
		assertThat(found.getLastName()).isEqualTo(ownerDTO.getLastName());
		assertThat(found.getAddress()).isEqualTo(ownerDTO.getAddress());
		assertThat(found.getCity()).isEqualTo(ownerDTO.getCity());
		assertThat(found.getTelephone()).isEqualTo(ownerDTO.getTelephone());
		assertThat(found.getPets().size()).isEqualTo(ownerDTO.getPets().size());

		for (PetDTO petDTO : found.getPets()) {
			assertThat(ownerDTO.getPets()).extracting("id").contains(petDTO.getId());
		}
	}

	@Test
	@Tag("dtosToEntities")
	@DisplayName("Verify the convertion from DTOs list to Entities list")
	void dtosToEntities() {
		List<OwnerDTO> ownerDTOS = ownerService.findAll();
		List<Owner> expected = new ArrayList<>();
		ownerDTOS.forEach(dto -> expected.add(ownerService.dtoToEntity(dto)));

		List<Owner> found = ownerService.dtosToEntities(ownerDTOS);

		assertThat(found).hasSameSizeAs(expected).containsAll(expected);
	}

	@Test
	@Tag("entitiesToDTOS")
	@DisplayName("Verify the convertion from Entities list to DTOs list")
	void entitiesToDTOS() {
		List<OwnerDTO> expected = ownerService.findAll();
		List<Owner> owners = new ArrayList<>();
		expected.forEach(dto -> owners.add(ownerService.dtoToEntity(dto)));

		List<OwnerDTO> found = ownerService.entitiesToDTOS(owners);

		assertThat(found).hasSameSizeAs(expected).containsOnlyOnceElementsOf(expected);
	}

	@Test
	@Tag("findById")
	@DisplayName("Verify that we get OwnerDTO by his ID")
	void findById() {
		List<OwnerDTO> allDTO = ownerService.findAll();
		OwnerDTO expected = allDTO.get(2);

		OwnerDTO found = ownerService.findById(expected.getId());

		assertThat(found).isEqualToComparingFieldByField(expected);
	}

	@Test
	@Tag("findByLastName")
	@DisplayName("Verify that we get OwnerDTO by his LastName")
	void findByLastName() {
		OwnerDTO expected = ownerService.findById(1);

		Optional<OwnerDTO> found = ownerService.findByLastName(expected.getLastName()).stream().findFirst();

		found.ifPresent(dto -> assertThat(dto).isEqualToComparingFieldByField(expected));
	}

	@Test
	@Tag("findAll")
	@DisplayName("Verify that the OwnerDTO list contain all previous elements and the new saved one")
	void findAll() {
		List<OwnerDTO> expected = ownerService.findAll();

		assertThat(expected).doesNotContain(ownerDTO);
		ownerService.save(ownerDTO);

		List<OwnerDTO> found = ownerService.findAll();

		assertThat(found).hasSize(expected.size() + 1)
				.usingElementComparatorOnFields("lastName", "firstName", "address", "city", "telephone")
				.contains(ownerDTO).containsAnyElementsOf(expected);

	}

	@Test
	@Tag("save")
	@DisplayName("Verify that all OwnerDTO list contain the new saved one")
	void save() {
		assertThat(ownerService.findAll()).doesNotContain(ownerDTO);

		OwnerDTO saved = ownerService.save(ownerDTO);
		List<OwnerDTO> found = ownerService.findAll();

		assertThat(saved).isEqualToIgnoringGivenFields(ownerDTO, CommonAttribute.OWNER_ID, CommonAttribute.OWNER_PETS);
		assertThat(found).usingElementComparatorOnFields("lastName", "firstName", "address", "city", "telephone")
				.contains(ownerDTO);
	}

}
