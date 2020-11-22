package org.springframework.samples.petclinic.service.business;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.model.business.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Service between PetType entity and PetTypeDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("PerTypeService")
public class PetTypeService implements BaseService<PetType, PetTypeDTO> {

	private final PetTypeRepository petTypeRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public PetTypeService(PetTypeRepository petTypeRepository) {
		this.petTypeRepository = petTypeRepository;
	}

	@Override
	public PetType dtoToEntity(PetTypeDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, PetType.class);
		}

		return new PetType();
	}

	@Override
	public PetTypeDTO entityToDTO(PetType entity) {
		if (entity != null) {
			return modelMapper.map(entity, PetTypeDTO.class);
		}

		return new PetTypeDTO();
	}

	@Override
	public List<PetTypeDTO> entitiesToDTOS(List<PetType> entities) {
		List<PetTypeDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<PetType> dtosToEntities(List<PetTypeDTO> dtos) {
		List<PetType> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public PetTypeDTO findById(int id) {
		return entityToDTO(petTypeRepository.findById(id));
	}

	@Override
	public List<PetTypeDTO> findAll() {
		return entitiesToDTOS(petTypeRepository.findAll());
	}

	@Override
	public PetTypeDTO save(PetTypeDTO dto) {
		PetType petType = dtoToEntity(dto);
		petType = petTypeRepository.save(petType);
		return entityToDTO(petType);
	}

}
