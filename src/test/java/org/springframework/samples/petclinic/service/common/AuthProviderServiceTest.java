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
import org.springframework.samples.petclinic.dto.common.AuthProviderDTO;
import org.springframework.samples.petclinic.dto.common.CredentialDTO;
import org.springframework.samples.petclinic.dto.common.UserDTO;
import org.springframework.samples.petclinic.model.common.AuthProvider;
import org.springframework.samples.petclinic.model.common.Credential;
import org.springframework.samples.petclinic.model.common.Role;
import org.springframework.samples.petclinic.repository.AuthProviderRepository;
import org.springframework.samples.petclinic.repository.CredentialRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
class AuthProviderServiceTest {

	private static final String PROVIDER_TEST_NAME = "Provider Test";

	private static final Integer PROVIDER_TEST_ID = 11;

	@MockBean
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
		allAuthProviders.add(authProvider);
		allAuthProviderDTOS = new ArrayList<>();
		allAuthProviders.forEach(
				provider -> allAuthProviderDTOS.add(new AuthProviderDTO(provider.getId(), provider.getName())));
	}

	@Test
	@Tag("dtoToEntity")
	@DisplayName("Verify the convertion from DTO to Entity")
	void dtoToEntity() {
		AuthProvider found = authProviderService.dtoToEntity(authProviderDTO);

		assertThat(found).isEqualToComparingFieldByField(authProvider);
	}

	@Test
	@Tag("entityToDTO")
	@DisplayName("Verify the convertion from Entity to DTO")
	void entityToDTO() {
		AuthProviderDTO found = authProviderService.entityToDTO(authProvider);

		assertThat(found).isEqualToComparingFieldByField(authProviderDTO);
	}

	@Test
	@Tag("dtosToEntities")
	@DisplayName("Verify the convertion from DTO List to Entity List")
	void dtosToEntities() {
		List<AuthProvider> found = authProviderService.dtosToEntities(allAuthProviderDTOS);

		assertThat(found).hasSameSizeAs(allAuthProviders).isEqualTo(allAuthProviders);

	}

	@Test
	@Tag("entitiesToDTOS")
	@DisplayName("Verify the convertion from Entity List to DTO List")
	void entitiesToDTOS() {
		List<AuthProviderDTO> found = authProviderService.entitiesToDTOS(allAuthProviders);

		assertThat(found).hasSameSizeAs(allAuthProviderDTOS).isEqualTo(allAuthProviderDTOS);
	}

	@Test
	@Tag("findById")
	@DisplayName("Verify that we call right method to find AuthProvider by ID")
	void findById() {
		given(authProviderRepository.findById(anyInt())).willReturn(authProvider);

		AuthProviderDTO found = authProviderService.findById(PROVIDER_TEST_ID);

		assertThat(found).isEqualTo(authProviderDTO);
	}

	@Test
	@Tag("findAll")
	@DisplayName("Verify that we call right method to find all AuthProviders")
	void findAll() {
		given(authProviderRepository.findAll()).willReturn(allAuthProviders);

		List<AuthProviderDTO> found = authProviderService.findAll();

		assertThat(found).isEqualTo(allAuthProviderDTOS);
	}

	@Test
	@Tag("findByName")
	@DisplayName("Verify that we call right method to find AuthProvider by name")
	void findByName() {
		given(authProviderRepository.findByName(anyString())).willReturn(authProvider);

		AuthProviderDTO found = authProviderService.findByName(PROVIDER_TEST_NAME);

		assertThat(found).isEqualTo(authProviderDTO);
	}

	@Test
	@Tag("save")
	@DisplayName("Verify that we call right method to save AuthProvider")
	void save() {
		given(authProviderRepository.save(any(AuthProvider.class))).willReturn(authProvider);

		AuthProviderDTO found = authProviderService.save(authProviderDTO);

		assertThat(found).isEqualToComparingFieldByField(authProviderDTO);
	}

}
