package com.yjf.common.cache.memcache;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.util.CollectionUtils;
/**
 * 
 *                       
 * @Filename MemcachedCacheManager.java
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
public class MemcachedCacheManager extends AbstractMemcachedCacheManager {
	
	private static final Logger	logger	= LoggerFactory.getLogger(MemcachedCacheManager.class
											.getName());
	
	private MemcachedConfig		memcachedConfig;
	
	public MemcachedConfig getMemcachedConfig() {
		return memcachedConfig;
	}
	
	public void setMemcachedConfig(MemcachedConfig memcachedConfig) {
		this.memcachedConfig = memcachedConfig;
	}
	
	@Override
	protected Collection<? extends Cache> loadCaches() {
		
		if (CollectionUtils.isEmpty(memcachedConfig.getCacheNameSpaces())) {
			return null;
		}
		Collection<Cache> caches = new LinkedHashSet<Cache>(memcachedConfig.getCacheNameSpaces()
			.size());
		for (MemcachedNameSpace namespace : this.memcachedConfig.getCacheNameSpaces()) {
			logger.info("[memcached]使用配置的命名空间:{},缓存过期时间:{}秒", namespace.getName(),
				namespace.getExpireSecond());
			caches.add(new MemcachedCache(memcachedConfig.getMemcachedClient(), namespace));
		}
		return caches;
	}
	
	@Override
	public Cache getCache(String name) {
		Cache cache = super.getCache(name);
		if (cache == null) {
			MemcachedNameSpace namespace = new MemcachedNameSpace(name,
				memcachedConfig.getDefaultExpireSecond());
			cache = new MemcachedCache(memcachedConfig.getMemcachedClient(), namespace);
			logger.info("[memcached]动态新增命名空间:{},缓存过期时间:{}秒", name,
				memcachedConfig.getDefaultExpireSecond());
			addCache(cache);
		}
		return cache;
	}
}
