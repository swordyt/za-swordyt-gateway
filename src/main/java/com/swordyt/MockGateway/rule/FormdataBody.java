package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午6:30:35
 */
public class FormdataBody extends ArithmeticOperationSymbolImpl {
	public FormdataBody(Rule rule,OperationUtensil operation,HttpServletRequest request,String key,LogHandler log) {
		super(".formdataBody("+key+")", request.getParameter(key), operation, request, rule, log);
	}
}
