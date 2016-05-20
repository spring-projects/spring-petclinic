/*
 * Copyright 2016 the original author or authors.
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
package org.springframework.samples.petclinic.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

/**
 * When the web application stops, various resources need to be cleaned up to
 * prevent memory leaks.
 */
public class CleanUp implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // NO-OP
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // Logback is configured to use JMX so make sure that all objects are
        // deregistered from JMX else there will be a memory leak
        // http://logback.qos.ch/manual/jmxConfig.html
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();
    }
}
