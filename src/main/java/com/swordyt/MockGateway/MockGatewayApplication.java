package com.swordyt.MockGateway;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.ImmutableMap;
import com.swordyt.MockGateway.core.ProxyServlet;
import com.swordyt.MockGateway.rule.Request;

@SpringBootApplication
@Configuration
@ServletComponentScan
public class MockGatewayApplication implements ApplicationContextAware{
	ApplicationContext applicationContext;
	public static void main(String[] args) {
		SpringApplication.run(MockGatewayApplication.class, args);
//		Request request=new Request(null,null,null);
//		request.type().equalTo("GET").forward("https://www.cnblogs.com");
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext=applicationContext;
	}
}
