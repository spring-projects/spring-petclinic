package org.springframework.samples.petclinic.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

}
