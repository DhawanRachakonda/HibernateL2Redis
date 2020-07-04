package com.hibernatel2.cache.redis.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hibernatel2.cache.redis.dtos.UserDto;
import com.hibernatel2.cache.redis.entities.Employee;
import com.hibernatel2.cache.redis.repositories.UserRepository;

@Service
@Transactional
public class UserService {
	
	private final UserRepository userRepository;
	private final EntityManager entityManager;
	
	@Autowired
	public UserService(UserRepository userRepository, @Qualifier("entityManagerFactoryBean")EntityManager entityManager) {
		this.userRepository = userRepository;
		this.entityManager = entityManager;
	}

	public List<UserDto> getUserDetails() {
		List<Employee> users = this.userRepository.findAll();
		return users.stream().map(user -> {user.getDocs(); return UserDto.buildUserDto(user);}).collect(Collectors.toList());
	}
	
	public List<UserDto> getUserDetailsWithEMF() {
		Query query = this.entityManager.createQuery("select e from Employee e").setHint("org.hibernate.cacheable", true);
		List<Employee> users = query.getResultList();
		return users.stream().map(user -> {user.getDocs(); return UserDto.buildUserDto(user);}).collect(Collectors.toList());
	}
	
	public List<UserDto> getOnlyUserDetails() {
		List<Employee> users = this.userRepository.findAll();
		return users.stream().map(user -> UserDto.buildOnlyUserDto(user)).collect(Collectors.toList());
	}
	
	public UserDto getUser(Long id) {
		return UserDto.buildOnlyUserDto(this.userRepository.findById(id).get());
	}
}
