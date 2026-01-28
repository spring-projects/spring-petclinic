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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for AppointmentService.
 *
 * @author Spring PetClinic Team
 */
@ExtendWith(MockitoExtension.class)
class AppointmentServiceTests {

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private AppointmentService appointmentService;

	private Owner testOwner;

	private Pet testPet;

	private AppointmentCreateDto validCreateDto;

	private LocalDateTime now;

	@BeforeEach
	void setUp() {
		now = LocalDateTime.now().plusDays(1); // Future time

		testOwner = new Owner();
		testOwner.setId(1);
		testOwner.setFirstName("John");
		testOwner.setLastName("Doe");

		testPet = new Pet();
		testPet.setId(1);
		testPet.setName("Fluffy");
		testPet.setBirthDate(java.time.LocalDate.now().minusYears(2));
		testOwner.addPet(testPet);

		validCreateDto = new AppointmentCreateDto();
		validCreateDto.setPetId(1);
		validCreateDto.setStartTime(now);
		validCreateDto.setEndTime(now.plusHours(1));
	}

	@Test
	void shouldCreateAppointmentSuccessfully() {
		// Given
		when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));
		when(appointmentRepository.findOverlappingAppointments(eq(1), any(LocalDateTime.class),
				any(LocalDateTime.class)))
			.thenReturn(Collections.emptyList());

		Appointment savedAppointment = new Appointment();
		savedAppointment.setId(1);
		savedAppointment.setPetId(1);
		savedAppointment.setOwnerId(1);
		savedAppointment.setStartTime(validCreateDto.getStartTime());
		savedAppointment.setEndTime(validCreateDto.getEndTime());
		savedAppointment.setStatus(AppointmentStatus.SCHEDULED);

		when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

		// When
		Appointment result = appointmentService.createAppointment(validCreateDto, 1);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1);
		assertThat(result.getPetId()).isEqualTo(1);
		assertThat(result.getOwnerId()).isEqualTo(1);
		assertThat(result.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);

		verify(ownerRepository).findById(1);
		verify(appointmentRepository).findOverlappingAppointments(eq(1), any(LocalDateTime.class),
				any(LocalDateTime.class));
		verify(appointmentRepository).save(any(Appointment.class));
	}

	@Test
	void shouldRejectAppointmentWhenPetNotFound() {
		// Given
		when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

		AppointmentCreateDto dto = new AppointmentCreateDto();
		dto.setPetId(999); // Non-existent pet
		dto.setStartTime(now);
		dto.setEndTime(now.plusHours(1));

		// When/Then
		assertThatThrownBy(() -> appointmentService.createAppointment(dto, 1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Pet not found with ID: 999 for owner 1");

		verify(ownerRepository).findById(1);
		verify(appointmentRepository, never()).findOverlappingAppointments(any(), any(), any());
		verify(appointmentRepository, never()).save(any());
	}

	@Test
	void shouldRejectAppointmentWhenOwnerNotFound() {
		// Given
		when(ownerRepository.findById(999)).thenReturn(Optional.empty());

		// When/Then
		assertThatThrownBy(() -> appointmentService.createAppointment(validCreateDto, 999))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Owner not found with ID: 999");

		verify(ownerRepository).findById(999);
		verify(appointmentRepository, never()).findOverlappingAppointments(any(), any(), any());
		verify(appointmentRepository, never()).save(any());
	}

	@Test
	void shouldRejectAppointmentWhenTimeOrderIsInvalid() {
		// Given
		AppointmentCreateDto dto = new AppointmentCreateDto();
		dto.setPetId(1);
		dto.setStartTime(now.plusHours(1));
		dto.setEndTime(now); // End time before start time

		// When/Then
		assertThatThrownBy(() -> appointmentService.createAppointment(dto, 1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Start time must be before end time");

		verify(ownerRepository, never()).findById(any());
		verify(appointmentRepository, never()).findOverlappingAppointments(any(), any(), any());
		verify(appointmentRepository, never()).save(any());
	}

	@Test
	void shouldRejectAppointmentWhenTimeSlotIsAlreadyBooked() {
		// Given
		when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

		Appointment existingAppointment = new Appointment();
		existingAppointment.setId(2);
		existingAppointment.setPetId(1);
		existingAppointment.setStartTime(now.minusMinutes(30));
		existingAppointment.setEndTime(now.plusMinutes(30));
		existingAppointment.setStatus(AppointmentStatus.SCHEDULED);

		when(appointmentRepository.findOverlappingAppointments(eq(1), any(LocalDateTime.class),
				any(LocalDateTime.class)))
			.thenReturn(Arrays.asList(existingAppointment));

		// When/Then
		assertThatThrownBy(() -> appointmentService.createAppointment(validCreateDto, 1))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("Time slot is already booked for this pet");

		verify(ownerRepository).findById(1);
		verify(appointmentRepository).findOverlappingAppointments(eq(1), any(LocalDateTime.class),
				any(LocalDateTime.class));
		verify(appointmentRepository, never()).save(any());
	}

	@Test
	void shouldCancelAppointmentSuccessfully() {
		// Given
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setPetId(1);
		appointment.setOwnerId(1);
		appointment.setStatus(AppointmentStatus.SCHEDULED);

		when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
		when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

		// When
		Appointment result = appointmentService.cancelAppointment(1, 1);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);

		verify(appointmentRepository).findById(1);
		verify(appointmentRepository).save(appointment);
	}

	@Test
	void shouldRejectCancellationWhenAppointmentNotFound() {
		// Given
		when(appointmentRepository.findById(999)).thenReturn(Optional.empty());

		// When/Then
		assertThatThrownBy(() -> appointmentService.cancelAppointment(999, 1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Appointment not found with ID: 999");

		verify(appointmentRepository).findById(999);
		verify(appointmentRepository, never()).save(any());
	}

	@Test
	void shouldRejectCancellationWhenAppointmentDoesNotBelongToOwner() {
		// Given
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setPetId(1);
		appointment.setOwnerId(2); // Different owner
		appointment.setStatus(AppointmentStatus.SCHEDULED);

		when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));

		// When/Then
		assertThatThrownBy(() -> appointmentService.cancelAppointment(1, 1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Appointment does not belong to owner");

		verify(appointmentRepository).findById(1);
		verify(appointmentRepository, never()).save(any());
	}

	@Test
	void shouldRejectCancellationWhenAppointmentNotScheduled() {
		// Given
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setPetId(1);
		appointment.setOwnerId(1);
		appointment.setStatus(AppointmentStatus.CANCELLED);

		when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));

		// When/Then
		assertThatThrownBy(() -> appointmentService.cancelAppointment(1, 1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Only scheduled appointments can be cancelled");

		verify(appointmentRepository).findById(1);
		verify(appointmentRepository, never()).save(any());
	}

	@Test
	void shouldFindAppointmentsByPetId() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setPetId(1);
		Page<Appointment> page = new PageImpl<>(Arrays.asList(appointment));

		when(appointmentRepository.findByPetId(1, pageable)).thenReturn(page);

		// When
		Page<Appointment> result = appointmentService.findAppointments(1, null, null, null, pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getPetId()).isEqualTo(1);

		verify(appointmentRepository).findByPetId(1, pageable);
	}

	@Test
	void shouldFindAppointmentsByOwnerId() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setOwnerId(1);
		Page<Appointment> page = new PageImpl<>(Arrays.asList(appointment));

		when(appointmentRepository.findByOwnerId(1, pageable)).thenReturn(page);

		// When
		Page<Appointment> result = appointmentService.findAppointments(null, 1, null, null, pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getOwnerId()).isEqualTo(1);

		verify(appointmentRepository).findByOwnerId(1, pageable);
	}

}