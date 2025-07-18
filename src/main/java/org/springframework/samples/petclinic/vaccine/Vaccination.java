package org.springframework.samples.petclinic.vaccine;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.samples.petclinic.owner.Pet;

import java.time.LocalDate;

@Entity
@Data
public class Vaccination {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String vaccineName;
	private LocalDate vaccinationDate;
	private String description;
	private Boolean injected;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id", nullable = false)
	private Pet pet;
}
