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

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

/**
 * Simple JavaBean domain object representing an appointment.
 *
 * @author Your Name
 */
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

	public enum Status {

		SCHEDULED, CANCELLED, COMPLETED

	}

	@Column(name = "pet_id")
	@NotNull
	private Integer petId;

	@Column(name = "owner_id")
	@NotNull
	private Integer ownerId;

	@Column(name = "start_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@NotNull
	@Future
	private LocalDateTime startTime;

	@Column(name = "end_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@NotNull
	@Future
	private LocalDateTime endTime;

	@Enumerated(EnumType.STRING)
	@NotNull
	private Status status = Status.SCHEDULED;

	@Column(name = "created_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Version
	private Integer version;

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}