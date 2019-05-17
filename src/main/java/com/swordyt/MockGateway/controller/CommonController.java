package com.swordyt.MockGateway.controller;

import java.io.OutputStream;
import java.util.ArrayList;
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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.swordyt.MockGateway.core.LogHandler;
import com.swordyt.MockGateway.core.ProxyProcessor;
import com.swordyt.MockGateway.core.RulePool;
import com.swordyt.MockGateway.dto.request.SaveRuleReq;
import com.swordyt.MockGateway.dto.response.ResultModel;
import com.swordyt.MockGateway.exception.BusinessException;
import com.swordyt.MockGateway.rule.RuleEngine;

@RestController
public class CommonController implements ApplicationContextAware {
	ApplicationContext applicationContext;
//	private final static String TARGET_DOMAIN = "https://www.baidu.com";
	private final static String URL_PREFIX = "/e10adc3949ba59abbe56e057f20f883e";
	private final static String IS_TRYRUN = "e10adc3949ba59abbe56e057f20f883e";

	@PostMapping(URL_PREFIX + "/rule/save")
	public ResultModel saveRule(HttpServletRequest request, @RequestBody SaveRuleReq req) {
		if (StringUtils.isBlank(req.getUrl()) || req.getPort() == null || req.getRule() == null
				|| req.getRule().isEmpty()) {
			throw new BusinessException("port/url/rule均不能为空");
		}
		RulePool.setRule(req);
		return ResultModel.success();
	}

	@GetMapping(URL_PREFIX + "/rule/query")
	public ResultModel<List<String>> queryRule(Integer port, String url) {
		return ResultModel.success(RulePool.getRule(port, url));
	}

	@PostMapping(URL_PREFIX + "/rule/sort")
	public ResultModel sortRule(@RequestBody SaveRuleReq req) {
		if (StringUtils.isBlank(req.getUrl()) || req.getPort() == null || req.getRule() == null
				|| req.getRule().isEmpty()) {
			throw new BusinessException("port/url/rule均不能为空");
		}
		RulePool.ruleSort(req.getPort(), req.getUrl(), req.getRule());
		return ResultModel.success();
	}

	@RequestMapping(value = "**")
	public void dispatchServlet(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws ServletException, Exception {
		System.out.println("规则匹配开始");
		List<LogHandler> logs = new ArrayList<LogHandler>();
		RuleEngine engine = new RuleEngine(servletRequest, servletResponse, this.applicationContext, logs);
		String targetDomain = engine.start();
		String isTryRun = servletRequest.getHeader(IS_TRYRUN);
		if (isTryRun != null && isTryRun.equalsIgnoreCase("true")) {
			System.out.println("试运行匹配");
			OutputStream out = servletResponse.getOutputStream();
			out.write(JSON.toJSONBytes(logs, SerializerFeature.SortField));
			servletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
			servletResponse.setStatus(200);
			return;
		}
		//规则匹配失败返回404页面
		if (StringUtils.isBlank(targetDomain)) {
			servletRequest.getRequestDispatcher("/error").forward(servletRequest, servletResponse);
			return;
		}
		ProxyProcessor proxyProcessor = new ProxyProcessor(servletRequest.getServletContext(), null, targetDomain,
				servletRequest, servletResponse);
		proxyProcessor.run();
		proxyProcessor.destroy();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
