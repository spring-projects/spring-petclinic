package org.springframework.samples.petclinic.core.domain.vet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Specialty {

	private Integer id;

	private String name;

	public boolean isNew() {
		return this.id == null;
	}

	@Override
	public String toString() {
		return name != null ? name : "<null>";
	}

}
