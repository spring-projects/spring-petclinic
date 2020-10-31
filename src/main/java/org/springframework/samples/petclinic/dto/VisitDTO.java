/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.dto;

import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Simple Data Transfert Object representing a visit.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public class VisitDTO extends BaseDTO {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@NotEmpty
	private String description;

	private Integer petId;

	/**
	 * Creates a new instance of Visit for the current date
	 */
	public VisitDTO() {
		this.date = LocalDate.now();
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPetId() {
		return this.petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VisitDTO))
			return false;

		VisitDTO visitDTO = (VisitDTO) o;

		if (!getDate().equals(visitDTO.getDate()))
			return false;
		if (getDescription() != null ? !getDescription().equals(visitDTO.getDescription())
				: visitDTO.getDescription() != null)
			return false;
		return getPetId().equals(visitDTO.getPetId());
	}

}
