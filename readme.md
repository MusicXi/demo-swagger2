# 说明
### 启动
运行: hello.Application 

### 快速尝试
1. 模拟登入(模拟登入后才能记录日志) 
```
http://localhost:8080/login?name=User
```
2. 访问接口在线文档(接口信息,可在页面直接请求测试接口,后台将自动记录操作日志可保存入库)
格式:http://host:port/xxx/swagger-ui.html 
```
http://localhost:8080/swagger-ui.html
```
3. 接口的swagger描述信息json格式,用于导入mock系统
http://{服务启动主机地址}:{服务启动端口}/v2/api-docs?group={需要生成的api所属组name}
```
http://localhost:8080/v2/api-docs?group=default
```
4. 支持多种格式接口文档下载,用于项目管理及前后端联调使用
```
http://localhost:8080/swagger/markdown
http://localhost:8080/swagger/confluence
http://localhost:8080/swagger/asciidoc
http://localhost:8080/swagger/json
http://localhost:8080/swagger/html
```

### 相关文档
1. [springboot/springmvc实现swagger2/及国际化](https://github.com/MusicXi/note/tree/master/java/third_lib/swagger2)














