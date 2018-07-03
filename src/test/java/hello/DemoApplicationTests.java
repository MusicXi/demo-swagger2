package hello;

import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

import org.asciidoctor.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 使用单元测试生成执行生成swagger2 adoc文档的代码
 * 
 * @author linrx1
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DemoApplicationTests {

	/**
	 * <ol>
	 * <li>MarkupLanguage.ASCIIDOC：指定了要输出的最终格式。除了ASCIIDOC之外，还有MARKDOWN和CONFLUENCE_MARKUP</li>
	 * <li>from(new URL("http://localhost:8080/v2/api-docs")：指定了生成静态部署文档的源头配置，可以是这样的URL形式，也可以是符合Swagger规范的String类型或者从文件中读取的流。如果是对当前使用的Swagger项目，我们通过使用访问本地Swagger接口的方式，如果是从外部获取的Swagger文档配置文件，就可以通过字符串或读文件的方式</li>
	 * <li>toFolder(Paths.get("src/docs/asciidoc/generated")：指定最终生成文件的具体目录位置</li>
	 * <ol/>
	 * @throws Exception
	 */
    @Test
    public void generateAsciiDocs() throws Exception {
        // 输出Ascii格式
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .build();

        Swagger2MarkupConverter.from(new URL("http://localhost:8080/v2/api-docs"))
                .withConfig(config)
                .build()
                //.toFolder(Paths.get("src/docs/asciidoc/generated"));
		        .toFile(Paths.get("src/docs/asciidoc/generated/all"));
        //如果不想分割结果文件，也可以通过替换toFolder(Paths.get("src/docs/asciidoc/generated")
        //为toFile(Paths.get("src/docs/asciidoc/generated/all"))
        
    }
    
    /**
     * 生成markdown格式
     * @throws MalformedURLException
     */
    @Test
	public void generateMarkdown() throws MalformedURLException {
		Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
				.withMarkupLanguage(MarkupLanguage.MARKDOWN).build();

		Swagger2MarkupConverter
				.from(new URL("http://localhost:8080/v2/api-docs"))
				.withConfig(config).build()
				//.toFolder(Paths.get("src/docs/markdown/generated"));
				.toFile(Paths.get("src/docs/markdown/generated/all"));
	}
    
	/**
	 * 生成confluence格式
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void generateConfluence() throws MalformedURLException {
		Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
				.withMarkupLanguage(MarkupLanguage.CONFLUENCE_MARKUP).build();

		Swagger2MarkupConverter
				.from(new URL("http://localhost:8080/v2/api-docs"))
				.withConfig(config).build()
				//.toFolder(Paths.get("src/docs/confluence/generated"));
				.toFile(Paths.get("src/docs/confluence/generated/all"));
	}



}