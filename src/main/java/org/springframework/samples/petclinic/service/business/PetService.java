package org.springframework.samples.petclinic.service.business;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.business.PetDTO;
import org.springframework.samples.petclinic.dto.business.PetTypeDTO;
import org.springframework.samples.petclinic.model.business.Owner;
import org.springframework.samples.petclinic.model.business.Pet;
import org.springframework.samples.petclinic.model.business.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Simple Service between Pet entity and PetDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("PetService")
public class PetService implements BaseService<Pet, PetDTO> {

	private final PetRepository petRepository;

	private final PetTypeService petTypeService;

	private final ModelMapper modelMapper = new ModelMapper();

	public PetService(PetRepository petRepository, PetTypeRepository petTypeRepository) {
		this.petRepository = petRepository;
		this.petTypeService = new PetTypeService(petTypeRepository);
	}

	@Override
	public Pet dtoToEntity(PetDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, Pet.class);
		}

		return new Pet();
	}

	@Override
	public PetDTO entityToDTO(Pet entity) {
		if (entity != null) {
			return modelMapper.map(entity, PetDTO.class);
		}

		return new PetDTO();
	}

	@Override
	public List<PetDTO> entitiesToDTOS(List<Pet> entities) {
		List<PetDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<Pet> dtosToEntities(List<PetDTO> dtos) {
		List<Pet> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public PetDTO findById(int petId) {
		Pet pet = petRepository.findById(petId);
		Owner owner = pet.getOwner();
		owner.setPets(new HashSet<>());
		petRepository.findByOwnerId(owner.getId()).forEach(owner::addPet);
		return entityToDTO(pet);
	}

	@Override
	public List<PetDTO> findAll() {
		return entitiesToDTOS(petRepository.findAll());
	}

	@Override
	public PetDTO save(PetDTO petDTO) {
		Pet pet = dtoToEntity(petDTO);
		pet = petRepository.save(pet);

		return entityToDTO(pet);
	}

	public List<PetDTO> findByOwnerId(int id) {
		return entitiesToDTOS(petRepository.findByOwnerId(id));
	}

	public List<PetTypeDTO> findPetTypes() {
		List<PetType> petTypes = petRepository.findPetTypes();

		return petTypeService.entitiesToDTOS(petTypes);
	}

}
