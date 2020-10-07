package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service("PetService")
public class PetService implements BaseService<Pet,PetDTO>{
	private final PetRepository petRepository;
	private final ModelMapper modelMapper = new ModelMapper();

	public PetService(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	@Override
	public Pet dtoToEntity(PetDTO dto) {
		return modelMapper.map(dto, Pet.class);
	}

	@Override
	public PetDTO entityToDTO(Pet entity) {
		return modelMapper.map(entity, PetDTO.class);
	}

	@Override
	public Collection<PetDTO> entitiesToDTOS(Collection<Pet> entities) {
		Collection<PetDTO> dtos = new HashSet<>();

		for(Pet entity:entities) {
			dtos.add(entityToDTO(entity));
		}
		return dtos;
	}

	@Override
	public Collection<Pet> dtosToEntities(Collection<PetDTO> dtos) {
		Collection<Pet> entities = new HashSet<>();

		for(PetDTO dto: dtos) {
			entities.add(dtoToEntity(dto));
		}

		return entities;
	}

	public void save(PetDTO petDTO) {
		Pet pet = dtoToEntity(petDTO);
		petRepository.save(pet);
	}

	public PetDTO findById(int petId) {
		Pet pet = petRepository.findById(petId);
		return entityToDTO(pet);
	}

}

