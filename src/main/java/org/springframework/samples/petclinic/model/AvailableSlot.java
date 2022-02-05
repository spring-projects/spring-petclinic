package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailableSlot {
	@JsonProperty("slotNumber")
	String slotNumber;
	@JsonProperty("direct")
	String direct;
}
