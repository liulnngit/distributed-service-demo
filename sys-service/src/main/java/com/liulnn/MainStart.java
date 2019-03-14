package com.liulnn;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by dy on 2017/6/9.
 */
public class MainStart {

    public static void main(String args[]){

        AbstractApplicationContext appContext=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        appContext.start();

        /*JdbcTemplate jdbcTemplate = (JdbcTemplate) appContext.getBean("jdbcTemplate");
        SpringJDBCTest test = new SpringJDBCTest(jdbcTemplate);
        test.testSQL();*/
        
    }

}
