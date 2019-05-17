根据自定义规则如：request.url().like("/test%").replaceFirst("/test","/321").forward("http://www.dsf.cn")
将接受到的请求转发到http://www.dsf.cn
request.url().like("/test%").replaceFirst("/test","/321")将请求中包含的"/test"替换为"/321"
最后请求地址为：http://www.dsf.cn/321
