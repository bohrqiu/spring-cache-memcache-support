package com.yjf.common.cache.memcache;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * memcached spring 工厂bean
 *                       
 * @Filename MemcachedConfigFactoryBean.java
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
public class MemcachedConfigFactoryBean implements FactoryBean<MemcachedConfig>, InitializingBean,
										DisposableBean {
	
	private static final Logger	logger	= LoggerFactory.getLogger(MemcachedConfigFactoryBean.class
											.getName());
	private MemcachedConfig		config;
	private Resource			configLocation;
	
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}
	
	@Override
	public void destroy() throws Exception {
		if (config.getMemcachedClient() != null) {
			config.getMemcachedClient().shutdown();
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("[memcached]初始化memcached缓存配置");
		Properties prop = loadProperties();
		config = new MemcachedConfig();
		parseDefaultExpireSecond(prop, config);
		parseCacheNamespace(prop, config);
		parseMemcachedClient(prop, config);
		
	}
	
	private void parseMemcachedClient(Properties prop, MemcachedConfig config) throws IOException {
		List<InetSocketAddress> addressList = AddrUtil.getAddresses(prop
			.getProperty("memcached.host"));
		for (InetSocketAddress address : addressList) {
			logger.info("[memcached]配置server:{}", address);
		}
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(addressList);
		//使用一致性hash
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		logger.info("[memcached]使用一致性哈希算法来实现client端集群");
		//使用二进制协议
		builder.setCommandFactory(new BinaryCommandFactory());
		
		logger.info("[memcached]使用二进制协议");
		int connectionPoolSize = 1;
		try {
			connectionPoolSize = Integer.parseInt(prop.getProperty("memcached.connectionPoolSize",
				"1"));
		} catch (NumberFormatException e) {
			logger.error("", e);
		}
		logger.info("[memcached]连接池大小为：{}", connectionPoolSize);
		
		builder.setConnectionPoolSize(connectionPoolSize);
		KryoTranscoder kryoTranscoder = new KryoTranscoder();
		builder.setTranscoder(kryoTranscoder);
		logger.info("[memcached]编解码器为：{}", kryoTranscoder);
		MemcachedClient client = builder.build();
		
		config.setMemcachedClient(client);
	}
	
	private void parseCacheNamespace(Properties prop, MemcachedConfig config) {
		String namespaceStr = prop.getProperty("memcached.namespace");
		if (null == namespaceStr) {
			logger.info("[memcached]没有配置命名空间，所有命名空间数据过期时间:{}", config.getDefaultExpireSecond());
			return;
		} else {
			List<MemcachedNameSpace> namespaceList = new ArrayList<MemcachedNameSpace>();
			String[] namespaces = namespaceStr.split(" ");
			for (String namespace : namespaces) {
				String[] n = namespace.split(":");
				MemcachedNameSpace memcachedNameSpace = null;
				try {
					memcachedNameSpace = new MemcachedNameSpace(n[0], Integer.parseInt(n[1]));
					namespaceList.add(memcachedNameSpace);
				} catch (Exception e) {
					logger
						.error(
							"[memcached]命名空间配置[{}]不符合格式,参考\nname:expirescond,name1:expirescond1\nexpiresecond必须为数字",
							namespace);
				}
				
			}
			config.setCacheNameSpaces(namespaceList);
		}
	}
	
	private void parseDefaultExpireSecond(Properties prop, MemcachedConfig config) {
		int defaultExpireSecond = Integer.parseInt(prop.getProperty(
			"memcached.defaultExpireSecond", "0"));
		config.setDefaultExpireSecond(defaultExpireSecond);
		logger.info("[memcached]设置默认过期时间:{}", config.getDefaultExpireSecond());
	}
	
	private Properties loadProperties() throws IOException {
		if (this.configLocation != null) {
			InputStream is = this.configLocation.getInputStream();
			try {
				Properties prop = new Properties();
				prop.load(is);
				return prop;
			} finally {
				is.close();
			}
		} else {
			throw new RuntimeException("memcache配置文件路径错误");
		}
		
	}
	
	@Override
	public MemcachedConfig getObject() throws Exception {
		return config;
	}
	
	@Override
	public Class<?> getObjectType() {
		return MemcachedClient.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
	
}
