package com.swordyt.MockGateway.rule;

import java.util.Date;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/** 
* @author 作者 swordyt@163.com: 
* @version 创建时间：2019年5月7日 下午3:33:39 
*/
public class Test {

//	public static void main(String[] args) throws Exception {
//		ScriptEngineManager factory = new ScriptEngineManager();  
//		  //每次生成一个engine实例  
//		  ScriptEngine engine = factory.getEngineByName("groovy");  
//		  System.out.println(engine.toString());  
//		  assert engine != null;
//		  //javax.script.Bindings  
//		  Bindings binding = engine.createBindings();  
//		  String ruleScript="request.header(\"headerKey\").like(\"likeKey\").and().parameter(\"parameterKey\").in(null).forward(\"https://domain\")";
//		  LogHandler log=new LogHandler(ruleScript);
//		  binding.put("request", new Request(null,log));  
//		  //如果script文本来自文件,请首先获取文件内容  
//		  engine.eval("	  def exeRule(){\r\n" + 
//		  		"	  try {\r\n" + 
//		  		"			return "+ruleScript+";\r\n" + 
//		  		"	  } catch(Exception ex) {\r\n" + 
//		  		"	ex.printStackTrace();\r\n"+
//		  		"        return null;\r\n" + 
//		  		"      }}",binding);   
//		  String domain = (String)((Invocable)engine).invokeFunction("exeRule", null);  
//		  System.out.println(domain);
//	}
	public void  tet() {
		Rule request=new Request(null, null, null);
//		request.url().greaterThan("");
	}
}
