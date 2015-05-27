package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(BusinessConfig.class)
@ImportResource("classpath:spring/tools-config.xml")
public class RootApplicationContextConfig {

}
