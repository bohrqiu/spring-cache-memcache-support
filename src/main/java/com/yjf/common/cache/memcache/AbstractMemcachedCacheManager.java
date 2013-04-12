package com.yjf.common.cache.memcache;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.CollectionUtils;
/**
 * 
 *                       
 * @Filename AbstractMemcachedCacheManager.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author bohr.qiu
 *
 * @Email qzhanbo@yiji.com
 *       
 * @History
 *<li>Author: bohr.qiu</li>
 *<li>Date: 2013-4-1</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public abstract class AbstractMemcachedCacheManager implements CacheManager, InitializingBean {
	
	private final ConcurrentMap<String, Cache>	cacheMap	= new ConcurrentHashMap<String, Cache>();
	
	private Set<String>							cacheNames	= new LinkedHashSet<String>();
	
	public void afterPropertiesSet() {
		Collection<? extends Cache> caches = loadCaches();
		if (CollectionUtils.isEmpty(caches)) {
			return;
		}
		this.cacheMap.clear();
		
		// preserve the initial order of the cache names
		for (Cache cache : caches) {
			this.cacheMap.put(cache.getName(), cache);
			this.cacheNames.add(cache.getName());
		}
	}
	
	protected final void addCache(Cache cache) {
		this.cacheMap.put(cache.getName(), cache);
		this.cacheNames.add(cache.getName());
	}
	
	public Cache getCache(String name) {
		return this.cacheMap.get(name);
	}
	
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheNames);
	}
	
	/**
	 * Load the caches for this cache manager. Occurs at startup.
	 * The returned collection must not be null.
	 */
	protected abstract Collection<? extends Cache> loadCaches();
	
}