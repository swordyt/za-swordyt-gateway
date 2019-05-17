package com.swordyt.MockGateway.rule;

import javax.servlet.http.HttpServletRequest;

import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午6:25:00
 */
public class Body {
	private Rule rule;
	private LogHandler log;
	private HttpServletRequest request;
	private OperationUtensil operation;

	public Body(OperationUtensil operation, HttpServletRequest request, Rule rule, LogHandler log) {
		this.rule = rule;
		this.log = log;
		this.request = request;
		this.operation = operation;
	}

	public FormdataBody formdataBody(String key) {
		return new FormdataBody(this.rule, this.operation, this.request, key, this.log);
	};

	public XWwwFormUrlencodedBody xWwwFormUrlencodedBody(String key) {
		return new XWwwFormUrlencodedBody(this.rule, this.operation, this.request, this.log, key);
	};

	public RawBody rawBody() {
		return new RawBody(this.rule, this.operation, this.request, this.log);
	};

	public BinaryBody binaryBody(String key) {
		return new BinaryBody(this.rule, this.operation, this.request, this.log, key);
	};
}
