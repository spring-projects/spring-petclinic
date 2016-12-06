package org.springframework.samples.petclinic.system;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Cache could be disable in unit test.
 */
@Configuration
@EnableCaching
@Profile("production")
class CacheConfig {
}
