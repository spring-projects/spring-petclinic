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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST controller for managing appointments.
 *
 * @author Spring PetClinic Team
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

	private final AppointmentService appointmentService;

	private final OwnerRepository ownerRepository;

	public AppointmentController(AppointmentService appointmentService, OwnerRepository ownerRepository) {
		this.appointmentService = appointmentService;
		this.ownerRepository = ownerRepository;
	}

	/**
	 * Create a new appointment.
	 * @param appointmentCreateDto the appointment data
	 * @param ownerId the owner ID (from request parameter or authentication)
	 * @return the created appointment
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<AppointmentDto> createAppointment(
			@Valid @RequestBody AppointmentCreateDto appointmentCreateDto, @RequestParam("ownerId") Integer ownerId) {

		// Validate owner exists
		ownerRepository.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with ID: " + ownerId));

		Appointment appointment = appointmentService.createAppointment(appointmentCreateDto, ownerId);
		AppointmentDto result = AppointmentMapper.toDto(appointment);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	/**
	 * Get appointments with optional filtering.
	 * @param petId optional pet ID filter
	 * @param ownerId optional owner ID filter
	 * @param from optional start time filter (ISO format)
	 * @param to optional end time filter (ISO format)
	 * @param pageable pagination information
	 * @return page of appointments
	 */
	@GetMapping
	public ResponseEntity<Page<AppointmentDto>> getAppointments(@RequestParam(required = false) Integer petId,
			@RequestParam(required = false) Integer ownerId, @RequestParam(required = false) LocalDateTime from,
			@RequestParam(required = false) LocalDateTime to, @PageableDefault(size = 20) Pageable pageable) {

		Page<Appointment> appointments = appointmentService.findAppointments(petId, ownerId, from, to, pageable);
		Page<AppointmentDto> result = appointments.map(AppointmentMapper::toDto);
		return ResponseEntity.ok(result);
	}

	/**
	 * Cancel an appointment.
	 * @param id the appointment ID
	 * @param ownerId the owner ID (from request parameter or authentication)
	 * @return the cancelled appointment
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<AppointmentDto> cancelAppointment(@PathVariable("id") Integer id,
			@RequestParam("ownerId") Integer ownerId) {

		Appointment appointment = appointmentService.cancelAppointment(id, ownerId);
		AppointmentDto result = AppointmentMapper.toDto(appointment);
		return ResponseEntity.ok(result);
	}

}