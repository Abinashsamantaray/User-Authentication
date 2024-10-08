package com.project.User_Authentication.Config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;





@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("otpCache") {
            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new TTLConcurrentMapCache(name, 5, TimeUnit.MINUTES);
            }
        };
        return cacheManager;
    }
}

