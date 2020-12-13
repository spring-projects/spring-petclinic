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
package org.springframework.samples.petclinic.dto.common;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Simple Data Transfert Object representing a person.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Getter
@Setter
public class PersonDTO extends BaseDTO {

	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PersonDTO))
			return false;

		PersonDTO person = (PersonDTO) o;

		if (!getFirstName().equals(person.getFirstName()))
			return false;
		return getLastName().equals(person.getLastName());
	}

}
