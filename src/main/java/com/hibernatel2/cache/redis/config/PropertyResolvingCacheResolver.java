package com.hibernatel2.cache.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.core.env.PropertyResolver;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class PropertyResolvingCacheResolver
        extends SimpleCacheResolver {

    private final PropertyResolver propertyResolver;
    private TenantContextHolder tenantContextHolder;

    public PropertyResolvingCacheResolver(CacheManager cacheManager, PropertyResolver propertyResolver,
                                          TenantContextHolder tenantContextHolder) {
        super(cacheManager);
        this.propertyResolver = propertyResolver;
        this.tenantContextHolder = tenantContextHolder;
    }

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        String currentTenant = tenantContextHolder.getTenantId();

        Collection<String> unresolvedCacheNames = super.getCacheNames(context);

        Collection<String> previousCollection = unresolvedCacheNames.stream()
                .map(unresolvedCacheName -> propertyResolver.resolveRequiredPlaceholders(unresolvedCacheName)).collect(Collectors.toList());

       return Collections.singletonList(currentTenant + ":" + previousCollection.iterator().next());
    }
}