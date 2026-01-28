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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Concurrent integration tests for AppointmentService to verify pessimistic locking.
 *
 * @author Spring PetClinic Team
 */
@SpringBootTest
@ActiveProfiles("test")
class AppointmentServiceConcurrentTests {

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private OwnerRepository ownerRepository;

	private Owner owner;

	@Test
	@Transactional
	void shouldPreventConcurrentDoubleBooking() throws InterruptedException {
		// Given - Create test data
		owner = new Owner();
		owner.setFirstName("Concurrent");
		owner.setLastName("Test");
		owner.setAddress("123 Concurrent St");
		owner.setCity("Concurrent City");
		owner.setTelephone("1234567890");
		owner = ownerRepository.save(owner);

		PetType petType = new PetType();
		petType.setName("dog");

		Pet pet = new Pet();
		pet.setName("Concurrent Pet");
		pet.setBirthDate(java.time.LocalDate.now().minusYears(1));
		pet.setType(petType);
		owner.addPet(pet);
		owner = ownerRepository.save(owner);

		LocalDateTime appointmentTime = LocalDateTime.now().plusDays(1);

		// When - Try to create multiple appointments concurrently for the same time slot
		int threadCount = 5;
		CountDownLatch latch = new CountDownLatch(threadCount);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger conflictCount = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					AppointmentCreateDto createDto = new AppointmentCreateDto();
					createDto.setPetId(pet.getId());
					createDto.setStartTime(appointmentTime);
					createDto.setEndTime(appointmentTime.plusHours(1));

					appointmentService.createAppointment(createDto, owner.getId());
					successCount.incrementAndGet();
				}
				catch (DataIntegrityViolationException e) {
					conflictCount.incrementAndGet();
				}
				catch (Exception e) {
					// Other exceptions (shouldn't happen in this test)
				}
				finally {
					latch.countDown();
				}
			});
		}

		latch.await(10, TimeUnit.SECONDS);
		executor.shutdown();

		// Then - Only one appointment should be created successfully
		assertThat(successCount.get()).isEqualTo(1);
		assertThat(conflictCount.get()).isEqualTo(threadCount - 1);
	}

}