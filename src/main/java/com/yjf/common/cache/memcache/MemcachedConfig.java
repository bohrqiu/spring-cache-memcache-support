package com.yjf.common.cache.memcache;

import java.util.List;

import org.springframework.util.Assert;

import net.rubyeye.xmemcached.MemcachedClient;
/**
 * 
 *                       
 * @Filename MemcachedConfig.java
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
public class MemcachedConfig {
	private MemcachedClient			memcachedClient;
	private List<MemcachedNameSpace>	cacheNameSpaces;
	private int						defaultExpireSecond;
	
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}
	
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
	
	public List<MemcachedNameSpace> getCacheNameSpaces() {
		return cacheNameSpaces;
	}
	
	public void setCacheNameSpaces(List<MemcachedNameSpace> cacheNameSpaces) {
		this.cacheNameSpaces = cacheNameSpaces;
	}
	
	public int getDefaultExpireSecond() {
		return defaultExpireSecond;
	}
	
	public void setDefaultExpireSecond(int defaultExpireSecond) {
		Assert.isTrue(defaultExpireSecond >= 0, "默认过期时间必须大于等于0");
		this.defaultExpireSecond = defaultExpireSecond;
	}
	
}
