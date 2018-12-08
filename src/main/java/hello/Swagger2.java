package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
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
        //添加head参数end


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalOperationParameters(pars)
                .select()
                .apis(RequestHandlerSelectors.basePackage("hello"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("MR.xxx","http://xxx.xxx.com/","xxxx@qq.com");
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs 并使用aop记录对于操作日志")
                .description("更多Spring Boot相关文章请关注：http://xxx.xxx.com/")
                .termsOfServiceUrl("http://xxx.xxx.com/")
                .contact(contact)
                .version("1.0")
                .build();
    }

}

