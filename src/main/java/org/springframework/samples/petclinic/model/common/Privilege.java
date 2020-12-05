package org.springframework.samples.petclinic.model.common;

import lombok.*;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "privileges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Privilege implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	@NotEmpty
	@Size(max = CommonParameter.PRIVILEGE_MAX)
	@Column(name = "name", length = CommonParameter.PRIVILEGE_MAX)
	private String name;

	@ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
	private Collection<Role> roles;

	/*
	 * @Override public boolean equals(Object o) { if (this == o) return true; if (!(o
	 * instanceof Role)) return false;
	 *
	 * Privilege privilege = (Privilege) o;
	 *
	 * if (!getId().equals(privilege.getId())) return false; if
	 * (!getName().equals(privilege.getName())) return false;
	 *
	 * return getRoles() != null ? getRoles().equals(privilege.getRoles()) :
	 * privilege.getRoles() == null; }
	 *
	 * @Override public String toString() { return "Privilege{" + "id=" + id + ", name='"
	 * + name + '\'' + ", roles=" + roles + '}'; }
	 */

}
