package convert;

import java.util.List;

/**
 * @Description: 文件转换接口类
 * @Author: liangruihao
 * @Date: 2021/5/6 9:57
 */
public interface Convert {

    /**
     * 文件转换，返回文件名称列表，不包含后缀名
     * @param filePath : 需要转换的文件所在文件夹路径，例如： /data/document/
     * @return java.util.List<java.lang.String>
     * @author: liangruihao
     * @date: 2021/5/6 10:00
     */
    List<String> convert(String filePath) throws Exception;
}
