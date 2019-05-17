package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.swordyt.MockGateway.core.LogHandler;

/** 
* @author 作者 swordyt@163.com: 
* @version 创建时间：2019年5月6日 下午6:31:18 
*/
public class BinaryBody extends ArithmeticOperationSymbolImpl{
	private LogHandler log;
	private OperationUtensil operation;
	public BinaryBody(Rule rule,OperationUtensil operation,HttpServletRequest request,LogHandler log,String key) {
		super(".binaryBody("+key+")", null, operation, request, rule, log);
	}
}
