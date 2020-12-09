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
import org.springframework.samples.petclinic.repository.RoleRepository;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
class UserServiceIntegrationTest {

	private final static Integer USER_ID = 11;

	private final static String USER_FIRST_NAME = "Sam";

	private final static String USER_LAST_NAME = "Schultz";

	private final static String USER_EMAIL = "Sam.Schultz@petclinic.com";

	private final static String USER_PASSWORD = "PASSWORD_TEST9879879$^m$*ùm*^$*ù";

	private final static String USER_ADDRESS = "4, Evans Street";

	private final static String USER_CITY = "Wollongong";

	private final static String USER_ZIPCODE = "65879";

	private final static String USER_COUNTRY = "USA";

	private final static String USER_PHONE = "1234567890";

	private final static Integer ROLE_ID = 4;

	private final static Integer PRIVILEGE_ID = 3;

	private final static String ROLE_NAME = "ROLE_TEST";

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PrivilegeService privilegeService;

	@Autowired
	private RoleService roleService;

	private UserService userService;

	private User user;

	private UserDTO userDTO;

	private Role role;

	private RoleDTO roleDTO;

	private Privilege privilege;

	private PrivilegeDTO privilegeDTO;

	@BeforeEach
	void beforeEach() {
		userService = new UserService(userRepository, roleRepository);
		user = new User();
		userDTO = new UserDTO();

		roleDTO = roleService.findById(2);
		role = roleService.dtoToEntity(roleDTO);

		user.setId(USER_ID);
		user.setFirstName(USER_FIRST_NAME);
		user.setLastName(USER_LAST_NAME);
		user.setEmail(USER_EMAIL);
		user.setPassword(USER_PASSWORD);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		user.setTelephone(USER_PHONE);
		user.setStreet1(USER_ADDRESS);
		user.setCity(USER_CITY);
		user.setZipCode(USER_ZIPCODE);
		user.setCountry(USER_COUNTRY);
		user.setRoles(Collections.singleton(role));
		role.addUser(user);

		userDTO.setId(USER_ID);
		userDTO.setFirstName(USER_FIRST_NAME);
		userDTO.setLastName(USER_LAST_NAME);
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setMatchingPassword(USER_PASSWORD);
		userDTO.setAccountNonExpired(true);
		userDTO.setAccountNonLocked(true);
		userDTO.setCredentialsNonExpired(true);
		userDTO.setEnabled(true);
		userDTO.setTelephone(USER_PHONE);
		userDTO.setStreet1(USER_ADDRESS);
		userDTO.setCity(USER_CITY);
		userDTO.setZipCode(USER_ZIPCODE);
		userDTO.setCountry(USER_COUNTRY);
		userDTO.setRoles(Collections.singleton(roleDTO));
		roleDTO.addUser(userDTO);

	}

	@Test
	@Tag("findById")
	@DisplayName("Verify that we get UserDTO by his ID")
	void findById() {
		List<UserDTO> userDTOS = userService.findAll();
		UserDTO expected = userDTOS.get(2);

		UserDTO found = userService.findById(expected.getId());

		assertThat(found).isEqualToIgnoringGivenFields(expected, "roles");
		assertThat(found.getRoles()).usingElementComparatorIgnoringFields("users", "privileges")
				.contains(expected.getRoles().toArray(new RoleDTO[0]));
	}

	@Test
	@Tag("findAll")
	@DisplayName("Verify that the UserDTO list contain all previous elements and the new saved one")
	void findAll() {
		List<UserDTO> expected = userService.findAll();
		userDTO.setRoles(new HashSet<>());

		assertThat(expected).doesNotContain(userDTO);

		UserDTO saved = userService.save(userDTO);
		expected.add(saved);
		List<UserDTO> found = userService.findAll();

		assertThat(found).usingElementComparatorIgnoringFields("roles").containsOnlyOnceElementsOf(expected);

		userService.delete(saved);
	}

	@Test
	@Tag("save")
	@DisplayName("Verify that all UserDTO list contain the new saved one")
	void save() {
		Collection<UserDTO> expected = userService.findAll();
		assertThat(expected).doesNotContain(userDTO);

		UserDTO saved = userService.save(userDTO);

		assertThat(saved).isEqualToIgnoringGivenFields(userDTO, "id", "roles");
		assertThat(saved.getRoles()).usingElementComparatorIgnoringFields("users", "privileges")
				.contains(userDTO.getRoles().toArray(new RoleDTO[0]));

		userService.delete(saved);
	}

}
