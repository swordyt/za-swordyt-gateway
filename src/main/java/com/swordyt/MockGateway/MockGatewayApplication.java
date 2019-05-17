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
//	@Bean
//	public ServletRegistrationBean registrationBean() {
//		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ProxyServlet(this.applicationContext),"/*");
//		Map<String, String> params = ImmutableMap.of("targetUri", "https://mbd.baidu.com/newspage/data/landingsuper?context=%7B%22nid%22%3A%22news_8882937857693590195%22%7D&n_type=0&p_from=1", "log", "true");
//		registrationBean.setInitParameters(params);
//		registrationBean.setOrder(3);
//		return registrationBean;
//	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext=applicationContext;
	}
}
