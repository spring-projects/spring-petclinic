package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:spring/business-config.xml", "classpath:spring/tools-config.xml"})
public class RootApplicationContextConfig {

}
