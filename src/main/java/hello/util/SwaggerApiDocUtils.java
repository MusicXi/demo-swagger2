package hello.util;

import hello.constants.ApiDocTypeEnum;
import hello.constants.SwaggerConstants;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.asciidoctor.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by linrx1 on 2018/6/27.
 * @author linrx
 */
public class SwaggerApiDocUtils {

    /**
     * 为web接口提供下载swagger2 接口文档
     * @param apiDocTypeEnum 文档类型枚举
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static ResponseEntity<InputStreamResource> downloadApiDoc(ApiDocTypeEnum apiDocTypeEnum) throws MalformedURLException,IOException{
        generate(apiDocTypeEnum);
        Path path = Paths.get(apiDocTypeEnum.getDocFile());
        FileSystemResource file = new FileSystemResource(path.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));

    }

    /**
     * 下载html格式的swagger2静态接口文档
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static ResponseEntity<InputStreamResource> downloadHtmlDoc() throws MalformedURLException,IOException{
        Path path = Paths.get(generateHtml());
        FileSystemResource file = new FileSystemResource(path.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));

    }

    /**
     * 以json方式下载swagger2接口描述信息
     * @return
     * @throws Exception
     */
    public static ResponseEntity<InputStreamResource> downloadApiDocJson() throws Exception {
        Swagger swagger = readSwagger(SwaggerConstants.SWAGGER_URL);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", SwaggerConstants.API_DOC_JSON));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(IOUtils.toInputStream(JacksonUtils.obj2jsonIgnoreNullPretty(swagger), "UTF-8")));

    }

    public static void generateMarkdown() throws MalformedURLException {
        generate(ApiDocTypeEnum.MARKDOWN);
    }
    public static void generateConfluence() throws MalformedURLException {
        generate(ApiDocTypeEnum.CONFLUENCE);
    }
    public static void generateAsciidoc() throws MalformedURLException {
        generate(ApiDocTypeEnum.ASCIIDOC);
    }
    public static void generate(ApiDocTypeEnum apiDocTypeEnum) throws MalformedURLException {
        // TODO 获取当前容器地址
        // TODO 删除文件获取最新
        generate(apiDocTypeEnum.getSwaggerURL(), apiDocTypeEnum.getMarkupLanguage(), apiDocTypeEnum.getToFile());
    }

    private static void generate(String swaggerURL, MarkupLanguage markupLanguage, String toFile) throws MalformedURLException {
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(markupLanguage).build();

        Swagger2MarkupConverter
                // 例:http://localhost:8080/v2/api-docs
                .from(new URL(swaggerURL))
                .withConfig(config).build()
                //.toFolder(Paths.get("src/docs/markdown/generated"));
                // 例:src/docs/markdown/generated/all
                .toFile(Paths.get(toFile));
    }

    private static String generateHtml() throws IOException {
        //html文件基于ApiDocTypeEnum.ASCIIDOC才能生成html
        generate(ApiDocTypeEnum.ASCIIDOC);
        File toFile = new File(SwaggerConstants.API_DOC_HTML_PATH);
        if (!toFile.exists()) {
            FileUtils.forceMkdir(toFile);
        }
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        Attributes attributes = new Attributes();
        attributes.setCopyCss(true);
        attributes.setLinkCss(false);
        attributes.setSectNumLevels(3);
        attributes.setAnchors(true);
        attributes.setSectionNumbers(true);
        attributes.setHardbreaks(true);
        attributes.setTableOfContents(Placement.LEFT);

//        attributes.setAttribute("generated", "E:\\IDEAProject\\meta-boot\\src\\docs");
        OptionsBuilder optionsBuilder = OptionsBuilder.options()
                .backend("html5")//html
                .docType("book")
                .eruby("")
                .inPlace(true)
                .safe(SafeMode.UNSAFE)
                .toFile(new File(SwaggerConstants.API_DOC_HTML_FILE))
                .attributes(attributes);

        // 读取ASCIIDOC文件
        String asciiInputFile = ApiDocTypeEnum.ASCIIDOC.getDocFile();
        asciidoctor.convertFile(
                new File(asciiInputFile),
                optionsBuilder.get());
        return SwaggerConstants.API_DOC_HTML_FILE;
    }

    /**
     * 读取swgger描述对象信息
     * @param swaggerLocation
     * @return
     */
    private static Swagger readSwagger(String swaggerLocation) {
        Swagger swagger = new SwaggerParser().read(swaggerLocation);
        if (swagger == null) {
            throw new IllegalArgumentException("Failed to read the Swagger source");
        }
        return swagger;
    }


}
