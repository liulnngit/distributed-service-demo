package com.liulnn.springcloud.service.member.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration;
import org.springframework.cloud.config.client.ConfigServiceBootstrapConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@PropertySource(value = { "classpath:application.properties" })
@Import(value = { ConfigServiceBootstrapConfiguration.class })
@ImportResource("classpath:spring-servlet.xml")
public class WebConfig {

	// 初始化sprinvcloudconfig的相关配置
	@Bean
	public PropertySourceBootstrapConfiguration propertySourceBootstrapConfiguration(
			ConfigurableApplicationContext applicationContext) {
		PropertySourceBootstrapConfiguration propertySourceBootstrapConfiguration = applicationContext.getBeanFactory()
				.createBean(PropertySourceBootstrapConfiguration.class);
		propertySourceBootstrapConfiguration.initialize(applicationContext);
		return propertySourceBootstrapConfiguration;

	}

}
