package com.hibernatel2.cache.redis.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hibernatel2.cache.redis.entities.Employee;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Employee, Long>, UserRepositoryCustom {

}
