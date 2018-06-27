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
import java.io.Reader;
import java.net.URISyntaxException;

/**
 * Created by linrx1 on 2018/6/27.
 */
@Controller
@RequestMapping("/swagger")
@ApiIgnore
public class SwaggerController {

    @RequestMapping(value = "/markdown", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(Long id)
            throws IOException {
        return SwaggerApiDocUtils.downloadApiDoc(ApiDocTypeEnum.MARKDOWN);
    }
    @RequestMapping(value = "/confluence", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadConfluence(Long id)
            throws IOException {
        return SwaggerApiDocUtils.downloadApiDoc(ApiDocTypeEnum.CONFLUENCE);
    }
    @RequestMapping(value = "/asciidoc", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadAsciidoc(Long id)
            throws IOException {
        return SwaggerApiDocUtils.downloadApiDoc(ApiDocTypeEnum.ASCIIDOC);
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadJsondoc(Long id)
            throws Exception {
        return SwaggerApiDocUtils.downloadApiDocJson(ApiDocTypeEnum.ASCIIDOC);
    }
}
