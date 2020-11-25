package org.springframework.samples.petclinic.model.common;

import org.springframework.samples.petclinic.common.CommonParameter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

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

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<User> users;

	public Role(Integer id, @NotNull @NotEmpty @Size(max = CommonParameter.ROLE_MAX) String name) {
		this.id = id;
		this.name = name;
	}

	public Role() {
		// empty constructor for creating empty role and add attribute after
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
