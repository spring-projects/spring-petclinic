package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Lists;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Service between Owner entity and OwnerDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("OwnerService")
public class OwnerService implements BaseService<Owner, OwnerDTO> {

	private final OwnerRepository ownerRepository;

	private final PetRepository petRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	private PetService petService;

	public OwnerService(OwnerRepository ownerRepository, PetRepository petRepository,
			PetTypeRepository petTypeRepository, VisitRepository visitRepository) {
		this.ownerRepository = ownerRepository;
		this.petRepository = petRepository;
		petService = new PetService(petRepository, petTypeRepository, visitRepository);
	}

	@Override
	public Owner dtoToEntity(OwnerDTO dto) {
		if (dto != null) {
			Owner owner = modelMapper.map(dto, Owner.class);
			dto.getPets().forEach(petDTO -> {
				Pet pet = modelMapper.map(petDTO, Pet.class);
				pet.setOwner(owner);
				owner.addPet(pet);
			});
			return owner;
		}

		return new Owner();
	}

	@Override
	public OwnerDTO entityToDTO(Owner entity) {
		if (entity != null) {
			OwnerDTO ownerDTO = modelMapper.map(entity, OwnerDTO.class);
			entity.getPets().forEach(pet -> {
				PetDTO petDTO = modelMapper.map(pet, PetDTO.class);
				petDTO.setOwner(ownerDTO);
				ownerDTO.addPet(petDTO);
			});
			return ownerDTO;
		}

		return new OwnerDTO();
	}

	@Override
	public List<OwnerDTO> entitiesToDTOS(List<Owner> entities) {
		List<OwnerDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<Owner> dtosToEntities(List<OwnerDTO> dtos) {
		List<Owner> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public OwnerDTO findById(int ownerId) {
		Owner owner = ownerRepository.findById(ownerId);
		return entityToDTO(owner);
	}

	@Override
	public List<OwnerDTO> findAll() {
		return entitiesToDTOS(ownerRepository.findAll());
	}

	@Override
	public void save(OwnerDTO ownerDTO) {
		Owner owner = dtoToEntity(ownerDTO);
		ownerRepository.save(owner);
	}

	public List<OwnerDTO> findByLastName(String lastName) {
		Collection<Owner> owners = ownerRepository.findByLastName(lastName);
		return entitiesToDTOS(Lists.from(owners.iterator()));
	}

}
