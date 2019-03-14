package com.liulnn.springcloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * configuration 自从spring3就已经支持了
 */
@Configuration
@PropertySource(value="classpath:sms-sys.properties")
public class AppConfig {

    @Autowired
    Environment env; //环境 spring

    @Bean
    @RefreshScope
    public JedisPool jedisPool(){
        String host = env.getProperty("redis.host");
        int port = env.getProperty("redis.port",Integer.class);
        //String password = env.getProperty("redis.password");

        //JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),host,port,6000,password);
        JedisPool jedisPool = new JedisPool(host,port);
        return jedisPool;

    }
}
