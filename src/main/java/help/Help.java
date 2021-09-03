package help;

import comm.Const;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @Description: help日志类
 * @Author: liangruihao
 * @Date: 2021/9/3 17:55
 */
public class Help {

    /**
     * 生成日志
     * @param logStr : 日志字符串
     * @param logPath :  生成的日志路径
     * @return void
     * @author: liangruihao
     * @date: 2021/9/3 17:57
     */
    public static void log(String logStr,String logPath){
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
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logPath + "执行结果.log"), Const.CHARSET_UTF_8));
            out.newLine();
            out.newLine();
            out.write(logStr);
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
