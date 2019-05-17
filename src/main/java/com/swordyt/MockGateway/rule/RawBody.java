package com.swordyt.MockGateway.rule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午6:31:02
 */
public class RawBody extends ArithmeticOperationSymbolImpl {
	private String key;
	private LogHandler log;
	private HttpServletRequest request;
	private OperationUtensil operation;

	public RawBody(Rule rule, OperationUtensil operation, HttpServletRequest request, LogHandler log) {
		super(".rawBody()", getValue(request), operation, request, rule, log);
	}

	private static String getValue(HttpServletRequest request) {
		try {
			InputStream in = request.getInputStream();
			byte[] buf = new byte[1024];
			StringBuilder builder = new StringBuilder();
			while ((in.read(buf)) != -1) {
				builder.append(new String(buf));
			}
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			request.getServletContext().log("获取body出错");
		}
		return null;
	}
}
