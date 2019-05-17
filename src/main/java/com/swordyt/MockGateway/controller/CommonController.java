package com.swordyt.MockGateway.controller;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.swordyt.MockGateway.core.ProxyProcessor;
import com.swordyt.MockGateway.core.RulePool;
import com.swordyt.MockGateway.dto.request.SaveRuleReq;
import com.swordyt.MockGateway.dto.response.ResultModel;
import com.swordyt.MockGateway.exception.BusinessException;
import com.swordyt.MockGateway.rule.RuleEngine;

@RestController
public class CommonController implements ApplicationContextAware{
	ApplicationContext applicationContext;
	private final static String TARGET_DOMAIN="https://www.baidu.com";
//	private final static String URL_PREFIX="/e10adc3949ba59abbe56e057f20f883e";
	@PostMapping("/e10adc3949ba59abbe56e057f20f883e/rule/save")
	public ResultModel saveRule(HttpServletRequest request,@RequestBody SaveRuleReq req) {
		if(StringUtils.isBlank(req.getUrl())||req.getPort()==null||req.getRule()==null||req.getRule().isEmpty()) {
			throw new BusinessException("port/url/rule均不能为空");
		}
		RulePool.setRule(req);
		return ResultModel.success();
	}

	@GetMapping("/e10adc3949ba59abbe56e057f20f883e/rule/query")
	public ResultModel<List<String>> queryRule(Integer port, String url) {
		return ResultModel.success(RulePool.getRule(port, url));
	}

	@PostMapping("/e10adc3949ba59abbe56e057f20f883e/rule/sort")
	public ResultModel sortRule(@RequestBody SaveRuleReq req) {
		if (StringUtils.isBlank(req.getUrl()) || req.getPort() == null || req.getRule() == null
				|| req.getRule().isEmpty()) {
			throw new BusinessException("port/url/rule均不能为空");
		}
		RulePool.ruleSort(req.getPort(), req.getUrl(), req.getRule());
		return ResultModel.success();
	}

	@RequestMapping(value="**")
	public void dispatchServlet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException {
		RuleEngine engine = new RuleEngine(servletRequest, servletResponse, this.applicationContext);
		String targetDomain = engine.start();
		if (StringUtils.isBlank(targetDomain)) {
			targetDomain = TARGET_DOMAIN;
		}
		ProxyProcessor proxyProcessor = new ProxyProcessor(servletRequest.getServletContext(), null, targetDomain,
				servletRequest, servletResponse);
		proxyProcessor.run();
		proxyProcessor.destroy();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
}
