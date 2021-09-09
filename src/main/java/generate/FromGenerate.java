package generate;

import entity.xml.XmlClass;

/**
 * @Description:
 * @Author: liangruihao
 * @Date: 2021/9/3 17:32
 */
public interface FromGenerate {
    /**
     * 生成class
     */
    void generateClass(XmlClass xmlClass) throws Exception;
}
