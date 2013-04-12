package com.yjf.common.cache.test.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yjf.common.cache.test.model.Person;
import com.yjf.common.cache.test.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService, InitializingBean {
	
	private static final Logger	logger	= LoggerFactory
											.getLogger(PersonServiceImpl.class.getName());
	Map<String, Person>			map;
	/**
	 * 命名空间为person，key=id，如果缓存中有，就不会执行此方法，从缓存中读取数据
	 * @param id
	 * @return
	 * @see com.yjf.common.cache.test.service.PersonService#getPersonById(java.lang.String)
	 */
	@Cacheable("person")
	public Person getPersonById(String id) {
		logger.info("请求用户查询服务：{}", id);
		return map.get(id);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		map = new HashMap<String, Person>();
		Person p1 = new Person();
		p1.setAge(1);
		p1.setId("1");
		p1.setName("1");
		map.put(p1.getId(), p1);
		
		Person p2 = new Person();
		p2.setAge(2);
		p2.setId("2");
		p2.setName("2");
		map.put(p2.getId(), p2);
	}
	
	/**
	 * 缓存命名空间为person，缓存key为person的id属性，value为方法返回的值，如果方法为void的，则不缓存任何值，请注意
	 * @param person
	 * @return
	 * @see com.yjf.common.cache.test.service.PersonService#savePerson(com.yjf.common.cache.test.model.Person)
	 */
	@CachePut(value = "person", key = "#person.id")
	@Override
	public Person savePerson(Person person) {
		logger.info("请求注册用户服务：{}", person);
		map.put(person.getId(), person);
		return person;
	}
	/**
	 *  缓存命名空间为person，清除key为id的缓存
	 * @param id
	 * @see com.yjf.common.cache.test.service.PersonService#deletePerson(java.lang.String)
	 */
	@CacheEvict(value = "person")
	@Override
	public void deletePerson(String id) {
		logger.info("请求删除用户服务：{}", id);
		map.remove(id);
	}
	
}
