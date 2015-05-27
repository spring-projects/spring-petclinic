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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MvcViewConfig {
	
	@Bean
	public ContentNegotiatingViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
	 	ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
	 	List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();
	 	viewResolvers.add(internalResourceViewResolver());
	 	viewResolvers.add(beanNameViewResolver());
		contentNegotiatingViewResolver.setViewResolvers(viewResolvers );
		contentNegotiatingViewResolver.setContentNegotiationManager(manager);
		return contentNegotiatingViewResolver;
	}
	
	@Bean
	@Description("Default viewClass: JSTL view (JSP with html output)")
	public ViewResolver internalResourceViewResolver() {
		// Example: a logical view name of 'vets' is mapped to
		// '/WEB-INF/jsp/vets.jsp'
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setViewClass(JstlView.class);
		bean.setPrefix("/WEB-INF/jsp/");
		bean.setSuffix(".jsp");
		return bean;
	}

	@Bean
	@Description("Used for 'xml' views")
	public ViewResolver beanNameViewResolver() {
		return new BeanNameViewResolver();
	}

	@Bean(name = "vets/vetList.xml")
	@Description("Renders an XML view. Used by the BeanNameViewResolver")
	public MarshallingView marshallingView() {
		return new MarshallingView(marshaller());
	}

	@Bean
	@Description("Object-XML mapping declared using annotations inside 'Vets'")
	public Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Vets.class);
		return marshaller;
	}

}
