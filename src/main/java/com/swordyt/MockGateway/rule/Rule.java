package com.swordyt.MockGateway.rule;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午4:27:44
 */
public interface Rule {
	public Body body();

	public Header header(String header);

	public Type type();

	public Parameter parameter(String key);
	
	public Url url();
}
