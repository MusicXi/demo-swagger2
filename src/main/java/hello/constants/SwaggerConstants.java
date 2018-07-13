package hello.constants;

/**
 * Created by linrx1 on 2018/6/27.
 */
public class SwaggerConstants {
    public static final String SWAGGER_URL = "http://localhost:8080/v2/api-docs";
    /** 生成swagger2 api 文档的根目录*/
    public static final String DOC_ROOT_PATH = "target/file-temp";

    public static final String API_DOC_JSON = "api_doc_json.json";
    public static final String API_DOC_HTML = "api_doc_html.html";
    public static final String API_DOC_HTML_PATH = DOC_ROOT_PATH  + "/docs/html/generated/";
    public static final String API_DOC_HTML_FILE = DOC_ROOT_PATH + "/docs/html/generated/" + API_DOC_HTML;
}
