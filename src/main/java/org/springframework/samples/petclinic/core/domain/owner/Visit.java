package org.springframework.samples.petclinic.core.domain.owner;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Visit {

	private Integer id;

	private LocalDate date;

	private String description;

	public Visit() {
		this.date = LocalDate.now();
	}

	public boolean isNew() {
		return this.id == null;
	}

}
