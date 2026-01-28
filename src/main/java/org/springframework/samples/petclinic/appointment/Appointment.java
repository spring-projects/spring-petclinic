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

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

/**
 * Simple JavaBean domain object representing an appointment.
 *
 * @author Spring PetClinic Team
 */
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

	@Column(name = "pet_id", nullable = false)
	@NotNull
	private Integer petId;

	@Column(name = "owner_id", nullable = false)
	@NotNull
	private Integer ownerId;

	@Column(name = "start_time", nullable = false)
	@NotNull
	private LocalDateTime startTime;

	@Column(name = "end_time", nullable = false)
	@NotNull
	private LocalDateTime endTime;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private AppointmentStatus status;

	@Column(name = "created_at", nullable = false, updatable = false)
	@NotNull
	private LocalDateTime createdAt;

	@Version
	private Integer version;

	public Integer getPetId() {
		return this.petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return this.endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public AppointmentStatus getStatus() {
		return this.status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId())
			.append("new", this.isNew())
			.append("petId", this.petId)
			.append("ownerId", this.ownerId)
			.append("startTime", this.startTime)
			.append("endTime", this.endTime)
			.append("status", this.status)
			.append("createdAt", this.createdAt)
			.append("version", this.version)
			.toString();
	}

}