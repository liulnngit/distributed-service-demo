package com.liulnn;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 使用countdownLatch模拟并发测试
 * 这里模拟200个并发
 */
public class ConcurrencyTest {
	
	private static String userId = "123456";
	
	private static final int NUM = 200;
	
	private CountDownLatch cdl = new CountDownLatch(NUM);
	
	@Test
	public void countdownlatchTest() throws InterruptedException{
		
		for (int i = 0; i < NUM; i++) {
			new Thread(new Request()).start();
			cdl.countDown();
		}
		/*
         * 在多线程编程中,Thread.CurrentThread 表示获取当前正在运行的线程，join方法是阻塞当前调用线程，
         * 直到某线程完全执行才调用线程才继续执行，
         * 如果获取的当前线程是主线程，调用Join方法,阻塞主线程
         */
		Thread.currentThread().join();
	}

	class Request implements Runnable{
		
		public void run() {
			try {
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//模拟耗时
			getTotalAmountById(userId);
			
		}
		
		public void getTotalAmountById(String userId){
			//模拟查询耗时
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+"--"+System.currentTimeMillis());
		}
		
	}
	
}


