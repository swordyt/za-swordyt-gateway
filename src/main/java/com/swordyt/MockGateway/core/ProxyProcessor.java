package com.swordyt.MockGateway.core;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.HttpCookie;
import java.net.URI;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月9日 上午10:01:56
 */
public class ProxyProcessor implements Runnable {
	protected boolean doForwardIP = true;
	protected boolean doLog = true;
	/** User agents shouldn't send the url fragment but what if it does? */
	protected boolean doSendUrlFragment = true;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String targetDomain;
	private URI targetUriObj;// new URI(targetUri)
	private HttpHost targetHost;// URIUtils.extractHost(targetUriObj);
	private ServletConfig servletConfig;
	private HttpClient proxyClient;
	private ServletContext servletContext;
	protected static final String P_TARGET_URI = "targetUri";
	protected static final String ATTR_TARGET_URI = ProxyServlet.class.getSimpleName() + ".targetUri";
	protected static final String ATTR_TARGET_HOST = ProxyServlet.class.getSimpleName() + ".targetHost";
	public static final String ATTR_REQUEST_URI = ProxyServlet.class.getSimpleName() + ".requestUri";
	/**
	 * A boolean parameter name to enable logging of input and target URLs to the
	 * servlet log.
	 */
	public static final String P_LOG = "log";

	/** A boolean parameter name to enable forwarding of the client IP */
	public static final String P_FORWARDEDFOR = "forwardip";

	public ProxyProcessor(ServletContext servletContext, ServletConfig servletConfig,String targetDomain,
			HttpServletRequest request, HttpServletResponse response) throws ServletException{
		this.request = request;
		this.response = response;
		this.targetDomain = targetDomain;
		this.servletConfig = servletConfig;
		this.servletContext = servletContext;
		this.init();
	}

	private void init() throws ServletException {
//		String doLogStr = getConfigParam(P_LOG);
//		if (doLogStr != null) {
//			this.doLog = Boolean.parseBoolean(doLogStr);
//		}
//		String doForwardIPString = getConfigParam(P_FORWARDEDFOR);
//		if (doForwardIPString != null) {
//			this.doForwardIP = Boolean.parseBoolean(doForwardIPString);
//		}

		initTarget();// sets target*

		HttpParams hcParams = new BasicHttpParams();
		hcParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
//		readConfigParam(hcParams, ClientPNames.HANDLE_REDIRECTS, Boolean.class);
		proxyClient = createHttpClient(hcParams);
	}

	private void initTarget() throws ServletException {
		if (this.targetDomain == null)
			throw new ServletException(P_TARGET_URI + " is required.");
		// test it's valid
		try {
			targetUriObj = new URI(targetDomain);
		} catch (Exception e) {
			throw new ServletException("Trying to process targetUri init parameter: " + e, e);
		}
		targetHost = URIUtils.extractHost(targetUriObj);
	}

	@Override
	public void run() {
		// initialize request attributes from caches if unset by a subclass by this
		// point
		if (this.request.getAttribute(ATTR_TARGET_URI) == null) {
			this.request.setAttribute(ATTR_TARGET_URI, targetDomain);
		}
		if (this.request.getAttribute(ATTR_TARGET_HOST) == null) {
			this.request.setAttribute(ATTR_TARGET_HOST, targetHost);
		}

		// Make the Request
		// note: we won't transfer the protocol version because I'm not sure it would
		// truly be compatible
		String method = this.request.getMethod();
		String proxyRequestUri = rewriteUrlFromRequest(this.request);
		HttpRequest proxyRequest;
		// spec: RFC 2616, sec 4.3: either of these two headers signal that there is a
		// message body.
		if (this.request.getHeader(HttpHeaders.CONTENT_LENGTH) != null
				|| this.request.getHeader(HttpHeaders.TRANSFER_ENCODING) != null) {
			HttpEntityEnclosingRequest eProxyRequest = new BasicHttpEntityEnclosingRequest(method, proxyRequestUri);
			// Add the input entity (streamed)
			// note: we don't bother ensuring we close the servletInputStream since the
			// container handles it
			try {
				eProxyRequest.setEntity(
						new InputStreamEntity(this.request.getInputStream(), this.request.getContentLength()));
			} catch (IOException e) {
				this.destroy();
				throw new RuntimeException(e);
			}
			proxyRequest = eProxyRequest;
		} else
			proxyRequest = new BasicHttpRequest(method, proxyRequestUri);

		copyRequestHeaders(this.request, proxyRequest);

		setXForwardedForHeader(this.request, proxyRequest);

		HttpResponse proxyResponse = null;
		try {
			// Execute the request
			if (doLog) {
				System.out.println("proxy " + method + " uri: " + this.request.getRequestURI() + " -- "
						+ proxyRequest.getRequestLine().getUri());
			}
			proxyResponse = proxyClient.execute(getTargetHost(this.request), proxyRequest);

			// Process the response
			int statusCode = proxyResponse.getStatusLine().getStatusCode();

			if (doResponseRedirectOrNotModifiedLogic(this.request, this.response, proxyResponse, statusCode)) {
				// the response is already "committed" now without any body to send
				// TODO copy response headers?
				return;
			}

			// Pass the response code. This method with the "reason phrase" is deprecated
			// but it's the only way to pass the
			// reason along too.
			// noinspection deprecation
			this.response.setStatus(statusCode, proxyResponse.getStatusLine().getReasonPhrase());

			copyResponseHeaders(proxyResponse, this.request, this.response);

			// Send the content to the client
			copyResponseEntity(proxyResponse, this.response);

		} catch (Exception e) {
			// abort request, according to best practice with HttpClient
			if (proxyRequest instanceof AbortableHttpRequest) {
				AbortableHttpRequest abortableHttpRequest = (AbortableHttpRequest) proxyRequest;
				abortableHttpRequest.abort();
			}
			throw new RuntimeException(e);

		} finally {
			// make sure the entire entity was consumed, so the connection is released
			if (proxyResponse != null)
				consumeQuietly(proxyResponse.getEntity());
			// Note: Don't need to close servlet outputStream:
			// http://stackoverflow.com/questions/1159168/should-one-call-close-on-httpservletresponse-getoutputstream-getwriter
			this.destroy();
		}
		this.destroy();
	}

	private void setXForwardedForHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
		String headerName = "X-Forwarded-For";
		if (doForwardIP) {
			String newHeader = servletRequest.getRemoteAddr();
			String existingHeader = servletRequest.getHeader(headerName);
			if (existingHeader != null) {
				newHeader = existingHeader + ", " + newHeader;
			}
			proxyRequest.setHeader(headerName, newHeader);
		}
	}

	/**
	 * Reads the request URI from {@code servletRequest} and rewrites it,
	 * considering targetUri. It's used to make the new request.
	 */
	protected String rewriteUrlFromRequest(HttpServletRequest servletRequest) {
		StringBuilder uri = new StringBuilder(500);
		uri.append(getTargetUri(servletRequest));
		// Handle the path given to the servlet
		String pathInfo = servletRequest.getRequestURI();
		if(servletRequest.getAttribute(this.ATTR_REQUEST_URI)!=null) {
			pathInfo=(String) servletRequest.getAttribute(this.ATTR_REQUEST_URI);
		}
		if (pathInfo != null) {// ex: /my/path.html
			uri.append(encodeUriQuery(pathInfo));
		}
		// Handle the query string & fragment
		String queryString = servletRequest.getQueryString();// ex:(following '?'): name=value&foo=bar#fragment
		String fragment = null;
		// split off fragment from queryString, updating queryString if found
		if (queryString != null) {
			int fragIdx = queryString.indexOf('#');
			if (fragIdx >= 0) {
				fragment = queryString.substring(fragIdx + 1);
				queryString = queryString.substring(0, fragIdx);
			}
		}

		queryString = rewriteQueryStringFromRequest(servletRequest, queryString);
		if (queryString != null && queryString.length() > 0) {
			uri.append('?');
			uri.append(encodeUriQuery(queryString));
		}

		if (doSendUrlFragment && fragment != null) {
			uri.append('#');
			uri.append(encodeUriQuery(fragment));
		}
		return uri.toString();
	}

	private String getTargetUri(HttpServletRequest servletRequest) {
		return (String) servletRequest.getAttribute(ATTR_TARGET_URI);
	}

	private HttpHost getTargetHost(HttpServletRequest servletRequest) {
		return (HttpHost) servletRequest.getAttribute(ATTR_TARGET_HOST);
	}

	/**
	 * Encodes characters in the query or fragment part of the URI.
	 *
	 * <p>
	 * Unfortunately, an incoming URI sometimes has characters disallowed by the
	 * spec. HttpClient insists that the outgoing proxied request has a valid URI
	 * because it uses Java's {@link URI}. To be more forgiving, we must escape the
	 * problematic characters. See the URI class for the spec.
	 *
	 * @param in
	 *            example: name=value&foo=bar#fragment
	 */
	protected static CharSequence encodeUriQuery(CharSequence in) {
		// Note that I can't simply use URI.java to encode because it will escape
		// pre-existing escaped things.
		StringBuilder outBuf = null;
		Formatter formatter = null;
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			boolean escape = true;
			if (c < 128) {
				if (asciiQueryChars.get((int) c)) {
					escape = false;
				}
			} else if (!Character.isISOControl(c) && !Character.isSpaceChar(c)) {// not-ascii
				escape = false;
			}
			if (!escape) {
				if (outBuf != null)
					outBuf.append(c);
			} else {
				// escape
				if (outBuf == null) {
					outBuf = new StringBuilder(in.length() + 5 * 3);
					outBuf.append(in, 0, i);
					formatter = new Formatter(outBuf);
				}
				// leading %, 0 padded, width 2, capital hex
				formatter.format("%%%02X", (int) c);// TODO
			}
		}
		return outBuf != null ? outBuf : in;
	}

	protected static final BitSet asciiQueryChars;
	static {
		char[] c_unreserved = "_-!.~'()*".toCharArray();// plus alphanum
		char[] c_punct = ",;:$&+=".toCharArray();
		char[] c_reserved = "?/[]@".toCharArray();// plus punct

		asciiQueryChars = new BitSet(128);
		for (char c = 'a'; c <= 'z'; c++)
			asciiQueryChars.set((int) c);
		for (char c = 'A'; c <= 'Z'; c++)
			asciiQueryChars.set((int) c);
		for (char c = '0'; c <= '9'; c++)
			asciiQueryChars.set((int) c);
		for (char c : c_unreserved)
			asciiQueryChars.set((int) c);
		for (char c : c_punct)
			asciiQueryChars.set((int) c);
		for (char c : c_reserved)
			asciiQueryChars.set((int) c);

		asciiQueryChars.set((int) '%');// leave existing percent escapes in place
	}
	/**
	 * These are the "hop-by-hop" headers that should not be copied.
	 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html I use an HttpClient
	 * HeaderGroup class instead of Set<String> because this approach does case
	 * insensitive lookup faster.
	 */
	protected static final HeaderGroup hopByHopHeaders;
	static {
		hopByHopHeaders = new HeaderGroup();
		String[] headers = new String[] { "Connection", "Keep-Alive", "Proxy-Authenticate", "Proxy-Authorization", "TE",
				"Trailers", "Transfer-Encoding", "Upgrade" };
		for (String header : headers) {
			hopByHopHeaders.addHeader(new BasicHeader(header, null));
		}
	}

	/** Copy request headers from the servlet client to the proxy request. */
	protected void copyRequestHeaders(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
		// Get an Enumeration of all of the header names sent by the client
		Enumeration enumerationOfHeaderNames = servletRequest.getHeaderNames();
		while (enumerationOfHeaderNames.hasMoreElements()) {
			String headerName = (String) enumerationOfHeaderNames.nextElement();
			// Instead the content-length is effectively set via InputStreamEntity
			if (headerName.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH))
				continue;
			if (hopByHopHeaders.containsHeader(headerName))
				continue;

			Enumeration headers = servletRequest.getHeaders(headerName);
			while (headers.hasMoreElements()) {// sometimes more than one value
				String headerValue = (String) headers.nextElement();
				// In case the proxy host is running multiple virtual servers,
				// rewrite the Host header to ensure that we get content from
				// the correct virtual server
				if (headerName.equalsIgnoreCase(HttpHeaders.HOST)) {
					HttpHost host = getTargetHost(servletRequest);
					headerValue = host.getHostName();
					if (host.getPort() != -1)
						headerValue += ":" + host.getPort();
				} else if (headerName.equalsIgnoreCase(org.apache.http.cookie.SM.COOKIE)) {
					headerValue = getRealCookie(headerValue);
				}
				proxyRequest.addHeader(headerName, headerValue);
			}
		}
	}

	/**
	 * Reads a servlet config parameter by the name {@code hcParamName} of type
	 * {@code type}, and set it in {@code hcParams}.
	 */
//	private void readConfigParam(HttpParams hcParams, String hcParamName, Class type) {
//		String val_str = getConfigParam(hcParamName);
//		if (val_str == null)
//			return;
//		Object val_obj;
//		if (type == String.class) {
//			val_obj = val_str;
//		} else {
//			try {
//				// noinspection unchecked
//				val_obj = type.getMethod("valueOf", String.class).invoke(type, val_str);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//		hcParams.setParameter(hcParamName, val_obj);
//	}

	/**
	 * Reads a configuration parameter. By default it reads servlet init parameters
	 * but it can be overridden.
	 */
	private String getConfigParam(String key) {
		return this.servletConfig.getInitParameter(key);
	}

	/**
	 * Called from {@link #init(javax.servlet.ServletConfig)}. HttpClient offers
	 * many opportunities for customization. By default, <a href=
	 * "http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/client/SystemDefaultHttpClient.html">
	 * SystemDefaultHttpClient</a> is used if available, otherwise it falls back to:
	 * 
	 * <pre>
	 * new DefaultHttpClient(new ThreadSafeClientConnManager(), hcParams)
	 * </pre>
	 * 
	 * SystemDefaultHttpClient uses PoolingClientConnectionManager. In any case, it
	 * should be thread-safe.
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private HttpClient createHttpClient(HttpParams hcParams) {
		try {
			// as of HttpComponents v4.2, this class is better since it uses System
			// Properties:
			Class clientClazz = Class.forName("org.apache.http.impl.client.SystemDefaultHttpClient");
			Constructor constructor = clientClazz.getConstructor(HttpParams.class);
			return (HttpClient) constructor.newInstance(hcParams);
		} catch (ClassNotFoundException e) {
			// no problem; use v4.1 below
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Fallback on using older client:
		return new DefaultHttpClient(new ThreadSafeClientConnManager(), hcParams);
	}

	private boolean doResponseRedirectOrNotModifiedLogic(HttpServletRequest servletRequest,
			HttpServletResponse servletResponse, HttpResponse proxyResponse, int statusCode)
			throws ServletException, IOException {
		// Check if the proxy response is a redirect
		// The following code is adapted from org.tigris.noodle.filters.CheckForRedirect
		if (statusCode >= HttpServletResponse.SC_MULTIPLE_CHOICES /* 300 */
				&& statusCode < HttpServletResponse.SC_NOT_MODIFIED /* 304 */) {
			Header locationHeader = proxyResponse.getLastHeader(HttpHeaders.LOCATION);
			if (locationHeader == null) {
				throw new ServletException("Received status code: " + statusCode + " but no " + HttpHeaders.LOCATION
						+ " header was found in the response");
			}
			// Modify the redirect to go to this proxy servlet rather that the proxied host
			String locStr = rewriteUrlFromResponse(servletRequest, locationHeader.getValue());

			servletResponse.sendRedirect(locStr);
			return true;
		}
		// 304 needs special handling. See:
		// http://www.ics.uci.edu/pub/ietf/http/rfc1945.html#Code304
		// We get a 304 whenever passed an 'If-Modified-Since'
		// header and the data on disk has not changed; server
		// responds w/ a 304 saying I'm not going to send the
		// body because the file has not changed.
		if (statusCode == HttpServletResponse.SC_NOT_MODIFIED) {
			servletResponse.setIntHeader(HttpHeaders.CONTENT_LENGTH, 0);
			servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return true;
		}
		return false;
	}

	/** Copy proxied response headers back to the servlet client. */
	private void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {
		for (Header header : proxyResponse.getAllHeaders()) {
			if (hopByHopHeaders.containsHeader(header.getName()))
				continue;
			if (header.getName().equals(org.apache.http.cookie.SM.SET_COOKIE)
					|| header.getName().equals(org.apache.http.cookie.SM.SET_COOKIE2)) {
				copyProxyCookie(servletRequest, servletResponse, header);
			} else {
				servletResponse.addHeader(header.getName(), header.getValue());
			}
		}
	}

	/**
	 * Copy response body data (the entity) from the proxy to the servlet client.
	 */
	private void copyResponseEntity(HttpResponse proxyResponse, HttpServletResponse servletResponse)
			throws IOException {
		HttpEntity entity = proxyResponse.getEntity();
		if (entity != null) {
			OutputStream servletOutputStream = servletResponse.getOutputStream();
			entity.writeTo(servletOutputStream);
		}
	}

	/**
	 * HttpClient v4.1 doesn't have the
	 * {@link org.apache.http.util.EntityUtils#consumeQuietly(org.apache.http.HttpEntity)}
	 * method.
	 */
	private void consumeQuietly(HttpEntity entity) {
		try {
			EntityUtils.consume(entity);
		} catch (IOException e) {// ignore
			e.printStackTrace();
		}
	}

	private String rewriteQueryStringFromRequest(HttpServletRequest servletRequest, String queryString) {
		return queryString;
	}

	/**
	 * Take any client cookies that were originally from the proxy and prepare them
	 * to send to the proxy. This relies on cookie headers being set correctly
	 * according to RFC 6265 Sec 5.4. This also blocks any local cookies from being
	 * sent to the proxy.
	 */
	protected String getRealCookie(String cookieValue) {
		StringBuilder escapedCookie = new StringBuilder();
		String cookies[] = cookieValue.split("; ");
		for (String cookie : cookies) {
			String cookieSplit[] = cookie.split("=");
			if (cookieSplit.length == 2) {
				String cookieName = cookieSplit[0];
				if (cookieName.startsWith(getCookieNamePrefix())) {
					cookieName = cookieName.substring(getCookieNamePrefix().length());
					if (escapedCookie.length() > 0) {
						escapedCookie.append("; ");
					}
					escapedCookie.append(cookieName).append("=").append(cookieSplit[1]);
				}
			}

			cookieValue = escapedCookie.toString();
		}
		return cookieValue;
	}

	/**
	 * For a redirect response from the target server, this translates
	 * {@code theUrl} to redirect to and translates it to one the original client
	 * can use.
	 */
	protected String rewriteUrlFromResponse(HttpServletRequest servletRequest, String theUrl) {
		// TODO document example paths
		final String targetUri = getTargetUri(servletRequest);
		if (theUrl.startsWith(targetUri)) {
			String curUrl = servletRequest.getRequestURL().toString();// no query
			String pathInfo = servletRequest.getPathInfo();
			if (pathInfo != null) {
				assert curUrl.endsWith(pathInfo);
				curUrl = curUrl.substring(0, curUrl.length() - pathInfo.length());// take pathInfo off
			}
			theUrl = curUrl + theUrl.substring(targetUri.length());
		}
		return theUrl;
	}

	/**
	 * Copy cookie from the proxy to the servlet client. Replaces cookie path to
	 * local path and renames cookie to avoid collisions.
	 */
	protected void copyProxyCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			Header header) {
		List<HttpCookie> cookies = HttpCookie.parse(header.getValue());
		String path = this.servletContext.getServletContextName();
		if (path == null) {
			path = "";
		}
		path += servletRequest.getServletPath();

		for (HttpCookie cookie : cookies) {
			// set cookie name prefixed w/ a proxy value so it won't collide w/ other
			// cookies
			String proxyCookieName = getCookieNamePrefix() + cookie.getName();
			Cookie servletCookie = new Cookie(proxyCookieName, cookie.getValue());
			servletCookie.setComment(cookie.getComment());
			servletCookie.setMaxAge((int) cookie.getMaxAge());
			servletCookie.setPath(path); // set to the path of the proxy servlet
			// don't set cookie domain
			servletCookie.setSecure(cookie.getSecure());
			servletCookie.setVersion(cookie.getVersion());
			servletResponse.addCookie(servletCookie);
		}
	}

	/** The string prefixing rewritten cookies. */
	protected String getCookieNamePrefix() {
		return "!Proxy!proxyServlet" ;//+ this.servletConfig.getServletName();
	}

	public void destroy() {
		// As of HttpComponents v4.3, clients implement closeable
		if (proxyClient instanceof Closeable) {// TODO AutoCloseable in Java 1.6
			try {
				((Closeable) proxyClient).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Older releases require we do this:
			if (proxyClient != null)
				proxyClient.getConnectionManager().shutdown();
		}
	}
}
