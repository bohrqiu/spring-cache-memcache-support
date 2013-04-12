/**
 * www.yiji.com Inc.
 * Copyright (c) 2012 All Rights Reserved.
 */
package com.yjf.common.cache;

/**
 *                       
 * @Filename LocalCache.java
 *
 * @Description <p>本地缓存必须实现的接口,提供刷新机制；减少大量的远程访问开销</p>
 *
 * @Version 1.0
 *
 * @Author peigen
 *
 * @Email peigen@yiji.com
 *       
 * @History
 *<li>Author: peigen</li>
 *<li>Date: 2012-2-6</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public interface LocalCache {
	
	/**
	 * 初始化本地缓存
	 */
	void initLocalCache();
	
	/**
	 * 刷新本地缓存信息
	 */
	void refreshLocalCache();
	
	/**
	 * 获取本地缓存的名称
	 * @return
	 */
	String getLocalCacheName();
	
	/**
	 * 打印缓存信息
	 * @return
	 */
	void dump();
	
}
