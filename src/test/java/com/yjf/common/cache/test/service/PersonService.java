package com.yjf.common.cache.test.service;

import com.yjf.common.cache.test.model.Person;

public interface PersonService {
	Person getPersonById(String id);
	
	Person savePerson(Person person);
	
	void deletePerson(String id);
}
