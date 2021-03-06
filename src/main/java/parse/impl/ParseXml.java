package parse.impl;

import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import comm.Const;
import entity.xml.*;
import entity.xml.enums.XmlCustomMethodAnnotation;
import entity.xml.enums.XmlJavaScope;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import parse.Parser;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: 解析xml文件
 * @Author: liangruihao
 * @Date: 2021/8/27 10:38
 */
public class ParseXml implements Parser<XmlClass> {
    /**
     * 解析xml文件，生成对象
     * @param filePath :  xml文件路径
     * @return java.util.List<entity.xml.XmlClass>
     * @author: liangruihao
     * @date: 2021/8/27 10:38
     */
    @Override
    public List<XmlClass> parse(String filePath) throws Exception {
        System.out.println("=====================解析xml配置文件开始============================");
        List<XmlClass> xmlClassList = new ArrayList<>();
        //TODO 目前一个xml中只要一个class标签，后面如果有多个的情况，可以修改此处的逻辑
        XmlClass xmlClass = new XmlClass();
        //创建reader对象
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        //加载xml
        Document document = reader.read(new File(filePath));
        //获取根节点
        Element root = document.getRootElement();
        Iterator iterator = root.elementIterator();
        while (iterator.hasNext()){
            Element next = (Element) iterator.next();
            String name = next.getName();
            //解析文件路径
            if(Const.xml_directory_path.equals(name)){
                xmlClass.setDirectoryPath(next.getText());
                System.out.println("文件路径："+next.getText());
            }else if(Const.xml_field.equals(name)){
                //解析字段所有属性
                XmlField xmlField = new XmlField();
                //获取标签所有属性
                getAttributes(next,xmlField);
                xmlClass.setField(xmlField);
                System.out.println(JSONObject.toJSONString(xmlField));
            }else if(Const.xml_custom.equals(name)){
                XmlCustom xmlCustom = new XmlCustom();
                //解析自定义标签
                Iterator it = next.elementIterator();
                while (it.hasNext()){
                    Element ele = (Element) it.next();
                    String eleName = ele.getName();
                    //解析自定义属性
                    if(Const.xml_custom_fields.equals(eleName)){
                        List<XmlCustomField> xmlCustomFields = new ArrayList<>();
                        Iterator itcusfields = ele.elementIterator();
                        //遍历所有custom-field标签
                        while (itcusfields.hasNext()){
                            Element cus = (Element) itcusfields.next();
                            XmlCustomField xmlCustomField = new XmlCustomField();
                            //获取所有的属性
                            getAttributes(cus,xmlCustomField);
                            //获取下面所有的子标签
                            getChilds(cus,xmlCustomField);
                            xmlCustomFields.add(xmlCustomField);
                            System.out.println(JSONObject.toJSONString(xmlCustomField));
                        }
                        xmlCustom.setCustomFields(xmlCustomFields);
                    }else if(Const.xml_custom_methods.equals(eleName)){
                        //解析自定义方法
                        List<XmlCustomMethod> xmlCustomMethods = new ArrayList<>();
                        Iterator methoditer = ele.elementIterator();
                        //遍历所有custom-method
                        while (methoditer.hasNext()){
                            Element cus = (Element) methoditer.next();
                            XmlCustomMethod xmlCustomMethod = new XmlCustomMethod();
                            //获取所有的属性
                            getAttributes(cus,xmlCustomMethod);
                            //获取下面所有的子标签
                            Iterator cusChilds = cus.elementIterator();
                            while(cusChilds.hasNext()){
                                Element child = (Element) cusChilds.next();
                                if(Const.xml_custom_method_params.equals(child.getName())){
                                    //获取方法参数
                                    List<XmlCustomMethodParam> xmlCustomMethodParams = new ArrayList<>();
                                    Iterator paramit = child.elementIterator();
                                    while (paramit.hasNext()){
                                        Element param = (Element) paramit.next();
                                        XmlCustomMethodParam xmlCustomMethodParam = new XmlCustomMethodParam();
                                        //获取所有的属性
                                        getAttributes(param,xmlCustomMethodParam);
                                        //获取所有标签内容
                                        System.out.println(param.getName()+"标签值："+param.getText());
                                        setFields(param.getName(),param.getText(),xmlCustomMethodParam);
                                        xmlCustomMethodParams.add(xmlCustomMethodParam);
                                    }
                                    xmlCustomMethod.setMethodParams(xmlCustomMethodParams);
                                }else if(Const.xml_custom_method_annotations.equals(child.getName())){
                                    //获取方法的所有注解
                                    List<XmlCustomMethodAnnotation> annotationList = new ArrayList<>();
                                    Iterator iteratorAno = child.elementIterator();
                                    while (iteratorAno.hasNext()){
                                        Element anno = (Element) iteratorAno.next();
                                        annotationList.add(new XmlCustomMethodAnnotation(anno.getTextTrim()));
                                    }
                                    xmlCustomMethod.setMethodAnnotations(annotationList);
                                }else{
                                    System.out.println(child.getName()+"标签值："+child.getText());
                                    setFields(child.getName(),child.getText(),xmlCustomMethod);
                                }
                            }
                            xmlCustomMethods.add(xmlCustomMethod);
                            System.out.println(JSONObject.toJSONString(xmlCustomMethod));
                        }
                        xmlCustom.setCustomMethods(xmlCustomMethods);
                    }
                }
                xmlClass.setCustom(xmlCustom);
                System.out.println(JSONObject.toJSONString(xmlClass));
            }
        }
        xmlClassList.add(xmlClass);
        System.out.println("=====================解析xml配置文件结束============================");
        return xmlClassList;
    }

    /**
     * 获取标签所有属性
     * @param element : 标签对象
     * @param obj :  操作对象
     * @return void
     * @author: liangruihao
     * @date: 2021/9/3 16:42
     */
    private static <T> void getAttributes(Element element,T obj) throws Exception {
        List<Attribute> attributes = element.attributes();
        for(Attribute attr : attributes){
            //通过反射设置属性值
            System.out.println(element.getName()+"标签："+attr.getName()+"="+attr.getValue());
            setFields(attr.getName(),attr.getValue(),obj);
        }
    }
    /**
     * 获取标签下面所有子标签，适用于子表签只有value的情况，如下：
     * 《field-name》LOCAL_PATH《/field-name》
     * @param element :
     * @param obj :
     * @return void
     * @author: liangruihao
     * @date: 2021/9/3 17:02
     */
    private static <T> void getChilds(Element element,T obj) throws Exception {
        Iterator cusChilds = element.elementIterator();
        while(cusChilds.hasNext()){
            Element child = (Element) cusChilds.next();
            System.out.println(child.getName()+"标签值："+child.getText());
            setFields(child.getName(),child.getText(),obj);
        }
    }

    /**
     * 通过反射设置类的字段值
     * @param fieldName :字段名称
     * @param fieldValue : 字段值
     * @param obj : 操作对象
     * @return void
     * @author: liangruihao
     * @date: 2021/8/27 14:48
     */
    private static void setFields(String fieldName,String fieldValue,Object obj)throws Exception{
        //将标签名称转换成驼峰命名法，可以和类属性对应上
        String[] split = fieldName.split("-");
        StringBuilder str = new StringBuilder();
        for (int i=0;i<split.length;i++){
            if(i>0){
                str.append(split[i].substring(0, 1).toUpperCase()).append(split[i].substring(1));
            }else{
                str.append(split[i]);
            }
        }
        fieldName = str.toString();
        Class cls = obj.getClass();
        boolean success = false; //true 表示属性设置成功，false 表示失败
        //遍历本类所有的属性
        for (Field field : cls.getDeclaredFields()){
            field.setAccessible(true);
            if(fieldName.equals(field.getName())){
                Object o = convertType(field.getType(), fieldValue);
                field.set(obj,o);
                success = true;
                break;
            }
        }
        //遍历所有父类的属性
        if(!success){
            Class<?> superclass = cls.getSuperclass();
            while (superclass != Object.class){
                for (Field field: superclass.getDeclaredFields()){
                    field.setAccessible(true);
                    if(fieldName.equals(field.getName())){
                        Object o = convertType(field.getType(), fieldValue);
                        field.set(obj,o);
                        break;
                    }
                }
                superclass = superclass.getSuperclass();
            }
        }
    }
    /**
     * xml 类型转换
     */
    private static Object convertType(Class<?> type, String fieldValue){
        if(type == Boolean.class){
            return Boolean.parseBoolean(fieldValue);
        }else if(type == XmlJavaScope.class){
            XmlJavaScope[] values = XmlJavaScope.values();
            for (XmlJavaScope scope : values){
                if(scope.getScope().equals(fieldValue)){
                    return scope;
                }
            }
        }
        return fieldValue;
    }


    public static void main(String[] args) throws Exception {

        String filPath = "config.xml";
        URL url = Thread.currentThread().getContextClassLoader().getResource(filPath);
        System.out.println(url);
        filPath = url.getPath();
        ParseXml p = new ParseXml();
        p.parse(filPath);

//        XmlField xmlField = new XmlField();
//        setFields("javaType","Test.class",xmlField);
//        System.out.println(xmlField.getShowDescription());
//        System.out.println(xmlField.getJavaType());
    }
}
