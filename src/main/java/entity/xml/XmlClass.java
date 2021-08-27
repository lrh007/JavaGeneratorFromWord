package entity.xml;

/**
 * @Description: xml class对象
 * @Author: liangruihao
 * @Date: 2021/8/26 18:42
 */
public class XmlClass {
    /**
     * 文件夹路径
     */
    private String directoryPath;
    /**
     * 字段属性
     */
    private XmlField field;
    /**
     * 自定义属性
     */
    private XmlCustom custom;

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public XmlField getField() {
        return field;
    }

    public void setField(XmlField field) {
        this.field = field;
    }

    public XmlCustom getCustom() {
        return custom;
    }

    public void setCustom(XmlCustom custom) {
        this.custom = custom;
    }
}
