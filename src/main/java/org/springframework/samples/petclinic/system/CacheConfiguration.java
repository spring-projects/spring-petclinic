package org.springframework.samples.petclinic.system;

import javax.cache.configuration.MutableConfiguration;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cache configuration intended for caches providing the JCache API. This configuration
 * creates the used cache for the application and enables statistics that
 * become accessible via JMX.
 *
 * <p>This is a minimal configuration that makes use of the JCache API to enable
 * caching with any JCache implementation out of the box. The utilized JCache implementation
 * is specified by the dependency in the {@code pom.xml}.
 *
 * <p>If the used cache implementation is configured via its specific and non standard mechanism,
 * this example configuration might be redundant and can be removed or replaced by
 * a programmatic configuration via the implementation specific methods.
 *
 * <p>In case no programmatic cache configuration is needed for the selected caching
 * implementation this configuration class can be removed and the {@link EnableCaching}
 * annotation can be put on the application class.
 *
 * @author Jens Wilke
 */
@Configuration
@EnableCaching
class CacheConfiguration {

    /**
     * A customizer that creates the the cache used by the application.
     * This implies that the cache is not existent before the application start and the
     * cache must be created. This is the default behavior of a JCache implemenation, in case
     * no additional configuration is present and typically will create a transient in memory cache.
     * If a persistent cache implementation is used, creating the cache on startup
     * may lead to a failure, when the cache was created before. If the cache life time
     * is controlled separately, the call of {@code createCache} must be removed.
     */
    @Bean
    public JCacheManagerCustomizer petclinicCacheConfigurationCustomizer() {
        return cm -> {
            cm.createCache("vets", cacheConfiguration());
        };
    }

    /**
     * Enable statistics via the JCache programmatic configuration API.
     * Within the configuration object that is provided by the JCache API standard, there is only
     * a very limited set of configuration options. The really relevant configuration options
     * (like the size limit) must be set via a configuration mechanism that is provided by the
     * selected JCache implementation.
     */
    private javax.cache.configuration.Configuration<Object, Object> cacheConfiguration() {
        return new MutableConfiguration<>().setStatisticsEnabled(true);
    }

}
