package com.swordyt.MockGateway.rule;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author 作者 swordyt@163.com:
 * @version 创建时间：2019年5月8日 上午10:40:51
 */
public class OperationUtensil {
	private String operation="";

	public void or() {
//		System.out.println("OR:"+"("+this.operation+") || ");
		this.operation="("+this.operation+") || ";
	}
	public void and() {
//		System.out.println("AND:"+"("+this.operation+") && ");
		this.operation="("+this.operation+") && ";
	}

	public boolean isTrue() {
//		System.out.println("true");
		this.operation += " true ";
		return true;
	}

	public boolean isFalse() {
		System.out.println("false");
		this.operation+=" false ";
		return false;
	}

	public Boolean operation() throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		// 每次生成一个engine实例
		ScriptEngine engine = factory.getEngineByName("groovy");
		System.out.println(engine.toString());
		assert engine != null;
		Bindings binding = engine.createBindings();
		System.out.println("Operation:" + this.operation);
		try {
			engine.eval("def exeOperation(){\r\n" + "	  try {\r\n" + "			return " + this.operation + ";\r\n"
					+ "	  } catch(Exception ex) {\r\n" + "	ex.printStackTrace();\r\n" + "        return null;\r\n"
					+ "      }}");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Boolean) ((Invocable) engine).invokeFunction("exeOperation", null);
	}

	/**
	 * 运算清除操作
	 */
	public void clearOperation() {
//		System.out.println("clearOperation:{"+this.operation+"}");
		this.operation="";
	}
}
