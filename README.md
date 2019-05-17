#1、根据自定义规则如：request.url().like("/test%").replaceFirst("/test","/321").forward("http://www.dsf.cn")
将接受到的请求转发到http://www.dsf.cn
request.url().like("/test%").replaceFirst("/test","/321")将请求中包含的"/test"替换为"/321"
最后请求地址为：http://www.dsf.cn/321
#2、提供的接口
###post /e10adc3949ba59abbe56e057f20f883e/rule/save
	private Integer port;
	private String url;
	private List<String> rule;
动态添加规则

###get /e10adc3949ba59abbe56e057f20f883e/rule/query
	Integer port;
	String url;
动态查询规则

###post /e10adc3949ba59abbe56e057f20f883e/rule/sort
	private Integer port;
	private String url;
	private List<String> rule;
对当前规则进行排序

#3、试运行规则匹配
在需要测试的接口header中增加
e10adc3949ba59abbe56e057f20f883e=true
Response中将展示规则匹配情况