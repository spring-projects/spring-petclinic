/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link WebConfiguration} (language selection), without starting Spring.
 */
class WebConfigurationTest {

	private final WebConfiguration configuration = new WebConfiguration();

	@Test
	void defaultLocaleIsEnglish() {
		LocaleResolver resolver = configuration.localeResolver();
		MockHttpServletRequest request = new MockHttpServletRequest();

		assertThat(resolver.resolveLocale(request)).isEqualTo(Locale.ENGLISH);
	}

	@Test
	void interceptorUsesLangParameter() {
		assertThat(configuration.localeChangeInterceptor().getParamName()).isEqualTo("lang");
	}

	@Test
	void langParameterChangesLocaleOfTheSession() throws Exception {
		LocaleResolver resolver = configuration.localeResolver();
		LocaleChangeInterceptor interceptor = configuration.localeChangeInterceptor();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("lang", "pt");
		request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, resolver);
		MockHttpServletResponse response = new MockHttpServletResponse();

		interceptor.preHandle(request, response, new Object());

		MockHttpServletRequest nextRequest = new MockHttpServletRequest();
		nextRequest.setSession(request.getSession());
		assertThat(resolver.resolveLocale(nextRequest)).isEqualTo(Locale.forLanguageTag("pt"));
	}

}
