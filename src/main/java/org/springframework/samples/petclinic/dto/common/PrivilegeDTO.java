package org.springframework.samples.petclinic.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Simple Data Transfert Object representing a Privileges.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDTO implements Serializable {

	private Integer id;

	private String name;

	private Collection<RoleDTO> roles;

	protected Collection<RoleDTO> getRolesInternal() {
		if (this.roles == null) {
			this.roles = new HashSet<>();
		}

		return this.roles;
	}

	public Collection<RoleDTO> getRoles() {
		return getRolesInternal();
	}

	public void addRole(RoleDTO role) {
		if (this.getRoles() == null || !this.getRoles().contains(role)) {
			getRolesInternal().add(role);
		}

		if (!role.getPrivileges().contains(this)) {
			role.addPrivilege(this);
		}
	}

}
