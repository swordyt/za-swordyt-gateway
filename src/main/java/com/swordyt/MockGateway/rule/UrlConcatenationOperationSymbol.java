package com.swordyt.MockGateway.rule;

import javax.servlet.http.HttpServletRequest;

import com.swordyt.MockGateway.core.LogHandler;
import com.swordyt.MockGateway.core.ProxyProcessor;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月8日 下午6:09:18
 */
public class UrlConcatenationOperationSymbol<T> extends ConcatenationOperationSymbol<T> {
	private String url;
	private HttpServletRequest request;

	public UrlConcatenationOperationSymbol(HttpServletRequest request, String url, OperationUtensil operation,
			T instance, LogHandler log) {
		super(operation, instance, log);
		this.url = url;
		this.request = request;
	}

	public UrlConcatenationOperationSymbol<T> replaceFirst(String regex, String replacement) {
		String message = ".replaceFirst(" + regex + "," + replacement + ")";
		String newUrl = null;
		Boolean flag = false;
		try {
			if (this.operation.operation()) {
				newUrl = this.url.replaceFirst(regex, replacement);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.operation.clearOperation();
		if (flag) {
			this.operation.isTrue();
		} else {
			this.operation.isFalse();
		}
		this.log.addStep(message, this.url, newUrl, flag);
		if (newUrl != null) {
			this.url = newUrl;
			this.request.setAttribute(ProxyProcessor.ATTR_REQUEST_URI, newUrl);
		}
		return this;
	}
}
