package org.springframework.samples.petclinic.model;


import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor @Getter @Setter
public class ApiError {

	private String errorCode;
	private String message;
	private LocalDateTime timestamp;

}
