package com.swordyt.MockGateway.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.testng.Assert;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.swordyt.MockGateway.core.LogHandler;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月10日 下午2:10:49
 */
public class ArithmeticOperationSymbolImpl implements ArithmeticOperationSymbol<ConcatenationOperationSymbol<Rule>> {
	protected Rule rule;
	protected LogHandler log;
	protected OperationUtensil operation;
	protected String value;
	protected HttpServletRequest request;
	protected ConcatenationOperationSymbol concatenationOperationSymbol;

	protected ArithmeticOperationSymbolImpl(String message, String value, OperationUtensil operation,
			HttpServletRequest request, Rule rule, LogHandler log) {
		this.rule = rule;
		this.log = log;
		this.operation = operation;
		this.value = value;
		this.request = request;
		this.log.addStep(message, this.value + "", null, null);
		this.concatenationOperationSymbol = new ConcatenationOperationSymbol<Rule>(this.operation, this.rule, this.log);
	}

	@Override
	public ConcatenationOperationSymbol<Rule> equalTo(Integer value) {
		String message = ".equalTo(" + value + ")";
		try {
			Assert.assertEquals(this.value == null ? null : Integer.parseInt(this.value), value);
			this.log.addStep(message, value + "", this.value, this.operation.isTrue());
			return this.concatenationOperationSymbol;
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, value + "", this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> notEqualTo(Integer value) {
		String message = ".notEqualTo(" + value + ")";
		try {
			Assert.assertNotEquals(value, this.value == null ? null : Integer.parseInt(this.value));
			this.log.addStep(message, value + "", this.value, this.operation.isTrue());
			return this.concatenationOperationSymbol;
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, value + "", this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> greaterThan(Integer value) {
		String message = ".greaterThan(" + value + ")";
		try {
			if (Integer.parseInt(this.value) > value) {
				this.log.addStep(message, value + "", this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, value + "", this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> greaterThanOrEqualTo(Integer value) {
		String message = ".greaterThanOrEqualTo(" + value + ")";
		try {
			if (Integer.parseInt(this.value) >= value) {
				this.log.addStep(message, value + "", this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, value + "", this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> lessThan(Integer value) {
		String message = ".lessThan(" + value + ")";
		try {
			if (Integer.parseInt(this.value) < value) {
				this.log.addStep(message, value + "", this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, value + "", this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> lessThanOrEqualTo(Integer value) {
		String message = ".lessThanOrEqualTo(" + value + ")";
		try {
			if (Integer.parseInt(this.value) <= value) {
				this.log.addStep(message, value + "", this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, value + "", this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	/**
	 * 包含value1,value2
	 */
	@Override
	public ConcatenationOperationSymbol<Rule> between(Integer value1, Integer value2) {
		String message = ".between(" + value1 +","+value2+ ")";
		try {
			if (Integer.parseInt(this.value) <= value2 && Integer.parseInt(this.value) >= value1) {
				this.log.addStep(message, "value1:" + value1 + ",value2:" + value2, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, "value1:" + value1 + ",value2:" + value2, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	/**
	 * value2<value<value1
	 */
	@Override
	public ConcatenationOperationSymbol<Rule> notBetween(Integer value1, Integer value2) {
		String message = ".notBetween(" + value1 +","+value2+ ")";
		try {
			if (Integer.parseInt(this.value) < value1 || Integer.parseInt(this.value) > value2) {
				this.log.addStep(message, "value1:" + value1 + ",value2:" + value2, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, "value1:" + value1 + ",value2:" + value2, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> isNull() {
		String message = ".isNull()";
		try {
			if (this.value==null) {
				this.log.addStep(message, null, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, null, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> isNotNull() {
		String message = ".isNotNull()";
		try {
			if (this.value != null) {
				this.log.addStep(message, null, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, null, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> equalTo(String value) {
		String message = ".equalTo(\""+value+"\")";
		try {
				Assert.assertEquals(this.value, value);
				this.log.addStep(message, value, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, value, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> notEqualTo(String value) {
		String message = ".notEqualTo(\""+value+"\")";
		try {
				Assert.assertNotEquals(this.value, value);
				this.log.addStep(message, value, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
		} catch (Throwable e) {
			// e.printStackTrace();
		}
		this.log.addStep(message, value, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

//	@Override
//	public ConcatenationOperationSymbol<Rule> greaterThan(String value) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ConcatenationOperationSymbol<Rule> greaterThanOrEqualTo(String value) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public ConcatenationOperationSymbol<Rule> lessThan(String value) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ConcatenationOperationSymbol<Rule> lessThanOrEqualTo(String value) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	/**
	 * 同sql %key%
	 */
	@Override
	public ConcatenationOperationSymbol<Rule> like(String value) {
		String message = ".like(\"" + value + "\")";
		try {
			if (value == null || (!value.startsWith("%") && !value.endsWith("%"))) {
				Assert.assertEquals(this.value, value);
				this.log.addStep(message, value + "", this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			} else if (value.startsWith("%") && value.endsWith("%")) {
				if (this.value.contains(value.substring(1, value.length() - 1))) {
					this.log.addStep(message, value + "", this.value, this.operation.isTrue());
					return this.concatenationOperationSymbol;
				}
			} else if (value.startsWith("%")) {
				if (this.value.endsWith((value.substring(1, value.length())))) {
					this.log.addStep(message, value + "", this.value, this.operation.isTrue());
					return this.concatenationOperationSymbol;
				}
			} else if (value.endsWith("%")) {
				if (this.value.startsWith((value.substring(0, value.length() - 1)))) {
					this.log.addStep(message, value + "", this.value, this.operation.isTrue());
					return this.concatenationOperationSymbol;
				}
			}
		} catch (Throwable e) {
		}
		this.log.addStep(message, value + "", this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	@Override
	public ConcatenationOperationSymbol<Rule> notLike(String value) {
		String message = ".notLike(\"" + value + "\")";
		try {
			if (value == null || (!value.startsWith("%") && !value.endsWith("%"))) {
				Assert.assertNotEquals(this.value, value);
				this.log.addStep(message, value, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			} else if (value.startsWith("%") && value.endsWith("%")) {
				if (!this.value.contains(value.substring(1, value.length() - 1))) {
					this.log.addStep(message, value, this.value, this.operation.isTrue());
					return this.concatenationOperationSymbol;
				}
			} else if (value.startsWith("%")) {
				if (!this.value.endsWith((value.substring(1, value.length())))) {
					this.log.addStep(message, value, this.value, this.operation.isTrue());
					return this.concatenationOperationSymbol;
				}
			} else if (value.endsWith("%")) {
				if (!this.value.startsWith((value.substring(0, value.length() - 1)))) {
					this.log.addStep(message, value, this.value, this.operation.isTrue());
					return this.concatenationOperationSymbol;
				}
			}
		} catch (Throwable e) {
		}
		this.log.addStep(message, value, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	/**
	 * ["1","2","3"]
	 */
	@Override
	public ConcatenationOperationSymbol<Rule> in(String value) {
		String message = ".in(" + value + ")";
		try {
			List<String> list=JSON.parseArray(value, String.class);
			if (list.contains(this.value)) {
				this.log.addStep(message, value, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
		}
		this.log.addStep(message, value, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	/**
	 * ["1","2","3"]
	 */
	@Override
	public ConcatenationOperationSymbol<Rule> notIn(String listJson) {
		String message = ".in(" + value + ")";
		try {
			List<String> list = JSON.parseArray(value, String.class);
			if (!list.contains(this.value)) {
				this.log.addStep(message, value, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
		}
		this.log.addStep(message, value, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

	/**
	 * 执行jsonpath后非空即为true，blank为false
	 */
	@Override
	public ConcatenationOperationSymbol<Rule> jsonPath(String jsonPath) {
		String message = ".jsonPath(\"" + jsonPath + "\")";
		try {
			List<Object> resutl=JsonPath.read(this.value, jsonPath);
			if (resutl!=null&&!resutl.isEmpty()) {
				this.log.addStep(message, jsonPath, this.value, this.operation.isTrue());
				return this.concatenationOperationSymbol;
			}
		} catch (Throwable e) {
		}
		this.log.addStep(message, jsonPath, this.value, this.operation.isFalse());
		return this.concatenationOperationSymbol;
	}

//	@Override
//	public ConcatenationOperationSymbol<Rule> between(String value1, String value2) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ConcatenationOperationSymbol<Rule> notBetween(String value1, String value2) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
