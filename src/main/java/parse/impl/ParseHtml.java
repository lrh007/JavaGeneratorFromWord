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
        org.jsoup.nodes.Document document = Jsoup.parse(new File(filePath + fileName + Const.SUFFIX_HTML), Const.CHARSET_UTF_8);
        Elements div = document.getElementsByTag(Const.HTML_P);
        Iterator<Element> it = div.iterator();
        Iterator<Element> iterator = div.iterator();
        List<ClassObj> classes = new ArrayList<>(); //存放所有的对象
        boolean supperFlag = false;
        String supperText = null;
        //判断是否存在统一处理标签@SUPPER
        while (iterator.hasNext()) {
            Element p = iterator.next();
            String pText = p.text().trim();
            if(pText.toUpperCase().contains(Const.SUPPER_MODE)){
                supperFlag = true;
                supperText = pText.toUpperCase();
                System.out.println("存在统一模式标签： "+pText);
                break;
            }
        }

        while (it.hasNext()){
            Element p = it.next();
            if(p.parent().is(Const.HTML_DIV)){
                String pText = p.text().trim();
                if(pText.contains(Const.CLASS)
                        || pText.contains(Const.CLASS_REQ)
                        || pText.contains(Const.CLASS_REQ_SUPPER)
                        || pText.contains(Const.CLASS_SUPPER)){
                    String str = null;
                    boolean reqClass = false;
                    //这里要先判断多的@或者$否则，走不到@@@@就会被@@@拦截
                    if(pText.contains(Const.CLASS_SUPPER)){
                        str = Const.CLASS_SUPPER;
                    }else if(pText.contains(Const.CLASS_REQ_SUPPER)){
                        str = Const.CLASS_REQ_SUPPER;
                        reqClass = true;
                    }else if(pText.contains(Const.CLASS)){
                        str = Const.CLASS;
                    }else if(pText.contains(Const.CLASS_REQ)) {
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
                    //判断当前table是否是特殊的
                    boolean specialFlag = false;
                    if(pText.contains(Const.CLASS_REQ_SUPPER) || pText.contains(Const.CLASS_SUPPER)){
                        specialFlag = true;
                    }
                    List<FieldObj> fields = parseTable(table,supperText,supperFlag,specialFlag);
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
     * 解析html文件中的table
     * @param table : table元素
     * @param supperText : 统一模式标签内容
     * @param supperFlag : 统一模式标识
     * @param specialFlag :  特殊table标识
     * @return java.util.List<entity.FieldObj>
     * @author: liangruihao
     * @date: 2021/5/7 18:38
     */
    private static List<FieldObj> parseTable(Element table,String supperText,boolean supperFlag,boolean specialFlag){
        int fieldIndex = -1; //字段名称在tr中的位置
        int fieldTypeIndex = -1; //字段类型在tr中的位置
        Set<Integer> fieldDescSet = new LinkedHashSet<>(); //字段描述在tr中的位置，可以有多个
        boolean fieldExists = false; //字段名称是否存在
        boolean supperChangeFlag = false; //是否被统一模式改变了数据
        List<FieldObj> fields = new ArrayList<>(); //所有的字段

        Elements trs = table.getElementsByTag(Const.HTML_TR);
        Iterator<Element> it = trs.iterator();
        while(it.hasNext()){
            Element tr = it.next();
            //去掉空白行
            if(!tr.hasText()){
                continue;
            }
            //存在统一解析模式,且每个table只能判断一次,且当前table不是特殊的
            if(supperFlag && !supperChangeFlag && !specialFlag){
                supperText = supperText.replaceAll(Const.SUPPER_MODE,"");
                String[] split = supperText.split(Const.SPLIT_6);
                for (String s : split){
                    if(s.contains(Const.FIELD_NAME)){ //字段名称
                        fieldExists = true;
                        supperChangeFlag = true;
                        fieldIndex = Integer.parseInt(s.replaceAll(Const.FIELD_NAME,"").replaceAll(Const.SPLIT_7,"").trim());
                    }else if(s.contains(Const.FIELD_TYPE)){//字段类型
                        fieldTypeIndex = Integer.parseInt(s.replaceAll(Const.FIELD_TYPE,"").replaceAll(Const.SPLIT_7,"").trim());
                    }else if(s.contains(Const.FIELD_DESC)){//字段注释
                        String[] descs = s.replaceAll(Const.FIELD_DESC, "").replaceAll(Const.SPLIT_7, "").trim().split(Const.SPLIT_8);
                        for(String idx : descs){
                            fieldDescSet.add(Integer.parseInt(idx));
                        }
                    }

                }
            }


            //找到了对应的字段名位置
            if(fieldExists){
                //字段名称
                String fieldName = tr.child(fieldIndex).text().trim();
                //由于存在统一模式，相关注解都会没有，此时table第一行可能是无效数据，所以在此对字段名称判断，只能是数字字母组合
                if(!fieldName.matches(Const.SPLIT_9)){
                    continue;
                }
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
            }else{
                Elements tds = tr.getElementsByTag(Const.HTML_TD);
                for (int i = 0; i < tds.size(); i++) {
                    Element td = tds.get(i);
                    //判断字段名的位置，和字段描述的位置
                    if(td.text().toUpperCase().contains(Const.FIELD_NAME)){
                        fieldIndex = i;
                        fieldExists = true;
                    }else if(td.text().toUpperCase().contains(Const.FIELD_TYPE)){
                        fieldTypeIndex = i;
                    }else if(td.text().toUpperCase().contains(Const.FIELD_DESC)){
                        fieldDescSet.add(i);
                    }
                }
            }
        }
        return fields;
    }


}
