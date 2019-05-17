# 1、根据自定义规则如：
request.url().like("/test%").replaceFirst("/test","/321").forward("http://www.dsf.cn")  
将接受到的请求转发到http://www.dsf.cn  
request.url().like("/test%").replaceFirst("/test","/321")将请求中包含的"/test"替换为"/321"  
最后请求地址为：http://www.dsf.cn/321  
# 2、提供的接口
### post /e10adc3949ba59abbe56e057f20f883e/rule/save
	private Integer port;
	private String url;
	private List<String> rule;
动态添加规则

### get /e10adc3949ba59abbe56e057f20f883e/rule/query
	Integer port;
	String url;
动态查询规则

### post /e10adc3949ba59abbe56e057f20f883e/rule/sort
	private Integer port;
	private String url;
	private List<String> rule;
对当前规则进行排序

# 3、试运行规则匹配
在需要测试的接口header中增加
e10adc3949ba59abbe56e057f20f883e=true
Response中将展示规则匹配情况

# 4、规则描述
+ 第一个必须”request.“开头，request表示请求
+ 规则格式：request.[操作对象].[运算操作].[关系操作||动作]  
当后面操作为[关系操作]时可继续书写[操作对象]
+ or()/and() 左侧运算会被放如（）中如  

	url=/test/test1

	request.url().like("/test%").or().url().like("/test1%").and().url().like("/test2%").replaceFirst("/test","/321")

	生成的表达式为
	
	((true)||true)&&false
+ 操作对象
 
	body();  
	
	header(String header); 
	 
	Type type();  
	
	parameter(String key); 
	 
	url();  
+ body()二级操作对象    

	formdataBody(String key)

	xWwwFormUrlencodedBody(String key)

	rawBody()

	binaryBody(String key)
+ 运算操作  

	 equalTo(Integer value);

	 notEqualTo(Integer value);

	 greaterThan(Integer value);

	 greaterThanOrEqualTo(Integer value) ;

	 lessThan(Integer value);

	 lessThanOrEqualTo(Integer value);

	 between(Integer value1, Integer value2);

	 notBetween(Integer value1, Integer value2);

	 isNull();

	 isNotNull();

	 equalTo(String value);

	 notEqualTo(String value);

	 like(String value);

	 notLike(String value);

	 in(String listJson);

	 notIn(String listJson);

	 /**
	  * 执行jsonpath返回非null即通过
	  * */
	 jsonPath(String jsonPath);
+ 关系操作

	or()

	and()

	replaceFirst(String regex, String replacement)//url类型独享
	
+ 动作  
 
	forward(String domain)//规则终止