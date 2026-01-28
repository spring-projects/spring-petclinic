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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

/**
 * Repository class for <code>Appointment</code> domain objects All method names are
 * compliant with Spring Data naming conventions.
 *
 * @author Spring PetClinic Team
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	/**
	 * Find overlapping appointments for a pet within a time range. This method uses
	 * pessimistic locking to prevent concurrent bookings.
	 * @param petId the pet ID
	 * @param startTime the start time of the appointment
	 * @param endTime the end time of the appointment
	 * @param excludeId the ID to exclude (for updates)
	 * @return list of overlapping appointments
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Appointment a WHERE a.petId = :petId " + "AND a.status = 'SCHEDULED' "
			+ "AND a.id != :excludeId " + "AND ((a.startTime <= :startTime AND a.endTime > :startTime) "
			+ "OR (a.startTime < :endTime AND a.endTime >= :endTime) "
			+ "OR (a.startTime >= :startTime AND a.endTime <= :endTime))")
	List<Appointment> findOverlappingAppointments(@Param("petId") Integer petId,
			@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,
			@Param("excludeId") Integer excludeId);

	/**
	 * Find overlapping appointments for a pet within a time range (for new appointments).
	 * @param petId the pet ID
	 * @param startTime the start time of the appointment
	 * @param endTime the end time of the appointment
	 * @return list of overlapping appointments
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Appointment a WHERE a.petId = :petId " + "AND a.status = 'SCHEDULED' "
			+ "AND ((a.startTime <= :startTime AND a.endTime > :startTime) "
			+ "OR (a.startTime < :endTime AND a.endTime >= :endTime) "
			+ "OR (a.startTime >= :startTime AND a.endTime <= :endTime))")
	List<Appointment> findOverlappingAppointments(@Param("petId") Integer petId,
			@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

	/**
	 * Find appointments by pet ID with pagination.
	 * @param petId the pet ID
	 * @param pageable pagination information
	 * @return page of appointments
	 */
	Page<Appointment> findByPetId(Integer petId, Pageable pageable);

	/**
	 * Find appointments by owner ID with pagination.
	 * @param ownerId the owner ID
	 * @param pageable pagination information
	 * @return page of appointments
	 */
	Page<Appointment> findByOwnerId(Integer ownerId, Pageable pageable);

	/**
	 * Find appointments by time range with pagination.
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param pageable pagination information
	 * @return page of appointments
	 */
	@Query("SELECT a FROM Appointment a WHERE a.startTime >= :startTime AND a.endTime <= :endTime")
	Page<Appointment> findByTimeRange(@Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime, Pageable pageable);

	/**
	 * Find appointments by pet ID and time range with pagination.
	 * @param petId the pet ID
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param pageable pagination information
	 * @return page of appointments
	 */
	@Query("SELECT a FROM Appointment a WHERE a.petId = :petId "
			+ "AND a.startTime >= :startTime AND a.endTime <= :endTime")
	Page<Appointment> findByPetIdAndTimeRange(@Param("petId") Integer petId,
			@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, Pageable pageable);

	/**
	 * Find appointments by owner ID and time range with pagination.
	 * @param ownerId the owner ID
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param pageable pagination information
	 * @return page of appointments
	 */
	@Query("SELECT a FROM Appointment a WHERE a.ownerId = :ownerId "
			+ "AND a.startTime >= :startTime AND a.endTime <= :endTime")
	Page<Appointment> findByOwnerIdAndTimeRange(@Param("ownerId") Integer ownerId,
			@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, Pageable pageable);

	/**
	 * Find a scheduled appointment by ID.
	 * @param id the appointment ID
	 * @return optional containing the appointment if found and scheduled
	 */
	Optional<Appointment> findByIdAndStatus(Integer id, AppointmentStatus status);

}