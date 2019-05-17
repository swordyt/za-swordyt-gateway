package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午6:25:27
 */
public class Header extends ArithmeticOperationSymbolImpl {
	public Header(OperationUtensil operation, HttpServletRequest request, Rule rule, String key, LogHandler log) {
		super(".header(" + key + ")", request.getHeader(key), operation, request, rule, log);
	}	
}
