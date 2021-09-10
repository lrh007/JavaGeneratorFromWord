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
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Description: 从workd文件生成java实体类
 * @Author: liangruihao
 * @Date: 2021/4/23 11:21
 */
public class JavaClassGeneratorFromWord {


    public static void main(String[] args) throws Exception {
//        String doc = "C:/Users/MACHENIKE/Desktop/cp项目实体类代码生成工具/";
        String doc = "./";
        //加载配置文件
        File file = loadConfig(doc);
        //加载xml配置文件
        Parser parse = new ParseXml();
        System.out.println("绝对路径："+file.getAbsoluteFile());
        List<XmlClass> xmlClassList = parse.parse(file.getAbsolutePath());
        //TODO 这里后面可以使用工厂模式代替
        FromGenerate generate = new FromByWord();
        generate.generateClass(xmlClassList.get(0)); //xml中只会返回一个xmlClass对象

    }
    /**
     * 加载配置文件
     * @param doc :
     * @return java.io.File
     * @author: liangruihao
     * @date: 2021/9/10 17:13
     */
    private static File loadConfig(String doc) throws Exception{
        String configPath = doc + Const.xml_config_path + Const.xml_config_name;
        File file = new File(configPath);
        //这里判断是否存在xml配置文件，不存在就在当前目录下生成此配置文件
        if(!file.exists()){
            //创建目录
            File fileCfg = new File(doc + Const.xml_config_path);
            if(!fileCfg.exists()){
                fileCfg.mkdirs();
            }
            InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(Const.xml_config_name);
            InputStream dtdStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(Const.xml_dtd_name);
            String dtdPath = doc + Const.xml_config_path + Const.xml_dtd_name;
            FileOutputStream dtdOutStream = new FileOutputStream(dtdPath);
            FileOutputStream configOutStream = new FileOutputStream(configPath);
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = configStream.read(bytes)) != -1){
                configOutStream.write(bytes,0,length);
                configOutStream.flush();
            }
            while ((length = dtdStream.read(bytes)) != -1){
                dtdOutStream.write(bytes,0,length);
                dtdOutStream.flush();
            }
            dtdOutStream.close();
            configOutStream.close();
        }
        return file;
    }

}
