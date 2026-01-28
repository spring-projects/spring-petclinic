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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AppointmentController.
 *
 * @author Spring PetClinic Team
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AppointmentControllerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	private Owner testOwner;

	private Pet testPet;

	private LocalDateTime futureTime;

	@BeforeEach
	void setUp() {
		// Create test data
		testOwner = new Owner();
		testOwner.setFirstName("Test");
		testOwner.setLastName("Owner");
		testOwner.setAddress("123 Test St");
		testOwner.setCity("Test City");
		testOwner.setTelephone("1234567890");
		testOwner = ownerRepository.save(testOwner);

		PetType petType = new PetType();
		petType.setName("dog");

		testPet = new Pet();
		testPet.setName("Test Pet");
		testPet.setBirthDate(java.time.LocalDate.now().minusYears(1));
		testPet.setType(petType);
		testOwner.addPet(testPet);

		ownerRepository.save(testOwner);

		futureTime = LocalDateTime.now().plusDays(1);
	}

	@Test
	void shouldCreateAppointmentSuccessfully() throws Exception {
		// Given
		AppointmentCreateDto createDto = new AppointmentCreateDto();
		createDto.setPetId(testPet.getId());
		createDto.setStartTime(futureTime);
		createDto.setEndTime(futureTime.plusHours(1));

		// When & Then
		mockMvc
			.perform(post("/api/appointments").param("ownerId", testOwner.getId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.petId").value(testPet.getId()))
			.andExpect(jsonPath("$.ownerId").value(testOwner.getId()))
			.andExpect(jsonPath("$.status").value("SCHEDULED"))
			.andExpect(jsonPath("$.startTime").exists())
			.andExpect(jsonPath("$.endTime").exists())
			.andExpect(jsonPath("$.createdAt").exists());
	}

	@Test
	void shouldRejectAppointmentWithInvalidTimeOrder() throws Exception {
		// Given
		AppointmentCreateDto createDto = new AppointmentCreateDto();
		createDto.setPetId(testPet.getId());
		createDto.setStartTime(futureTime.plusHours(1));
		createDto.setEndTime(futureTime); // End time before start time

		// When & Then
		mockMvc
			.perform(post("/api/appointments").param("ownerId", testOwner.getId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Start time must be before end time"));
	}

	@Test
	void shouldRejectAppointmentWithPastTime() throws Exception {
		// Given
		LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
		AppointmentCreateDto createDto = new AppointmentCreateDto();
		createDto.setPetId(testPet.getId());
		createDto.setStartTime(pastTime);
		createDto.setEndTime(pastTime.plusHours(1));

		// When & Then
		mockMvc
			.perform(post("/api/appointments").param("ownerId", testOwner.getId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors.startTime").exists());
	}

	@Test
	void shouldRejectAppointmentWhenPetDoesNotBelongToOwner() throws Exception {
		// Given - Create another owner and pet
		Owner otherOwner = new Owner();
		otherOwner.setFirstName("Other");
		otherOwner.setLastName("Owner");
		otherOwner.setAddress("456 Other St");
		otherOwner.setCity("Other City");
		otherOwner.setTelephone("0987654321");
		otherOwner = ownerRepository.save(otherOwner);

		PetType petType = new PetType();
		petType.setName("cat");

		Pet otherPet = new Pet();
		otherPet.setName("Other Pet");
		otherPet.setBirthDate(java.time.LocalDate.now().minusYears(2));
		otherPet.setType(petType);
		otherOwner.addPet(otherPet);
		ownerRepository.save(otherOwner);

		AppointmentCreateDto createDto = new AppointmentCreateDto();
		createDto.setPetId(otherPet.getId()); // Pet belongs to other owner
		createDto.setStartTime(futureTime);
		createDto.setEndTime(futureTime.plusHours(1));

		// When & Then
		mockMvc.perform(post("/api/appointments").param("ownerId", testOwner.getId().toString()) // Different
																									// owner
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message")
				.value("Pet not found with ID: " + otherPet.getId() + " for owner " + testOwner.getId()));
	}

	@Test
	void shouldRejectAppointmentWithOverlappingTimeSlot() throws Exception {
		// Given - Create an existing appointment
		Appointment existingAppointment = new Appointment();
		existingAppointment.setPetId(testPet.getId());
		existingAppointment.setOwnerId(testOwner.getId());
		existingAppointment.setStartTime(futureTime.minusMinutes(30));
		existingAppointment.setEndTime(futureTime.plusMinutes(30));
		existingAppointment.setStatus(AppointmentStatus.SCHEDULED);
		existingAppointment.setCreatedAt(LocalDateTime.now());
		appointmentRepository.save(existingAppointment);

		AppointmentCreateDto createDto = new AppointmentCreateDto();
		createDto.setPetId(testPet.getId());
		createDto.setStartTime(futureTime); // Overlaps with existing appointment
		createDto.setEndTime(futureTime.plusHours(1));

		// When & Then
		mockMvc
			.perform(post("/api/appointments").param("ownerId", testOwner.getId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.error").value("Conflict"));
	}

	@Test
	void shouldGetAppointmentsWithPagination() throws Exception {
		// Given - Create multiple appointments
		for (int i = 0; i < 3; i++) {
			Appointment appointment = new Appointment();
			appointment.setPetId(testPet.getId());
			appointment.setOwnerId(testOwner.getId());
			appointment.setStartTime(futureTime.plusHours(i));
			appointment.setEndTime(futureTime.plusHours(i + 1));
			appointment.setStatus(AppointmentStatus.SCHEDULED);
			appointment.setCreatedAt(LocalDateTime.now());
			appointmentRepository.save(appointment);
		}

		// When & Then
		mockMvc
			.perform(get("/api/appointments").param("ownerId", testOwner.getId().toString())
				.param("page", "0")
				.param("size", "2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.content.length()").value(2))
			.andExpect(jsonPath("$.totalElements").value(3))
			.andExpect(jsonPath("$.totalPages").value(2));
	}

	@Test
	void shouldCancelAppointmentSuccessfully() throws Exception {
		// Given - Create an appointment
		Appointment appointment = new Appointment();
		appointment.setPetId(testPet.getId());
		appointment.setOwnerId(testOwner.getId());
		appointment.setStartTime(futureTime);
		appointment.setEndTime(futureTime.plusHours(1));
		appointment.setStatus(AppointmentStatus.SCHEDULED);
		appointment.setCreatedAt(LocalDateTime.now());
		appointment = appointmentRepository.save(appointment);

		// When & Then
		mockMvc
			.perform(delete("/api/appointments/{id}", appointment.getId()).param("ownerId",
					testOwner.getId().toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(appointment.getId()))
			.andExpect(jsonPath("$.status").value("CANCELLED"));
	}

	@Test
	void shouldRejectCancellationWhenAppointmentDoesNotBelongToOwner() throws Exception {
		// Given - Create another owner and appointment
		Owner otherOwner = new Owner();
		otherOwner.setFirstName("Other");
		otherOwner.setLastName("Owner");
		otherOwner.setAddress("456 Other St");
		otherOwner.setCity("Other City");
		otherOwner.setTelephone("0987654321");
		otherOwner = ownerRepository.save(otherOwner);

		Appointment appointment = new Appointment();
		appointment.setPetId(testPet.getId());
		appointment.setOwnerId(otherOwner.getId());
		appointment.setStartTime(futureTime);
		appointment.setEndTime(futureTime.plusHours(1));
		appointment.setStatus(AppointmentStatus.SCHEDULED);
		appointment.setCreatedAt(LocalDateTime.now());
		appointment = appointmentRepository.save(appointment);

		// When & Then
		mockMvc
			.perform(delete("/api/appointments/{id}", appointment.getId()).param("ownerId",
					testOwner.getId().toString())) // Different owner
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Appointment does not belong to owner"));
	}

	@Test
	void shouldRejectCancellationWhenAppointmentNotFound() throws Exception {
		// When & Then
		mockMvc.perform(delete("/api/appointments/999").param("ownerId", testOwner.getId().toString()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Appointment not found with ID: 999"));
	}

}