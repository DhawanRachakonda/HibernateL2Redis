package com.hibernatel2.cache.redis.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hibernatel2.cache.redis.entities.Employee;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories("com.hibernatel2.cache.redis.repositories")
@EnableTransactionManagement
public class HibernateConfig {
	
	private Map<String, Object> getRedissonCacheSettings() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(ResourceUtils.getFile(
			      "classpath:redisson_cache.json"), new TypeReference<Map<String, Object>>() {
        });
		
	}

	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {

        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        //hibernateProps.put(Environment.USE_SECOND_LEVEL_CACHE, Boolean.TRUE.toString());
        hibernateProps.put(Environment.JPA_SHARED_CACHE_MODE, "ENABLE_SELECTIVE");
        //hibernateProps.put(Environment.USE_QUERY_CACHE, Boolean.TRUE.toString());
        hibernateProps.put(Environment.HBM2DDL_AUTO, "create-drop");
        hibernateProps.put(Environment.CACHE_REGION_FACTORY, "org.redisson.hibernate.RedissonRegionFactory");
        hibernateProps.put(Environment.USE_STRUCTURED_CACHE, Boolean.FALSE.toString());
        //hibernateProps.put(Environment.GENERATE_STATISTICS, Boolean.TRUE.toString());
        //hibernateProps.put("hibernate.cache.redisson.fallback", Boolean.TRUE.toString());
        //hibernateProps.put("hibernate.cache.redisson.config", "./redisson.yaml");
        
        //hibernateProps.putAll(this.getRedissonCacheSettings());
        
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setPackagesToScan(new String[] {Employee.class.getPackage().getName() });
        result.setJpaVendorAdapter(jpaVendorAdapter());
        result.setJpaPropertyMap(hibernateProps);
        result.setDataSource(dataSource());
        return result;
    }
	
	@Bean
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }
	
	@Bean("transactionManager")
	public JpaTransactionManager jpaTransactionManager(@Qualifier("entityManagerFactoryBean")EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
	
	private JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdaptor = new HibernateJpaVendorAdapter();
		jpaVendorAdaptor.setShowSql(true);
		jpaVendorAdaptor.setGenerateDdl(false);
		jpaVendorAdaptor.setDatabasePlatform(H2Dialect.class.getName());
		return jpaVendorAdaptor;
	}
	
	private DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setUsername("sa");
		dataSource.setPassword("password");
		dataSource.setJdbcUrl("jdbc:h2:mem:testdb");
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setMinimumIdle(2);
		dataSource.setMaximumPoolSize(20);
		return dataSource;
	}
}
