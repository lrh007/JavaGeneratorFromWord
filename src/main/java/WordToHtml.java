import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Iterator;

/**
 * @Description:
 * @Author: liangruihao
 * @Date: 2021/4/29 14:04
 */
public class WordToHtml {


    public static void main(String[] args) throws Exception {

        String filePath = "C:/Users/MACHENIKE/Desktop/testjava/";
//        wordToHtml(filePath);
        parseHtml(filePath);
    }

    public static void parseHtml(String filePath) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.parse(new File(filePath + "1.htm"), "UTF-8");
        Elements div = document.getElementsByTag("p");
        Iterator<Element> it = div.iterator();
        while (it.hasNext()){
            Element p = it.next();
            if(p.parent().is("div")){
                if(p.text().contains("@C")){
                    System.out.println(p.text());
                    //获取p标签后面第一个table，用来解析
                    Element table = p.siblingElements().select("table").first();
                    String text = table.html();
                    System.out.println(text);

                }
            }
        }
    }
    public static void wordToHtml(String filePath) throws Exception {
        File file = new File(filePath);
        File[] files = file.listFiles();
        String name = null;
        for (File file2 : files) {
            Thread.sleep(500);
            name = file2.getName().substring(0, file2.getName().lastIndexOf("."));
            System.out.println(file2.getName());
            if ((file2.getName().endsWith(".docx") || file2.getName().endsWith(".DOCX")) && !file2.getName().contains("~$")) {
                docx(filePath, file2.getName(), name + ".htm");
            } else if((file2.getName().endsWith(".doc") || file2.getName().endsWith(".DOC")) && !file2.getName().contains("~$")){
                doc(filePath, file2.getName(), name + ".htm");
            }
        }
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
