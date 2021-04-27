package hello.controller;

import hello.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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

    @PostMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "上传图片", notes = "上传图片", httpMethod = "POST")
    @ResponseBody
    public Map<String, Object> upload(@ApiParam(value = "测试String", required = true) User ddd, @ApiParam(value = "测试图片", required = true) MultipartFile file, @ApiParam(value = "测试图片2", required = true) MultipartFile file2) {
        Map<String, Object> resultMap = new HashedMap();
        this.saveFile("test01", file, resultMap);
        this.saveFile("test02", file2, resultMap);
        return resultMap;
    }

    /**
     * 保存文件
     * @param name
     * @param file
     * @return
     */
    private Map<String, Object> saveFile(String name, MultipartFile file, Map<String, Object> resultMap) {
        if (file.isEmpty()) {
            resultMap.put(name, "文件没有变化");
            return resultMap;
        }

        if (file.getContentType().contains("image")) {
            try {
                String temp =   File.separator +  "images" + File.separator + "upload" + File.separator;
                // 获取图片的文件名
                String fileName = file.getOriginalFilename();
                // 获取图片的扩展名
                String extensionName = fileName.substring(fileName.indexOf("."));
                // 新的图片文件名 = 获取时间戳+"."图片扩展名
                String newFileName = name + extensionName;
                // 数据库保存的目录
                String datedDirectory = temp.concat(String.valueOf(1)).concat(File.separator);
                // 文件路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                if(!path.exists()) {
                    path = new File("");
                }

                String filePath = path.getAbsolutePath().concat(File.separator + "static" + datedDirectory);

                File dest = new File(filePath, newFileName);
                // 创建文件目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                // 上传到指定目录
                file.transferTo(dest);
                Map<String, Object> detail = new HashedMap();
                LOGGER.info("文件:{} 上传成功!", file.getOriginalFilename());
                detail.put("originalFilename", fileName);
                detail.put("url", "localhost:8080" + datedDirectory + newFileName);
                detail.put("absolutePath", dest.getAbsoluteFile().toString());
                resultMap.put(name, detail);
            }catch (Exception e){
                LOGGER.error("文件上传失败" + e.getMessage(), e);
                resultMap.put(name, "文件上传失败" + e.getMessage());
            }
        }
        return resultMap;

    }

}