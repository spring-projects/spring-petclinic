package org.springframework.samples.petclinic.config;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.samples.petclinic.util.CallMonitoringAspect;

@Configuration
@EnableCaching // enables scanning for @Cacheable annotation
@EnableMBeanExport
@EnableAspectJAutoProxy
public class ToolsConfig {
	
	@Bean
    @Description("Call monitoring aspect that monitors call count and call invocation time")
	public CallMonitoringAspect callMonitor() {
		return new CallMonitoringAspect();
	}
	
	@Bean
	@Autowired
	public EhCacheCacheManager ehCacheCacheManager(CacheManager cacheManager) {
		EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
		ehCacheCacheManager.setCacheManager(cacheManager);
		return ehCacheCacheManager;
	}

	@Bean
	public EhCacheManagerFactoryBean cacheManager() {
		EhCacheManagerFactoryBean ehCacheManager = new EhCacheManagerFactoryBean();
		ehCacheManager.setConfigLocation(new ClassPathResource("cache/ehcache.xml"));
		return ehCacheManager;
	}
}
