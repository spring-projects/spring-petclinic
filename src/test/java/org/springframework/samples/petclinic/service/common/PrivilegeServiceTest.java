package org.springframework.samples.petclinic.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
import org.springframework.samples.petclinic.repository.PrivilegeRepository;
import org.springframework.samples.petclinic.repository.RoleRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
class PrivilegeServiceTest {

	private static final Integer USER_ID = 2;

	private static final Integer ROLE_ID = 4;

	private static final Integer PRIVILEGE_ID = 5;

	private static final String PRIVILEGE_NAME = "TEST_UPDATE";

	private static final String ROLE_NAME = "ROLE_TEST";

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	private PrivilegeService privilegeService;

	private Role role;

	private RoleDTO roleDTO;

	private Privilege privilege;

	private PrivilegeDTO privilegeDTO;

	@BeforeEach
	void beforeEach() {
		privilegeService = new PrivilegeService(privilegeRepository);
		privilege = new Privilege();
		privilegeDTO = new PrivilegeDTO();
		role = new Role();
		roleDTO = new RoleDTO();

		UserDTO userDTO = userService.findById(USER_ID);
		User user = userService.dtoToEntity(userDTO);

		privilege.setId(PRIVILEGE_ID);
		privilege.setName(PRIVILEGE_NAME);
		privilege.setRoles(Collections.singleton(role));

		privilegeDTO.setId(PRIVILEGE_ID);
		privilegeDTO.setName(PRIVILEGE_NAME);
		privilegeDTO.setRoles(Collections.singleton(roleDTO));

		role.setId(ROLE_ID);
		role.setName(ROLE_NAME);
		role.setUsers(Collections.singleton(user));
		role.setPrivileges(Collections.singleton(privilege));
		user.setRoles(Collections.singleton(role));

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
		Privilege found = privilegeService.dtoToEntity(privilegeDTO);

		assertThat(found).isEqualToIgnoringGivenFields(privilegeDTO, "roles");
		assertThat(found.getRoles()).usingElementComparatorIgnoringFields("users", "privileges").contains(role);
	}

	@Test
	@Tag("entityToDTO")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entityToDTO() {
		PrivilegeDTO found = privilegeService.entityToDTO(privilege);

		assertThat(found).isEqualToIgnoringGivenFields(privilegeDTO, "roles");
		assertThat(found.getRoles()).usingElementComparatorIgnoringFields("users", "privileges").contains(roleDTO);
	}

	@Test
	@Tag("dtosToEntities")
	@DisplayName("Verify the convertion from DTOs list to Entities list")
	void dtosToEntities() {
		List<Privilege> expected = new ArrayList<>();
		List<PrivilegeDTO> privilegeDTOS = privilegeService.findAll();
		privilegeDTOS.forEach(dto -> expected.add(privilegeService.dtoToEntity(dto)));
		List<Privilege> found = privilegeService.dtosToEntities(privilegeDTOS);

		assertThat(found).hasSameSizeAs(expected);

		for (int i = 0; i < found.size(); i++) {
			assertThat(found.get(i)).isEqualToIgnoringGivenFields(expected.get(i), "roles");
			assertThat(found.get(i).getRoles()).usingElementComparatorIgnoringFields("users", "privileges")
					.contains(expected.get(i).getRoles().toArray(new Role[0]));
		}
	}

	@Test
	@Tag("entitiesToDTOS")
	@DisplayName("Verify the convertion from Entities list to DTOs list")
	void entitiesToDTOS() {
		List<PrivilegeDTO> expected = privilegeService.findAll();
		List<Privilege> privileges = new ArrayList<>();
		expected.forEach(dto -> privileges.add(privilegeService.dtoToEntity(dto)));

		List<PrivilegeDTO> found = privilegeService.entitiesToDTOS(privileges);

		assertThat(found).hasSameSizeAs(expected);

		for (int i = 0; i < found.size(); i++) {
			assertThat(found.get(i)).isEqualToIgnoringGivenFields(expected.get(i), "roles");
			assertThat(found.get(i).getRoles()).usingElementComparatorIgnoringFields("users", "privileges")
					.contains(expected.get(i).getRoles().toArray(new RoleDTO[0]));
		}
	}

	@Test
	@Tag("findById")
	@DisplayName("Verify that we get PrivilegeDTO by his ID")
	void findById() {
		List<PrivilegeDTO> privilegeDTOS = privilegeService.findAll();
		PrivilegeDTO expected = privilegeDTOS.get(2);

		PrivilegeDTO found = privilegeService.findById(expected.getId());

		assertThat(found).isEqualToIgnoringGivenFields(expected, "roles");
		assertThat(found.getRoles()).usingElementComparatorIgnoringFields("users", "privileges")
				.contains(expected.getRoles().toArray(new RoleDTO[0]));
	}

	@Test
	@Tag("findAll")
	@DisplayName("Verify that the PrivilegeDTO list contain all previous elements and the new saved one")
	void findAll() {
		List<PrivilegeDTO> expected = privilegeService.findAll();

		privilegeDTO.setRoles(new HashSet<>());
		assertThat(expected).doesNotContain(privilegeDTO);
		PrivilegeDTO saved = privilegeService.save(privilegeDTO);
		expected.add(saved);

		List<PrivilegeDTO> found = privilegeService.findAll();

		assertThat(found).usingElementComparatorOnFields("name").containsOnlyOnceElementsOf(expected);

		privilegeService.delete(saved);
	}

	@Test
	@Tag("findByName")
	@DisplayName("Verify that we get PrivilegeDTO by his Name")
	void findByName() {
		PrivilegeDTO expected = privilegeService.findById(1);

		PrivilegeDTO found = privilegeService.findByName(expected.getName());

		assertThat(found).isEqualToIgnoringGivenFields(expected, "roles");
		assertThat(found.getRoles()).usingElementComparatorIgnoringFields("users", "privileges")
				.contains(expected.getRoles().toArray(new RoleDTO[0]));

	}

	@Test
	@Tag("save")
	@DisplayName("Verify that all PrivilegeDTO list contain the new saved one")
	void save() {
		List<PrivilegeDTO> expected = privilegeService.findAll();

		assertThat(expected).isNotEmpty().doesNotContain(privilegeDTO);

		privilegeDTO.setRoles(new HashSet<>());
		PrivilegeDTO saved = privilegeService.save(privilegeDTO);

		assertThat(saved).isEqualToIgnoringGivenFields(privilegeDTO, "id", "roles");
		assertThat(saved.getRoles()).usingElementComparatorIgnoringFields("users", "privileges")
				.contains(saved.getRoles().toArray(new RoleDTO[0]));

		privilegeService.delete(saved);
	}

}
