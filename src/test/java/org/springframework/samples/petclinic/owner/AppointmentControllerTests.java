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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Integration tests for AppointmentController.
 *
 * @author Your Name
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppointmentControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void shouldCreateAppointment() throws Exception {
		AppointmentDTO dto = new AppointmentDTO();
		dto.setPetId(1);
		dto.setStartTime(LocalDateTime.now().plusDays(1));
		dto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

		mockMvc
			.perform(post("/api/appointments").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated());
	}

	@Test
	public void shouldNotCreateOverlappingAppointment() throws Exception {
		AppointmentDTO dto1 = new AppointmentDTO();
		dto1.setPetId(1);
		dto1.setStartTime(LocalDateTime.now().plusDays(1));
		dto1.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

		mockMvc
			.perform(post("/api/appointments").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto1)))
			.andExpect(status().isCreated());

		AppointmentDTO dto2 = new AppointmentDTO();
		dto2.setPetId(1);
		dto2.setStartTime(LocalDateTime.now().plusDays(1).plusMinutes(30));
		dto2.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));

		mockMvc
			.perform(post("/api/appointments").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto2)))
			.andExpect(status().isConflict());
	}

	@Test
	public void shouldGetAppointments() throws Exception {
		mockMvc.perform(get("/api/appointments").param("petId", "1").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	public void shouldCancelAppointment() throws Exception {
		AppointmentDTO dto = new AppointmentDTO();
		dto.setPetId(1);
		dto.setStartTime(LocalDateTime.now().plusDays(1));
		dto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

		String response = mockMvc
			.perform(post("/api/appointments").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andReturn()
			.getResponse()
			.getContentAsString();

		Appointment appointment = objectMapper.readValue(response, Appointment.class);

		mockMvc.perform(delete("/api/appointments/" + appointment.getId()).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

}