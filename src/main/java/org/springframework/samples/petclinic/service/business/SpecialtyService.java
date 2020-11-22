package org.springframework.samples.petclinic.service.business;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.SpecialtyDTO;
import org.springframework.samples.petclinic.model.business.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Service between Specialty entity and SpecialtyDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("SpecialityService")
public class SpecialtyService implements BaseService<Specialty, SpecialtyDTO> {

	private final SpecialtyRepository specialtyRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public SpecialtyService(SpecialtyRepository specialtyRepository) {
		this.specialtyRepository = specialtyRepository;
	}

	@Override
	public Specialty dtoToEntity(SpecialtyDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, Specialty.class);
		}

		return new Specialty();
	}

	@Override
	public SpecialtyDTO entityToDTO(Specialty entity) {
		if (entity != null) {
			return modelMapper.map(entity, SpecialtyDTO.class);
		}

		return new SpecialtyDTO();
	}

	@Override
	public List<SpecialtyDTO> entitiesToDTOS(List<Specialty> entities) {
		List<SpecialtyDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<Specialty> dtosToEntities(List<SpecialtyDTO> dtos) {
		List<Specialty> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public SpecialtyDTO findById(int id) {
		return entityToDTO(specialtyRepository.findById(id));
	}

	@Override
	public List<SpecialtyDTO> findAll() {
		return entitiesToDTOS(specialtyRepository.findAll());
	}

	@Override
	public SpecialtyDTO save(SpecialtyDTO dto) {
		Specialty specialty = dtoToEntity(dto);
		specialty = specialtyRepository.save(specialty);

		return entityToDTO(specialty);
	}

}
