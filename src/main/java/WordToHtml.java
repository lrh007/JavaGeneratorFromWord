import entiry.ClassObj;
import entiry.FieldObj;
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
import java.util.*;

/**
 * @Description:
 * @Author: liangruihao
 * @Date: 2021/4/29 14:04
 */
public class WordToHtml {

    /**
     * 所有类对象
     */
    private List<ClassObj> classes = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        String filePath = "C:/Users/MACHENIKE/Desktop/testjava/";
        wordToHtml(filePath);
        List<ClassObj> classObjs = parseHtml(filePath);
        System.out.println(Arrays.asList(classObjs));
    }



    private static List<ClassObj> parseHtml(String filePath) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.parse(new File(filePath + "1.htm"), "UTF-8");
        Elements div = document.getElementsByTag("P");
        Iterator<Element> it = div.iterator();
        List<ClassObj> classes = new ArrayList<>(); //存放所有的对象
        while (it.hasNext()){
            Element p = it.next();
            if(p.parent().is("DIV")){
                String pText = p.text().trim();
                String c = "@@@";
                if(pText.contains(c)){
//                    System.out.println(p.text());
                    //解析类名和类备注，格式：类描述&&类名
                    int index = pText.indexOf(c);
                    String classDesc = pText.substring(0,index).trim();
                    String className = pText.substring(index+c.length(),pText.length()).trim();
                    //如果类名称为空，将类描述当做类名称
                    if("".equals(className)){
                        className = classDesc;
                    }
                    ClassObj cls = new ClassObj(className,classDesc);

//                    System.out.println(classDesc+"   "+className);

                    //获取p标签后面第一个table，用来解析
                    Element table = p.nextElementSiblings().select("TABLE").first();
                    List<FieldObj> fields = parseTable(table);
                    if(fields != null && fields.size() > 0){
                        cls.setFields(fields);
                    }
                    classes.add(cls);
                }
            }
        }
        return classes;
    }

    private static List<FieldObj> parseTable(Element table){
//        String text = table.html();
//        System.out.println(text);
        int fieldIndex = 0; //字段名称在tr中的位置
        Set<Integer> fieldDescSet = new LinkedHashSet<>(); //字段描述在tr中的位置，可以有多个
        boolean fieldExists = false; //字段名称是否存在
        List<FieldObj> fields = new ArrayList<>(); //所有的字段
        Elements trs = table.getElementsByTag("tr");
        Iterator<Element> it = trs.iterator();
        while(it.hasNext()){
            Element tr = it.next();
//            Element es = tr.getElementsByIndexEquals(0).select("td").first();
//            System.out.println(es.text());

            //去掉空白行
            if(!tr.hasText()){
                continue;
            }
            //找到了对应的字段名位置
            if(fieldExists){
                //字段名称
                String fieldName = tr.child(fieldIndex).text().trim();
                //所有的字段描述，逗号分隔
                StringBuilder values = new StringBuilder();
                for (Integer idx : fieldDescSet) {
                    values.append(tr.child(idx).text().trim()).append("，");
                }
                String fieldDesc = values.toString();
                if(fieldDesc.length() > 0){
                    fieldDesc = fieldDesc.substring(0,fieldDesc.length() - 1);
                }
                fields.add(new FieldObj(fieldName,fieldDesc));
//                System.out.print(es.text()+"   ");
            }


            Elements tds = tr.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i++) {
                Element td = tds.get(i);
//                System.out.print(td.text());
                //判断字段名的位置，和字段描述的位置
                if(td.text().contains("@FIELD")){
                    fieldIndex = i;
                    fieldExists = true;
                }else if(td.text().contains("@FDESC")){
                    fieldDescSet.add(i);
                }
            }
//            System.out.println("");
//            System.out.println(tr.text());

        }

//        System.out.println(fields);
        return fields;
    }


    public static void wordToHtml(String filePath) throws Exception {
        File file = new File(filePath);
        File[] files = file.listFiles();
        String name = null;
        for (File file2 : files) {
            Thread.sleep(500);
            name = file2.getName().substring(0, file2.getName().lastIndexOf("."));
//            System.out.println(file2.getName());
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
