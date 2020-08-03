package com.hibernatel2.cache.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class TenantKeyGenerator {

    private static TenantContextHolder tenantContextHolder;

    @Autowired
    public void setAppContext(TenantContextHolder tenantContextHolder) {
        TenantKeyGenerator.tenantContextHolder = tenantContextHolder;
    }

    public static String generateKey(Object ...args) {
        String tenantId = tenantContextHolder.getTenantId();
        String combinedArgs = String.join(":", Arrays.stream(args).map(arg -> String.valueOf(arg)).collect(Collectors.toList()));
        return String.format("%s:%s", tenantId, combinedArgs);
    }

    public static String generateKey() {
        String tenantId = tenantContextHolder.getTenantId();
        return String.format("%s", tenantId);
    }

}
