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
import org.springframework.samples.petclinic.model.common.AuthProvider;
import org.springframework.samples.petclinic.model.common.Credential;
import org.springframework.samples.petclinic.repository.AuthProviderRepository;
import org.springframework.samples.petclinic.repository.CredentialRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@AutoConfigureTestDatabase
@SpringBootTest
@RunWith(SpringRunner.class)
class CredentialServiceTest {

	private static String PROVIDER_TEST_NAME = "Provider Test";
	private static Integer PROVIDER_TEST_ID = 11;
	private static final String EMAIL_TEST = "eduardo.rodriguez@petclinic.com";
	private static final String PASSWORD_TEST = "$2a$10$8KypNYtPopFo8Sk5jbKJ4.lCKeBhdApsrkmFfhwjB8nCls8qpzjZG";

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
		credentialService = new CredentialService(credentialRepository, bCryptPasswordEncoder, authProviderRepository);
		authProvider = new AuthProvider(PROVIDER_TEST_ID,PROVIDER_TEST_NAME);
		credential = new Credential(PROVIDER_TEST_ID, EMAIL_TEST, PASSWORD_TEST,true);
		credentialDTO = new CredentialDTO(PROVIDER_TEST_NAME, EMAIL_TEST, PASSWORD_TEST,true);




	}

	@Test
	@Tag("dtoToEntity")
	@DisplayName("Verify the convertion from DTO to Entity")
	void dtoToEntity() {
		given(authProviderRepository.findByName(PROVIDER_TEST_NAME)).willReturn(authProvider);
		Credential found = credentialService.dtoToEntity(credentialDTO);

		assertThat(found).isEqualToComparingFieldByField(credential);
	}

	@Test
	@Tag("entityToDTO")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entityToDTO() {
		given(authProviderRepository.findById(PROVIDER_TEST_ID)).willReturn(authProvider);

		CredentialDTO found = credentialService.entityToDTO(credential);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}



	@Test
	@Tag("findByEmailAndProvider")
	@DisplayName("Verify that we call right method to get Credential by Email and Provider")
	void findByEmailAndProvider() {
		given(credentialRepository.findByEmailAndProvider(EMAIL_TEST,PROVIDER_TEST_ID)).willReturn(credential);
		given(authProviderRepository.findByName(PROVIDER_TEST_NAME)).willReturn(authProvider);
		given(authProviderRepository.findById(PROVIDER_TEST_ID)).willReturn(authProvider);

		CredentialDTO found = credentialService.findByEmailAndProvider(EMAIL_TEST,PROVIDER_TEST_NAME);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}

}
