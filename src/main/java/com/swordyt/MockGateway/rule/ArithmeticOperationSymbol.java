package com.swordyt.MockGateway.rule;

import java.util.List;

/**
 * @author 作者swordyt@163.com:
 * @version 创建时间：2019年5月6日 下午5:59:52
 */
public interface ArithmeticOperationSymbol<T> {
	 public T equalTo(Integer value);

	 public T notEqualTo(Integer value);

	 public T greaterThan(Integer value);

	 public T greaterThanOrEqualTo(Integer value) ;

	 public T lessThan(Integer value);

	 public T lessThanOrEqualTo(Integer value);

	 public T between(Integer value1, Integer value2);

	 public T notBetween(Integer value1, Integer value2);

	 public T isNull();

	 public T isNotNull();

	 public T equalTo(String value);

	 public T notEqualTo(String value);

//	 public T greaterThan(String value) ;
//
//	 public T greaterThanOrEqualTo(String value);
//
//	 public T lessThan(String value);
//
//	 public T lessThanOrEqualTo(String value);

	 public T like(String value);

	 public T notLike(String value);

	 public T in(String listJson);

	 public T notIn(String listJson);

//	 public T between(String value1, String value2);
//
//	 public T notBetween(String value1, String value2);
	 /**
	  * 执行jsonpath返回非null即通过
	  * */
	 public T jsonPath(String jsonPath);
}
