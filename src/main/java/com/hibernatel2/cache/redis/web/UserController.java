package com.hibernatel2.cache.redis.web;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/only-users-pagable")
	public List<UserDto> getOnlyUsersPagable(Pageable page) {
		return this.userService.getOnlyUserDetails(page);
	}
	
	@GetMapping("/{id}")
	public UserDto getUser(@PathVariable("id") Long id) {
		return this.userService.getUser(id);
	}

	@PostMapping
	public UserDto saveUser(@RequestParam("id") long i) {
		return this.userService.saveUser(i);
	}

	@PutMapping
	public UserDto updateUser(@RequestParam("id") long i, @RequestParam("name") String name) {
		return this.userService.updateUser(i, name);
	}

	@DeleteMapping
	public boolean deleteUser(@RequestParam("id") long i) {
		this.userService.deleteUser(i);
		return true;
	}
}
