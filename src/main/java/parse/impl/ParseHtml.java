package parse.impl;

import comm.Const;
import convert.impl.WordToHtml;
import entity.ClassObj;
import entity.FieldObj;
import entity.FileObj;
import factory.DyFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parse.Parser;
import util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Description: 解析html文件
 * @Author: liangruihao
 * @Date: 2021/5/6 9:51
 */
public class ParseHtml implements Parser<FileObj> {

    /**
     * 解析html文件
     * @param filePath :
     * @return java.util.List<entity.FileObj>
     * @author: liangruihao
     * @date: 2021/5/6 14:07
     */
    @Override
    public List<FileObj> parse(String filePath) throws Exception {
        List<FileObj> fileObjs = new ArrayList<>();
        List<String> fileNames = DyFactory.getInstance(WordToHtml.class).convert(filePath);
        //如果文件夹下面有多个word文件的话，生成的java类根据不同的文件名生成对应的文件夹，并将java类放进去
        for (String name : fileNames){
            List<ClassObj> classObjs = parseHtml(filePath,name);
            fileObjs.add(new FileObj(name,classObjs));
        }
        return fileObjs;
    }




    /**
     * 解析html
     * @param filePath : 文件路径
     * @param fileName :  文件名称
     * @return java.util.List<entiry.ClassObj>
     * @author: liangruihao
     * @date: 2021/4/30 16:56
     */
    private static List<ClassObj> parseHtml(String filePath, String fileName) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.parse(new File(filePath + fileName + Const.SUFFIX_HTML), "UTF-8");
        Elements div = document.getElementsByTag(Const.HTML_P);
        Iterator<Element> it = div.iterator();
        List<ClassObj> classes = new ArrayList<>(); //存放所有的对象
        while (it.hasNext()){
            Element p = it.next();
            if(p.parent().is(Const.HTML_DIV)){
                String pText = p.text().trim();
                if(pText.contains(Const.CLASS) || pText.contains(Const.CLASS_REQ)){
                    String str = null;
                    boolean reqClass = false;
                    if(pText.contains(Const.CLASS)){
                        str = Const.CLASS;
                    }else if(pText.contains(Const.CLASS_REQ)){
                        str = Const.CLASS_REQ;
                        reqClass = true;
                    }
                    //解析类名和类备注，普通类格式：类描述@@@类名，请求类格式：类描述$$$类名<返回类>，尖括号不写默认是T
                    int index = pText.indexOf(str);
                    String classDesc = pText.substring(0,index).trim();
                    String className = pText.substring(index + str.length(),pText.length()).trim();
                    String responseClass = Const.DEFAULT_RESP_TYPE;
                    //如果类名称为空，将类描述当做类名称
                    if("".equals(className)){
                        className = classDesc;
                    }else{
                        //判断是否是请求类
                        if(reqClass){
                            responseClass = StringUtil.getClassInterface(className);
                            if(responseClass == null){
                                responseClass = Const.DEFAULT_RESP_TYPE;
                            }
                        }
                        int idx = className.indexOf(Const.SPLIT_5);
                        if(idx != -1){
                            className = className.substring(0,idx);
                        }
                    }
                    ClassObj cls = new ClassObj(className,classDesc,reqClass,responseClass);
                    //获取p标签后面第一个table，用来解析
                    Element table = p.nextElementSiblings().select(Const.HTML_TABLE).first();
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

    /**
     *  解析html文件中的table
     * @param table : table元素
     * @return java.util.List<entiry.FieldObj>
     * @author: liangruihao
     * @date: 2021/4/30 16:55
     */
    private static List<FieldObj> parseTable(Element table){
        int fieldIndex = -1; //字段名称在tr中的位置
        int fieldTypeIndex = -1; //字段类型在tr中的位置
        Set<Integer> fieldDescSet = new LinkedHashSet<>(); //字段描述在tr中的位置，可以有多个
        boolean fieldExists = false; //字段名称是否存在
        List<FieldObj> fields = new ArrayList<>(); //所有的字段
        Elements trs = table.getElementsByTag(Const.HTML_TR);
        Iterator<Element> it = trs.iterator();
        while(it.hasNext()){
            Element tr = it.next();
            //去掉空白行
            if(!tr.hasText()){
                continue;
            }
            //找到了对应的字段名位置
            if(fieldExists){
                //字段名称
                String fieldName = tr.child(fieldIndex).text().trim();
                //字段类型
                String fieldType = null;
                if(fieldTypeIndex != -1){
                    fieldType = tr.child(fieldTypeIndex).text().trim();
                    //只有自定义的类型才使用，其他的全部使用默认类型String
                    if(fieldType.contains(Const.SPLIT_3)){
                        fieldType = fieldType.replaceAll(Const.SPLIT_3,"");
                    }else{
                        fieldType = Const.DEFAULT_TYPE;
                    }
                }else{
                    fieldType = Const.DEFAULT_TYPE;
                }
                //所有的字段描述，逗号分隔
                StringBuilder values = new StringBuilder();
                for (Integer idx : fieldDescSet) {
                    values.append(tr.child(idx).text().trim()).append("，");
                }
                String fieldDesc = values.toString();
                if(fieldDesc.length() > 0){
                    fieldDesc = fieldDesc.substring(0,fieldDesc.length() - 1);
                }
                fields.add(new FieldObj(fieldName,fieldDesc,fieldType));
            }
            Elements tds = tr.getElementsByTag(Const.HTML_TD);
            for (int i = 0; i < tds.size(); i++) {
                Element td = tds.get(i);
                //判断字段名的位置，和字段描述的位置
                if(td.text().contains(Const.FIELD_NAME)){
                    fieldIndex = i;
                    fieldExists = true;
                }else if(td.text().contains(Const.FIELD_TYPE)){
                    fieldTypeIndex = i;
                }else if(td.text().contains(Const.FIELD_DESC)){
                    fieldDescSet.add(i);
                }
            }
        }
        return fields;
    }


}
