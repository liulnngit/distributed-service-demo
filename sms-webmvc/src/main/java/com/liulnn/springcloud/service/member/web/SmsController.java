package com.liulnn.springcloud.service.member.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.JedisPool;
@RestController
@RequestMapping("/sms")
@RefreshScope
public class SmsController {

	// 注入
	@Value("${jesse.configString}")
	private String configString;

	@Autowired
	private JedisPool jedisPool;

	@RequestMapping("/send")
	public String sendSms() {
		String value = jedisPool.getResource().get("hello");

		return "configString value is:" + configString + "<<<<      >>>> redis hello value:" + value;
	}
}