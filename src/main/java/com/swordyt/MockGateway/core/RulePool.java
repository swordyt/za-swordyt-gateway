package com.swordyt.MockGateway.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.swordyt.MockGateway.dto.request.SaveRuleReq;
import com.swordyt.MockGateway.exception.BusinessException;

public class RulePool {
	private final static Map<Integer, Map<String, List<String>>> RULE_POOL = new HashMap<Integer, Map<String, List<String>>>();
	private volatile static boolean lock=false;
	/**
	 * 添加规则
	 */
	public synchronized static void setRule(SaveRuleReq req) {
		if(lock) {
			throw new BusinessException("系统繁忙，请稍后再试");
		}
		Map<String, List<String>> business;
		if (RULE_POOL.containsKey(req.getPort())) {
			business = RULE_POOL.get(req.getPort());
		} else {
			List<String> rules = Lists.newArrayList(req.getRule());
			business = new HashMap<String, List<String>>();
			business.put(req.getUrl(), rules);
			RULE_POOL.put(req.getPort(), business);
			return;
		}
		List<String> rules;
		if (business.containsKey(req.getUrl())) {
			rules = business.get(req.getUrl());
		} else {
			rules = Lists.newArrayList(req.getRule());
			business.put(req.getUrl(), rules);
			return;
		}
		for (String rule : req.getRule()) {
			if (rules.contains(rule)) {
				throw new BusinessException(rule + "该规则已经存在，不可重复添加。");
			}
		}
		rules.addAll(req.getRule());
	}

	/**
	 * 获取规则列表
	 */
	public static List<String> getRule(Integer port, String url) {
		Map<String, List<String>> business = RULE_POOL.get(port);
		if (business == null || business.isEmpty()) {
			return null;
		}
		return business.get(url);
	}

	/**
	 * 规则排序
	 */
	public synchronized static void ruleSort(Integer port, String url, List<String> rules) {
		lock=true;
		List<String> targetRules = getRule(port, url);
		if (targetRules == null || targetRules.isEmpty()) {
			lock=false;
			throw new BusinessException("对应规则不存在");
		}
		if (!targetRules.containsAll(rules) || !rules.containsAll(targetRules)) {
			lock=false;
			throw new BusinessException("传入规则与原规则不匹配");
		}
		RULE_POOL.get(port).put(url, rules);
		lock=false;
	}
}
