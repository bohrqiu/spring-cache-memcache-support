package com.yjf.common.cache.memcache;

import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;
/**
 * 
 *                       
 * @Filename MemcachedCache.java
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
public class MemcachedCache implements Cache {
	
	private static final Logger	logger				= LoggerFactory.getLogger(MemcachedCache.class
														.getName());
	private MemcachedClient		memcachedClient;
	public static final String	NAMESPACE_SEPARATOR	= "#";
	
	private MemcachedNameSpace	namespace;
	
	public MemcachedCache(MemcachedClient memcachedClient, MemcachedNameSpace namespace) {
		Assert.notNull(memcachedClient, "memcachedClient must not be null");
		Assert.notNull(namespace, "namespace must not be null");
		this.memcachedClient = memcachedClient;
		this.namespace = namespace;
	}
	
	@Override
	public String getName() {
		return this.namespace.getName();
	}
	
	@Override
	public Object getNativeCache() {
		return memcachedClient;
	}
	
	private String genKey(Object key) {
		return this.namespace.getName() + NAMESPACE_SEPARATOR + key;
	}
	
	@Override
	public ValueWrapper get(Object key) {
		Object value = null;
		try {
			String genedKey = genKey(key);
			if (logger.isDebugEnabled()) {
				logger.debug("get:{}", genedKey);
			}
			value = this.memcachedClient.get(genedKey);
			if (value == null) {
				return null;
			}
			
		} catch (Exception e) {
			logger.error("获取数据异常", e);
			return null;
		}
		return new SimpleValueWrapper(value);
	}
	
	@Override
	public void put(Object key, Object value) {
		if (value != null) {
			try {
				String genedKey = genKey(key);
				if (logger.isDebugEnabled()) {
					logger.debug("set:{} value:{} expireSecond:{}", new Object[] { genedKey, value,
							namespace.getExpireSecond() });
				}
				this.memcachedClient.set(genedKey, namespace.getExpireSecond(), value);
			} catch (Exception e) {
				logger.error("写数据异常", e);
			}
		}
		
	}
	
	@Override
	public void evict(Object key) {
		try {
			String genedKey = genKey(key);
			if (logger.isDebugEnabled()) {
				logger.debug("evict:{}", genedKey);
			}
			this.memcachedClient.delete(genedKey);
		} catch (Exception e) {
			logger.error("删除数据异常", e);
		}
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException("不支持删除所有数据操作");
	}
}
