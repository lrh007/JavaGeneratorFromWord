package entity.xml;

import entity.xml.abstracts.XmlFieldObj;

/**
 * @Description: xml 自定义字段对象
 * @Author: liangruihao
 * @Date: 2021/8/27 9:47
 */
public class XmlCustomField extends XmlFieldObj {
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段值
     */
    private String fieldValue;
    /**
     * 字段描述
     */
    private String fieldDescription;
    /**
     * 是否在所有类中显示，true 是，false 不是，false的情况下需要配合注解@custom才能起作用
     */
    private Boolean allClass = true;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public Boolean getAllClass() {
        return allClass;
    }

    public void setAllClass(Boolean allClass) {
        this.allClass = allClass;
    }
}
