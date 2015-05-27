package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BusinessConfig.class, ToolsConfig.class})
public class RootApplicationContextConfig {

}
