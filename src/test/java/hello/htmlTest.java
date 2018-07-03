package hello;

import org.asciidoctor.*;

import java.io.File;

/**
 * Created by linrx1 on 2018/6/27.
 */
public class htmlTest {
    public static void main(String[] args) {

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
                .attributes(attributes);
        String asciiInputFile = "src/docs/asciidoc/generated/all.adoc";
        asciidoctor.convertFile(
                new File(asciiInputFile),
                optionsBuilder.get());
    }


}

