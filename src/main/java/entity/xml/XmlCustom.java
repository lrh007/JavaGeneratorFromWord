package entity.xml;

import java.util.List;

/**
 * @Description: xml custom 自定义属性对象
 * @Author: liangruihao
 * @Date: 2021/8/27 9:26
 */
public class XmlCustom {

    /**
     * 自定义字段集合
     */
    private List<XmlCustomField> customFields;
    /**
     * 自定义方法集合
     */
    private List<XmlCustomMethod> customMethods;

    public List<XmlCustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<XmlCustomField> customFields) {
        this.customFields = customFields;
    }

    public List<XmlCustomMethod> getCustomMethods() {
        return customMethods;
    }

    public void setCustomMethods(List<XmlCustomMethod> customMethods) {
        this.customMethods = customMethods;
    }
}
