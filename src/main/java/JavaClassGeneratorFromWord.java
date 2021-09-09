import comm.Const;
import entity.word.ClassObj;
import entity.word.FieldObj;
import entity.word.FileObj;
import entity.xml.XmlClass;
import generate.FromGenerate;
import generate.impl.FromByWord;
import parse.Parser;
import parse.impl.ParseHtml;
import parse.impl.ParseXml;

import java.io.*;
import java.util.List;

/**
 * @Description: 从workd文件生成java实体类
 * @Author: liangruihao
 * @Date: 2021/4/23 11:21
 */
public class JavaClassGeneratorFromWord {


    public static void main(String[] args) throws Exception {
        String doc = "C:/Users/MACHENIKE/Desktop/cp项目实体类代码生成工具/";
//        String doc = "./";
        //TODO 这里判断是否存在xml配置文件，不存在就在当前目录下生成此配置文件

        //加载xml配置文件
        Parser parse = new ParseXml();
        List<XmlClass> xmlClassList = parse.parse(doc);
        //TODO 这里后面可以使用工厂模式代替
        FromGenerate generate = new FromByWord();
        generate.generateClass(xmlClassList.get(0)); //xml中只会返回一个xmlClass对象

    }


}
