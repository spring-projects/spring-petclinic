package org.springframework.samples.petclinic.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.dto.common.AuthProviderDTO;
import org.springframework.samples.petclinic.model.common.AuthProvider;
import org.springframework.samples.petclinic.repository.AuthProviderRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
class AuthProviderServiceIntegrationTest {

	private static final String PROVIDER_TEST_NAME = "Provider Test";

	private static final Integer PROVIDER_TEST_ID = 11;

	@Autowired
	private AuthProviderRepository authProviderRepository;

	private AuthProviderService authProviderService;

	private AuthProvider authProvider;

	private AuthProviderDTO authProviderDTO;

	private List<AuthProvider> allAuthProviders;

	private List<AuthProviderDTO> allAuthProviderDTOS;

	@BeforeEach
	void beforeEach() {
		authProviderService = new AuthProviderService(authProviderRepository);

		authProvider = new AuthProvider(PROVIDER_TEST_ID, PROVIDER_TEST_NAME);
		authProviderDTO = new AuthProviderDTO(PROVIDER_TEST_ID, PROVIDER_TEST_NAME);

		allAuthProviders = authProviderRepository.findAll();
		allAuthProviderDTOS = new ArrayList<>();
		allAuthProviders.forEach(
				provider -> allAuthProviderDTOS.add(new AuthProviderDTO(provider.getId(), provider.getName())));
	}

	@Test
	@Tag("findById")
	@DisplayName("Verify that we call right method to find AuthProvider by ID")
	void findById() {

		allAuthProviderDTOS.forEach(provider -> {
			AuthProviderDTO found = authProviderService.findById(provider.getId());
			assertThat(found).isEqualTo(provider);
		});
	}

	@Test
	@Tag("findAll")
	@DisplayName("Verify that we call right method to find all AuthProviders")
	void findAll() {

		List<AuthProviderDTO> found = authProviderService.findAll();

		assertThat(found).isEqualTo(allAuthProviderDTOS);
	}

	@Test
	@Tag("findByName")
	@DisplayName("Verify that we call right method to find AuthProvider by name")
	void findByName() {

		allAuthProviderDTOS.forEach(provider -> {
			AuthProviderDTO found = authProviderService.findByName(provider.getName());
			assertThat(found).isEqualTo(provider);
		});
	}

	@Test
	@Tag("save")
	@DisplayName("Verify that we call right method to save AuthProvider")
	void save() {
		AuthProviderDTO found = authProviderService.findByName(PROVIDER_TEST_NAME);

		assertThat(found.isNew()).isTrue();
		authProviderDTO.setId(null);
		found = authProviderService.save(authProviderDTO);

		assertThat(found).isEqualToIgnoringGivenFields(authProviderDTO, "id");
	}

}
