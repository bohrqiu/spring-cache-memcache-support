package com.yjf.common.cache;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import org.springframework.cache.annotation.SpringCacheAnnotationParser;
import org.springframework.cache.interceptor.CacheOperation;

/**
 * 
 * 待扩展                     
 * @Filename YJFSpringCacheAnnotationParser.java
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
 *<li>Date: 2013-1-18</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
@SuppressWarnings("serial")
public class YJFSpringCacheAnnotationParser extends SpringCacheAnnotationParser {
	
	@Override
	public Collection<CacheOperation> parseCacheAnnotations(AnnotatedElement ae) {
		return super.parseCacheAnnotations(ae);
	}
}
