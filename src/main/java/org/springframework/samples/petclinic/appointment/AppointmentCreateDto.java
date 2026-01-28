/*
 * Copyright 2012-2025 the original author or authors.
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
package org.springframework.samples.petclinic.appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a new appointment.
 *
 * @author Spring PetClinic Team
 */
public class AppointmentCreateDto {

	@NotNull(message = "Pet ID is required")
	private Integer petId;

	@NotNull(message = "Start time is required")
	@Future(message = "Start time must be in the future")
	private LocalDateTime startTime;

	@NotNull(message = "End time is required")
	@Future(message = "End time must be in the future")
	private LocalDateTime endTime;

	public Integer getPetId() {
		return this.petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return this.endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

}