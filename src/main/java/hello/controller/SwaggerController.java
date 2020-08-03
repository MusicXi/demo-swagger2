package hello.controller;

import hello.constants.ApiDocTypeEnum;
import hello.util.SwaggerApiDocUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

/**
 * swagger2 接口文档下载控制器
 * Created by linrx1 on 2018/6/27.
 */
@Controller
@RequestMapping("/swagger")
@ApiIgnore
public class SwaggerController {

    @RequestMapping(value = "/markdown", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile()
            throws IOException {
        return SwaggerApiDocUtils.downloadApiDoc(ApiDocTypeEnum.MARKDOWN);
    }
    @RequestMapping(value = "/confluence", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadConfluence()
            throws IOException {
        return SwaggerApiDocUtils.downloadApiDoc(ApiDocTypeEnum.CONFLUENCE);
    }
    @RequestMapping(value = "/asciidoc", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadAsciidoc()
            throws IOException {
        return SwaggerApiDocUtils.downloadApiDoc(ApiDocTypeEnum.ASCIIDOC);
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadJsondoc()
            throws Exception {
        return SwaggerApiDocUtils.downloadApiDocJson();
    }

    @RequestMapping(value = "/html", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadHtmldoc()
            throws Exception {
        return SwaggerApiDocUtils.downloadHtmlDoc();
    }
}
