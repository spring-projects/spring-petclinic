package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.samples.petclinic.owner.Pet;
import java.time.LocalDateTime;

@Entity
@Table(name = "pet_details")
@Data
@Builder
public class PetDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@OneToOne
	@JoinColumn(name = "pet_id", nullable = false)
	private Pet pet;
	private String temperament;
	private Double weight;
	private Double length;
	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
}

