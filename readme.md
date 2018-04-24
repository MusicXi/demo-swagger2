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

5. 待实现功能
- 支持pdf导出
- 下载离线文档
- 命令maven确认

6. 参考文档
- Spring Boot中使用Swagger2构建强大的RESTful API文档  https://www.jianshu.com/p/8033ef83a8ed 
- 翟永超 《Spring Cloud微服务实战》作者 Spring4All社区联合发起人 
- 作者博客(重点关注) http://blog.didispace.com/springbootswagger2/
- 《使用Swagger2Markup实现API文档的静态部署（一）》 https://blog.csdn.net/qq_34368762/article/details/79129303
- 《使用Swagger2Markup实现API文档的静态部署（二）：Markdown和Confluence》 https://blog.csdn.net/qq_35873847/article/details/79191971
























