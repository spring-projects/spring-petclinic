package org.springframework.samples.petclinic.vaccine;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.samples.petclinic.owner.Pet;

import java.time.LocalDate;

@Entity
@Data
public class Vaccinations {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate vaccinationDate;

	private String description;

	private Boolean injected;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pet_id", nullable = false)
	private Pet pet;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vaccine_id", nullable = false)
	private Vaccine vaccine;

}
