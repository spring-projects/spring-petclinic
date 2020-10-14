package org.springframework.samples.petclinic.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class OwnerServiceTest {
	private final static Integer OWNER_ID = 55;
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

	private PetService petService;
	private OwnerService ownerService;
	private static Owner owner;
	private static OwnerDTO ownerDTO;
	private static Pet pet;
	private static PetDTO petDTO;

	@BeforeEach
	void beforeEach() {
		petService = new PetService(petRepository);
		ownerService = new OwnerService(ownerRepository, petRepository);
		pet = new Pet();
		pet.setId(PET_ID);
		pet.setName(PET_NAME);
		pet.setBirthDate(LocalDate.parse(PET_BIRTH_DATE));
		petDTO = new PetDTO();
		petDTO.setId(PET_ID);
		petDTO.setName(PET_NAME);
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
	void dtoToEntity() {
		Owner found = ownerService.dtoToEntity(ownerDTO);
		assertThat(found.getFirstName()).isEqualTo(owner.getFirstName());
		assertThat(found.getLastName()).isEqualTo(owner.getLastName());
		assertThat(found.getAddress()).isEqualTo(owner.getAddress());
		assertThat(found.getCity()).isEqualTo(owner.getCity());
		assertThat(found.getTelephone()).isEqualTo(owner.getTelephone());

		assertThat(found.getPets().size()).isEqualTo(owner.getPets().size());

		for(Pet pet: found.getPets()) {
			assertThat(owner.getPets()).contains(pet);
		}

	}

	@Test
	@Tag("entityToDTO")
	void entityToDTO() {
		OwnerDTO found = ownerService.entityToDTO(owner);
		assertThat(found.getFirstName()).isEqualTo(ownerDTO.getFirstName());
		assertThat(found.getLastName()).isEqualTo(ownerDTO.getLastName());
		assertThat(found.getAddress()).isEqualTo(ownerDTO.getAddress());
		assertThat(found.getCity()).isEqualTo(ownerDTO.getCity());
		assertThat(found.getTelephone()).isEqualTo(ownerDTO.getTelephone());
		assertThat(found.getPets().size()).isEqualTo(ownerDTO.getPets().size());

		for(PetDTO petDTO: found.getPets()) {
			assertThat(ownerDTO.getPets()).contains(petDTO);
		}
	}

	@Test
	@Tag("entitiesToDTOS")
	void entitiesToDTOS() {
		Collection<Owner> owners = new HashSet<>();
		Collection<OwnerDTO> expected = new HashSet<>();
		Collection<OwnerDTO> found;

		for(int i =1 ; i<5; i++) {
			OwnerDTO ownerDTO = ownerService.findById(i);
			expected.add(ownerDTO);
			owners.add(ownerService.dtoToEntity(ownerDTO));
		}

		found = ownerService.entitiesToDTOS(owners);

		assertThat(found).hasSameSizeAs(expected);

		for( int i=1; i<5; i++) {
			assertThat(expected).contains(found.iterator().next());
		}
	}

	@Test
	@Tag("dtosToEntities")
	void dtosToEntities() {
		Collection<OwnerDTO> ownerDTOS = new HashSet<>();
		Collection<Owner> expected = new HashSet<>();
		Collection<Owner> found;

		for(int i =1 ; i<5; i++) {
			OwnerDTO ownerDTO = ownerService.findById(i);
			expected.add(ownerService.dtoToEntity(ownerDTO));
			ownerDTOS.add(ownerDTO);
		}

		found = ownerService.dtosToEntities(ownerDTOS);

		assertThat(found).hasSameSizeAs(expected);

		for( int i=1; i<5; i++) {
			assertThat(expected).contains(found.iterator().next());
		}
	}

	@Test
	@Transactional
	@Tag("save")
	void save() {
		Collection<OwnerDTO> founds = ownerService.findByLastName(OWNER_LAST_NAME);
		assertThat(founds).isEmpty();

		ownerService.save(ownerDTO);

		OwnerDTO found = ownerService.findByLastName(OWNER_LAST_NAME).stream().findFirst().get();

		assertThat(found).isEqualToIgnoringGivenFields(ownerDTO, "id");
	}

	@Test
	@Tag("findByLastName")
	void findByLastName() {
		OwnerDTO expected = ownerService.findById(1);
		OwnerDTO found = ownerService.findByLastName(expected.getLastName()).stream().findFirst().get();

		assertThat(found).isEqualToComparingFieldByField(expected);

	}

	@Test
	@Tag("findById")
	void findById() {
		ownerService.save(ownerDTO);
		OwnerDTO expected = ownerService.findByLastName(OWNER_LAST_NAME).stream().findFirst().get();
		OwnerDTO found = ownerService.findById(expected.getId());

		assertThat(found).isEqualToComparingFieldByField(expected);
	}
}
