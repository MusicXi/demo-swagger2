package hello.constants;

import io.github.swagger2markup.markup.builder.MarkupLanguage;

/**
 * swagger2 api 文档类型
 * Created by linrx1 on 2018/6/27.
 */
public enum ApiDocTypeEnum {
    MARKDOWN(MarkupLanguage.MARKDOWN, SwaggerConstants.DOC_ROOT_PATH + "/docs/markdown/generated/api_doc_markdown"),
    CONFLUENCE(MarkupLanguage.CONFLUENCE_MARKUP, SwaggerConstants.DOC_ROOT_PATH + "/docs/confluence/generated/api_doc_confluence"),
    ASCIIDOC(MarkupLanguage.ASCIIDOC, SwaggerConstants.DOC_ROOT_PATH + "/docs/asciidoc/generated/api_doc_asciidoc");


    private MarkupLanguage markupLanguage;
    private String toFile;

    ApiDocTypeEnum(MarkupLanguage markupLanguage, String toFile) {
        this.markupLanguage = markupLanguage;
        this.toFile = toFile;
    }

    /**
     * 生成接口文件的路径
     * @return
     */
    public String  getDocFile() {
        return this.toFile + this.markupLanguage.getFileNameExtensions().get(0);
    }

    public String getSwaggerURL () {
        return SwaggerConstants.SWAGGER_URL;
    }

    public MarkupLanguage getMarkupLanguage() {
        return markupLanguage;
    }

    public String getToFile() {
        return toFile;
    }
}
