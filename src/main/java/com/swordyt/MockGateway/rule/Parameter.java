package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.testng.Assert;

import com.alibaba.fastjson.JSON;
import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午6:26:23
 */
public class Parameter extends ArithmeticOperationSymbolImpl {

	public Parameter(OperationUtensil operation, HttpServletRequest request, Rule rule, String key, LogHandler log) {
		super(".parameter(\"" + key + "\")", request.getParameter(key), operation, request, rule, log);
	}
}
