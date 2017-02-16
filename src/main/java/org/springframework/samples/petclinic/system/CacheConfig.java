package org.springframework.samples.petclinic.system;

import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Profile;

/**
 * Cache could be disabled in unit test.
 */
@org.springframework.context.annotation.Configuration
@EnableCaching
@Profile("production")
class CacheConfig implements CacheManagerCustomizer<JCacheCacheManager> {

    @Override
    public void customize(JCacheCacheManager cacheManager) {
        Configuration<Object, Object> cacheConfiguration = createCacheConfiguration();
        cacheManager.getCacheManager().createCache("vets", cacheConfiguration);
    }

    private Configuration<Object, Object> createCacheConfiguration() {
        // Create a cache using infinite heap. A real application will want to use an implementation dependent
        // configuration that will better fit your needs
        return new MutableConfiguration<>().setStatisticsEnabled(true);
    }
}
