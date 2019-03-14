package com.liulnn.springcloud.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.liulnn")
public class SmsServiceApplication {

    public static void main(String[] args){
        new SpringApplicationBuilder(SmsServiceApplication.class).web(true).run();
    }

}
