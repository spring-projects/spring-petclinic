package org.springframework.samples.petclinic.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBean {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
