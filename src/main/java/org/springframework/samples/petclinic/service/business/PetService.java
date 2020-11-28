package org.springframework.samples.petclinic.service.business;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.business.OwnerDTO;
import org.springframework.samples.petclinic.dto.business.PetDTO;
import org.springframework.samples.petclinic.dto.business.PetTypeDTO;
import org.springframework.samples.petclinic.model.business.Owner;
import org.springframework.samples.petclinic.model.business.Pet;
import org.springframework.samples.petclinic.model.business.PetType;
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

	private final VisitService visitService;

	private final ModelMapper modelMapper = new ModelMapper();

	public PetService(PetRepository petRepository, PetTypeRepository petTypeRepository,
			VisitRepository visitRepository) {
		this.petRepository = petRepository;
		this.visitService = new VisitService(visitRepository);
		this.petTypeService = new PetTypeService(petTypeRepository);
	}

	@Override
	public Pet dtoToEntity(PetDTO dto) {
		if (dto != null) {
			Pet pet = modelMapper.map(dto, Pet.class);
			PetType petType = modelMapper.map(dto.getType(), PetType.class);
			Owner owner = modelMapper.map(dto.getOwner(), Owner.class);

			dto.getVisits().forEach(visitDTO -> pet.addVisit(visitService.dtoToEntity(visitDTO)));

			dto.getOwner().getPets().forEach(petDTO -> {
				if (dto.getId() == null || petDTO.getId().equals(dto.getId())) {
					owner.addPet(pet);
				}
				else {
					Pet otherPet = modelMapper.map(petDTO, Pet.class);
					otherPet.setOwner(owner);
					owner.addPet(otherPet);
				}
			});
			pet.setOwner(owner);
			pet.setType(petType);
			return pet;
		}

		return new Pet();
	}

	@Override
	public PetDTO entityToDTO(Pet entity) {
		if (entity != null) {
			PetDTO petDTO = modelMapper.map(entity, PetDTO.class);
			PetTypeDTO petTypeDTO = modelMapper.map(entity.getType(), PetTypeDTO.class);
			OwnerDTO ownerDTO = modelMapper.map(entity.getOwner(), OwnerDTO.class);

			petRepository.findByOwnerId(ownerDTO.getId()).forEach(pet -> {
				PetDTO otherPetDTO = modelMapper.map(pet, PetDTO.class);
				otherPetDTO.setOwner(ownerDTO);
				ownerDTO.addPet(otherPetDTO);
			});

			entity.getVisits().forEach(visit -> petDTO.addVisit(visitService.entityToDTO(visit)));

			petDTO.setOwner(ownerDTO);
			petDTO.setType(petTypeDTO);
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
