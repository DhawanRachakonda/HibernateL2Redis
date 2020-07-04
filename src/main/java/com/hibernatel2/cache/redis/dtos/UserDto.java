package com.hibernatel2.cache.redis.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hibernatel2.cache.redis.entities.Employee;

public class UserDto {

	private Long userId;
	private String userName;
	private String comment;
	private List<String> documentTitles;
	
	public UserDto(Long userId, String userName, String comment, List<String> documentTitles) {
		this.userId = userId;
		this.userName = userName;
		this.comment = comment;
		this.documentTitles = documentTitles;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<String> getDocumentTitles() {
		return documentTitles;
	}

	public void setDocumentTitles(List<String> documentTitles) {
		this.documentTitles = documentTitles;
	}

	public static UserDto buildUserDto(Employee user) {
		return new UserDto(user.getId(), user.getName(), user.getComment(), user.getDocs().stream().map(doc -> doc.getTitle()).collect(Collectors.toList()));
	}
	
	public static UserDto buildOnlyUserDto(Employee user) {
		return new UserDto(user.getId(), user.getName(), user.getComment(), new ArrayList<String>(0));
	}
}
