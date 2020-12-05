package org.springframework.samples.petclinic.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@AllArgsConstructor
public class PrivilegeDTO implements Serializable {

	private Integer id;

	private String name;

	private Collection<RoleDTO> roles;

	/*
	 * @Override public boolean equals(Object o) { if (this == o) return true;
	 *
	 * if (!(o instanceof PrivilegeDTO)) return false;
	 *
	 * PrivilegeDTO that = (PrivilegeDTO) o;
	 *
	 * return new EqualsBuilder().append(getId(), that.getId()).append(getName(),
	 * that.getName()) .append(getRoles(), that.getRoles()).isEquals(); }
	 */

}
