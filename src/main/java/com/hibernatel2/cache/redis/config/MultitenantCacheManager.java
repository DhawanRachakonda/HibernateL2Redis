package com.hibernatel2.cache.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;

public class MultitenantCacheManager implements CacheManager {
	/** Actual {@link CacheManager} implementation to which calls will be delegated */
	private final CacheManager delegate;
	/** Defines whether or not calls to {@link #getCache(String)} should fail if there is no multi-tenant context defined */
	private final boolean contextRequired;
	private TenantContextHolder tenantContextHolder;

	/**
	 * Creates a new {@link MultitenantCacheManager} that wraps the given delegate. The contextRequired parameter
	 * defines whether or not calls to {@link #getCache(String)} should fail if there is no multi-tenant context defined.
	 * 
	 * @param delegate {@link CacheManager} implementation to which calls will be delegated
	 * @param contextRequired Defines whether or not calls to {@link #getCache(String)} should fail
	 */
	public MultitenantCacheManager(final CacheManager delegate, final boolean contextRequired, final TenantContextHolder tenantContextHolder) {
		if (delegate == null) throw new NullPointerException("Cache manager delegate may not be null");
		this.delegate = delegate;
		if (contextRequired) this.contextRequired = true;
		else this.contextRequired = false;
		this.tenantContextHolder = tenantContextHolder;
	}

	/**
	 * Convenience constructor equivalent to {@code MultitenantCacheManager(CacheManager, false)}.
	 * 
	 * @param delegate {@link CacheManager} implementation to which calls will be delegated
	 */
	public MultitenantCacheManager(final CacheManager delegate) {
		this(delegate, false, null);
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = this.delegate.getCache(name);
		if (isContextRequired() && getTenantContext() == null) throw new RuntimeException("Tenant context required but not available");
		if (cache != null) cache = new MultitenantCache(cache, isContextRequired(), tenantContextHolder);
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return this.delegate.getCacheNames();
	}

	public boolean isContextRequired() {
		return contextRequired;
	}

	public String getTenantContext() {
		String ctx = tenantContextHolder.getTenantId();
		return ctx != null && ctx.trim().isEmpty() ? null : ctx;
	}

	public String getDelegateName() {
		return this.delegate.getClass().getSimpleName();
	}

	@Override
	public String toString() {
		return String.format("Cache manager '%s' is used by tenant '%s'", getDelegateName(), getTenantContext());
	}
}