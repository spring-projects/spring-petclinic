package org.springframework.samples.petclinic.model.common;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Entity representing a Role.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Role")
@Table(name = "roles")
public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@NotEmpty
	@Size(max = CommonParameter.ROLE_MAX)
	@Column(name = "name", length = CommonParameter.ROLE_MAX)
	private String name;

	@ManyToMany(mappedBy = "roles")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<User> users;

	@ManyToMany // (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	private Collection<Privilege> privileges;

	protected Collection<User> getUsersInternal() {
		if (this.users == null) {
			this.users = new HashSet<>();
		}

		return this.users;
	}

	public Collection<User> getUsers() {
		return getUsersInternal();
	}

	public void addUser(User user) {
		if (this.getUsers() == null || !this.getUsers().contains(user)) {
			getUsersInternal().add(user);
		}

		if (!user.getRoles().contains(this)) {
			user.addRole(this);
		}
	}

	public void removeUser(User user) {
		this.getUsers().remove(user);
	}

	protected Collection<Privilege> getPrivilegesInternal() {
		if (this.privileges == null) {
			this.privileges = new HashSet<>();
		}

		return this.privileges;
	}

	public Collection<Privilege> getPrivileges() {
		return getPrivilegesInternal();
	}

	public void addPrivilege(Privilege privilege) {
		if (this.getPrivileges() == null || !this.getPrivileges().contains(privilege)) {
			getPrivilegesInternal().add(privilege);
		}
		if (!privilege.getRoles().contains(this)) {
			privilege.addRole(this);
		}
	}

	public void removePrivilege(Privilege privilege) {
		this.getPrivileges().remove(privilege);
	}

}
