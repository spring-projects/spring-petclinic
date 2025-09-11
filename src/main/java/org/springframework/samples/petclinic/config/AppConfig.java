package org.springframework.samples.petclinic.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

	   @Bean
	    public ModelMapper modelMapper() {
	        return new ModelMapper();
	    }
}
