package com.swordyt.MockGateway.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.swordyt.MockGateway.controller.CommonController;
import com.swordyt.MockGateway.rule.RuleEngine;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.HttpCookie;
import java.net.URI;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年4月30日 下午5:40:46
 *
 *          An HTTP reverse proxy/gateway servlet. It is designed to be extended
 *          for customization if desired. Most of the work is handled by
 *          <a href="http://hc.apache.org/httpcomponents-client-ga/">Apache
 *          HttpClient</a>.
 *          <p>
 *          There are alternatives to a servlet based proxy such as Apache
 *          mod_proxy if that is available to you. However this servlet is
 *          easily customizable by Java, secure-able by your web application's
 *          security (e.g. spring-security), portable across servlet engines,
 *          and is embeddable into another web application.
 *          </p>
 *          <p>
 *          Inspiration: http://httpd.apache.org/docs/2.0/mod/mod_proxy.html
 *          </p>
 */
public class ProxyServlet extends HttpServlet {
	public final static String URL_PREFIX="/c33367701511b4f6020ec61ded352059";
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
	ApplicationContext applicationContext;

	public ProxyServlet(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws ServletException, IOException {
		InputStream in = servletRequest.getInputStream();
		byte[] buf = new byte[1024];
		StringBuilder builder = new StringBuilder();
		while ((in.read(buf)) != -1) {
			builder.append(new String(buf));
		}
		System.out.println(builder.toString());
		RuleEngine engine = new RuleEngine(servletRequest, servletResponse, this.applicationContext);
		String targetDomain = engine.start();
		if (StringUtils.isBlank(targetDomain)) {
			targetDomain = "https://www.baidu.com";
		}
		// fixedThreadPool.execute(
		ProxyProcessor proxyProcessor = new ProxyProcessor(getServletContext(), getServletConfig(), targetDomain,
				servletRequest, servletResponse);
		proxyProcessor.run();
		proxyProcessor.destroy();
	}
}
