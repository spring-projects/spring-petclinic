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
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service class for managing appointments.
 *
 * @author Spring PetClinic Team
 */
@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;

	private final OwnerRepository ownerRepository;

	@Autowired
	public AppointmentService(AppointmentRepository appointmentRepository, OwnerRepository ownerRepository) {
		this.appointmentRepository = appointmentRepository;
		this.ownerRepository = ownerRepository;
	}

	/**
	 * Create a new appointment. This method uses pessimistic locking to prevent
	 * concurrent bookings.
	 * @param appointmentCreateDto the appointment creation data
	 * @param ownerId the owner ID
	 * @return the created appointment
	 * @throws IllegalArgumentException if validation fails
	 * @throws DataIntegrityViolationException if there's a time conflict
	 */
	@Transactional
	public Appointment createAppointment(AppointmentCreateDto appointmentCreateDto, Integer ownerId) {
		Assert.notNull(appointmentCreateDto, "Appointment data must not be null");
		Assert.notNull(ownerId, "Owner ID must not be null");

		// Validate pet ownership
		Pet pet = validatePetOwnership(appointmentCreateDto.getPetId(), ownerId);

		// Validate time order
		validateTimeOrder(appointmentCreateDto.getStartTime(), appointmentCreateDto.getEndTime());

		// Check for overlapping appointments using pessimistic locking
		List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
				appointmentCreateDto.getPetId(), appointmentCreateDto.getStartTime(),
				appointmentCreateDto.getEndTime());

		if (!overlappingAppointments.isEmpty()) {
			throw new DataIntegrityViolationException(
					"Time slot is already booked for this pet. Conflicting appointment ID: "
							+ overlappingAppointments.get(0).getId());
		}

		// Create and save appointment
		Appointment appointment = AppointmentMapper.toEntity(appointmentCreateDto, ownerId);
		return appointmentRepository.save(appointment);
	}

	/**
	 * Cancel an appointment.
	 * @param appointmentId the appointment ID
	 * @param ownerId the owner ID
	 * @return the cancelled appointment
	 * @throws IllegalArgumentException if appointment not found or not owned by user
	 */
	@Transactional
	public Appointment cancelAppointment(Integer appointmentId, Integer ownerId) {
		Assert.notNull(appointmentId, "Appointment ID must not be null");
		Assert.notNull(ownerId, "Owner ID must not be null");

		Appointment appointment = appointmentRepository.findById(appointmentId)
			.orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

		if (!appointment.getOwnerId().equals(ownerId)) {
			throw new IllegalArgumentException("Appointment does not belong to owner");
		}

		if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
			throw new IllegalArgumentException("Only scheduled appointments can be cancelled");
		}

		appointment.setStatus(AppointmentStatus.CANCELLED);
		return appointmentRepository.save(appointment);
	}

	/**
	 * Find appointments with pagination and filtering.
	 * @param petId optional pet ID filter
	 * @param ownerId optional owner ID filter
	 * @param from optional start time filter
	 * @param to optional end time filter
	 * @param pageable pagination information
	 * @return page of appointments
	 */
	@Transactional(readOnly = true)
	public Page<Appointment> findAppointments(Integer petId, Integer ownerId, LocalDateTime from, LocalDateTime to,
			Pageable pageable) {
		if (petId != null && from != null && to != null) {
			return appointmentRepository.findByPetIdAndTimeRange(petId, from, to, pageable);
		}
		else if (ownerId != null && from != null && to != null) {
			return appointmentRepository.findByOwnerIdAndTimeRange(ownerId, from, to, pageable);
		}
		else if (petId != null) {
			return appointmentRepository.findByPetId(petId, pageable);
		}
		else if (ownerId != null) {
			return appointmentRepository.findByOwnerId(ownerId, pageable);
		}
		else if (from != null && to != null) {
			return appointmentRepository.findByTimeRange(from, to, pageable);
		}
		else {
			return appointmentRepository.findAll(pageable);
		}
	}

	/**
	 * Find an appointment by ID.
	 * @param appointmentId the appointment ID
	 * @return optional containing the appointment
	 */
	@Transactional(readOnly = true)
	public Optional<Appointment> findAppointmentById(Integer appointmentId) {
		Assert.notNull(appointmentId, "Appointment ID must not be null");
		return appointmentRepository.findById(appointmentId);
	}

	/**
	 * Validate that a pet belongs to an owner.
	 * @param petId the pet ID
	 * @param ownerId the owner ID
	 * @return the pet if validation passes
	 * @throws IllegalArgumentException if pet not found or doesn't belong to owner
	 */
	private Pet validatePetOwnership(Integer petId, Integer ownerId) {
		Owner owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with ID: " + ownerId));

		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Pet not found with ID: " + petId + " for owner " + ownerId);
		}

		return pet;
	}

	/**
	 * Validate that start time is before end time.
	 * @param startTime the start time
	 * @param endTime the end time
	 * @throws IllegalArgumentException if time order is invalid
	 */
	private void validateTimeOrder(LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
			throw new IllegalArgumentException("Start time must be before end time");
		}
	}

}