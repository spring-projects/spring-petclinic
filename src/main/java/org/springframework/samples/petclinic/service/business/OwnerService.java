package org.springframework.samples.petclinic.service.business;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Lists;
import org.springframework.samples.petclinic.dto.business.OwnerDTO;
import org.springframework.samples.petclinic.model.business.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
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

	private final ModelMapper modelMapper = new ModelMapper();

	public OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@Override
	public Owner dtoToEntity(OwnerDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, Owner.class);
		}

		return new Owner();
	}

	@Override
	public OwnerDTO entityToDTO(Owner entity) {
		if (entity != null) {
			return modelMapper.map(entity, OwnerDTO.class);
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
		return entityToDTO(ownerRepository.findById(ownerId));
	}

	@Override
	public List<OwnerDTO> findAll() {
		return entitiesToDTOS(ownerRepository.findAll());
	}

	@Override
	public OwnerDTO save(OwnerDTO ownerDTO) {
		Owner owner = dtoToEntity(ownerDTO);
		owner = ownerRepository.save(owner);

		return entityToDTO(owner);
	}

	public OwnerDTO delete(OwnerDTO ownerDTO) {
		Owner owner = dtoToEntity(ownerDTO);
		owner = ownerRepository.delete(owner);

		return entityToDTO(owner);
	}

	public List<OwnerDTO> findByLastName(String lastName) {
		Collection<Owner> owners = ownerRepository.findByLastName(lastName);
		return entitiesToDTOS(Lists.from(owners.iterator()));
	}

}
