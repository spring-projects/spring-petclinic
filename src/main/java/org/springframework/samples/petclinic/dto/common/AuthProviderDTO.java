package org.springframework.samples.petclinic.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Simple Data Transfert Object representing a Authorization Provider.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@NoArgsConstructor
@Getter
@Setter
public class AuthProviderDTO extends NamedDTO {

	public AuthProviderDTO(Integer id, String name) {
		this.setId(id);
		this.setName(name);
	}

}
