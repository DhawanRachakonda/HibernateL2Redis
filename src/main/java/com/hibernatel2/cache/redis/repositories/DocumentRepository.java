package com.hibernatel2.cache.redis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hibernatel2.cache.redis.entities.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
	
}
