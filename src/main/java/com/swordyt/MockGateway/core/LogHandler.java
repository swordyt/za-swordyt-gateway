package com.swordyt.MockGateway.core;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Data;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月7日 下午5:40:37
 */
@Data
public class LogHandler {
	private String url;
	private Boolean isSuccess;
	private String message;

	public void result(Boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
	}

	public LogHandler(String rule, String url) {
		this.rule = rule;
		this.url = url;
	}

	@Override
	public String toString() {
		String stepResult = "";
		for (int num = 0; num < stepStack.size(); num++) {
			stepResult += stepStack.get(num).toString() + "\r\n";
		}
		return "Url:" + this.url + ",Rule:" + this.rule + "\r\n" + "Result:\r\n" + stepResult;
	}

	private String rule;
	List<Step> stepStack = Lists.newArrayList();

	public void addStep(String operation, String input, String actual, Boolean result) {
		System.out.println(".addStep(" + operation + "," + input + ", " + actual + "," + result + ")");
		stepStack.add(new Step(operation, result, input, actual));
	}

	@Data
	private class Step {
		private String operation;// 预算操作
		private Boolean result;// 比较结果
		private String inputValue;// 输入值
		private String actualValue;// 实际值

		public Step(String operation, Boolean result, String inputValue, String actualValue) {
			this.operation = operation;
			this.result = result;
			this.inputValue = inputValue;
			this.actualValue = actualValue;
		}

		public String getOperation() {
			return operation;
		}

		public Boolean getResult() {
			return result;
		}

		public String getInputValue() {
			return inputValue;
		}

		public String getActualValue() {
			return actualValue;
		}

		@Override
		public String toString() {
			return "Step [operation=" + operation + ", result=" + result + ", inputValue=" + inputValue
					+ ", actualValue=" + actualValue + "]";
		}

	}
}
