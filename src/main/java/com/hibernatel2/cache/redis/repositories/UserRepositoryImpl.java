package com.hibernatel2.cache.redis.repositories;

import com.hibernatel2.cache.redis.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public UserRepositoryImpl(@Qualifier("entityManagerFactoryBean") final EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public Page<Employee> findAll(Pageable page) {
        return this.getRecords(page, Employee.class);
    }

    private <T> Page<T> getRecords(Pageable page, Class<T> clazz) {
        Query query = this.entityManager.createQuery(String.format("select i from %s i", clazz.getSimpleName()));

        query.setHint("org.hibernate.cacheable", false);
        query.setFirstResult(page.getPageNumber() * page.getPageSize());
        query.setMaxResults(page.getPageSize());

        List<T> records = query.getResultList();
        return new PageImpl<T>(records, page, records.size());
    }
}
