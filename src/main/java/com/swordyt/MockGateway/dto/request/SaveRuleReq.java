package com.swordyt.MockGateway.dto.request;

import java.util.List;

public class SaveRuleReq {
	private Integer port;
	private String url;
	private List<String> rule;

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getRule() {
		return rule;
	}

	public void setRule(List<String> rule) {
		this.rule = rule;
	}
	

}
