package com.hibernatel2.cache.redis.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hibernatel2.cache.redis.dtos.UserDto;
import com.hibernatel2.cache.redis.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public List<UserDto> getUsers() {
		return this.userService.getUserDetails();
	}
	
	@GetMapping("/query")
	public List<UserDto> getUsersWithEMF() {
		return this.userService.getUserDetailsWithEMF();
	}
	
	@GetMapping("/only-users")
	public List<UserDto> getOnlyUsers() {
		return this.userService.getOnlyUserDetails();
	}
	
	@GetMapping("/{id}")
	public UserDto getUser(@PathVariable("id") Long id) {
		return this.userService.getUser(id);
	}
}
