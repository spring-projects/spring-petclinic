package org.springframework.samples.petclinic.service.common;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.dto.common.PrivilegeDTO;
import org.springframework.samples.petclinic.dto.common.RoleDTO;
import org.springframework.samples.petclinic.dto.common.UserDTO;
import org.springframework.samples.petclinic.model.common.Privilege;
import org.springframework.samples.petclinic.model.common.Role;
import org.springframework.samples.petclinic.model.common.User;
import org.springframework.samples.petclinic.repository.RoleRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest
@RunWith(SpringRunner.class)
class RoleServiceTest {

	private final static Integer USER_ID = 2;

	private final static Integer ROLE_ID = 4;

	private final static Integer PRIVILEGE_ID = 3;

	private final static String ROLE_NAME = "ROLE_TEST";

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PrivilegeService privilegeService;

	private RoleService roleService;

	private User user;

	private UserDTO userDTO;

	private Role role;

	private RoleDTO roleDTO;

	private Privilege privilege;

	private PrivilegeDTO privilegeDTO;

	@BeforeEach
	void beforeEach() {
		userDTO = userService.findById(USER_ID);
		user = userService.dtoToEntity(userDTO);
		privilegeDTO = privilegeService.findById(PRIVILEGE_ID);
		privilege = privilegeService.dtoToEntity(privilegeDTO);

		roleService = new RoleService(roleRepository);
		role = new Role();
		role.setId(ROLE_ID);
		role.setName(ROLE_NAME);
		role.setUsers(Collections.singleton(user));
		role.setPrivileges(Collections.singleton(privilege));
		user.setRoles(Collections.singleton(role));

		roleDTO = new RoleDTO();
		roleDTO.setId(ROLE_ID);
		roleDTO.setName(ROLE_NAME);
		roleDTO.setUsers(Collections.singleton(userDTO));
		roleDTO.setPrivileges(Collections.singleton(privilegeDTO));
		userDTO.setRoles(Collections.singleton(roleDTO));
	}

	@Test
	@Tag("dtoToEntity")
	@DisplayName("Verify the convertion from DTO to Entity")
	void dtoToEntity() {
		Role found = roleService.dtoToEntity(roleDTO);

		assertThat(found.getId()).isEqualTo(role.getId());
		assertThat(found.getName()).isEqualTo(role.getName());
		assertThat(found.getUsers()).usingElementComparatorIgnoringFields("roles").contains(user);
		assertThat(found.getPrivileges()).usingElementComparatorIgnoringFields("roles").contains(privilege);
	}

	@Test
	@Tag("entityToDTO")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entityToDTO() {
		RoleDTO found = roleService.entityToDTO(role);

		assertThat(found.getId()).isEqualTo(roleDTO.getId());
		assertThat(found.getName()).isEqualTo(roleDTO.getName());
		assertThat(found.getUsers()).usingElementComparatorIgnoringFields("roles").contains(userDTO);
		assertThat(found.getPrivileges()).usingElementComparatorIgnoringFields("roles").contains(privilegeDTO);
	}

	@Test
	@Tag("dtosToEntities")
	@DisplayName("Verify the convertion from DTOs list to Entities list")
	void dtosToEntities() {
		List<RoleDTO> roleDTOS = roleService.findAll();
		List<Role> expected = new ArrayList<>();
		roleDTOS.forEach(dto -> expected.add(roleService.dtoToEntity(dto)));
		List<Role> found = roleService.dtosToEntities(roleDTOS);

		assertThat(found).hasSameSizeAs(expected);

		for (int i = 0; i < found.size(); i++) {
			assertThat(found.get(i).getId()).isEqualTo(expected.get(i).getId());
			assertThat(found.get(i).getName()).isEqualTo(expected.get(i).getName());
			assertThat(found.get(i).getUsers()).usingElementComparatorIgnoringFields("roles")
					.contains(expected.get(i).getUsers().toArray(new User[0]));
			assertThat(found.get(i).getPrivileges()).usingElementComparatorIgnoringFields("roles")
					.contains(expected.get(i).getPrivileges().toArray(new Privilege[0]));
		}
	}

	@Test
	@Tag("entitiesToDTOS")
	@DisplayName("Verify the convertion from Entities list to DTOs list")
	void entitiesToDTOS() {
		List<RoleDTO> expected = roleService.findAll();
		List<Role> roles = new ArrayList<>();
		expected.forEach(dto -> roles.add(roleService.dtoToEntity(dto)));

		List<RoleDTO> found = roleService.entitiesToDTOS(roles);

		assertThat(found).hasSameSizeAs(expected);

		for (int i = 0; i < found.size(); i++) {
			assertThat(found.get(i).getId()).isEqualTo(expected.get(i).getId());
			assertThat(found.get(i).getName()).isEqualTo(expected.get(i).getName());
			assertThat(found.get(i).getUsers()).usingElementComparatorIgnoringFields("roles")
					.contains(expected.get(i).getUsers().toArray(new UserDTO[0]));
			assertThat(found.get(i).getPrivileges()).usingElementComparatorIgnoringFields("roles")
					.contains(expected.get(i).getPrivileges().toArray(new PrivilegeDTO[0]));
		}
	}

	@Test
	@Tag("findById")
	@DisplayName("Verify that we get RoleDTO by his ID")
	void findById() {
		List<RoleDTO> allDTO = roleService.findAll();
		RoleDTO expected = allDTO.get(2);

		RoleDTO found = roleService.findById(expected.getId());

		assertThat(found.getId()).isEqualTo(expected.getId());
		assertThat(found.getName()).isEqualTo(expected.getName());
		assertThat(found.getUsers()).usingElementComparatorIgnoringFields("roles")
				.contains(expected.getUsers().toArray(new UserDTO[0]));
		assertThat(found.getPrivileges()).usingElementComparatorIgnoringFields("roles")
				.contains(expected.getPrivileges().toArray(new PrivilegeDTO[0]));
	}

	@Test
	@Tag("findAll")
	@DisplayName("Verify that the RoleDTO list contain all previous elements and the new saved one")
	void findAll() {
		List<RoleDTO> expected = roleService.findAll();

		roleDTO.setUsers(new HashSet<>());
		assertThat(expected).doesNotContain(roleDTO);
		RoleDTO saved = roleService.save(roleDTO);

		List<RoleDTO> found = roleService.findAll();

		assertThat(found).hasSize(expected.size() + 1).usingElementComparatorOnFields("id", "name")
				.containsOnlyOnceElementsOf(expected).contains(roleDTO);

		roleService.delete(saved);
	}

	@Test
	@Tag("findByName")
	@DisplayName("Verify that we get RoleDTO by his Name")
	void findByName() {
		RoleDTO expected = roleService.findById(1);

		RoleDTO found = roleService.findByName(expected.getName());

		assertThat(found.getId()).isEqualTo(expected.getId());
		assertThat(found.getName()).isEqualTo(expected.getName());
		assertThat(found.getUsers()).usingElementComparatorIgnoringFields("roles")
				.contains(expected.getUsers().toArray(new UserDTO[0]));
		assertThat(found.getPrivileges()).usingElementComparatorIgnoringFields("roles")
				.contains(expected.getPrivileges().toArray(new PrivilegeDTO[0]));

	}

	@Test
	@Tag("save")
	@DisplayName("Verify that all RoleDTO list contain the new saved one")
	void save() {
		assertThat(roleService.findAll()).doesNotContain(roleDTO);
		roleDTO.setUsers(new HashSet<>());
		RoleDTO saved = roleService.save(roleDTO);
		RoleDTO found = roleService.findById(saved.getId());

		assertThat(found.getId()).isEqualTo(saved.getId());
		assertThat(found.getName()).isEqualTo(saved.getName());
		assertThat(found.getUsers()).usingElementComparatorIgnoringFields("roles")
				.contains(saved.getUsers().toArray(new UserDTO[0]));
		assertThat(found.getPrivileges()).usingElementComparatorIgnoringFields("roles")
				.contains(saved.getPrivileges().toArray(new PrivilegeDTO[0]));

		RoleDTO deleted = roleService.delete(saved);
	}

}
