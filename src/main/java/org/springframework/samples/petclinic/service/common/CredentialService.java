package org.springframework.samples.petclinic.service.common;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.common.CommonParameter;
import org.springframework.samples.petclinic.dto.common.CredentialDTO;
import org.springframework.samples.petclinic.dto.common.UserDTO;
import org.springframework.samples.petclinic.model.common.AuthProvider;
import org.springframework.samples.petclinic.model.common.Credential;
import org.springframework.samples.petclinic.repository.AuthProviderRepository;
import org.springframework.samples.petclinic.repository.CredentialRepository;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Simple Service between User entity and UserDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("CredentialService")
public class CredentialService {

	private final CredentialRepository credentialRepository;

	private final AuthProviderRepository authProviderRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public CredentialService(CredentialRepository credentialRepository, AuthProviderRepository authProviderRepository) {
		this.credentialRepository = credentialRepository;
		this.authProviderRepository = authProviderRepository;
	}

	public Credential dtoToEntity(CredentialDTO dto) {
		if (dto != null) {
			Credential entity = modelMapper.map(dto, Credential.class);
			AuthProvider authProvider = authProviderRepository.findByName(dto.getProvider());
			if (authProvider == null) {
				authProvider = authProviderRepository.findByName(CommonParameter.DEFAULT_PROVIDER);
			}
			entity.setProviderId(authProvider.getId());
			return entity;
		}

		return new Credential();
	}

	public CredentialDTO entityToDTO(Credential entity) {
		if (entity != null) {
			CredentialDTO dto = modelMapper.map(entity, CredentialDTO.class);
			AuthProvider authProvider = authProviderRepository.findById(entity.getProviderId());

			if (authProvider == null) {
				dto.setProvider(CommonParameter.DEFAULT_PROVIDER);
			}
			else {
				dto.setProvider(authProvider.getName());
			}

			return dto;
		}

		return new CredentialDTO();
	}

	public CredentialDTO findByEmailAndProvider(String email, String provider) {
		AuthProvider authProvider = authProviderRepository.findByName(provider);
		Credential credential = credentialRepository.findByEmailAndProvider(email, authProvider.getId());
		return entityToDTO(credential);
	}

	public CredentialDTO findByToken(String token) {
		Credential credential = credentialRepository.findByToken(token);
		return entityToDTO(credential);
	}

	public CredentialDTO findByAuthentication(OAuth2AuthenticationToken authentication) {
		String email = authentication.getPrincipal().getAttribute("email");
		String provider = authentication.getAuthorizedClientRegistrationId();

		AuthProvider authProvider = authProviderRepository.findByName(provider);
		Credential credential = credentialRepository.findByEmailAndProvider(email, authProvider.getId());
		return entityToDTO(credential);
	}

	public CredentialDTO save(CredentialDTO dto) {
		Credential credential = dtoToEntity(dto);
		credential = credentialRepository.save(credential);
		return entityToDTO(credential);
	}

	public CredentialDTO saveNew(UserDTO user) {
		Credential credential = new Credential();

		AuthProvider authProvider = authProviderRepository.findByName(CommonParameter.DEFAULT_PROVIDER);

		credential.setEmail(user.getEmail());
		credential.setProviderId(authProvider.getId());
		credential.setPassword(user.getPassword());
		credential.setVerified(false);
		credential.setToken();
		credential.setExpiration();

		credential = credentialRepository.save(credential);
		return entityToDTO(credential);
	}

	public CredentialDTO saveNew(OAuth2AuthenticationToken authentication) {
		Credential credential = new Credential();

		AuthProvider authProvider = authProviderRepository
				.findByName(authentication.getAuthorizedClientRegistrationId());

		credential.setEmail(authentication.getPrincipal().getAttribute("email"));
		credential.setProviderId(authProvider.getId());
		credential.setPassword(authentication.getPrincipal().getAttribute("sub"));
		credential.setVerified(false);
		credential.setToken();
		credential.setExpiration();

		credential = credentialRepository.save(credential);
		return entityToDTO(credential);
	}

	public CredentialDTO delete(CredentialDTO dto) {
		Credential credential = dtoToEntity(dto);
		credential = credentialRepository.delete(credential);
		return entityToDTO(credential);
	}

}
