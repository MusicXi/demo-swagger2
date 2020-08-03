package hello.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Swagger2Config.class);
    @Autowired
    private  ServletContext servletContext;

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("业务接口")
                .apiInfo(apiInfo())
                //.useDefaultResponseMessages(false)
                // swagger-ui.html界面发起请求添加前缀
                .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        String basePath = super.getApplicationBasePath();
//                        String basePath ="/customPath";
                        LOGGER.info("base path:[{}]", basePath);
                        return basePath;
                    }
                })
                .select()
                .apis(RequestHandlerSelectors.basePackage("hello"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createTestRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("测试接口")
                .apiInfo(apiInfo())
                .globalOperationParameters(this.buildHeaderParam())
                //.useDefaultResponseMessages(false)
                // swagger-ui.html界面发起请求添加前缀
                .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        String basePath = super.getApplicationBasePath();
//                        String basePath ="/customPath";
                        LOGGER.info("base path:[{}]", basePath);
                        return basePath;
                    }
                })
                .select()
                .apis(RequestHandlerSelectors.basePackage("hello"))
                .paths(PathSelectors.regex(".*/test/.*"))
                .build();
    }


    private ApiInfo apiInfo() {
        Contact contact = new Contact("Myron", "http://xxx.xxx.com/", "xxxx@qq.com");
        StringBuilder description = new StringBuilder();
        description.append("查看json格式:<a target='_blank' href='./v2/api-docs'>api-docs</a> <br>")
                .append("接口文档下载:<a href='./swagger/markdown'>markdown</a>  ")
                .append("<a href='./swagger/confluence'>confluence</a>  ")
                .append("<a href='./swagger/html'>html</a> ")
                .append("<a href='./swagger/json'>json</a> ");
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs 并使用aop记录对于操作日志")
                .description(description.toString())
//                .termsOfServiceUrl("http://xxx.xxx.com/")
                .contact(contact)
                .version("1.0")
                .build();
    }

    private List<Parameter> buildHeaderParam() {
        //添加head参数start
        Parameter tokenParam = new ParameterBuilder()
                .name("x-access-token")
                .description("令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        //添加head参数start
        Parameter languageParam = new ParameterBuilder()
                .name("Accept-Language")
                .description("国际化,例:zh_CN,en")
                .defaultValue(Locale.CHINA.toLanguageTag())
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        List<Parameter> pars = new ArrayList<>();
        pars.add(tokenParam);
        pars.add(languageParam);
        return pars;
    }


}