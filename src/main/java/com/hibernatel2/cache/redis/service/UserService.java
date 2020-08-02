package com.hibernatel2.cache.redis.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.hibernatel2.cache.redis.entities.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
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

	@Cacheable(value = "usersCache",  unless = "#result == null")
	public List<UserDto> getUserDetails() {
		List<Employee> users = StreamSupport.stream(this.userRepository.findAll().spliterator(), false).collect(Collectors.toList());
		return users.stream().map(user -> {user.getDocs(); return UserDto.buildUserDto(user);}).collect(Collectors.toList());
	}
	
	public List<UserDto> getUserDetailsWithEMF() {
		Query query = this.entityManager.createQuery("select e from Employee e").setHint("org.hibernate.cacheable", true);
		List<Employee> users = query.getResultList();
		return users.stream().map(user -> {user.getDocs(); return UserDto.buildUserDto(user);}).collect(Collectors.toList());
	}
	
	public List<UserDto> getOnlyUserDetails() {
		List<Employee> users = StreamSupport.stream(this.userRepository.findAll().spliterator(), false).collect(Collectors.toList());
		return users.stream().map(user -> UserDto.buildOnlyUserDto(user)).collect(Collectors.toList());
	}

	@Cacheable(value = "usersCache",  unless = "#result == null")
	public List<UserDto> getOnlyUserDetails(Pageable page) {
//		Query query = this.entityManager.createQuery("select e from Employee e").setHint("org.hibernate.cacheable", true);
//		query.setFirstResult(page.getPageNumber() * page.getPageSize());
//		query.setMaxResults(page.getPageSize());
//		List<Employee> users = query.getResultList();
		List<Employee> users = this.userRepository.findAll(page).getContent();
		return users.stream().map(user -> UserDto.buildOnlyUserDto(user)).collect(Collectors.toList());
	}

	@Cacheable(value = "usersCache", key="#id",  unless = "#result == null")
	public UserDto getUser(Long id) {
		return UserDto.buildOnlyUserDto(this.userRepository.findById(id).get());
	}

	@CacheEvict(value = "userCache", key = "#i")
	public UserDto saveUser(Long i) {
		Employee emp = new Employee();
		emp.setName("user"+i);
		emp.setComment("comment"+i);
		Document doc = new Document();
		doc.setTitle("document"+i);
		doc.setUserId(emp);
		emp.getDocs().add(doc);
		return UserDto.buildOnlyUserDto(this.userRepository.save(emp));
	}

	@CacheEvict(value = "usersCache")
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	@CachePut(value = "usersCache", key = "#p0")
	public UserDto updateUser(Long i, String name) {
		Employee emp = userRepository.getOne(i);
		emp.setName(name);
		return UserDto.buildOnlyUserDto(userRepository.save(emp));
	}
}
