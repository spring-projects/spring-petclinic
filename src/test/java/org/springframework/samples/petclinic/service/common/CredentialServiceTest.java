package org.springframework.samples.petclinic.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.dto.common.CredentialDTO;
import org.springframework.samples.petclinic.dto.common.UserDTO;
import org.springframework.samples.petclinic.model.common.AuthProvider;
import org.springframework.samples.petclinic.model.common.Credential;
import org.springframework.samples.petclinic.repository.AuthProviderRepository;
import org.springframework.samples.petclinic.repository.CredentialRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
class CredentialServiceTest {

	private static String PROVIDER_TEST_NAME = "Provider Test";

	private static Integer PROVIDER_TEST_ID = 11;

	private static final String EMAIL_TEST = "eduardo.rodriguez@petclinic.com";

	private static final String PASSWORD_TEST = "$2a$10$8KypNYtPopFo8Sk5jbKJ4.lCKeBhdApsrkmFfhwjB8nCls8qpzjZG";

	private static final String TOKEN_TEST = UUID.randomUUID().toString();

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@MockBean
	private AuthProviderRepository authProviderRepository;

	@MockBean
	private CredentialRepository credentialRepository;

	private CredentialService credentialService;

	private Credential credential;

	private CredentialDTO credentialDTO;

	private AuthProvider authProvider;

	@BeforeEach
	void beforeEach() {
		credentialService = new CredentialService(credentialRepository, authProviderRepository);
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

		given(authProviderRepository.findByName(anyString())).willReturn(authProvider);
		given(authProviderRepository.findById(anyInt())).willReturn(authProvider);
	}

	@Test
	@Tag("dtoToEntity")
	@DisplayName("Verify the convertion from DTO to Entity")
	void dtoToEntity() {
		Credential found = credentialService.dtoToEntity(credentialDTO);

		assertThat(found).isEqualToComparingFieldByField(credential);
	}

	@Test
	@Tag("entityToDTO")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entityToDTO() {
		CredentialDTO found = credentialService.entityToDTO(credential);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}

	@Test
	@Tag("findByEmailAndProvider")
	@DisplayName("Verify that we call right method to get Credential by Email and Provider")
	void findByEmailAndProvider() {
		given(credentialRepository.findByEmailAndProvider(EMAIL_TEST, PROVIDER_TEST_ID)).willReturn(credential);

		CredentialDTO found = credentialService.findByEmailAndProvider(EMAIL_TEST, PROVIDER_TEST_NAME);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}

	@Test
	@Tag("findByToken")
	@DisplayName("Verify that we call right method to get Credential by Token")
	void findByToken() {
		given(credentialRepository.findByToken(TOKEN_TEST)).willReturn(credential);

		CredentialDTO found = credentialService.findByToken(TOKEN_TEST);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}

	@Test
	@Tag("save")
	@DisplayName("Verify that we call right method to save Credential")
	void save() {
		given(credentialRepository.save(any(Credential.class))).willReturn(credential);

		CredentialDTO found = credentialService.save(credentialDTO);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}

	@Test
	@Tag("saveNew")
	@DisplayName("Verify that we call right method to save Credential from User")
	void saveNew() {
		UserDTO user = new UserDTO();
		user.setEmail(EMAIL_TEST);
		user.setPassword(PASSWORD_TEST);
		user.setMatchingPassword(PASSWORD_TEST);

		given(credentialRepository.save(any(Credential.class))).willReturn(credential);

		CredentialDTO found = credentialService.saveNew(user);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}

	@Test
	@Tag("delete")
	@DisplayName("Verify that we call right method to delete Credential")
	void delete() {
		given(credentialRepository.delete(any(Credential.class))).willReturn(credential);

		CredentialDTO found = credentialService.delete(credentialDTO);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}

}
