package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.swordyt.MockGateway.core.LogHandler;
import com.swordyt.MockGateway.core.RulePool;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月7日 下午6:54:22
 */
public class RuleEngine {
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private String url;
	private ApplicationContext applicationContext;
	private List<LogHandler> logs;

	public RuleEngine(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			ApplicationContext applicationContext, List<LogHandler> logs) {
		this.servletRequest = servletRequest;
		this.servletResponse = servletResponse;
		this.url = servletRequest.getRequestURI();
		this.applicationContext = applicationContext;
		this.logs = logs;
	}

	public String start() {
		System.out.println("规则开始匹配：" + this.servletRequest.getRequestURI());
		// 规则查询
		List<String> rules = RulePool.getRule(this.servletRequest.getServerPort(), this.servletRequest.getRequestURI());
		if (rules == null || rules.isEmpty()) {
			System.out.println("无匹配规则");
			return null;
		}
		// 规则执行

		// 无规则匹配成功
		String domain = null;
		for (String rule : rules) {
			LogHandler log = new LogHandler(rule, this.servletRequest.getRequestURI());
			this.logs.add(log);// 记录所有规则日志
			OperationUtensil operation = new OperationUtensil();
			if (rule == null || !rule.startsWith("request.")) {
				String message = "规则错误，未以request.开头：" + rule + ",跳过";
				System.out.println(message);
				log.result(false, message);
			}
			try {
				domain = this.runRule(rule, log, operation);
				if (StringUtils.isNotBlank(domain)) {
					log.result(true, null);
					break;
				}
				log.result(false, null);
			} catch (Exception e) {
				String message = "规则异常:" + JSON.toJSONString(e.getStackTrace()) + ",规则：" + rule + ",跳过";
				System.out.println(message);
				log.result(false, message);
			}
		}
		return domain;
	}

	private String runRule(String ruleScript, LogHandler log, OperationUtensil operation) throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		// 每次生成一个engine实例
		ScriptEngine engine = factory.getEngineByName("groovy");
		System.out.println(engine.toString());
		assert engine != null;
		// javax.script.Bindings
		Bindings binding = engine.createBindings();
		binding.put("request", new Request(servletRequest, log, operation));
		// 如果script文本来自文件,请首先获取文件内容
		engine.eval("	  def exeRule(){\r\n" + "	  try {\r\n" + "			return " + ruleScript + ";\r\n"
				+ "	  } catch(Exception ex) {\r\n" + "	ex.printStackTrace();\r\n" + "        return null;\r\n"
				+ "      }}", binding);
		Object result = ((Invocable) engine).invokeFunction("exeRule", null);
		System.out.println("执行结果日志：" + log.toString());
		return result instanceof String ? result.toString() : null;
	}

}
