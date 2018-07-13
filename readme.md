# 说明
### 技术说明
- springboot 项目结构
- swagger2 在线接口文档/支持markdown/confluence/html/json 格式接口文档下载
- 通过aop方式整合swagger2接口注解, 使用swagger2定义的接口自动记录操作日志
### 项目中使用 
```java
@Api(value = "/users", tags = "用户操作接口")
@RestController
@RequestMapping(value="/users")     
public class UserController {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    @ApiOperation(value="获取用户列表", notes="")
    @RequestMapping(value={""}, method=RequestMethod.GET)
    public List<User> getUserList() {
        List<User> r = new ArrayList<User>(users.values());
        return r;
    }

    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @RequestMapping(value="", method=RequestMethod.POST)
    public String postUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return "success";
    }
}
```
### 启动
运行: hello.Application 

### 快速尝试
1. 模拟登入(模拟登入后才能记录日志) 
```
http://localhost:8080/login?name=User
```
2. 访问接口在线文档(接口信息,可在页面直接请求测试接口,后台将自动记录操作日志可保存入库)
> 格式:http://host:port/xxx/swagger-ui.html 
```
http://localhost:8080/swagger-ui.html
```
模拟登入后,选择不同接口测试，输入请求参数，点击"Try it out"，后台模拟入库对应操作日志。

3. 接口的swagger描述信息json格式,用于导入mock系统
> http://{主机地址}:{服务启动端口}/xxx/v2/api-docs?group={需要生成的api所属组name}
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














