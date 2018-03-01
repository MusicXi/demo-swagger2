# springboot实现swagger2接口文档
- 快速尝试:http://localhost:8080/greeting?name=User
- 接口文档:http://localhost:8080/swagger-ui.html 
- 格式:http://host:port/xxx/swagger-ui.html (生成的接口都是默认的)
1. 构建基础springboot restful项目 
2. 引入依赖swagger2及swagger2 UI
```
		<!-- swagger2 -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>${springfox-swagger2.version}</version>
			<scope>compile</scope>
		</dependency>

		<!-- swagger2 UI -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>${springfox-swagger2.version}</version>
			<scope>compile</scope>
		</dependency>
		
```
3. 构建配置对象
```
package hello;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("hello"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                .description("更多Spring Boot相关文章请关注：http://xxx.com/")
                .termsOfServiceUrl("http://blog.didispace.com/")
                .contact("程序猿DD")
                .version("1.0")
                .build();
    }

}


```
4. 定义需要发布文档接口
```
package hello;

@Api(value = "/", tags = "swagger2 Demo接口")
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @ApiOperation(value="遇见用户", notes="根据用户打招呼")
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
```

6.国际化实现
国际化文件位于springfox-swagger-ui-2.7.0.jar/META-INF/resources/webjars/springfox-swagger-ui/lang目录中
翻译支持：
swagger的index.html(swagger-ui.html) 中添加
···
	<!--国际化操作：选择中文版 -->
    <script src='webjars/springfox-swagger-ui/lang/translator.js' type='text/javascript'></script>
    <script src='webjars/springfox-swagger-ui/lang/zh-cn.js' type='text/javascript'></script>

···
- 添加 src/main/resources/META-INF/resources/swagger-ui.html
- 复制swaager-ui jar包中  /META-INF/resources/swagger-ui.html的内容
- 在新建的swagger-ui.html补充插入以下js,访问http://localhost:8080/swagger-ui.html 就是开启中文的UI界面
···
	<!--国际化操作：选择中文版 -->
    <script src='webjars/springfox-swagger-ui/lang/translator.js' type='text/javascript'></script>
    <script src='webjars/springfox-swagger-ui/lang/zh-cn.js' type='text/javascript'></script>

···

5. 参考文档
- Spring Boot中使用Swagger2构建强大的RESTful API文档  https://www.jianshu.com/p/8033ef83a8ed 
- 翟永超 《Spring Cloud微服务实战》作者 Spring4All社区联合发起人 
- 作者博客(重点关注) http://blog.didispace.com/springbootswagger2/


- SpringBoot 使用Swagger2打造在线接口文档（附汉化教程）https://www.jianshu.com/p/7e543f0f0bd8





















