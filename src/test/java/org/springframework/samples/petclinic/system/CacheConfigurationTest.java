package org.springframework.samples.petclinic.system;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;

import org.junit.jupiter.api.Test;

class CacheConfigurationTest {

	@Test
	void customizer_callsCreateCache_withVets() {
		CacheConfiguration cfg = new CacheConfiguration();
		org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer customizer = cfg
			.petclinicCacheConfigurationCustomizer();

		CacheManager cm = mock(CacheManager.class);
		@SuppressWarnings("unchecked")
		Cache<Object, Object> cache = mock(Cache.class);
		when(cm.createCache(anyString(), any(Configuration.class))).thenReturn(cache);

		customizer.customize(cm);

		verify(cm).createCache(eq("vets"), any(Configuration.class));
	}

}
