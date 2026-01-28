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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for AppointmentService.
 *
 * @author Your Name
 */
@SpringBootTest
@Transactional
public class AppointmentServiceTests {

	@Autowired
	private AppointmentService appointmentService;

	@Test
	public void shouldCreateAppointment() {
		AppointmentDTO dto = new AppointmentDTO();
		dto.setPetId(1);
		dto.setStartTime(LocalDateTime.now().plusDays(1));
		dto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

		Appointment appointment = appointmentService.createAppointment(dto, 1);
		assertTrue(appointment.getId() != null);
	}

	@Test
	public void shouldNotCreateOverlappingAppointment() {
		AppointmentDTO dto1 = new AppointmentDTO();
		dto1.setPetId(1);
		dto1.setStartTime(LocalDateTime.now().plusDays(1));
		dto1.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
		appointmentService.createAppointment(dto1, 1);

		AppointmentDTO dto2 = new AppointmentDTO();
		dto2.setPetId(1);
		dto2.setStartTime(LocalDateTime.now().plusDays(1).plusMinutes(30));
		dto2.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));

		assertThrows(IllegalStateException.class, () -> appointmentService.createAppointment(dto2, 1));
	}

	@Test
	public void shouldHandleConcurrentCreation() throws InterruptedException {
		int threadCount = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					AppointmentDTO dto = new AppointmentDTO();
					dto.setPetId(1);
					dto.setStartTime(LocalDateTime.now().plusDays(1));
					dto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
					appointmentService.createAppointment(dto, 1);
				}
				catch (Exception e) {
					// Expected for overlapping appointments
				}
				finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();
	}

}