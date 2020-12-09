package org.springframework.samples.petclinic.service.common;

import org.junit.jupiter.api.*;
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
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
class CredentialServiceIntegrationTest {

	private static String PROVIDER_TEST_NAME = "Provider Test";

	private static Integer PROVIDER_TEST_ID = 11;

	private static final String EMAIL_TEST = "eduardo.rodriguez@petclinic.com";

	private static final String PASSWORD_TEST = "$2a$10$8KypNYtPopFo8Sk5jbKJ4.lCKeBhdApsrkmFfhwjB8nCls8qpzjZG";

	private static final String TOKEN_TEST = UUID.randomUUID().toString();

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AuthProviderRepository authProviderRepository;

	@Autowired
	private CredentialRepository credentialRepository;

	private CredentialService credentialService;

	private Credential credential;

	private CredentialDTO credentialDTO;

	private AuthProvider authProvider;

	private List<Credential> allCredentials;

	@BeforeEach
	void beforeEach() {
		allCredentials = credentialRepository.findAll();

		credentialService = new CredentialService(credentialRepository, bCryptPasswordEncoder, authProviderRepository);
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
	}


	@Test
	@Tag("findByEmailAndProvider")
	@DisplayName("Verify that we call right method to get Credential by Email and Provider")
	void findByEmailAndProvider() {

		for(Credential credential: allCredentials) {
			String email = credential.getEmail();
			String provider = authProviderRepository.findById(credential.getProviderId()).getName();
			CredentialDTO found = credentialService.findByEmailAndProvider(email,provider);
			assertThat(found).isEqualToComparingFieldByField(credentialService.entityToDTO(credential));
		}

	}

	@Test
	@Tag("findByToken")
	@DisplayName("Verify that we call right method to get Credential by Token")
	@Disabled
	void findByToken() {
		credentialDTO = credentialService.save(credentialDTO);

		CredentialDTO found = credentialService.findByToken(TOKEN_TEST);

		assertThat(found).isEqualToComparingFieldByField(credentialDTO);
	}

}
