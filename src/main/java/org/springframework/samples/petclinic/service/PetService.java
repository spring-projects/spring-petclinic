package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

	private final PetTypeRepository petTypeRepository;

	private final VisitService visitService;

	private final ModelMapper modelMapper = new ModelMapper();

	public PetService(PetRepository petRepository, PetTypeRepository petTypeRepository,
			VisitRepository visitRepository) {
		this.petRepository = petRepository;
		this.petTypeRepository = petTypeRepository;
		this.visitService = new VisitService(visitRepository);
		this.petTypeService = new PetTypeService(petTypeRepository);
	}

	@Override
	public Pet dtoToEntity(PetDTO dto) {
		if (dto != null) {
			Pet pet = modelMapper.map(dto, Pet.class);
			dto.getVisits().forEach(visitDTO -> pet.addVisit(visitService.dtoToEntity(visitDTO)));
			return pet;
		}

		return new Pet();
	}

	@Override
	public PetDTO entityToDTO(Pet entity) {
		if (entity != null) {
			PetDTO petDTO = modelMapper.map(entity, PetDTO.class);
			entity.getVisits().forEach(visit -> petDTO.addVisit(visitService.entityToDTO(visit)));
			return petDTO;
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
		return entityToDTO(pet);
	}

	@Override
	public List<PetDTO> findAll() {
		return entitiesToDTOS(petRepository.findAll());
	}

	@Override
	public void save(PetDTO petDTO) {
		petRepository.save(dtoToEntity(petDTO));
	}

	public List<PetTypeDTO> findPetTypes() {
		List<PetType> petTypes = petRepository.findPetTypes();

		return petTypeService.entitiesToDTOS(petTypes);
	}

}
