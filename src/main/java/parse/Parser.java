package parse;


import java.util.List;

/**
 * @Description: 语法解析接口
 * @Author: liangruihao
 * @Date: 2021/5/6 9:41
 */
public interface Parser<T> {
    /**
     * 语法解析，将文件解析成对应的java对象并返回
     * @param filePath : 文件所在文件夹路径，例如： /temp/document/
     * @return java.util.List<T>
     * @author: liangruihao
     * @date: 2021/5/6 9:48
     */
    List<T> parse(String filePath) throws Exception;
}
