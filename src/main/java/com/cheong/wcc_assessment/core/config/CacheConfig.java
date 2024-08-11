package com.cheong.wcc_assessment.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> locationCacheConfig(){
        return Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.DAYS)
                .expireAfterWrite(30, TimeUnit.DAYS);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCacheNames(List.of("fullpostcode", "outcode"));
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
