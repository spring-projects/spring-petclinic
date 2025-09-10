package org.springframework.samples.petclinic.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor @Getter @Setter
public class ApiResponse {

	private String status;
	private String message;
	private LocalDateTime timestamp;
}
