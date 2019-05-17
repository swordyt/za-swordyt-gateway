package com.swordyt.MockGateway.rule;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;

import com.swordyt.MockGateway.core.LogHandler;
import com.swordyt.MockGateway.core.ProxyProcessor;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午5:11:53
 */
public class ConcatenationOperationSymbol<T> {
	protected T instance;
	protected LogHandler log;
	protected OperationUtensil operation;
	protected String value;
	public ConcatenationOperationSymbol(OperationUtensil operation, T instance, LogHandler log) {
		this.instance = instance;
		this.log = log;
		this.operation = operation;
	}

	public T or() {
		this.operation.or();
		return instance;
	}

	public T and() {
		this.operation.and();
		return instance;
	}

	public String forward(String domain) {
		try {
			boolean result=this.operation.operation();
			System.out.println(result);
			if (result) {
				return domain;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
