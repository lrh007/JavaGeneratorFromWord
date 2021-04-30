import comm.Const;
import entiry.ClassObj;
import entiry.FieldObj;
import entiry.FileObj;
import util.WordToHtml;

import java.io.*;
import java.util.List;

/**
 * @Description: 从workd文件生成java实体类
 * @Author: liangruihao
 * @Date: 2021/4/23 11:21
 */
public class JavaClassGeneratorFromWord {

    private String test;

    public static void main(String[] args) throws Exception {
//        if(args == null || args.length == 0){
//            System.out.println("请输入文件名称!");
//            return;
//        }
//        String doc = args[0];
        String doc = "C:/Users/MACHENIKE/Desktop/testjava/";
//        if(!doc.endsWith(Const.SUFFIX_DOC) && !doc.endsWith(Const.SUFFIX_DOCX)){
//            System.out.println("请输入文件类型是 " + Const.SUFFIX_DOC+" 或 " + Const.SUFFIX_DOCX);
//            return;
//        }


        List<FileObj> classes = WordToHtml.getClasses(doc);

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
                    //生成类名称
                    className = "public class " + className + " {";
                    out.write(className);
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
                            fieldName = "\tprivate String " + fieldName + ";";
                            out.write(fieldName);
                        }
                        for (FieldObj field : fields){
                            out.newLine();
                            out.newLine();
                            //生成getter 和 setter方法
                            getterAndSetter(out,field.getFieldName());
                        }
                    }
                    out.newLine();
                    out.write("}");
                    out.close();
                }
            }
        }
    }


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    /**
     * 生成字段getter 和 setter方法
     * @param out :  文件输出流
     * @param fieldName :  字段名称
     * @return void
     * @author: liangruihao
     * @date: 2021/4/30 18:18
     */
    private static void getterAndSetter(BufferedWriter out,String fieldName) throws IOException {
        out.newLine();
        String newName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        String getter = "\tpublic String get" + newName + "() {\n" +
                    "        return "+ fieldName +";\n" +
                    "    }";
        out.write(getter);
        out.newLine();
        out.newLine();
        String setter = "\tpublic void set" + newName + "(String " + fieldName + ") {\n" +
                "        this." + fieldName + " = " + fieldName + ";\n" +
                "    }";
        out.write(setter);
        out.newLine();
    }


}