package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.testng.Assert;

import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月8日 下午5:53:01
 */
public class Url implements ArithmeticOperationSymbol<UrlConcatenationOperationSymbol<Rule>> {
	private OperationUtensil operation;
	private HttpServletRequest request;
	private Rule rule;
	private LogHandler log;
	private String value;
	private _Url _this;
	private UrlConcatenationOperationSymbol<Rule> returnObj;
	public Url(OperationUtensil operation, HttpServletRequest request, Rule rule, LogHandler log) {
		String message = ".url()";
		this.rule = rule;
		this.log = log;
		this.operation = operation;
		this.request = request;
		this.value = this.request.getPathInfo();
		this.log.addStep(message, null, null, null);
		this.returnObj=new UrlConcatenationOperationSymbol<Rule>(this.request, this.value, this.operation, this.rule, this.log);
		this._this = new _Url(message, this.value, operation, request, rule, log);
	}

	private class _Url extends ArithmeticOperationSymbolImpl {
		protected _Url(String message, String value, OperationUtensil operation, HttpServletRequest request, Rule rule,
				LogHandler log) {
			super(message, value, operation, request, rule, log);
		}
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> equalTo(String value) {
		this._this.equalTo(value);
		return this.returnObj;
	}

	/**
	 * %like% %like like%
	 */
	@Override
	public UrlConcatenationOperationSymbol<Rule> like(String value) {
		this._this.like(value);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> equalTo(Integer value) {
		this._this.equalTo(value);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> notEqualTo(Integer value) {
		this._this.notEqualTo(value);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> greaterThan(Integer value) {
		this._this.greaterThan(value);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> greaterThanOrEqualTo(Integer value) {
		this._this.greaterThanOrEqualTo(value);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> lessThan(Integer value) {
		this._this.lessThan(value);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> lessThanOrEqualTo(Integer value) {
		this._this.lessThanOrEqualTo(value);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> between(Integer value1, Integer value2) {
		this._this.between(value1,value2);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> notBetween(Integer value1, Integer value2) {
		this._this.notBetween(value1,value2);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> isNull() {
		this._this.isNull();
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> isNotNull() {
		this._this.isNotNull();
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> notEqualTo(String value) {
		this._this.notEqualTo(value);
		return this.returnObj;
	}

//	@Override
//	public UrlConcatenationOperationSymbol<Rule> greaterThan(String value) {
//		this._this.greaterThan(value);
//		return this.returnObj;
//	}
//
//	@Override
//	public UrlConcatenationOperationSymbol<Rule> greaterThanOrEqualTo(String value) {
//		this._this.greaterThanOrEqualTo(value);
//		return this.returnObj;
//	}
//
//	@Override
//	public UrlConcatenationOperationSymbol<Rule> lessThan(String value) {
//		this._this.lessThan(value);
//		return this.returnObj;
//	}
//
//	@Override
//	public UrlConcatenationOperationSymbol<Rule> lessThanOrEqualTo(String value) {
//		this._this.lessThanOrEqualTo(value);
//		return this.returnObj;
//	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> notLike(String value) {
		this._this.notLike(value);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> in(String listJson) {
		this._this.in(listJson);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> notIn(String listJson) {
		this._this.notIn(listJson);
		return this.returnObj;
	}

	@Override
	public UrlConcatenationOperationSymbol<Rule> jsonPath(String jsonPath) {
		
		return null;
	}

//	@Override
//	public UrlConcatenationOperationSymbol<Rule> between(String value1, String value2) {
//		this._this.between(value1,value2);
//		return this.returnObj;
//	}
//
//	@Override
//	public UrlConcatenationOperationSymbol<Rule> notBetween(String value1, String value2) {
//		this._this.between(value1,value2);
//		return this.returnObj;
//	}
}
