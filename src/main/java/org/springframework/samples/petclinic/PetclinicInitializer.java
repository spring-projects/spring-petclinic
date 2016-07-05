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
package org.springframework.samples.petclinic;

import com.github.dandelion.core.web.DandelionFilter;
import com.github.dandelion.core.web.DandelionServlet;
import com.github.dandelion.datatables.core.web.filter.DatatablesFilter;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.*;
import java.util.EnumSet;


/**
 * In Servlet 3.0+ environments, this class replaces the traditional {@code web.xml}-based approach in order to configure the
 * {@link ServletContext} programmatically.
 * <p/>
 * Create the Spring "<strong>root</strong>" application context.<br/>
 * Register a {@link DispatcherServlet} and a {@link DandelionServlet} in the servlet context.<br/>
 * For both servlets, register a {@link CharacterEncodingFilter}, a {@link DandelionFilter} an a {@link DatatablesFilter}.
 * <p/>
 *
 * @author Antoine Rey
 */
public class PetclinicInitializer extends AbstractDispatcherServletInitializer {

    /**
     * Spring profile used to choose the persistence layer implementation.
     * <p/>
     * When using Spring jpa, use: jpa
     * When using Spring JDBC, use: jdbc
     * When using Spring Data JPA, use: spring-data-jpa
     */
    private static final String SPRING_PROFILE = "jpa";

    private static final String DANDELION_SERVLET = "dandelionServlet";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        registerDandelionServlet(servletContext);
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        XmlWebApplicationContext rootAppContext = new XmlWebApplicationContext();
        rootAppContext.setConfigLocations("classpath:spring/business-config.xml", "classpath:spring/tools-config.xml");
        rootAppContext.getEnvironment().setActiveProfiles(SPRING_PROFILE);
        return rootAppContext;
    }


    @Override
    protected WebApplicationContext createServletApplicationContext() {
        XmlWebApplicationContext webAppContext = new XmlWebApplicationContext();
        webAppContext.setConfigLocation("classpath:spring/mvc-core-config.xml");
        return webAppContext;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        // Used to provide the ability to enter Chinese characters inside the Owner Form
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);

        // Dandelion filter definition and mapping -->
        DandelionFilter dandelionFilter = new DandelionFilter();

        // Dandelion-Datatables filter, used for basic export -->
        DatatablesFilter datatablesFilter = new DatatablesFilter();

        return new Filter[]{characterEncodingFilter, dandelionFilter, datatablesFilter};
    }

    @Override
    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
        FilterRegistration.Dynamic registration = super.registerServletFilter(servletContext, filter);
        registration.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, DANDELION_SERVLET);
        return registration;
    }

    private void registerDandelionServlet(ServletContext servletContext) {
        DandelionServlet dandelionServlet = new DandelionServlet();
        ServletRegistration.Dynamic registration = servletContext.addServlet(DANDELION_SERVLET, dandelionServlet);
        registration.setLoadOnStartup(2);
        registration.addMapping("/dandelion-assets/*");
    }
}
