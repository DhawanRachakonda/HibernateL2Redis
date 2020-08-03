package com.hibernatel2.cache.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
@EnableCaching
public class SpringCacheConfig {

    @Autowired
    private TenantContextHolder tenantContextHolder;
    @Autowired
    private Environment springEnv;

    @Bean(destroyMethod="shutdown")
    RedissonClient redissonClient(@Value("classpath:/redisson.yaml") Resource configFile) throws IOException {
        Config config = Config.fromYAML(configFile.getInputStream());
        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient, "classpath:/cache-config.yml");
    }

//    @Bean
//    public CacheResolver cacheResolver(CacheManager cacheManager) {
//        return new PropertyResolvingCacheResolver(cacheManager, springEnv, tenantContextHolder);
//    }


//    @Lazy
//    @Bean
//    public MultitenantCacheManager cacheManager(RedissonClient redissonClient) {
//        return new MultitenantCacheManager(new RedissonSpringCacheManager(redissonClient,
//                "classpath:/cache-config.yml"), true, tenantContextHolder);
//    }

}
