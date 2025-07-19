package org.springframework.samples.petclinic.vaccine;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.samples.petclinic.owner.PetType;

@Entity
@Data
public class Vaccine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String vaccineName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pet_type_id", nullable = false)
	private PetType petType;

}
