package com.hibernatel2.cache.redis.repositories;

import com.hibernatel2.cache.redis.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<Employee> findAll(Pageable page);
}
