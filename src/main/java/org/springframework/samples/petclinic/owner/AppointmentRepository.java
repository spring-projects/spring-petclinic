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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

/**
 * Repository interface for Appointment entities.
 *
 * @author Your Name
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Appointment a WHERE a.petId = :petId AND a.status = 'SCHEDULED' AND "
			+ "((a.startTime < :endTime AND a.endTime > :startTime))")
	Page<Appointment> findOverlappingAppointments(@Param("petId") Integer petId,
			@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, Pageable pageable);

	@Query("SELECT a FROM Appointment a WHERE (a.petId = :petId OR :petId IS NULL) AND "
			+ "(a.ownerId = :ownerId OR :ownerId IS NULL) AND " + "(a.startTime >= :from OR :from IS NULL) AND "
			+ "(a.endTime <= :to OR :to IS NULL)")
	Page<Appointment> findByFilters(@Param("petId") Integer petId, @Param("ownerId") Integer ownerId,
			@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

}