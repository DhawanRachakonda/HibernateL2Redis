package com.hibernatel2.cache.redis.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Cacheable
//1) TRANSACTIONAL 2) READ_WRITE 3)NONSTRICT_READ_WRITE 4) READ_ONLY decreasing strictness comes increasing performance and scalability.
@Cache(
		usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,
		region = "userCache"
		)
@Table(name= "EMPLOYEE")
public class Employee {

	@Id
    @GeneratedValue
    private Long id;

	private String name;

	private String comment;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.SUBSELECT)
    private List<Document> docs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Document> getDocs() {
		return docs;
	}

	public void setDocs(List<Document> docs) {
		this.docs = docs;
	}
}
