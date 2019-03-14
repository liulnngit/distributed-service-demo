package com.liulnn;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Objects;
import com.liulnn.domain.Order;
import com.liulnn.service.impl.OrderServiceImpl;
import com.liulnn.service.impl.UserInfoImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试  
@ContextConfiguration(locations={"classpath*:applicationContext*.xml"}) //加载配置文件   
public class DaoTest {
	
	@Autowired
	UserInfoImpl userInfoImpl;
	
	@Autowired
	OrderServiceImpl orderServiceImpl;
	
	@Autowired
	JedisPool jedisPool;
	
	//从缓存中获取数据
	public String getCacheData(String merchantId){
		Jedis jedis = jedisPool.getResource();
		String data = jedis.get(merchantId+"_totalAmt");
		jedis.close();
		return data;
	}
	
	//放数据进入缓存
	public void putDataToCache(String merchantId,String data){
		Jedis jedis = jedisPool.getResource();
		jedis.set(merchantId+"_totalAmt",data);
		jedis.close();
		//System.out.println("将数据放入缓存成功!");
	}
	
	@Test
	public void TestgetCatcheData(){
		System.out.println(getCacheData("1234567"));
	}
	
	@Test
	public void TestputDataToCache(){
		putDataToCache("123","124");
	}
	
	@Test
	public void testQuery(){
		userInfoImpl.selectUserInfo();;
	}
	
	@Test
	public void testInsert(){
		Order order = new Order("123456",17);
		orderServiceImpl.insert(order);
	}
	
	@Test
	public void testSelectOrderById(){
		String merchantId ="123456";
		Order order = orderServiceImpl.selectOrderByMerchantId(merchantId);
		System.out.println("商家id:"+order.getMerchantId()+",amount:"+order.getTxMoney());
	}
	
	@Test
	public void testgetTotalAmountByMerchantId(){
		String merchantId= "123456";
		double txAmt = orderServiceImpl.getTotalAmountByMerchantId(merchantId);
		System.out.println("商家id："+merchantId+",交易总金额:"+txAmt);
	}
	
	//==============================================
	private static final int NUM =100;
	private CountDownLatch cdl = new CountDownLatch(NUM);
	
	@Test
	public void testHighCurrency(){
		//创建num个线程用于访问数据库
		for (int i = 0; i < NUM; i++) {
			new Thread(new MyThread()).start();
			cdl.countDown();
		}
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	class MyThread implements Runnable{

		public void run() {
			try {
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String merchantId = "123456";
			//String merchantId = UUID.randomUUID().toString();
			/*String totalAmt = getCacheData(merchantId);
			if(!Objects.equal(totalAmt, null)){
				System.out.println(Thread.currentThread().getName()+" get From 缓存 ====商家id:"+merchantId+"，总金额:"+totalAmt);
			}else{
				Double txAmount = orderServiceImpl.getTotalAmountByMerchantId(merchantId);
				System.out.println(Thread.currentThread().getName()+" get From DB ====商家id:"+merchantId+"，总金额:"+String.valueOf(txAmount));
				putDataToCache(merchantId, String.valueOf(txAmount));
			}*/
			
			Double txAmount = orderServiceImpl.getTotalAmountByMerchantId(merchantId);
			System.out.println(Thread.currentThread().getName()+" get From DB ====商家id:"+merchantId+"，总金额:"+txAmount);
		}
		
	}
	
	
	
}





/**
 * create table orders( merchant_id varchar(30),tx_money double);
 */
