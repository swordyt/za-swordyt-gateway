package com.swordyt.MockGateway.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年4月30日 上午11:04:44
 */
//@slf4j
public interface InterfaceMapper {
	/**
	 * 第一步请求解析
	 */
	public default Object firstParseRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/********************** 获取请求的XML *********************************/
		InputStream reqStream = null;
		ByteArrayOutputStream ms = null;
		reqStream = request.getInputStream();
		ms = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int count;
		while ((count = reqStream.read(buf, 0, buf.length)) > 0) {
			ms.write(buf, 0, count);
		}
		reqStream.close();
		String reqText = new String(ms.toByteArray(), "GBK");
		System.out.println("收到的请求：\n" + reqText);
		return reqText;
	};

	/**
	 * 第二步解密验签
	 */
	public default Object secondDecode(HttpServletRequest request, HttpServletResponse response, Object reqText) {
		return reqText;
	}

	/**
	 * 第三步路由规则,返回true：mock,false:gateway
	 */
	public default Boolean thirdRuleMatching(HttpServletRequest request, HttpServletResponse response, Object reqText) {
		return true;
	}

	/**
	 * 第四步发送MounteBank
	 */
	public Object fourthRequestMounteBank(HttpServletRequest request, HttpServletResponse response, Object reqText);

	/**
	 * 第五步加密
	 * 
	 * @throws Exception
	 */
	public default void fifthEncode(HttpServletRequest request, HttpServletResponse response, Object reqText)
			throws Exception {
		byte[] postData = reqText.toString().getBytes("GBK");
		ServletOutputStream resStream = response.getOutputStream();
		resStream.write(postData);
		resStream.close();
	}

	/**
	 * 获取网关接口地址,该方法会在this对象实例化前调用，因此@Value将失效，获取application.properties中配置，请使用this.getEnv(key)方法。
	 */
	public String getGatewayUrl();
}
