package org.springframework.samples.petclinic.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

/**
 * Simple Data Transfert Object representing a Privileges.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Getter
@Setter
@NoArgsConstructor
public class PrivilegeDTO implements Serializable {

	private Integer id;

	private String name;

	private Collection<RoleDTO> roles;

}
