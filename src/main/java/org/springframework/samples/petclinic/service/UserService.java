package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.RoleDTO;
import org.springframework.samples.petclinic.dto.UserDTO;
import org.springframework.samples.petclinic.model.common.Role;
import org.springframework.samples.petclinic.model.common.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Service between User entity and UserDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("UserService")
public class UserService implements BaseService<User, UserDTO> {

	private final UserRepository userRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User dtoToEntity(UserDTO dto) {

		if (dto == null) {
			return null;
		}

		User user = modelMapper.map(dto, User.class);
		user.setPassword(dto.getPassword());

		if (dto.getRoles() != null) {
			for (RoleDTO roleDTO : dto.getRoles()) {
				Role role = modelMapper.map(roleDTO, Role.class);
				user.addRole(role);
			}
		}

		return user;
	}

	@Override
	public UserDTO entityToDTO(User entity) {
		if (entity == null) {
			return null;
		}

		UserDTO userDto = modelMapper.map(entity, UserDTO.class);
		userDto.setPassword(entity.getPassword());
		userDto.setMatchingPassword(entity.getPassword());

		if (entity.getRoles() != null) {
			for (Role role : entity.getRoles()) {
				RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
				userDto.addRole(roleDTO);
			}
		}

		return userDto;
	}

	@Override
	public List<UserDTO> entitiesToDTOS(List<User> entities) {
		List<UserDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<User> dtosToEntities(List<UserDTO> dtos) {
		List<User> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public UserDTO findById(int id) {
		User user = userRepository.findById(id);

		return entityToDTO(user);
	}

	@Override
	public List<UserDTO> findAll() {
		List<User> users = userRepository.findAll();

		return entitiesToDTOS(users);
	}

	@Override
	public UserDTO save(UserDTO dto) {
		User user = dtoToEntity(dto);

		user = userRepository.save(user);

		return entityToDTO(user);
	}

	public UserDTO findByEmail(String email) {
		User user = userRepository.findByEmail(email);

		return entityToDTO(user);
	}

}
