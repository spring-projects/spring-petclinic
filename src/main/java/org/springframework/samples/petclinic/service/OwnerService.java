package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service("OwnerService")
public class OwnerService implements BaseService<Owner, OwnerDTO> {

	private final OwnerRepository ownerRepository;
	private final PetRepository petRepository;
	private final ModelMapper modelMapper = new ModelMapper();
	private PetService petService;

	public OwnerService(OwnerRepository ownerRepository, PetRepository petRepository) {
		this.ownerRepository = ownerRepository;
		this.petRepository = petRepository;
		petService = new PetService(petRepository);
	}

	@Override
	public Owner dtoToEntity(OwnerDTO dto) {
		if(dto == null) {
			return new Owner();
		}
		Owner owner = modelMapper.map(dto, Owner.class);

		for(PetDTO petDTO: dto.getPets()) {
			owner.addPet(petService.dtoToEntity(petDTO));
		}

		return owner;
	}

	@Override
	public OwnerDTO entityToDTO(Owner entity) {
		if(entity == null) {
			return new OwnerDTO();
		}
		OwnerDTO ownerDTO = modelMapper.map(entity, OwnerDTO.class);

		for(Pet pet : entity.getPets()) {
			ownerDTO.addPet(petService.entityToDTO(pet));
		}

		return ownerDTO;
	}

	@Override
	public Collection<OwnerDTO> entitiesToDTOS(Collection<Owner> entities) {
		Collection<OwnerDTO> dtos = new HashSet<>();

		for (Owner entity : entities) {
			dtos.add(entityToDTO(entity));
		}

		return dtos;
	}

	@Override
	public Collection<Owner> dtosToEntities(Collection<OwnerDTO> dtos) {
		Collection<Owner> entities = new HashSet<>();

		for (OwnerDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}

		return entities;
	}

	public void save(OwnerDTO ownerDTO) {
		Owner owner = dtoToEntity(ownerDTO);
		ownerRepository.save(owner);
	}

	public Collection<OwnerDTO> findByLastName(String lastName) {
		Collection<Owner> owners = ownerRepository.findByLastName(lastName);
		return entitiesToDTOS(owners);
	}

	public OwnerDTO findById(int ownerId) {
		Owner owner = ownerRepository.findById(ownerId);
		return entityToDTO(owner);
	}

}
