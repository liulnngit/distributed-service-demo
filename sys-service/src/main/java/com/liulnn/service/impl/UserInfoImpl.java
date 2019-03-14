package com.liulnn.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.liulnn.service.IUserInfo;

public class UserInfoImpl implements IUserInfo{
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void selectUserInfo() {
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
