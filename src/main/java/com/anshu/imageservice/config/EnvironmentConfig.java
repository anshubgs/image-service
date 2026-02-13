package com.anshu.imageservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class EnvironmentConfig {

    // ------------------------------
    // LOCAL Profile Beans
    // ------------------------------
    /*@Profile("local")
    @Bean
    public RedisConnectionFactory localRedisConnectionFactory() {
        // Local Redis
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Profile("local")
    @Bean
    public RedisTemplate<String, Object> localRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Profile("local")
    @Bean
    public DataSource localDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/monitoring");
        ds.setUsername("postgres");
        ds.setPassword("root");
        return ds;
    }

    // ------------------------------
    // DEV Profile Beans
    // ------------------------------
    @Profile("dev")
    @Bean
    public DataSource devDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://dev-server:5432/monitoring");
        ds.setUsername("devuser");
        ds.setPassword("devpass");
        return ds;
    }

    @Profile("dev")
    @Bean
    public RedisConnectionFactory devRedisConnectionFactory() {
        return new LettuceConnectionFactory("dev-redis-server", 6379);
    }

    @Profile("dev")
    @Bean
    public RedisTemplate<String, Object> devRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    // ------------------------------
    // TEST Profile Beans
    // ------------------------------
    @Profile("test")
    @Bean
    public DataSource testDataSource() {
        // In-memory H2 DB for tests
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }

    @Profile("test")
    @Bean
    public RedisTemplate<String, Object> testRedisTemplate() {
        // Dummy RedisTemplate for test
        return new RedisTemplate<>();
    }*/
}
