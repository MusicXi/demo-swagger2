package hello.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ServletSwaggerPlugin implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        return new ArrayList<ApiDescription>(
                Arrays.asList(
                        new ApiDescription(
                                "/demoServlet/{id}",  //url
                                "测试http接口", //描述
                                Arrays.asList( 
                                        new OperationBuilder(
                                                new CachingOperationNameGenerator())
                                                .method(HttpMethod.POST)
                                                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                                                .summary("DemoServlet Test")
                                                .notes("测试调用DemoServlet接口")
                                                .tags(Sets.newHashSet("demo-servlet"))
                                                .parameters(
                                                        Arrays.asList( 
                                                                new ParameterBuilder()
                                                                        .description("id参数")
                                                                        .type(new TypeResolver().resolve(String.class))
                                                                        .name("id")
                                                                        .defaultValue("10086")
                                                                        .parameterType("path")
                                                                        .parameterAccess("access")
                                                                        .required(true)
                                                                        .modelRef(new ModelRef("string"))
                                                                        .build(),
                                                                new ParameterBuilder()
                                                                        .description("请求参数")
                                                                        .type(new TypeResolver().resolve(Object.class))
                                                                        .name("postRequest")
                                                                        .parameterType("body")
                                                                        .parameterAccess("access")
                                                                        .required(true)
                                                                        .modelRef(new ModelRef("object"))
                                                                        .build()
                                                        ))
                                                .build()),
                                false)));
    }
 
    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}