package org.springframework.samples.petclinic.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/*
 * Data Transfer Object for PetTypeAttribute.
 * Purpose: Used to expose only necessary fields to clients.
 * @Author: Sujeet Sharma
 * @Date: 08--9-2025
 */

@Data
public class PetAttributeDto {

    /** Unique identifier of the attribute */
	    private Integer id;

	    /** Pet temperament (e.g., Aggressive, Friendly) */
	    @NotBlank(message = "Temperament is required")
	    private String temperament;

	    /** Pet length in cm */
	    @NotNull(message = "Length is required")
	    private Double length;

	    /** Pet weight in kg */
	    @NotNull(message = "Weight is required")
	    private Double weight;
}
