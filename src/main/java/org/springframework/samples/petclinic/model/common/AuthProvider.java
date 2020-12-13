package org.springframework.samples.petclinic.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class used to manage Authorization providers
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "AuthProvider")
@Table(name = "auth_providers")
public class AuthProvider extends NamedEntity {

	public AuthProvider(Integer id, String name) {
		this.setId(id);
		this.setName(name);
	}

}
