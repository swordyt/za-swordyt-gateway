package com.swordyt.MockGateway.dto.request;

import java.util.Map;

import lombok.Data;

@Data
public class RunRuleReq {
	private Integer port;
	private String url;
	private Map<String, Object> header;
	private String requestType;
	private Integer postType;//1 form-data 2 raw
	// post 生效
	private Map<String, String> parameter;
	// post 生效
	private String raw;
}
