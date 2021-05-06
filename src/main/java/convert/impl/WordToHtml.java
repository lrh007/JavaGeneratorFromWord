package convert.impl;

import comm.Const;
import convert.Convert;
import factory.DyFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * @Description:
 * @Author: liangruihao
 * @Date: 2021/4/29 14:04
 */
public class WordToHtml implements Convert {


    public static void main(String[] args) throws Exception {

        String filePath = "C:/Users/MACHENIKE/Desktop/testjava/";
        List<String> list = DyFactory.getInstance(WordToHtml.class).convert(filePath);
        System.out.println(list);
    }


    @Override
    public List<String> convert(String filePath) throws Exception {
        File file = new File(filePath);
        File[] files = file.listFiles();
        String name = null;
        List<String> names = new ArrayList<>();
        for (File file2 : files) {
            if(file2.isDirectory()){
                continue;
            }
            Thread.sleep(500);
            name = file2.getName().substring(0, file2.getName().lastIndexOf("."));
            if ((file2.getName().endsWith(Const.SUFFIX_DOCX_LOW)
                    || file2.getName().endsWith(Const.SUFFIX_DOCX_UP))
                    && !file2.getName().contains(Const.SUFFIX_DOCX_$)) {
                docx(filePath, file2.getName(), name + Const.SUFFIX_HTML);
                names.add(name);
            } else if((file2.getName().endsWith(Const.SUFFIX_DOC_LOW)
                    || file2.getName().endsWith(Const.SUFFIX_DOC_UP))
                    && !file2.getName().contains(Const.SUFFIX_DOCX_$)){
                doc(filePath, file2.getName(), name + Const.SUFFIX_HTML);
                names.add(name);
            }
        }
        return names;
    }

    /**
     * 转换docx
     *
     * @param filePath
     * @param fileName
     * @param htmlName
     * @throws Exception
     */

    private static void docx(String filePath, String fileName, String htmlName) throws Exception {
        final String file = filePath + fileName;
        File f = new File(file);
        // ) 加载word文档生成 XWPFDocument对象
        InputStream in = new FileInputStream(f);
        XWPFDocument document = new XWPFDocument(in);
        // ) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
        File imageFolderFile = new File(filePath);
        XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
        options.setExtractor(new FileImageExtractor(imageFolderFile));
        options.setIgnoreStylesIfUnused(false);
        options.setFragment(true);
        // ) 将 XWPFDocument转换成XHTML
        OutputStream out = new FileOutputStream(new File(filePath + htmlName));
        XHTMLConverter.getInstance().convert(document, out, options);
    }

    /**
     * 转换doc
     *
     * @param filePath
     * @param fileName
     * @param htmlName
     * @throws Exception
     */

    private static void doc(String filePath, String fileName, String htmlName) throws Exception {
        final String file = filePath + fileName;
        InputStream input = new FileInputStream(new File(file));
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        //解析word文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        File htmlFile = new File(filePath + htmlName);
        OutputStream outStream = new FileOutputStream(htmlFile);
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
    }


}
