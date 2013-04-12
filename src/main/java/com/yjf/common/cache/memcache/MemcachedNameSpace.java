package com.yjf.common.cache.memcache;

import org.springframework.util.Assert;
/**
 * memcached命名空间，针对不同的业务划分命名空间，防止数据错乱<br/>
 * 比如核心会员服务使用命名空间paycore.customer<br/>
 * 核心帐务使用命名空间paycore.accounttrans<br/>
 * 各个命名空间里的内容就不会冲突
 *                       
 * @Filename MemcachedNameSpace.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author bohr
 *
 * @Email qzhanbo@yiji.com
 *       
 * @History
 *<li>Author: bohr.qiu</li>
 *<li>Date: 2013-1-22</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class MemcachedNameSpace {
	
	/**
	 * 
	 * 构建一个<code>MemcachedNameSpace.java</code>
	 * @param name 命名空间名称
	 * @param expireSecond 命名空间数据过期时间，单位秒
	 */
	public MemcachedNameSpace(String name, int expireSecond) {
		Assert.notNull(name, "命名空间名称不能为空");
		Assert.isTrue(expireSecond>=0, "过期时间不能小于零");
		this.name = name;
		this.expireSecond = expireSecond;
	}
	
	private String	name;
	private int		expireSecond;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getExpireSecond() {
		return expireSecond;
	}
	
	public void setExpireSecond(int expireSecond) {
		this.expireSecond = expireSecond;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemcachedNameSpace [name=").append(name).append(", expireSecond=")
			.append(expireSecond).append("]");
		return builder.toString();
	}
	
}