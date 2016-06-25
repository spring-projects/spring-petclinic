/*
 * Copyright 2002-2016 the original author or authors.
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

import com.github.dandelion.core.web.DandelionFilter;
import com.github.dandelion.core.web.DandelionServlet;
import com.github.dandelion.datatables.core.web.filter.DatatablesFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * Java configuration for the Dandelion datatables component.
 *
 * @author Antoine Rey
 */
@Configuration
public class DandelionConfig {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        if (environment.acceptsProfiles("production")) {
            System.setProperty("dandelion.profile.active", "prod");
        }
    }

    @Bean
    @Description("Dandelion filter definition and mapping")
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new DandelionFilter());
        return filterRegistrationBean;
    }

    @Bean
    @Description("Dandelion-Datatables filter, used for basic export")
    public FilterRegistrationBean datatablesRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new DatatablesFilter());
        return filterRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new DandelionServlet());
        servletRegistrationBean.addUrlMappings("/dandelion-assets/*");
        servletRegistrationBean.setName("dandelionServlet");
        return servletRegistrationBean;
    }
}
