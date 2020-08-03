package hello.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Api 说明
 * <li>tags="说明该类的作用，可以在UI界面上看到的注解" （非空时将覆盖value的值）</li>
 * <li>value="说明类的作用"</li>
 */
@Api(value = "说明类的作用", tags = "说明该类的作用，可以在UI界面上看到的注解")
@RestController
@RequestMapping(value="/test")
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @ApiOperation(value="说明该方法的作用和用途", notes="对该方法的备注信息说明")
    @RequestMapping(value="showRequestHeader", method=RequestMethod.GET)
    public Map<String, Object> showRequestHeader(HttpServletRequest request) {
        Map<String, Object> result = new HashedMap();
        result.put("Accept-Language", request.getHeader("Accept-Language"));
        result.put("x-access-token", request.getHeader("x-access-token"));
        result.put("request.getLocale()", request.getLocale());
        result.put("request.getLocale().toLanguageTag()", request.getLocale().toLanguageTag());
        LOGGER.info("x-access-token:{}" , request.getHeader("x-access-token"));
        LOGGER.info("Accept-Language:{}" , request.getHeader("Accept-Language"));
        return result;
    }

    @ApiOperation(value="测试@RequestHeader", notes="对该方法的备注信息说明")
    @RequestMapping(value="testRequestHeader", method=RequestMethod.GET)
    public Map<String, Object> testRequestHeader(@RequestHeader("Accept-Language") String language, HttpServletRequest request) {
        Map<String, Object> result = new LinkedMap();
        result.put("Accept-Language", language);
        result.put("request.getLocale()", request.getLocale());
        result.put("request.getLocale().toLanguageTag()", request.getLocale().toLanguageTag());
        return result;
    }
}