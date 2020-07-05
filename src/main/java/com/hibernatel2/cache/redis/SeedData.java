package com.hibernatel2.cache.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hibernatel2.cache.redis.entities.Document;
import com.hibernatel2.cache.redis.entities.Employee;
import com.hibernatel2.cache.redis.repositories.UserRepository;

@Component
public class SeedData implements CommandLineRunner {
	
	private final UserRepository userRepository;
	
	@Autowired
	public SeedData(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Preparing Seed Data");
		List<Employee> employees = new ArrayList<Employee>();
		for(int i=0; i<10000; i++) {
			Employee emp = new Employee();
			emp.setName("user"+i);
			emp.setComment("comment"+i);
			Document doc = new Document();
			doc.setTitle("document"+i);
			doc.setUserId(emp);
			emp.getDocs().add(doc);
			employees.add(emp);
		}
		userRepository.saveAll(employees);
	}

}
