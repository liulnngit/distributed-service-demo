package com.liulnn.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.liulnn.domain.Order;
import com.liulnn.service.IOrderService;

public class OrderServiceImpl implements IOrderService{
	
    private static Log log = LogFactory.getLog(OrderServiceImpl.class);
    
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void insert(Order order) {
		final String merchantId = order.getMerchantId();
		final double txMoney = order.getTxMoney();
		jdbcTemplate.update("insert into orders(merchant_id,tx_money) values(?,?)",  
                new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, merchantId);
						ps.setDouble(2, txMoney);
					}  
                });  
		log.info("保存到订单，商家ID："+merchantId+",amount:"+txMoney);
	}
	
	public Order selectOrderByMerchantId(String merchantId) {
		final Order order = new Order();  
		jdbcTemplate.query("select * from orders where merchant_id = ?",  
		                    new Object[] {merchantId},  
		                    new RowCallbackHandler() {  
		                        public void processRow(ResultSet rs) throws SQLException {  
		                        	order.setMerchantId(rs.getString("merchant_id")); 
		                        	order.setTxMoney(rs.getDouble("tx_money"));
		                        }  
		                    });  
		return order;
	}
	
	
	public Double getTotalAmountByMerchantId(String merchantId) {
		Double txAmt = null;
		String sql = "select sum(tx_money) as total from orders where merchant_id = '"+merchantId+"'";
		//txAmt =jdbcTemplate.queryForObject(sql, Double.class);
		List<Double> amts = jdbcTemplate.query(sql, new Object[] {}, new RowMapper<Double>() {
            public Double mapRow(ResultSet result, int rowNum) throws SQLException {
                return result.getDouble("total");
            }
        });
		if(amts.size()!=0){
			return amts.get(0);
		}
		return txAmt;
	}
	
	
	

}
