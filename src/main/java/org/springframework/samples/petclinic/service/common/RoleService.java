package org.springframework.samples.petclinic.service.common;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.common.RoleDTO;
import org.springframework.samples.petclinic.model.common.Role;
import org.springframework.samples.petclinic.repository.RoleRepository;
import org.springframework.samples.petclinic.service.business.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Service between Role entity and RoleDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("RoleService")
public class RoleService implements BaseService<Role, RoleDTO> {

	private final RoleRepository roleRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role dtoToEntity(RoleDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, Role.class);
		}

		return new Role();
	}

	@Override
	public RoleDTO entityToDTO(Role entity) {
		if (entity != null) {
			RoleDTO roleDTO = modelMapper.map(entity, RoleDTO.class);
			roleDTO.getUsers().forEach(userDTO -> userDTO.setMatchingPassword(userDTO.getPassword()));
			return roleDTO;
		}

		return new RoleDTO();
	}

	@Override
	public List<RoleDTO> entitiesToDTOS(List<Role> entities) {
		List<RoleDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<Role> dtosToEntities(List<RoleDTO> dtos) {
		List<Role> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public List<RoleDTO> findAll() {
		Collection<Role> roles = roleRepository.findAll();
		List<RoleDTO> roleDTOS = new ArrayList<>();

		roles.forEach(role -> roleDTOS.add(entityToDTO(role)));

		return roleDTOS;
	}

	@Override
	public RoleDTO findById(int id) {
		return entityToDTO(roleRepository.findById(id));
	}

	@Override
	public RoleDTO save(RoleDTO dto) {
		Role role = dtoToEntity(dto);
		role = roleRepository.save(role);

		return entityToDTO(role);
	}

	public RoleDTO delete(RoleDTO dto) {
		Role role = dtoToEntity(dto);
		role = roleRepository.delete(role);

		return entityToDTO(role);
	}

	public RoleDTO findByName(String name) {
		return entityToDTO(roleRepository.findByName(name));
	}

}
