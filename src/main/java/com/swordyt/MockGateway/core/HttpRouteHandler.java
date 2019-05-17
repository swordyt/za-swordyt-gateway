package com.swordyt.MockGateway.core;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.ImmutableMap;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年4月30日 上午11:01:22
 */
public class HttpRouteHandler implements InterfaceMapper {
	@Override
	public Boolean thirdRuleMatching(HttpServletRequest request, HttpServletResponse response, Object reqText) {
		// TODO Auto-generated method stub
		return interfaceMapper.thirdRuleMatching(request, response, reqText);
	}

	@Override
	public Object firstParseRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return interfaceMapper.firstParseRequest(request, response);
	}

	@Override
	public Object secondDecode(HttpServletRequest request, HttpServletResponse response, Object reqText) {
		// TODO Auto-generated method stub
		return interfaceMapper.secondDecode(request, response, reqText);
	}

	@Override
	public void fifthEncode(HttpServletRequest request, HttpServletResponse response, Object reqText) throws Exception {
		// TODO Auto-generated method stub
		interfaceMapper.fifthEncode(request, response, reqText);
	}

	private ApplicationContext applicationContext;
	private InterfaceMapper interfaceMapper;

	public HttpRouteHandler(ApplicationContext applicationContext, String beanName) throws Exception {
		System.out.println("Request Url:" + beanName);
		this.applicationContext = applicationContext;
		if (this.applicationContext.containsBean(beanName)) {
			interfaceMapper = this.applicationContext.getBean(beanName, InterfaceMapper.class);
		}
	}

	@Override
	public Object fourthRequestMounteBank(HttpServletRequest request, HttpServletResponse response, Object reqText) {
		// TODO Auto-generated method stub
		return interfaceMapper.fourthRequestMounteBank(request, response, reqText);
	}

	@Override
	public String getGatewayUrl() {
		// TODO Auto-generated method stub
		return interfaceMapper.getGatewayUrl();
	}
}
