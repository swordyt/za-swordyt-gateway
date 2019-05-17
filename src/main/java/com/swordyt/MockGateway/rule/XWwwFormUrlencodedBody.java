package com.swordyt.MockGateway.rule;

import javax.servlet.http.HttpServletRequest;

import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午6:30:47
 */
public class XWwwFormUrlencodedBody extends ArithmeticOperationSymbolImpl {
	public XWwwFormUrlencodedBody(Rule rule,OperationUtensil operation, HttpServletRequest request, LogHandler log, String key) {
		super(".xWwwFormUrlencodedBody("+key+")", null, operation, request, rule, log);
	}
}
