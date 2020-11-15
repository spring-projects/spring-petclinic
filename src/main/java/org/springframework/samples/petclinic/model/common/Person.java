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
package org.springframework.samples.petclinic.model.common;

import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@MappedSuperclass
public class Person extends BaseEntity {

	@NotEmpty
	@Size(min = CommonParameter.FIRSTNAME_MIN, max = CommonParameter.FIRSTNAME_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.FIRSTNAME_MIN + " AND " + CommonParameter.FIRSTNAME_MAX + " !")
	@Column(name = "first_name", length = CommonParameter.FIRSTNAME_MAX)
	private String firstName;

	@NotEmpty
	@Size(min = CommonParameter.LASTNAME_MIN, max = CommonParameter.LASTNAME_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.LASTNAME_MIN + " AND " + CommonParameter.LASTNAME_MAX + " !")
	@Column(name = "last_name", length = CommonParameter.LASTNAME_MAX)
	private String lastName;

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Person))
			return false;

		Person person = (Person) o;

		if (!getFirstName().equals(person.getFirstName()))
			return false;
		return getLastName().equals(person.getLastName());
	}

}
