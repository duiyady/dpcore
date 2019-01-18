package com.duiya.config;

import com.duiya.utils.PropertiesUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;


@Configuration
public class DataConfig {
    @Bean
    public BasicDataSource getBasicDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(PropertiesUtil.getStringValue("driverClassName"));
        dataSource.setUrl(PropertiesUtil.getStringValue("url"));
        dataSource.setUsername(PropertiesUtil.getStringValue("username"));
        dataSource.setPassword(PropertiesUtil.getStringValue("password"));
        dataSource.setInitialSize(PropertiesUtil.getIntValue("initialSize"));
        dataSource.setMaxTotal(PropertiesUtil.getIntValue("maxTotal"));
        dataSource.setMaxIdle(PropertiesUtil.getIntValue("maxIdle"));
        dataSource.setMinIdle(PropertiesUtil.getIntValue("minIdle"));
        dataSource.setMaxWaitMillis(PropertiesUtil.getLongValue("maxWait"));
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean getSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setDataSource(getBasicDataSource());
        sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath:com/duiya/*/*.xml"));
        return sqlSessionFactory;
    }

    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.duiya.dao");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("getSqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }

    @Bean
    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(PropertiesUtil.getIntValue("redis.maxTotal"));
        jedisPoolConfig.setMaxIdle(PropertiesUtil.getIntValue("redis.maxIdle"));
        jedisPoolConfig.setMinIdle(PropertiesUtil.getIntValue("redis.minIdle"));
        jedisPoolConfig.setMaxWaitMillis(PropertiesUtil.getLongValue("redis.maxWaitMillis"));
        jedisPoolConfig.setTestOnBorrow(PropertiesUtil.getBooleanValue("redis.testOnBorrow"));
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory getJedisConnectionFactory(){
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(PropertiesUtil.getStringValue("redis.ip"));
        connectionFactory.setPort(PropertiesUtil.getIntValue("redis.port"));
        connectionFactory.setPassword(PropertiesUtil.getStringValue("redis.password"));
        connectionFactory.setPoolConfig(getJedisPoolConfig());
        return connectionFactory;
    }

    @Bean
    public StringRedisTemplate getRedisTemplate(){
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());
        return redisTemplate;
    }


}
