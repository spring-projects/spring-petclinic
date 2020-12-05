package org.springframework.samples.petclinic.service.common;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.common.PrivilegeDTO;
import org.springframework.samples.petclinic.model.common.Privilege;
import org.springframework.samples.petclinic.repository.PrivilegeRepository;
import org.springframework.samples.petclinic.service.business.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Service between Privilege entity and PrivilegeDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("PrivilegeService")
public class PrivilegeService implements BaseService<Privilege, PrivilegeDTO> {

	private final PrivilegeRepository privilegeRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public PrivilegeService(PrivilegeRepository privilegeRepository) {
		this.privilegeRepository = privilegeRepository;
	}

	@Override
	public Privilege dtoToEntity(PrivilegeDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, Privilege.class);
		}

		return new Privilege();
	}

	@Override
	public PrivilegeDTO entityToDTO(Privilege entity) {
		if (entity != null) {
			return modelMapper.map(entity, PrivilegeDTO.class);
		}

		return new PrivilegeDTO();
	}

	@Override
	public List<PrivilegeDTO> entitiesToDTOS(List<Privilege> entities) {
		List<PrivilegeDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<Privilege> dtosToEntities(List<PrivilegeDTO> dtos) {
		List<Privilege> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public PrivilegeDTO findById(int id) {
		return entityToDTO(privilegeRepository.findById(id));
	}

	@Override
	public List<PrivilegeDTO> findAll() {
		Collection<Privilege> privileges = privilegeRepository.findAll();
		List<PrivilegeDTO> privilegeDTOS = new ArrayList<>();

		privileges.forEach(privilege -> {
			privilegeDTOS.add(entityToDTO(privilege));
		});

		return privilegeDTOS;
	}

	@Override
	public PrivilegeDTO save(PrivilegeDTO dto) {
		Privilege privilege = dtoToEntity(dto);
		privilege = privilegeRepository.save(privilege);

		return entityToDTO(privilege);
	}

	public PrivilegeDTO findByName(String name) {
		return entityToDTO(privilegeRepository.findByName(name));
	}

}
