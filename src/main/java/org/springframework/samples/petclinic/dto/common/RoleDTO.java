package org.springframework.samples.petclinic.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Simple Data Transfert Object representing a list of roles.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable {

	private Integer id;

	@NotNull
	@NotEmpty
	@Size(max = CommonParameter.ROLE_MAX)
	private String name;

	private Collection<UserDTO> users;

	private Collection<PrivilegeDTO> privileges;

	protected Collection<UserDTO> getUsersInternal() {
		if (this.users == null) {
			this.users = new HashSet<>();
		}

		return this.users;
	}

	public Collection<UserDTO> getUsers() {
		return getUsersInternal();
	}

	public void addUser(UserDTO user) {
		if (this.getUsers() == null || !this.getUsers().contains(user)) {
			getUsersInternal().add(user);
		}
		if (!user.getRoles().contains(this)) {
			user.addRole(this);
		}
	}

	protected Collection<PrivilegeDTO> getPrivilegesInternal() {
		if (this.privileges == null) {
			this.privileges = new HashSet<>();
		}

		return this.privileges;
	}

	public Collection<PrivilegeDTO> getPrivileges() {
		return getPrivilegesInternal();
	}

	public void addPrivilege(PrivilegeDTO privilege) {
		if (this.getPrivileges() == null || !this.getPrivileges().contains(privilege)) {
			getPrivilegesInternal().add(privilege);
		}
		if (!privilege.getRoles().contains(this)) {
			privilege.addRole(this);
		}
	}

}
