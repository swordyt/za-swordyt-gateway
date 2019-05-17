package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午6:25:53
 */
public class Type extends ArithmeticOperationSymbolImpl {
	public Type(OperationUtensil operation,HttpServletRequest request,Rule rule,LogHandler log) {
		super(".type()", request.getMethod(), operation, request, rule, log);
	}
}
