package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午4:31:02
 */
public class Request implements Rule {
	private HttpServletRequest request;
	private LogHandler log;
	private OperationUtensil operation;

	public Request(HttpServletRequest request, LogHandler log,OperationUtensil operation) {
		if(request==null||log==null||operation==null) {
			throw new RuntimeException("对象初始化不能为null");
		}
		this.request = request;
		this.log = log;
		this.operation=operation;
	}

	public Body body() {
		System.out.println("body()");
		return new Body(this.operation,this.request, this, this.log);
	}

	public Header header(String key) {
		return new Header(this.operation,request, this, key, this.log);
	}

	public Type type() {
		return new Type(this.operation,this.request, this, this.log);
	}

	public Parameter parameter(String key) {
		System.out.println(".parameter(" + key + ")");
		return new Parameter(this.operation,this.request, this, key, this.log);
	}

	@Override
	public Url url() {
		// TODO Auto-generated method stub
		return new Url(this.operation,this.request,this,this.log);
	}
}
