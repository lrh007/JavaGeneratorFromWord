import comm.Const;
import entity.ClassObj;
import entity.FieldObj;
import entity.FileObj;
import parse.impl.ParseHtml;

import java.io.*;
import java.util.List;

/**
 * @Description: 从workd文件生成java实体类
 * @Author: liangruihao
 * @Date: 2021/4/23 11:21
 */
public class JavaClassGeneratorFromWord {


    public static void main(String[] args) throws Exception {
//        if(args == null || args.length == 0){
//            System.out.println("请输入文件名称!");
//            return;
//        }
//        String doc = args[0];
        String doc = "C:/Users/MACHENIKE/Desktop/testjava/";
        doc = "./";
//        if(!doc.endsWith(Const.SUFFIX_DOC) && !doc.endsWith(Const.SUFFIX_DOCX)){
//            System.out.println("请输入文件类型是 " + Const.SUFFIX_DOC+" 或 " + Const.SUFFIX_DOCX);
//            return;
//        }


        List<FileObj> classes = new ParseHtml().parse(doc);
        for (FileObj fileObj : classes) {
            List<ClassObj> classObjs = fileObj.getClassObjs();
            if (classObjs != null && classObjs.size() > 0) {
                for (ClassObj classObj : classObjs) {
                    String className = classObj.getClassName();
                    String classDesc = classObj.getClassDesc();
                    List<FieldObj> fields = classObj.getFields();

                    String javaPath = doc +fileObj.getFileName();
                    String javaName = doc +fileObj.getFileName() + "/" + className + Const.SUFFIX_JAVA;
                    System.out.println("开始生成java文档："+javaName);
                    File file = new File(javaPath);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javaName)));
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
                    }
                    out.newLine();
                    out.write("}");
                    out.close();
                }
            }
        }
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
        for (FieldObj f : fields){
            builder.append("\t\tmap.put(\"" + f.getFieldName() + "\"," + f.getFieldName() + ");\n");
        }
        builder.append("\t\treturn map;\n" +
                "    }");
        out.write(builder.toString());
        out.newLine();
        out.newLine();
    }

}