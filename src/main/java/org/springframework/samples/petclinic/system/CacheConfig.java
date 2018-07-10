package org.springframework.samples.petclinic.system;

import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@EnableCaching
class CacheConfig {

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            Configuration<Object, Object> cacheConfiguration = createCacheConfiguration();
            cm.createCache("vets", cacheConfiguration);
        };
    }

    private Configuration<Object, Object> createCacheConfiguration() {
        // Create a cache using infinite heap. A real application will want to use an
        // implementation dependent configuration that will better fit your needs
        return new MutableConfiguration<>().setStatisticsEnabled(true);
    }
}
