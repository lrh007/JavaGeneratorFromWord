package generate.impl;

import comm.Const;
import entity.word.ClassObj;
import entity.word.FieldObj;
import entity.word.FileObj;
import entity.xml.XmlClass;
import entity.xml.XmlCustomField;
import generate.FromGenerate;
import help.Help;
import parse.impl.ParseHtml;

import java.io.*;
import java.util.List;

/**
 * @Description: 从word生成文件
 * @Author: liangruihao
 * @Date: 2021/9/3 17:28
 */
public class FromByWord implements FromGenerate {

    @Override
    public void generateClass(XmlClass xmlClass) throws Exception {
        System.out.println("正在生成文件，请稍等...");
        String path = xmlClass.getDirectoryPath();
        int count = 0;//统计文件个数
        StringBuilder builder = new StringBuilder();//用来生成日志以及命令使用方法
        List<FileObj> classes = new ParseHtml().parse(path);
        for (FileObj fileObj : classes) {
            List<ClassObj> classObjs = fileObj.getClassObjs();
            if (classObjs != null && classObjs.size() > 0) {
                for (ClassObj classObj : classObjs) {
                    String className = classObj.getClassName();
                    String classDesc = classObj.getClassDesc();
                    List<FieldObj> fields = classObj.getFields();

                    String javaPath = path +fileObj.getFileName() +"/";
                    //将请求类生成到request文件夹下面
                    String reqPath = "";
                    if(classObj.isReqClass()){
                        reqPath = Const.REQUEST_CLASS_PATH + "/";
                        javaPath += reqPath;
                    }
                    String javaName = path +fileObj.getFileName() + "/"+ reqPath + className + Const.SUFFIX_JAVA;
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
                    //TODO 这里将来要解析xml标签
                    //判断是否是请求类
                    String interfaceStr = "";
                    if(classObj.isReqClass()){
                        interfaceStr = " implements CapitalPlatformRequest<" + classObj.getResponseClass() + ">";
                    }

                    //生成类名称
                    className = "public class " + className + interfaceStr + " {";
                    out.write(className);

                    //TODO 这里要解析@custom注解
                    //判断是否是请求类
//                    if(classObj.isReqClass()){
//                        genReqStaticVar(out);
//                    }
                    //存在@custom标签则生成自定义字段
                    generateCustomFields(out,classObj.getNeedCustom(),xmlClass);

                    //生成属性字段
                    if(fields != null && fields.size() > 0){
                        for (FieldObj field : fields){
                            out.newLine();
                            out.newLine();
                            String fieldName = field.getFieldName();
                            String fieldDesc = field.getFieldDesc();
                            StringBuilder nameBuilder = new StringBuilder();
                            //生成属性字段注释
                            if(xmlClass.getField().getShowDescription()){
                                fieldDesc = "\t/** " + fieldDesc + " **/";
                                out.write(fieldDesc);
                                out.newLine();
                            }
                            //生成属性字段
                            nameBuilder.append("\t"+xmlClass.getField().getScope().getScope()+" ");
                            if(xmlClass.getField().getStaticVar()){
                                nameBuilder.append("static ");
                            }
                            if(xmlClass.getField().getFinalVar()){
                                nameBuilder.append("final ");
                            }
                            nameBuilder.append(field.getFieldType()).append(" ").append(fieldName + ";");
                            out.write(nameBuilder.toString());
                        }
                        //TODO 这里要解析@custom注解
                        //判断是否是请求类
                        if(classObj.isReqClass()){
                            genReqOverrideMethod(out,fields,classObj.getResponseClass());
                        }

                        for (FieldObj field : fields){
                            //生成getter 和 setter方法
                            getterAndSetter(out,field.getFieldName(),field.getFieldType(),xmlClass.getField().getGetter(),xmlClass.getField().getSetter());
                        }
                    }else{
                        //没有属性字段的情况下也要生成override方法
                        //判断是否是请求类
                        //TODO 这里要解析@cusom 注解
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
        Help.log(builder.toString(),path);
    }
    /**
     * 生成自定义的属性字段及setter,getter方法
     * @param out :
     * @param needCustom :
     * @param xmlClass :
     * @return void
     * @author: liangruihao
     * @date: 2021/9/9 17:58
     */
    private static void generateCustomFields(BufferedWriter out,boolean needCustom,XmlClass xmlClass) throws Exception{
        List<XmlCustomField> customFields = xmlClass.getCustom().getCustomFields();
        for (XmlCustomField field :customFields){
            boolean b = false;
            //判断是否存在@custom标签
            if(needCustom){
                b = true;
            }else{
                //判断是否是所有类中都存在
                if(field.getAllClass()){
                    b = true;
                }
            }
            if(b){
                //生成自定义属性
                StringBuilder builder = new StringBuilder();
                if(field.getFieldDescription() != null){
                    builder.append("\t/** " + field.getFieldDescription() + " **/\r\n");
                }
                builder.append("\t").append(field.getScope().getScope()).append(" ");
                if(field.getStaticVar()){
                    builder.append(" static ");
                }
                if(field.getFinalVar()){
                    builder.append(" final ");
                }
                builder.append(field.getJavaType());
                builder.append(" ").append(field.getFieldName()).append(" = ").append(field.getFieldValue()).append(";");
                out.newLine();
                out.write(builder.toString());
                //生成自定义setter,getter方法
//                getterAndSetter(out,field.getFieldName(),field.getJavaType(),field.getGetter(),field.getSetter());
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
    private static void getterAndSetter(BufferedWriter out,String fieldName,String fieldType,boolean getterFlag,boolean setterFlag) throws IOException {
        out.newLine();
        out.newLine();
        String newName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        if(getterFlag){
            String getter = "\tpublic " + fieldType + " get" + newName + "() {\n" +
                    "        return "+ fieldName + ";\n" +
                    "    }";
            out.write(getter);
            out.newLine();
        }
        if(setterFlag){
            String setter = "\tpublic void set" + newName + "(" + fieldType + " " + fieldName + ") {\n" +
                    "        this." + fieldName + " = " + fieldName + ";\n" +
                    "    }";
            out.write(setter);
        }

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
    private static void genReqOverrideMethod(final BufferedWriter out,List<FieldObj> fields,String responseClass) throws IOException {
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

}
