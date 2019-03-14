package com.liulnn;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dy on 2017/6/9.
 */
public class SpringJDBCTest {

    private JdbcTemplate jdbcTemplate;
    
    //貌似不能和spring.xml配置共同使用
    public SpringJDBCTest(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	public void testSQL(){
        try {
            String sql = "select * from test";
            List rows = jdbcTemplate.queryForList(sql);
            Iterator it = rows.iterator();
            while(it.hasNext()) {
                Map testMap = (Map) it.next();
                System.out.print(testMap.get("id") + "\t");
                System.out.print(testMap.get("testname") + "\t");
                System.out.print(testMap.get("testremark") + "\t");

            }
        }catch (DataAccessException es){
            es.printStackTrace();
        }

    }
   
}
