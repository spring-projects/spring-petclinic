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

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

/**
 * Service layer for appointment management.
 *
 * @author Your Name
 */
@Service
@Validated
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;

	private final AppointmentMapper appointmentMapper;

	public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper) {
		this.appointmentRepository = appointmentRepository;
		this.appointmentMapper = appointmentMapper;
	}

	@Transactional
	public Appointment createAppointment(@Valid AppointmentDTO dto, Integer ownerId) {
		// Validate time order
		if (dto.getStartTime().isAfter(dto.getEndTime()) || dto.getStartTime().isEqual(dto.getEndTime())) {
			throw new IllegalArgumentException("Start time must be before end time");
		}

		// Check for overlapping appointments with pessimistic lock
		Page<Appointment> overlapping = appointmentRepository.findOverlappingAppointments(dto.getPetId(),
				dto.getStartTime(), dto.getEndTime(), Pageable.unpaged());

		if (!overlapping.isEmpty()) {
			throw new IllegalStateException("Appointment overlaps with existing scheduled appointment");
		}

		// Create and save appointment
		Appointment appointment = appointmentMapper.toEntity(dto, ownerId);
		return appointmentRepository.save(appointment);
	}

	@Transactional(readOnly = true)
	public Page<Appointment> getAppointments(Integer petId, Integer ownerId, LocalDateTime from, LocalDateTime to,
			Pageable pageable) {
		return appointmentRepository.findByFilters(petId, ownerId, from, to, pageable);
	}

	@Transactional
	public Appointment cancelAppointment(Integer id) {
		Appointment appointment = appointmentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

		if (appointment.getStatus() == Appointment.Status.CANCELLED) {
			throw new IllegalStateException("Appointment is already cancelled");
		}

		appointment.setStatus(Appointment.Status.CANCELLED);
		return appointmentRepository.save(appointment);
	}

}