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
    private PropertiesUtil propertiesUtil = new PropertiesUtil("dpcore-slave.properties");

    @Bean
    public BasicDataSource getBasicDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(propertiesUtil.getStringValue("driverClassName"));
        dataSource.setUrl(propertiesUtil.getStringValue("url"));
        dataSource.setUsername(propertiesUtil.getStringValue("username"));
        dataSource.setPassword(propertiesUtil.getStringValue("password"));
        dataSource.setInitialSize(propertiesUtil.getIntValue("initialSize"));
        dataSource.setMaxTotal(propertiesUtil.getIntValue("maxTotal"));
        dataSource.setMaxIdle(propertiesUtil.getIntValue("maxIdle"));
        dataSource.setMinIdle(propertiesUtil.getIntValue("minIdle"));
        dataSource.setMaxWaitMillis(propertiesUtil.getLongValue("maxWait"));
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
        jedisPoolConfig.setMaxTotal(propertiesUtil.getIntValue("redis.maxTotal"));
        jedisPoolConfig.setMaxIdle(propertiesUtil.getIntValue("redis.maxIdle"));
        jedisPoolConfig.setMinIdle(propertiesUtil.getIntValue("redis.minIdle"));
        jedisPoolConfig.setMaxWaitMillis(propertiesUtil.getLongValue("redis.maxWaitMillis"));
        jedisPoolConfig.setTestOnBorrow(propertiesUtil.getBooleanValue("redis.testOnBorrow"));
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory getJedisConnectionFactory(){
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(propertiesUtil.getStringValue("redis.ip"));
        connectionFactory.setPort(propertiesUtil.getIntValue("redis.port"));
        connectionFactory.setPassword(propertiesUtil.getStringValue("redis.password"));
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
