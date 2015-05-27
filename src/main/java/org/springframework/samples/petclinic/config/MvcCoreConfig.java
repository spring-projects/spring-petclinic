/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.web.PetTypeFormatter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * <p>
 * The ContentNegotiatingViewResolver delegates to the
 * InternalResourceViewResolver and BeanNameViewResolver, and uses the requested
 * media type (determined by the path extension) to pick a matching view. When
 * the media type is 'text/html', it will delegate to the
 * InternalResourceViewResolver's JstlView, otherwise to the
 * BeanNameViewResolver.
 * 
 */
@Configuration
@EnableWebMvc
@Import(MvcViewConfig.class)
// POJOs labeled with the @Controller and @Service annotations are
// auto-detected.
@ComponentScan(basePackages = { "org.springframework.samples.petclinic.web" })
public class MvcCoreConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private ClinicService clinicService;

	@Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(true);
		configurer.defaultContentType(MediaType.TEXT_HTML);
		configurer.mediaType("html", MediaType.TEXT_HTML);
		configurer.mediaType("xml", MediaType.APPLICATION_XML);
	}

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		// Serve static resources (*.html, ...) from src/main/webapp/
		configurer.enable();
	}

	@Override
	public void addFormatters(FormatterRegistry formatterRegistry) {
		formatterRegistry.addFormatter(petTypeFormatter());
	}

	@Bean
	public PetTypeFormatter petTypeFormatter() {
		return new PetTypeFormatter(clinicService);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// all resources inside folder src/main/webapp/resources are mapped so
		// they can be refered to inside JSP files (see header.jsp for more
		// details)
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/");
		// uses WebJars so Javascript and CSS libs can be declared as Maven dependencies (Bootstrap, jQuery...)
		registry.addResourceHandler("/webjars/**").addResourceLocations(
				"classpath:/META-INF/resources/webjars/");
	}
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("welcome");
    }

	@Bean(name = "messageSource")
	@Description("Message source for this context, loaded from localized 'messages_xx' files.")
	public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
		// Files are stored inside src/main/resources
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:messages/messages");
		return messageSource;
	}

	/**
	 * Resolves specific types of exceptions to corresponding logical view names
	 * for error views.
	 * 
	 * <p>
	 * View name resolved using bean of type InternalResourceViewResolver
	 * (declared in {@link MvcViewConfig}).
	 */
	@Override
	public void configureHandlerExceptionResolvers(
			List<HandlerExceptionResolver> exceptionResolvers) {
		SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
		// results into 'WEB-INF/jsp/exception.jsp'
		exceptionResolver.setDefaultErrorView("exception");
		// needed otherwise exceptions won't be logged anywhere
		exceptionResolver.setWarnLogCategory("warn");
		exceptionResolvers.add(exceptionResolver);
	}

}
