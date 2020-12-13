package org.springframework.samples.petclinic.model.common;

import lombok.*;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Privilege")
@Table(name = "privileges")
public class Privilege implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@NotEmpty
	@Size(max = CommonParameter.PRIVILEGE_MAX)
	@Column(name = "name", length = CommonParameter.PRIVILEGE_MAX)
	private String name;

	@ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
	private Collection<Role> roles;

	protected Collection<Role> getRolesInternal() {
		if (this.roles == null) {
			this.roles = new HashSet<>();
		}

		return this.roles;
	}

	public Collection<Role> getRoles() {
		return getRolesInternal();
	}

	public void addRole(Role role) {
		if (this.getRoles() == null || !this.getRoles().contains(role)) {
			getRolesInternal().add(role);
		}

		if (!role.getPrivileges().contains(this)) {
			role.addPrivilege(this);
		}
	}

}
