package com.hibernatel2.cache.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Callable;

public final class MultitenantCache implements Cache {
	/** Actual {@link Cache} implementation to which calls will be delegated */
	private final Cache delegate;
	/** Defines whether or not cache methods should fail if there is no multi-tenant context defined */
	private final boolean contextRequired;
	@Autowired
	private TenantContextHolder tenantContextHolder;

	/**
	 * Creates a new {@link MultitenantCache} that wraps the given delegate. The contextRequired parameter defines
	 * whether or not cache methods should fail if there is no multi-tenant context defined.
	 * 
	 * @param delegate {@link Cache} implementation to which calls will be delegated
	 * @param contextRequired Defines whether or not cache methods should fail
	 */
	public MultitenantCache(final Cache delegate, final boolean contextRequired, TenantContextHolder tenantContextHolder) {
		if (delegate == null) throw new NullPointerException("Cache delegate may not be null");
		this.delegate = delegate;
		this.contextRequired = contextRequired;
		this.tenantContextHolder = tenantContextHolder;
	}

	/**
	 * Convenience constructor for {@code MultitenantCache(Cache, false)}.
	 * 
	 * @param delegate {@link Cache} implementation to which calls will be delegated
	 */
	public MultitenantCache(final Cache delegate) {
		this(delegate, false, null);
	}

	@Override
	public String getName() {
		return this.delegate.getName();
	}

	@Override
	public Object getNativeCache() {
		return this.delegate.getNativeCache();
	}

	@Override
	public ValueWrapper get(Object key) {
		return this.delegate.get(translateKey(key));
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		return this.delegate.get(translateKey(key), type);
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return this.delegate.get(translateKey(key), valueLoader);
	}

	@Override
	public void put(Object key, Object value) {
		this.delegate.put(translateKey(key), value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		return delegate.putIfAbsent(translateKey(key), value);
	}

	@Override
	public void evict(Object key) {
		this.delegate.evict(translateKey(key));
	}

	@Override
	public void clear() {
		this.delegate.clear();
	}

	public boolean isContextRequired() {
		return this.contextRequired;
	}

	private TenantKey translateKey(Object key) {
		String ctx = getTenantContext();
		if (isContextRequired() && ctx == null) throw new RuntimeException("Tenant context is required but is not available");
		return new TenantKey(ctx, key);
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
		return String.format("Cache implementation '%s' is used by tenant '%s'", getDelegateName(), getTenantContext());
	}

	static class TenantKey implements Serializable {
		private final String tenantContext;
		private final Object key;

		public TenantKey(final String tenantContext, Object key) {
			this.tenantContext = tenantContext;
			this.key = key;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof TenantKey)) return false;
			TenantKey that = (TenantKey) o;
			return Objects.equals(this.tenantContext, that.tenantContext) && Objects.equals(this.key, that.key);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.tenantContext, this.key);
		}
	}

}