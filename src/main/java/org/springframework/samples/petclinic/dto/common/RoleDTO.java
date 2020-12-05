package org.springframework.samples.petclinic.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

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

	/*
	 * @Override public boolean equals(Object o) { if (this == o) return true;
	 *
	 * if (!(o instanceof RoleDTO)) return false;
	 *
	 * RoleDTO roleDTO = (RoleDTO) o;
	 *
	 * return new EqualsBuilder().append(getId(), roleDTO.getId()).append(getName(),
	 * roleDTO.getName()) .append(getUsers(), roleDTO.getUsers()).append(getPrivileges(),
	 * roleDTO.getPrivileges()).isEquals(); }
	 */

}
