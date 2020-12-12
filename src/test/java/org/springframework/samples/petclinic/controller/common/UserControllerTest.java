package org.springframework.samples.petclinic.controller.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.controller.WebSecurityConfig;
import org.springframework.samples.petclinic.dto.common.*;
import org.springframework.samples.petclinic.model.common.*;
import org.springframework.samples.petclinic.service.common.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link UserController}
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest extends WebSocketSender {

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

	private final static String ROLE_NAME = "ROLE_TEST";

	private final static Integer PRIVILEGE_ID = 3;

	private final static String PRIVILEGE_NAME = "PRIVILEGE_TEST";

	private static final String PROVIDER_TEST_NAME = "Provider Test";

	private static final Integer PROVIDER_TEST_ID = 11;

	private static final String EMAIL_TEST = "eduardo.rodriguez@petclinic.com";

	private static final String PASSWORD_TEST = "$2a$10$8KypNYtPopFo8Sk5jbKJ4.lCKeBhdApsrkmFfhwjB8nCls8qpzjZG";

	private static final String TOKEN_TEST = UUID.randomUUID().toString();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	private SecurityServiceImpl securityService;

	@MockBean
	private SimpMessagingTemplate simpMessagingTemplate;

	@MockBean
	private UserService userService;

	@MockBean
	private CredentialService credentialService;

	@MockBean
	private RoleService roleService;

	@MockBean
	private EmailService emailService;

	private User user;

	private UserDTO userDTO;

	private Role role;

	private RoleDTO roleDTO;

	private Privilege privilege;

	private PrivilegeDTO privilegeDTO;

	private Credential credential;

	private CredentialDTO credentialDTO;

	private AuthProvider authProvider;

	@BeforeEach
	void setup() {
		user = new User();
		userDTO = new UserDTO();
		privilege = new Privilege();
		privilegeDTO = new PrivilegeDTO();
		role = new Role();
		roleDTO = new RoleDTO();

		privilege.setId(PRIVILEGE_ID);
		privilege.setName(PRIVILEGE_NAME);
		privilege.setRoles(Collections.singleton(role));
		privilegeDTO.setId(PRIVILEGE_ID);
		privilegeDTO.setName(PRIVILEGE_NAME);
		privilegeDTO.setRoles(Collections.singleton(roleDTO));

		role.setId(ROLE_ID);
		role.setName(ROLE_NAME);
		role.setPrivileges(Collections.singleton(privilege));
		role.setUsers(Collections.singleton(user));
		roleDTO.setId(ROLE_ID);
		roleDTO.setName(ROLE_NAME);
		roleDTO.setPrivileges(Collections.singleton(privilegeDTO));
		roleDTO.setUsers(Collections.singleton(userDTO));

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

		authProvider = new AuthProvider(PROVIDER_TEST_ID, PROVIDER_TEST_NAME);
		credential = new Credential(PROVIDER_TEST_ID, EMAIL_TEST, PASSWORD_TEST, true);
		credential.setToken(TOKEN_TEST);
		credentialDTO = new CredentialDTO(PROVIDER_TEST_NAME, EMAIL_TEST, PASSWORD_TEST, true);
		credentialDTO.setToken(TOKEN_TEST);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, credential.getTokenExpiration());
		credential.setExpiration(cal.getTime());
		credentialDTO.setExpiration(cal.getTime());

		doNothing().when(emailService).sendMailAsynch(any(MessageDTO.class), any(Locale.class));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initCreationForm")
	@DisplayName("Verify that we get the right registration view and the right attribute name")
	void initCreationForm() throws Exception {
		mockMvc.perform(get(CommonEndPoint.REGISTER)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.USER_REGISTRATION))
				.andExpect(model().attributeExists(CommonAttribute.USER));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that call the right view with parameters when attempt to create User with right parameters")
	void processCreationForm() throws Exception {
		given(userService.existByEmail(anyString())).willReturn(false);
		given(userService.save(any(UserDTO.class))).willReturn(userDTO);
		given(roleService.findByName(anyString())).willReturn(roleDTO);
		given(credentialService.save(any(CredentialDTO.class))).willReturn(credentialDTO);

		mockMvc.perform(post(CommonEndPoint.REGISTER).flashAttr(CommonAttribute.USER, userDTO))
				.andExpect(status().is3xxRedirection()).andExpect(view().name(CommonView.HOME + user.getId()));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initLoginForm")
	@DisplayName("Verify that we get the right view for login")
	void initLoginForm() throws Exception {
		mockMvc.perform(get(CommonEndPoint.LOGIN)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.USER_LOGIN)).andExpect(model().attributeExists(CommonAttribute.USER))
				.andExpect(model().attributeExists(CommonAttribute.URLS));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("postLogin")
	@DisplayName("Verify that we get the right parameters and view after login with email and password")
	void postLogin() throws Exception {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDTO, USER_PASSWORD, userDTO.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

		mockMvc.perform(get(CommonEndPoint.LOGIN_SUCCESS)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(CommonView.HOME));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("postLoginOAUTH2")
	@DisplayName("Verify that we get UserDTO by his ID")
	void postLoginOAUTH2() {
		given(credentialService.findByAuthentication(any(OAuth2AuthenticationToken.class))).willReturn(credentialDTO);
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("confirmUserAccount")
	@DisplayName("Verify that we get UserDTO by his ID")
	void confirmUserAccount() {
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("logout")
	@DisplayName("Verify the User is disconnected")
	void logout() throws Exception {
		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();

		mockMvc.perform(get(CommonEndPoint.LOGOUT)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(CommonEndPoint.LOGOUT_SUCCESS));

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initUpdateOwnerForm")
	@DisplayName("Verify that we get the right update view and the right attribute name when user is connected")
	void givenConnectedUser_whenCallUpdateForm_ThenRedirectUpdateViewWithUser() throws Exception {
		given(userService.findByEmail(anyString())).willReturn(userDTO);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDTO, USER_PASSWORD, userDTO.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

		mockMvc.perform(get(CommonEndPoint.USERS_EDIT)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.USER_UPDATE)).andExpect(model().attributeExists(CommonAttribute.USER))
				.andExpect(model().attributeExists(CommonAttribute.USER_ID));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initUpdateOwnerForm")
	@DisplayName("Verify that we get the right update view and the right attribute name when user is connected")
	void givenNoConnectedUser_whenCallUpdateForm_ThenRedirectHome() throws Exception {
		given(userService.findByEmail(anyString())).willReturn(userDTO);

		mockMvc.perform(get(CommonEndPoint.USERS_EDIT)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(CommonView.HOME)).andExpect(model().attributeDoesNotExist(CommonAttribute.USER))
				.andExpect(model().attributeDoesNotExist(CommonAttribute.USER_ID));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateOwnerForm")
	@DisplayName("Verify that we get UserDTO by his ID")
	void processUpdateOwnerForm() {
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("showOwner")
	@DisplayName("Verify that we get UserDTO by his ID")
	void showOwner() {
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("editPassword")
	@DisplayName("Verify that we get UserDTO by his ID")
	void editPassword() {
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("updatePassword")
	@DisplayName("Verify that we get UserDTO by his ID")
	void updatePassword() {
	}

}
