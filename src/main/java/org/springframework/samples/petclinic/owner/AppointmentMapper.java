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
package org.springframework.samples.petclinic.owner;

import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Appointment entity and DTO.
 *
 * @author Your Name
 */
@Component
public class AppointmentMapper {

	public Appointment toEntity(AppointmentDTO dto, Integer ownerId) {
		Appointment appointment = new Appointment();
		appointment.setPetId(dto.getPetId());
		appointment.setOwnerId(ownerId);
		appointment.setStartTime(dto.getStartTime());
		appointment.setEndTime(dto.getEndTime());
		appointment.setStatus(Appointment.Status.SCHEDULED);
		appointment.setCreatedAt(LocalDateTime.now());
		return appointment;
	}

	public AppointmentDTO toDTO(Appointment appointment) {
		AppointmentDTO dto = new AppointmentDTO();
		dto.setPetId(appointment.getPetId());
		dto.setStartTime(appointment.getStartTime());
		dto.setEndTime(appointment.getEndTime());
		return dto;
	}

}