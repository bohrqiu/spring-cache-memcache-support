package com.yjf.common.cache.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.yjf.common.cache.test.model.Person;
import com.yjf.common.cache.test.service.PersonService;

/**
 * memcached spring cache支持测试类
 *                       
 * @Filename MemcachedTest.java
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/cache/test-context.xml", })
@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = BenchmarkOptions.CONCURRENCY_AVAILABLE_CORES)
public class MemcachedTest {
	@Rule
	public MethodRule			benchmarkRun	= new BenchmarkRule();
	
	private static final Logger	logger			= LoggerFactory.getLogger(MemcachedTest.class
													.getName());
	@Autowired
	private PersonService		personService;
	
	@Test
	public void testReadOnce() throws Exception {
		
		try {
			//参数为null时，会出现取id异常
			personService.savePerson(null);
		} catch (Exception e) {
			logger.error("", e);
		}
		personService.deletePerson("1");
		Person p = personService.getPersonById("1");
		Assert.assertTrue(p == null);
		p = personService.getPersonById("2");
		Assert.assertTrue(p.getName().equals("2"));
		
	}
	
	/**
	 * 测试保存
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = BenchmarkOptions.CONCURRENCY_AVAILABLE_CORES)
	public void testSave() {
		Person p1 = new Person();
		p1.setId("3");
		p1.setName("3");
		p1.setAge(3);
		personService.savePerson(p1);
		Person p2 = personService.getPersonById("3");
		Assert.assertTrue(p1.equals(p2));
	}
	
	/**
	 * 测试删除
	 */
	@Test
	public void testDelete() {
		Person p1 = new Person();
		p1.setId("1");
		p1.setName("1");
		p1.setAge(1);
		personService.savePerson(p1);
		personService.deletePerson("1");
		Person p = personService.getPersonById("1");
		Assert.assertTrue(p == null);
	}
	
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 0, concurrency = BenchmarkOptions.CONCURRENCY_AVAILABLE_CORES)
	@Test
	public void testCur() {
		new SaveTask("" + 1, personService).start();
		new DeleteTask("" + 1, personService).start();
		System.out.println("test end.");
	}
	
	public static class SaveTask {
		private String			name;
		private PersonService	personService;
		
		public SaveTask(String name, PersonService personService) {
			this.name = name;
			this.personService = personService;
		}
		
		public void start() {
			Thread saveThread = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 100; i++) {
						Person p1 = new Person();
						p1.setId(name + "-" + i);
						p1.setName(name + i);
						p1.setAge(i);
						personService.savePerson(p1);
					}
				}
			});
			saveThread.start();
			try {
				saveThread.join();
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}
	
	public static class DeleteTask {
		private String			name;
		private PersonService	personService;
		
		public DeleteTask(String name, PersonService personService) {
			this.name = name;
			this.personService = personService;
		}
		
		public void start() {
			Thread deleteThread = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 100; i++) {
						personService.deletePerson(name + "-" + i);
					}
				}
			});
			deleteThread.start();
			try {
				deleteThread.join();
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}
}
