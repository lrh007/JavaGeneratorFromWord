import comm.Const;
import entity.ClassObj;
import entity.FieldObj;
import entity.FileObj;
import parse.impl.ParseHtml;
import util.FileUtil;

import java.io.*;
import java.util.List;

/**
 * @Description: 从workd文件生成java实体类
 * @Author: liangruihao
 * @Date: 2021/4/23 11:21
 */
public class JavaClassGeneratorFromWord {


    public static void main(String[] args) throws Exception {
//        String doc = "C:/Users/MACHENIKE/Desktop/juzi/HX-210705-14 长安银行/新建文件夹/";
        String doc = "./";
        System.out.println("正在生成文件，请稍等...");
        int count = 0;//统计文件个数
        StringBuilder builder = new StringBuilder();//用来生成日志以及命令使用方法
        List<FileObj> classes = new ParseHtml().parse(doc);
        for (FileObj fileObj : classes) {
            List<ClassObj> classObjs = fileObj.getClassObjs();
            if (classObjs != null && classObjs.size() > 0) {
                for (ClassObj classObj : classObjs) {
                    String className = classObj.getClassName();
                    String classDesc = classObj.getClassDesc();
                    List<FieldObj> fields = classObj.getFields();

                    String javaPath = doc +fileObj.getFileName() +"/";
                    //将请求类生成到request文件夹下面
                    String reqPath = "";
                    if(classObj.isReqClass()){
                        reqPath = Const.REQUEST_CLASS_PATH + "/";
                        javaPath += reqPath;
                    }
                    String javaName = doc +fileObj.getFileName() + "/"+ reqPath + className + Const.SUFFIX_JAVA;
                    System.out.println("生成java文件："+javaName+"   "+classDesc);
                    builder.append(javaName+"   "+classDesc+"\n");
                    File file = new File(javaPath);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javaName),Const.CHARSET_UTF_8));
                    //生成类注释
                    classDesc = "/**\n" +
                            "* "+classDesc+"\n" +
                            "*/";
                    out.write(classDesc);
                    out.newLine();
                    //判断是否是请求类
                    String interfaceStr = "";
                    if(classObj.isReqClass()){
                        interfaceStr = " implements CapitalPlatformRequest<" + classObj.getResponseClass() + ">";
                    }

                    //生成类名称
                    className = "public class " + className + interfaceStr + " {";
                    out.write(className);

                    //判断是否是请求类
                    if(classObj.isReqClass()){
                        genReqStaticVar(out);
                    }

                    //生成属性字段
                    if(fields != null && fields.size() > 0){
                        for (FieldObj field : fields){
                            out.newLine();
                            out.newLine();
                            String fieldName = field.getFieldName();
                            String fieldDesc = field.getFieldDesc();
                            //生成属性字段注释
                            fieldDesc = "\t/** " + fieldDesc + " **/";
                            out.write(fieldDesc);
                            out.newLine();
                            //生成属性字段
                            fieldName = "\tprivate "+field.getFieldType()+ " "+ fieldName + ";";
                            out.write(fieldName);
                        }

                        //判断是否是请求类
                        if(classObj.isReqClass()){
                            genReqOverrideMethod(out,fields,classObj.getResponseClass());
                        }

                        for (FieldObj field : fields){
                            out.newLine();
                            out.newLine();
                            //生成getter 和 setter方法
                            getterAndSetter(out,field.getFieldName(),field.getFieldType());
                        }
                    }else{
                        //没有属性字段的情况下也要生成override方法
                        //判断是否是请求类
                        if(classObj.isReqClass()){
                            genReqOverrideMethod(out,fields,classObj.getResponseClass());
                        }
                    }
                    out.newLine();
                    out.write("}");
                    out.close();
                    count ++;
                }
            }
        }
        builder.append("文件生成完成，共生成 " + count + "个文件");
        System.out.println("文件生成完成，共生成 " + count + "个文件");
        //生成执行日志
        generatorResultLog(builder.toString(),doc);
    }


    /**
     * 生成字段getter 和 setter方法
     * @param out :  文件输出流
     * @param fieldName :  字段名称
     * @param fieldType :  字段类型
     * @return void
     * @author: liangruihao
     * @date: 2021/4/30 18:18
     */
    private static void getterAndSetter(BufferedWriter out,String fieldName,String fieldType) throws IOException {
        out.newLine();
        String newName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        String getter = "\tpublic " + fieldType + " get" + newName + "() {\n" +
                    "        return "+ fieldName + ";\n" +
                    "    }";
        out.write(getter);
        out.newLine();
        out.newLine();
        String setter = "\tpublic void set" + newName + "(" + fieldType + " " + fieldName + ") {\n" +
                "        this." + fieldName + " = " + fieldName + ";\n" +
                "    }";
        out.write(setter);
        out.newLine();
    }

    /**
     * 生成请求类的静态变量
     * @param out :
     * @return void
     * @author: liangruihao
     * @date: 2021/5/6 16:23
     */
    private static void genReqStaticVar(BufferedWriter out) throws IOException {
        out.newLine();
        out.newLine();
        String str = "\t/** 请求资方url地址 **/\n"
                     + "\tprivate static final String METHOD_URL =\"\";\n"
                     + "\t/** 本地请求url地址 **/\n"
                     + "\tprivate static final String LOCAL_PATH =\"\";";
        out.write(str);
        out.newLine();
    }
    /**
     * 生成请求类的Overrid方法
     * @param out :
     * @return void
     * @author: liangruihao
     * @date: 2021/5/6 16:40
     */
    private static void genReqOverrideMethod(BufferedWriter out,List<FieldObj> fields,String responseClass) throws IOException {
        out.newLine();
        out.newLine();

        String str = "\t@Override\n" +
                "    @JSONField(serialize = false)\n" +
                "    public String getHttpMethod() {\n" +
                "        return Cons.HTTP_POST_JSON;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    @JSONField(serialize = false)\n" +
                "    public Class<" + responseClass + "> getResponseClass() {\n" +
                "        return " + responseClass + ".class;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    @JSONField(serialize = false)\n" +
                "    public String getMethod() {\n" +
                "        return METHOD_URL;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    @JSONField(serialize = false)\n" +
                "    public String getLocalPath() {\n" +
                "        return LOCAL_PATH;\n" +
                "    }";

        out.write(str);
        out.newLine();
        out.newLine();
        genReqMapParams(out,fields);
    }

    private static void genReqMapParams(BufferedWriter out,List<FieldObj> fields) throws IOException {
        out.newLine();
        out.newLine();
        StringBuilder builder = new StringBuilder();
        builder.append("\t@Override\n" +
                "    @JSONField(serialize = false)\n" +
                "    public Map<String, Object> getParams() {\n" +
                "        Map<String,Object> map = new HashMap<>();\n");
        if(fields != null && fields.size() >0){
            for (FieldObj f : fields){
                builder.append("\t\tmap.put(\"" + f.getFieldName() + "\"," + f.getFieldName() + ");\n");
            }
        }
        builder.append("\t\treturn map;\n" +
                "    }");
        out.write(builder.toString());
        out.newLine();
        out.newLine();
    }

    private static void generatorResultLog(String str,String path){
        String s = "# JavaGeneratorFromWord\n" +
                "从word文档生成java实体类  \n" +
                "1、标签类型：  \n" +
                "(1)@@@ 表示普通类，格式： 类注释@@@类名称，如果没有类名称，将会把类注释当做名称，仅仅是普通的实体类  \n" +
                "\n" +
                "(2)$$$ 表示请求类，格式：类注释$$$类名称，或 类注释$$$类名称<返回类>，请求类生成的方法较多，用于请求资方使用，<返回类>这部分可以不写，默认是T  \n" +
                "(3)@@@ 和 $$$ 其中一个必须存在，否则不会生成java文件，而且有这个符号的当前行的下方第一个表格将作为类属性，进行解析  \n" +
                "\n" +
                "(4)@FIELD （必须）表示类的字段名称，用于表格中标题部分  \n" +
                "(5)@FDESC （非必须）表示类的字段的注释，用户表格标题部分，多列描述可以写多个注解，最终将使用逗号分隔连接到一起  \n" +
                "(6)@FTYPE （非必须）表示类的字段的类型，用于表格中标题部分，默认类型是String，和原文档中写的类型无关，如果想要自定义类型，那么在所在列使用  @类型名 即可改变成自定义类型  \n" +
                "(7)@SUPPER （非必须） 统一模式标签，表示当前文档中所有的表格列数一样，且每列代表的内容一样，那么在表格中将不需要使用@FIELD、@FDESC、@FTYPE 这些标签来表示字段信息，即可使用此标签来简化开发。  \n" +
                "语法如下：  \n" +
                "@supper@FIELD=字段名称在表格中的索引位置，从0开始&@FDESC=字段注释在表格中的索引位置，多个使用逗号分开&@FTYPE=字段类型在表格中的索引位置  \n" +
                "例如：@supper@FIELD=0 & @FDESC=1,4&@FTYPE=3  \n" +
                "\n" +
                "如果当前文档中大多数表格都是5列，只有其中不是5列，那么这些表格要使用@FIELD、@FDESC、@FTYPE这些标签来表示。  \n" +
                "\n" +
                "（8）标签优先级：  \n" +
                "@SUPPER标签和这些标签同时存在的情况下，  \n" +
                "优先使用表格中的@FIELD、\t@FDESC、@FTYPE来解析   \n" +
                "（9）以上标签的大小写不敏感  \n" +
                "（10）注意点：所有的标签需要在同一行，不能回车换行\n" +
                "\n" +
                "#使用方法\n" +
                "2、将jar包和run.bat文件和接口文档放到同一个文件下  \n" +
                "3、打开接口文档，使用下面指定的标签对文档进行修改  \n" +
                "4、双击run.bat运行程序，等待生成对应java实体类，生成的java文件放在当前文件夹下面和文档同名的文件夹下，  \n" +
                "\trequest 是单独存放接口请求类的，request文件夹外面的都是普通的实体类  \n" +
                "5、如果没有run.bat文件，可以在当前文件夹打开cmd命令行，运行命令    java -jar JavaGeneratorFromWord.jar  ";
        try{
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "执行结果.log"),Const.CHARSET_UTF_8));
            out.newLine();
            out.newLine();
            out.write(str);
            out.newLine();
            out.newLine();
            out.write("------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            out.newLine();
            out.newLine();
            out.write(s);
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("生成执行结果日志异常："+e.getMessage());
        }
    }
}
