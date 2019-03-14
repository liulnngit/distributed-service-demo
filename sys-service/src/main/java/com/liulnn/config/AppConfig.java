package com.liulnn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import redis.clients.jedis.JedisPool;

@Configuration
@PropertySource(value = "classpath:config.properties")
public class AppConfig {
	
	@Autowired
	Environment env;
	
	/*
	 * @Bean是一个方法级别上的注解，主要用在@Configuration注解的类里，也可以用在@Component注解的类里
	 * @Bean明确地指示了一种方法，什么方法呢——产生一个bean的方法，并且交给Spring容器管理
	 */
	@Bean	
	public JedisPool jedisPool(){
		String host = env.getProperty("redis.host");
		int port = env.getProperty("redis.port",Integer.class);
		JedisPool jedisPool = new JedisPool(host,port);
		return jedisPool;
	}
}
