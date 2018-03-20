package com.cn.mySpringBootCustomer.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.cn.mySpringBootCustomer")
public class Application {
	public static void main(String[] args) {
		  ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
	}
	
	

}
