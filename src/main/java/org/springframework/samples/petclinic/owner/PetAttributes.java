package org.springframework.samples.petclinic.owner;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="pet_attributes")
public class PetAttributes {

	@Id
	@Column(name="pet_id",insertable=false, updatable=false)
	private Integer petId;

	private String temperament;
	private int height;
	private double weight;

}


