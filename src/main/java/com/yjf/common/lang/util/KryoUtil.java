package com.yjf.common.lang.util;

import com.esotericsoftware.kryo.Kryo;

/**
 * 
 * 获取kryo对象工具类                      
 * @Filename KryoUtils.java
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
public class KryoUtil {
	/**
	 * 输出缓冲区最大大小Integer.MAX_VALUE
	 */
	public static final int				MAX_BUFFER_SIZE		= -1;
	
	/**
	 * 输出缓冲区初始大小
	 */
	public static final int				INITIAL_BUFFER_SIZE	= 1024 * 2;
	/**
	 * 由于Kryo对象不是threadsafe的，所以在线程内缓存
	 */
	private static ThreadLocal<Kryo>	kryoThreadLocal		= new ThreadLocal<Kryo>() {
																protected Kryo initialValue() {
																	return new Kryo();
																}
															};
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				kryoThreadLocal.remove();
			}
		}));
	}
	
	/**
	 * 获得 kryo对象
	 * @return
	 */
	public static Kryo get() {
		return kryoThreadLocal.get();
	}
	
}
