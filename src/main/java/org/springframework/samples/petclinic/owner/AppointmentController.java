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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST controller for appointment management.
 *
 * @author Your Name
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

	private final AppointmentService appointmentService;

	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@PostMapping
	public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody AppointmentDTO dto) {
		// In a real application, you would get ownerId from authentication
		Integer ownerId = 1; // Hardcoded for demo purposes
		try {
			Appointment appointment = appointmentService.createAppointment(dto, ownerId);
			return new ResponseEntity<>(appointment, HttpStatus.CREATED);
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@GetMapping
	public ResponseEntity<Page<Appointment>> getAppointments(@RequestParam(required = false) Integer petId,
			@RequestParam(required = false) Integer ownerId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
			Pageable pageable) {
		Page<Appointment> appointments = appointmentService.getAppointments(petId, ownerId, from, to, pageable);
		return new ResponseEntity<>(appointments, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelAppointment(@PathVariable Integer id) {
		try {
			appointmentService.cancelAppointment(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

}