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

/**
 * Mapper for converting between Appointment entity and DTOs.
 *
 * @author Spring PetClinic Team
 */
public final class AppointmentMapper {

	private AppointmentMapper() {
		// Utility class
	}

	/**
	 * Convert Appointment entity to AppointmentDto.
	 * @param appointment the entity
	 * @return the DTO
	 */
	public static AppointmentDto toDto(Appointment appointment) {
		if (appointment == null) {
			return null;
		}

		AppointmentDto dto = new AppointmentDto();
		dto.setId(appointment.getId());
		dto.setPetId(appointment.getPetId());
		dto.setOwnerId(appointment.getOwnerId());
		dto.setStartTime(appointment.getStartTime());
		dto.setEndTime(appointment.getEndTime());
		dto.setStatus(appointment.getStatus());
		dto.setCreatedAt(appointment.getCreatedAt());
		dto.setVersion(appointment.getVersion());
		return dto;
	}

	/**
	 * Convert AppointmentCreateDto to Appointment entity.
	 * @param dto the create DTO
	 * @param ownerId the owner ID
	 * @return the entity
	 */
	public static Appointment toEntity(AppointmentCreateDto dto, Integer ownerId) {
		if (dto == null) {
			return null;
		}

		Appointment appointment = new Appointment();
		appointment.setPetId(dto.getPetId());
		appointment.setOwnerId(ownerId);
		appointment.setStartTime(dto.getStartTime());
		appointment.setEndTime(dto.getEndTime());
		appointment.setStatus(AppointmentStatus.SCHEDULED);
		appointment.setCreatedAt(java.time.LocalDateTime.now());
		return appointment;
	}

}